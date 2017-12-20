package com.appodeal.iab.mraid;


import android.view.Gravity;

public enum ClosePosition {
    TOP_LEFT(Gravity.TOP | Gravity.LEFT),
    TOP_CENTER(Gravity.TOP | Gravity.CENTER_HORIZONTAL),
    TOP_RIGHT(Gravity.TOP | Gravity.RIGHT),
    CENTER(Gravity.CENTER),
    BOTTOM_LEFT(Gravity.BOTTOM | Gravity.LEFT),
    BOTTOM_CENTER(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL),
    BOTTOM_RIGHT(Gravity.BOTTOM | Gravity.RIGHT);

    private final int mGravity;

    ClosePosition(final int mGravity) {
        this.mGravity = mGravity;
    }

    public int getGravity() {
        return mGravity;
    }

}