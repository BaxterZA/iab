package com.appodeal.iab.mraid;


import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;


import com.appodeal.iab.views.ViewHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class MraidViewabilityTracker {
    private static final int TRACKING_PERIOD = 200;
    @VisibleForTesting
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private ViewabilityCheckTask task;

    interface MraidViewabilityListener {
        void onExposureChanged(ExposureState exposureState);
    }

    MraidViewabilityTracker(@NonNull View view, @NonNull MraidViewabilityListener listener) {
        //noinspection ConstantConditions
        if (view == null) {
            return;
        }

        task = new ViewabilityCheckTask(view, listener);
        handler.postDelayed(task, TRACKING_PERIOD);

        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                stopTracking();
                v.removeOnAttachStateChangeListener(this);
            }
        });
    }

    void stopTracking() {
        handler.removeCallbacks(task);
    }


    private static class ViewabilityCheckTask implements Runnable {
        private final WeakReference<View> viewRef;
        private final MraidViewabilityListener listener;
        private float percent = -1f;
        private Rect viewableRect = new Rect();

        ViewabilityCheckTask(View view, @NonNull MraidViewabilityListener listener) {
            this.viewRef = new WeakReference<>(view);
            this.listener = listener;
        }

        public void run() {
            View adView = viewRef.get();
            if (adView != null) {
                Rect localVisibleViewRect = new Rect();
                adView.getLocalVisibleRect(localVisibleViewRect);
                Rect globalVisibleViewRect = new Rect();
                boolean isAdVisible = adView.getGlobalVisibleRect(globalVisibleViewRect);
                int[] location = new int[2];
                adView.getLocationOnScreen(location);
                boolean isAdShown = adView.isShown();
                boolean isAdTransparent = adView.getAlpha() == 0.0F;
                float newPercent;
                List<Rect> occlusionRectangles = new ArrayList<>();
                if (!isAdVisible || !isAdShown || isAdTransparent) {
                    newPercent = 0.0f;
                } else {
                    Pair<Boolean, List<Rect>> result = ViewHelper.getParentTransparencyAndOverlappingRectList(globalVisibleViewRect, adView);
                    if (result.first) {
                        newPercent = 0;
                    } else {
                        List<Rect> overlappingRectList = result.second;
                        OverlappingCalculator calculator = new OverlappingCalculator(overlappingRectList, location);
                        float totalOverlappingArea = calculator.getTotalArea();
                        float totalAdViewArea = adView.getWidth() * adView.getHeight();
                        float visibleAdViewArea = localVisibleViewRect.width() * localVisibleViewRect.height();
                        if (totalAdViewArea > 0) {
                            newPercent = 100 * (visibleAdViewArea - totalOverlappingArea) / totalAdViewArea;
                        } else {
                            newPercent = 0;
                        }
                        occlusionRectangles = calculator.getOcclusionRectangles();
                    }
                }
                if (Float.compare(newPercent, percent) != 0 || !localVisibleViewRect.equals(viewableRect)) {
                    percent = newPercent;
                    viewableRect = localVisibleViewRect;
                    DisplayMetrics displayMetrics = adView.getResources().getDisplayMetrics();
                    listener.onExposureChanged(new ExposureState(percent, ViewHelper.convertRectToDp(viewableRect, displayMetrics.densityDpi), ViewHelper.convertListOfRectToDp(occlusionRectangles, displayMetrics.densityDpi)));
                }
                handler.postDelayed(this, TRACKING_PERIOD);
            }
        }
    }
}
