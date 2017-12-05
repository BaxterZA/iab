package com.appodeal.mraid;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class ViewHelper {

    static void removeViewFromParent(View view) {
        if (view == null || view.getParent() == null) {
            return;
        }

        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Nullable
    static ViewGroup getTopRootView(View view, @Nullable Activity activity) {
        ViewGroup rootView = null;
        View tempRootView = null;
        if (activity != null) {
            tempRootView = activity.getWindow().getDecorView();
        } else if (view != null) {
            tempRootView = view.getRootView();
        }
        if ((tempRootView instanceof ViewGroup)) {
            rootView = (ViewGroup) tempRootView;
        }
        return rootView;
    }

    static int getDensity(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    static Activity getActivityForView(View view) {
        Activity activity = null;
        if (view != null) {
            if (view.getContext() instanceof Activity) {
                activity = (Activity) view.getContext();
            } else if (view.getContext() instanceof ContextWrapper && ((ContextWrapper) view.getContext()).getBaseContext() instanceof Activity) {
                activity = (Activity) ((ContextWrapper) view.getContext()).getBaseContext();
            } else {
                if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) view.getParent();
                    if (parent.getContext() instanceof Activity) {
                        activity = (Activity) ((ViewGroup) view.getParent()).getContext();
                    } else if (parent.getContext() instanceof ContextWrapper && ((ContextWrapper) parent.getContext()).getBaseContext() instanceof Activity) {
                        activity = (Activity) ((ContextWrapper) parent.getContext()).getBaseContext();
                    }
                }
            }
        }
        return activity;
    }

    static Pair<Boolean, List<Rect>> getParentTransparencyAndOverlappingRectList(Rect localVisibleViewRect, View view) {
        List<Rect> overlappingRectList = new ArrayList<>();
        ViewGroup rootView = (ViewGroup) view.getRootView();
        ViewGroup parent = (ViewGroup) view.getParent();
        boolean isTransparency = false;

        while (parent != null) {
            if (parent.getAlpha() == 0) {
                isTransparency = true;
                break;
            }
            int index = parent.indexOfChild(view);
            for (int i = index + 1; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE && !(child instanceof CircleCountdownView)) {
                    Rect childGlobalVisibleViewRect = new Rect();
                    child.getGlobalVisibleRect(childGlobalVisibleViewRect);
                    if (Rect.intersects(localVisibleViewRect, childGlobalVisibleViewRect)) {
                        overlappingRectList.add(getRectIntersection(localVisibleViewRect, childGlobalVisibleViewRect));
                    }
                }
            }
            if (parent != rootView) {
                view = parent;
                parent = (ViewGroup) view.getParent();
            } else {
                parent = null;
            }
        }

        return new Pair<>(isTransparency, overlappingRectList);
    }

    private static Rect getRectIntersection(Rect rect1, Rect rect2) {
        return new Rect(Math.max(rect1.left, rect2.left), Math.max(rect1.top, rect2.top), Math.min(rect1.right, rect2.right), Math.min(rect1.bottom, rect2.bottom));
    }

    static List<Rect> convertListOfRectToDp(List<Rect> rectList, int densityDpi) {
        List<Rect> list = new ArrayList<>();
        for (Rect rect : rectList) {
            list.add(convertRectToDp(rect, densityDpi));
        }
        return list;
    }

    static Rect convertRectToDp(Rect rect, int densityDpi) {
        return new Rect(px2dip(rect.left, densityDpi),
                px2dip(rect.top, densityDpi),
                px2dip(rect.right, densityDpi),
                px2dip(rect.bottom, densityDpi));
    }

    static int px2dip(int pixels, int densityDpi) {
        return pixels * DisplayMetrics.DENSITY_DEFAULT / densityDpi;
    }

    static int dip2px(int pixels, int densityDpi) {
        return pixels * densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }
}
