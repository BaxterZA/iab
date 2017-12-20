package com.appodeal.iab.mraid;


import android.content.pm.ActivityInfo;

public enum MraidOrientation {
    PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    NONE(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    private final int mActivityInfoOrientation;

    MraidOrientation(final int activityInfoOrientation) {
        mActivityInfoOrientation = activityInfoOrientation;
    }

    int getActivityInfoOrientation() {
        return mActivityInfoOrientation;
    }
}
