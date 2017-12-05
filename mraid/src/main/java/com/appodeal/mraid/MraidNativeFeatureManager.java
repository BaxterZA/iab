package com.appodeal.mraid;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.List;

class MraidNativeFeatureManager {
    private final Context context;
    private final List<MraidNativeFeature> supportedNativeFeatures;
    private final MraidNativeFeatureListener mraidNativeFeatureListener;

    MraidNativeFeatureManager(@NonNull Context context, @NonNull List<MraidNativeFeature> supportedNativeFeatures, @NonNull MraidNativeFeatureListener mraidNativeFeatureListener) {
        this.context = context;
        this.supportedNativeFeatures = supportedNativeFeatures;
        this.mraidNativeFeatureListener = mraidNativeFeatureListener;
    }

    boolean isSmsSupported() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:"));
        return supportedNativeFeatures.contains(MraidNativeFeature.SMS) && deviceCanHandleIntent(context, intent);
    }

    boolean isTelSupported() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:"));
        return supportedNativeFeatures.contains(MraidNativeFeature.TEL) && deviceCanHandleIntent(context, intent);
    }

    boolean isCalendarSupported() {
        return supportedNativeFeatures.contains(MraidNativeFeature.CALENDAR) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    boolean isStorePictureSupported() {
        return supportedNativeFeatures.contains(MraidNativeFeature.STORE_PICTURE);
    }

    boolean isInlineVideoSupported() {
        return supportedNativeFeatures.contains(MraidNativeFeature.INLINE_VIDEO);
    }

    boolean isVpaidSupported() {
        return supportedNativeFeatures.contains(MraidNativeFeature.VPAID) && supportedNativeFeatures.contains(MraidNativeFeature.INLINE_VIDEO);
    }

    boolean isLocationSupported() {
        return supportedNativeFeatures.contains(MraidNativeFeature.LOCATION);
    }

    Location getLocation() {
        return mraidNativeFeatureListener.mraidNativeFeatureGetLocation();
    }

    void open(String url) {
        mraidNativeFeatureListener.mraidNativeFeatureOpenBrowser(url);
    }

    void playVideo(String url) {
        mraidNativeFeatureListener.mraidNativeFeaturePlayVideo(url);
    }

    void storePicture(String url) {
        mraidNativeFeatureListener.mraidNativeFeatureStorePicture(url);
    }

    void createCalendarEvent(String event) {
        mraidNativeFeatureListener.mraidNativeFeatureCreateCalendarEvent(event);
    }

    void calTell(String url) {
        mraidNativeFeatureListener.mraidNativeFeatureCallTel(url);
    }

    void sendSms(String url) {
        mraidNativeFeatureListener.mraidNativeFeatureSendSms(url);
    }

    private boolean deviceCanHandleIntent(@NonNull final Context context, @NonNull final Intent intent) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            return !activities.isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }
}
