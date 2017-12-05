package com.appodeal.mraid;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

class MraidAppOrientationProperties {
    final String orientation;
    final boolean locked;

    @VisibleForTesting
    MraidAppOrientationProperties(String orientation, boolean locked) {
        this.orientation = orientation;
        this.locked = locked;
    }

    MraidAppOrientationProperties(@NonNull Context context, @Nullable Activity activity) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = "landscape";
        } else {
            orientation = "portrait";
        }

        if (activity != null) {
            locked = activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        } else {
            MraidLog.d("MraidAppOrientationProperties: Context is not an Activity, set locked to false");
            locked = false;
        }
    }
}
