package com.appodeal.iab.views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.appodeal.iab.mraid.ClosePosition;


public class CloseableLayout extends FrameLayout {
    private final static int CLOSE_REGION_SIZE = 50;
    private final static int PROGRESS_TIMER_INTERVAL = 50;

    public interface OnCloseListener {
        void onClose();
    }

    public interface SkippableStateListener {
        void onSkippableStateChange();
    }

    private int closeTimerPosition;
    private OnCloseListener closeListener;
    private SkippableStateListener skippableStateListener;
    private CircleCountdownView closeButton;

    public CloseableLayout(@NonNull Context context) {
        this(context, Color.parseColor("#B4FFFFFF"), Color.parseColor("#52000000"));
    }

    public CloseableLayout(@NonNull Context context, int assetsColor, int assetsBackgroundColor) {
        super(context);
        closeButton = new CircleCountdownView(getContext(), assetsColor, assetsBackgroundColor);
        int closeButtonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CLOSE_REGION_SIZE, context.getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(closeButtonSize, closeButtonSize);
        closeButton.setLayoutParams(params);
        closeButton.setVisibility(VISIBLE);
        closeButton.setBackgroundColor(Color.TRANSPARENT);
        setClosePosition(ClosePosition.TOP_RIGHT);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeListener != null) {
                    closeListener.onClose();
                }
            }
        });
        addView(closeButton);
    }

    public void setOnCloseListener(@NonNull OnCloseListener onCloseListener) {
        closeListener = onCloseListener;
    }

    public void setSkippableStateListener(@NonNull SkippableStateListener skippableStateListener) {
        this.skippableStateListener = skippableStateListener;
    }

    void setClosePosition(@NonNull final ClosePosition closePosition) {
        ((LayoutParams) closeButton.getLayoutParams()).gravity = closePosition.getGravity();
        invalidate();
    }

    void setUseCustomClose(boolean useCustomClose) {
        if (closeButton != null) {
            closeButton.setVisibility(useCustomClose ? INVISIBLE : VISIBLE);
        }
    }

    public static Rect getCloseButtonRect(ClosePosition closePosition, Rect resizeRect) {
        Rect closeRect = new Rect();
        Gravity.apply(closePosition.getGravity(), CLOSE_REGION_SIZE, CLOSE_REGION_SIZE, resizeRect, closeRect);
        return closeRect;
    }

    public void startTimer(final int timeInMills) {
        startTimer(timeInMills, 0);
    }

    public void startTimer(final int timeInMills, final int currentPosition) {
        closeTimerPosition = currentPosition;
        final Handler handler = new Handler(Looper.getMainLooper());
        closeButton.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (closeButton != null) {
                    closeTimerPosition = closeTimerPosition + PROGRESS_TIMER_INTERVAL;
                    int percent = 100;
                    if (timeInMills > 0) {
                        percent = 100 * closeTimerPosition / timeInMills;
                    }
                    closeButton.changePercentage(percent, (int) Math.ceil((double) (timeInMills - closeTimerPosition) / 1000));
                    if (closeTimerPosition >= timeInMills) {
                        closeButton.setClickable(true);
                        closeButton.setImage(getBitmapFromBase64(close));
                        closeButton.setEnabled(true);
                        if (skippableStateListener != null) {
                            skippableStateListener.onSkippableStateChange();
                        }
                    } else {
                        handler.postDelayed(this, PROGRESS_TIMER_INTERVAL);
                    }
                }
            }
        }, PROGRESS_TIMER_INTERVAL);
    }

    public void showCloseButton() {
        closeButton.changePercentage(100, 0);
        closeButton.setImage(getBitmapFromBase64(close));
        if (skippableStateListener != null) {
            skippableStateListener.onSkippableStateChange();
        }
    }

    public int getCloseTimerPosition() {
        return closeTimerPosition;
    }

    private static final String close = "iVBORw0KGgoAAAANSUhEUgAAAJEAAACRCAYAAADD2FojAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAKwwAACsMBNCkkqwAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAcdEVYdFNvZnR3YXJlAEFkb2JlIEZpcmV3b3JrcyBDUzbovLKMAAAE1UlEQVR4nO3dva4bRRiH8efwJSj3EqAApYjScBVUiASJ6+IqiKKgVFxFOopISBGXsCURCJnieI4TzrG99rsz+378n9KytVP8tNqdnfHe7HY7lLL00dYDUPETImVOiJQ5IVLmhEiZEyJlToiUOSFS5oRImRMiZU6IlDkhUuaESJkTImVOiJQ5IVLmhEiZEyJlToiUOSFS5oRImftk5MHmeX4MPAGeT9P0z8hjZ26e54+BZ8DbaZpejz7+sDPRHtD3wFfAT/M8fzrq2JnbA/oR+Ab4bp7nb0ePYQii9wDd7D/6EkEy9x6gr9/7eDik7ogeANQSJENHALWGQuqK6ASgliBd0RlArWGQuiFaAKglSBe0EFBrCKSeZ6JHnAfUEqQFXQio9ajTcO7qiegl8McF3xekE10J6E/glz4jOtQN0TRN/wIvECRzFkAj5uO6XlgLkj3vgGDALb4gXV8EQDBoslGQLi8KIBj42EOQlhcJEAx+ii9I54sGCDZYCiJIx4sICDZaTyRI94sKCDZclCZIhyIDgo1XNgpSfEDgYHlsZUgZAIEDRFATUhZA4AQR1IKUCRA4QgQ1IGUDBM4QQW5IGQGBQ0SQE1JWQOAUEeSClBkQOEYEOSBlBwTOEUFsSBUAQQBEEBNSFUAQBBHEglQJEARCBDEgVQMEwRCBb0gVAUFAROATUlVAEBQR+IJUGRAERgQ+IFUHBMERwbaQBOi28IhgG0gCdCgFIhgLSYA+LA0iGANJgO6XChH0hSRAD5cOEfSBJEDHS4kI1oUkQKdLiwjWgSRA57vZ7XZbj6F7BggvuP3zUgE6UQlEcDWkd8DnF3y/HCAohAiuhrS0koAg+TXR/7vyGmlJZQFBMUTQBVJpQFAQEawKqTwgKIoIVoEkQPvKIoI7SL8Cf13407+BlwJ0W2lE+7u1H4AvLvzpZ8DTrfe1eaksohVu911skPRQSUQrzhcJEgURdZhwLA+pFKKOM9alIZVBZHh2trSykEogMjzF/xkH+9q8lx6RcT3QO5xskPRcakRrLCjzsEHSe2kRrbkiUZBOlxJRjyWtgnS8dIh6rokWpIdLhWjEonpBul8aRCN3ZQjSh6VAtMW2HkE6FB7RlvvCBOm20Ig8bCwUpMCIPABqVYcUEpEnQK3KkMIh8gioVRVSKESeAbUqQgqDKAKgVjVIIRBFAtSqBMk9ooiAWlUguUYUGVCrAiS3iDIAamWH5BJRJkCtzJDcIcoIqJUVkitEmQG1MkJyg6gCoFY2SC4QVQLUygRpc0QVAbWyQNoUUWVArQyQNkMkQIeiQ9oEkQDdLzKk4YgE6HhRIQ1FJEDniwhpGCIBWl40SEMQCdDlRYLUHZEAXV8USF0RCZC9CJC6IRKg9fIOqeeZ6CkCtFoWSH1GdKgnojfA0jfyCdCCroT0ptNw7uqGaJqm34FXnIckQBd0IaTfpml63XlIfS+sF0ASoCtaCGkIIBhwi38CkgAZOgNpGCAYNNn4ACQBWqEjkIYCgsFvo57n+THwBHguQOu1n055BrwdDQiKvdJc9Wnz5bEqfkKkzAmRMidEypwQKXNCpMwJkTInRMqcEClzQqTMCZEyJ0TKnBApc0KkzAmRMidEypwQKXNCpMwJkTInRMqcEClzQqTMCZEy9x9PtEuyzhRHVgAAAABJRU5ErkJggg==";

    private static Bitmap getBitmapFromBase64(String encodedString) {
        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
