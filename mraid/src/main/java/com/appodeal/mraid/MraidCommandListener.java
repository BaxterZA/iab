package com.appodeal.mraid;


import android.support.annotation.NonNull;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;

interface MraidCommandListener {

    void mraidViewPageFinished();

    void mraidViewRenderProcessGone();

    void onAudioVolumeChange(float volumePercentage);

    void onLoaded();

    void onFailedToLoad();

    void onResize(MraidResizeProperties resizeProperties) throws MraidError;

    void onExpand() throws MraidError;

    void onClose();

    void onUnload();

    void onSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidError;

    void onOpen(String url);

    void onPlayVideo(String url);

    void onStorePicture(String url);

    void onCreateCalendarEvent(String event);

    boolean onJsAlert(@NonNull String message, @NonNull JsResult result);

    boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage);
}
