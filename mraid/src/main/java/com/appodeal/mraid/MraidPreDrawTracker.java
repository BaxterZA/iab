package com.appodeal.mraid;


import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

class MraidPreDrawTracker {
    interface MraidDrawListener {
        void onPreDraw();
    }

    MraidPreDrawTracker(@NonNull final View view, @NonNull final MraidDrawListener listener) {
        //noinspection ConstantConditions
        if (view == null) {
            return;
        }

        if (view.getWidth() > 0 || view.getHeight() > 0) {
            listener.onPreDraw();
        } else {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    listener.onPreDraw();
                    return true;
                }
            });
        }
    }
}
