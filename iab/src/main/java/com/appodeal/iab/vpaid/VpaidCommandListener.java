package com.appodeal.iab.vpaid;

import android.support.annotation.NonNull;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;

import com.appodeal.iab.WebViewDebugListener;


interface VpaidCommandListener extends WebViewDebugListener {

    void vpaidViewPageFinished();

    void vpaidViewRenderProcessGone();

    void vpaidAdStarted();

    void vpaidAdStopped();

    void vpaidAdSkipped();

    void vpaidAdLoaded();

    void vpaidAdLinearChange();

    void vpaidAdSizeChange();

    void vpaidAdExpandedChange();

    void vpaidAdSkippableStateChange(String state);

    void vpaidAdDurationChange(String duration);

    void vpaidAdVolumeChange(String volume);

    void vpaidAdImpression();

    void vpaidAdClickThru(String url);

    void vpaidAdInteraction();

    void vpaidAdVideoStart();

    void vpaidAdVideoFirstQuartile();

    void vpaidAdVideoMidpoint();

    void vpaidAdVideoThirdQuartile();

    void vpaidAdVideoComplete();

    void vpaidAdUserAcceptInvitation();

    void vpaidAdUserMinimize();

    void vpaidAdUserClose();

    void vpaidAdPaused();

    void vpaidAdPlaying();

    void vpaidAdError(String error);

    void vpaidAdLog(String log);

    void vpaidAdRemainingTime(String time);

    @Override
    boolean onJsAlert(@NonNull String message, @NonNull JsResult result);

    @Override
    boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage);
}

