package com.appodeal.iab.views;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewHelper {
    public static final String repeat = "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";
    public static final String close = "iVBORw0KGgoAAAANSUhEUgAAAJEAAACRCAYAAADD2FojAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAKwwAACsMBNCkkqwAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAcdEVYdFNvZnR3YXJlAEFkb2JlIEZpcmV3b3JrcyBDUzbovLKMAAAE1UlEQVR4nO3dva4bRRiH8efwJSj3EqAApYjScBVUiASJ6+IqiKKgVFxFOopISBGXsCURCJnieI4TzrG99rsz+378n9KytVP8tNqdnfHe7HY7lLL00dYDUPETImVOiJQ5IVLmhEiZEyJlToiUOSFS5oRImRMiZU6IlDkhUuaESJkTImVOiJQ5IVLmhEiZEyJlToiUOSFS5oRImftk5MHmeX4MPAGeT9P0z8hjZ26e54+BZ8DbaZpejz7+sDPRHtD3wFfAT/M8fzrq2JnbA/oR+Ab4bp7nb0ePYQii9wDd7D/6EkEy9x6gr9/7eDik7ogeANQSJENHALWGQuqK6ASgliBd0RlArWGQuiFaAKglSBe0EFBrCKSeZ6JHnAfUEqQFXQio9ajTcO7qiegl8McF3xekE10J6E/glz4jOtQN0TRN/wIvECRzFkAj5uO6XlgLkj3vgGDALb4gXV8EQDBoslGQLi8KIBj42EOQlhcJEAx+ii9I54sGCDZYCiJIx4sICDZaTyRI94sKCDZclCZIhyIDgo1XNgpSfEDgYHlsZUgZAIEDRFATUhZA4AQR1IKUCRA4QgQ1IGUDBM4QQW5IGQGBQ0SQE1JWQOAUEeSClBkQOEYEOSBlBwTOEUFsSBUAQQBEEBNSFUAQBBHEglQJEARCBDEgVQMEwRCBb0gVAUFAROATUlVAEBQR+IJUGRAERgQ+IFUHBMERwbaQBOi28IhgG0gCdCgFIhgLSYA+LA0iGANJgO6XChH0hSRAD5cOEfSBJEDHS4kI1oUkQKdLiwjWgSRA57vZ7XZbj6F7BggvuP3zUgE6UQlEcDWkd8DnF3y/HCAohAiuhrS0koAg+TXR/7vyGmlJZQFBMUTQBVJpQFAQEawKqTwgKIoIVoEkQPvKIoI7SL8Cf13407+BlwJ0W2lE+7u1H4AvLvzpZ8DTrfe1eaksohVu911skPRQSUQrzhcJEgURdZhwLA+pFKKOM9alIZVBZHh2trSykEogMjzF/xkH+9q8lx6RcT3QO5xskPRcakRrLCjzsEHSe2kRrbkiUZBOlxJRjyWtgnS8dIh6rokWpIdLhWjEonpBul8aRCN3ZQjSh6VAtMW2HkE6FB7RlvvCBOm20Ig8bCwUpMCIPABqVYcUEpEnQK3KkMIh8gioVRVSKESeAbUqQgqDKAKgVjVIIRBFAtSqBMk9ooiAWlUguUYUGVCrAiS3iDIAamWH5BJRJkCtzJDcIcoIqJUVkitEmQG1MkJyg6gCoFY2SC4QVQLUygRpc0QVAbWyQNoUUWVArQyQNkMkQIeiQ9oEkQDdLzKk4YgE6HhRIQ1FJEDniwhpGCIBWl40SEMQCdDlRYLUHZEAXV8USF0RCZC9CJC6IRKg9fIOqeeZ6CkCtFoWSH1GdKgnojfA0jfyCdCCroT0ptNw7uqGaJqm34FXnIckQBd0IaTfpml63XlIfS+sF0ASoCtaCGkIIBhwi38CkgAZOgNpGCAYNNn4ACQBWqEjkIYCgsFvo57n+THwBHguQOu1n055BrwdDQiKvdJc9Wnz5bEqfkKkzAmRMidEypwQKXNCpMwJkTInRMqcEClzQqTMCZEyJ0TKnBApc0KkzAmRMidEypwQKXNCpMwJkTInRMqcEClzQqTMCZEy9x9PtEuyzhRHVgAAAABJRU5ErkJggg==";
    public static final String unmute = "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";
    public static final String mute = "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";


    public static void removeViewFromParent(View view) {
        if (view == null || view.getParent() == null) {
            return;
        }

        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Nullable
    public static ViewGroup getTopRootView(View view, @Nullable Activity activity) {
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

    public static int getDensity(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float getDisplayAspectRatio(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (window != null) {
            Display display = window.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return (float) size.x / size.y;
        } else {
            return 16f / 9;
        }
    }

    public static Activity getActivityForView(View view) {
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

    public static Pair<Boolean, List<Rect>> getParentTransparencyAndOverlappingRectList(Rect localVisibleViewRect, View view) {
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

    public static List<Rect> convertListOfRectToDp(List<Rect> rectList, int densityDpi) {
        List<Rect> list = new ArrayList<>();
        for (Rect rect : rectList) {
            list.add(convertRectToDp(rect, densityDpi));
        }
        return list;
    }

    public static Rect convertRectToDp(Rect rect, int densityDpi) {
        return new Rect(px2dip(rect.left, densityDpi),
                px2dip(rect.top, densityDpi),
                px2dip(rect.right, densityDpi),
                px2dip(rect.bottom, densityDpi));
    }

    public static int px2dip(int pixels, int densityDpi) {
        return pixels * DisplayMetrics.DENSITY_DEFAULT / densityDpi;
    }

    public static int dip2px(int pixels, int densityDpi) {
        return pixels * densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }

    public static TextView createCtaButton(Context context, Pair<Integer, Integer> position, int assetsColor, int assetsBackgroundColor, String text) {
        RelativeLayout.LayoutParams learnMoreTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        learnMoreTextParams.setMargins(0, 0, 5, 5);
        TextView ctaButton = new TextView(context);

        ctaButton.setTextColor(assetsColor);
        ctaButton.setGravity(Gravity.CENTER_VERTICAL);
        ctaButton.setShadowLayer(6.0f, 0.0f, 0.0f, Color.parseColor("#5c000000"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ctaButton.setBackground(getButtonBackground(assetsBackgroundColor));
        } else {
            //noinspection deprecation
            ctaButton.setBackgroundDrawable(getButtonBackground(assetsBackgroundColor));
        }
        ctaButton.setPadding(30, 10, 30, 10);
        learnMoreTextParams.addRule(position.first);
        learnMoreTextParams.addRule(position.second);
        ctaButton.setText(text);
        ctaButton.setLayoutParams(learnMoreTextParams);
        return ctaButton;
    }

    private static Drawable getButtonBackground(int assetsBackgroundColor) {
        GradientDrawable backgroundShape =  new GradientDrawable();
        backgroundShape.setShape(GradientDrawable.RECTANGLE);
        backgroundShape.setColor(assetsBackgroundColor);
        backgroundShape.setCornerRadius(100);
        return backgroundShape;
    }

    public static CircleCountdownView createCloseButton(Context context, int size, Pair<Integer, Integer> position, int assetsColor, int assetsBackgroundColor) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);
        layoutParams.addRule(position.first);
        layoutParams.addRule(position.second);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        CircleCountdownView closeButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        closeButton.setLayoutParams(layoutParams);
        return closeButton;
    }

    public static CircleCountdownView createMuteButton(Context context, int size, Pair<Integer, Integer> position, int assetsColor, int assetsBackgroundColor) {
        CircleCountdownView muteButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        RelativeLayout.LayoutParams muteButtonParams = new RelativeLayout.LayoutParams(size, size);
        muteButtonParams.addRule(position.first);
        muteButtonParams.addRule(position.second);
        muteButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        muteButton.setLayoutParams(muteButtonParams);
        muteButton.setImage(getBitmapFromBase64(mute));
        return muteButton;
    }

    public static CircleCountdownView createRepeatButton(Context context, int size, int assetsColor, int assetsBackgroundColor) {
        CircleCountdownView repeatButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        RelativeLayout.LayoutParams bannerRepeatButtonParams = new RelativeLayout.LayoutParams(size, size);
        bannerRepeatButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        bannerRepeatButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        bannerRepeatButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        repeatButton.setLayoutParams(bannerRepeatButtonParams);
        repeatButton.setImage(getBitmapFromBase64(repeat));
        return repeatButton;
    }

    public static LinearCountdownView createProgressBar(Context context, int assetsColor) {
        LinearCountdownView progressBar = new LinearCountdownView(context, assetsColor);
        RelativeLayout.LayoutParams vastCountdownParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        vastCountdownParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(vastCountdownParams);
        progressBar.changePercentage(0);
        return progressBar;
    }

    public static Bitmap getBitmapFromBase64(String encodedString) {
        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
