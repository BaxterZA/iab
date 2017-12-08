package com.appodeal.vast.vpaid;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.appodeal.mraid.AdWebView;
import com.appodeal.vast.PlayerLayerInterface;
import com.appodeal.vast.PlayerLayerListener;
import com.appodeal.vast.PlayerTracker;
import com.appodeal.vast.VastLog;

@SuppressLint("ViewConstructor")
public class VpaidPlayer extends RelativeLayout implements VpaidCommandListener, PlayerLayerInterface {
    private final VpaidBridge vpaidBridge;
    private String adParameters;
    @VisibleForTesting AdWebView adWebView;
    private int currentPosition;

    private PlayerLayerListener listener;

    private boolean destroyed;
    private boolean loaded;

    public VpaidPlayer(Context context, String fileUrl, String adParameters, PlayerLayerListener listener) {
        super(context);
        this.vpaidBridge = new VpaidBridge();
        this.listener = listener;
        this.adParameters = adParameters;

        adWebView = new AdWebView(context);
        adWebView.allowMediaPlayback();
        vpaidBridge.initVpaidWebView(adWebView,this);

        String baseUrl = "http://localhost";
        vpaidBridge.loadContentHtml(fileUrl, baseUrl);
        addView(adWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void vpaidViewPageFinished() {
        if (isDestroyed()) {
            return;
        }

        vpaidBridge.setCreativeData(adParameters);
    }

    @Override
    public void vpaidViewRenderProcessGone() {
        if (isDestroyed()) {
            return;
        }
        VastLog.d("Render process gone");
        if (listener != null) {
            listener.onError();
        }
        destroy();
    }

    @Override
    public void vpaidAdStarted() {

    }

    @Override
    public void vpaidAdStopped() {

    }

    @Override
    public void vpaidAdSkipped() {
        if (listener != null) {
            listener.onComplete();
        }
    }

    @Override
    public void vpaidAdLoaded() {
        loaded = true;
        if (listener != null) {
            listener.onLoaded();
        }
    }

    @Override
    public void vpaidAdLinearChange() {

    }

    @Override
    public void vpaidAdSizeChange() {

    }

    @Override
    public void vpaidAdExpandedChange() {

    }

    @Override
    public void vpaidAdSkippableStateChange(String state) {

    }

    @Override
    public void vpaidAdDurationChange(String duration) {

    }

    @Override
    public void vpaidAdVolumeChange(String volume) {

    }

    @Override
    public void vpaidAdImpression() {

    }

    @Override
    public void vpaidAdClickThru(String url) {
        if (listener != null) {
            listener.onClick(url);
        }
    }

    @Override
    public void vpaidAdInteraction() {

    }

    @Override
    public void vpaidAdVideoStart() {
        if (listener != null) {
            listener.onStarted();
        }
    }

    @Override
    public void vpaidAdVideoFirstQuartile() {
        if (listener != null) {
            listener.onFirstQuartile();
        }
    }

    @Override
    public void vpaidAdVideoMidpoint() {
        if (listener != null) {
            listener.onMidpoint();
        }
    }

    @Override
    public void vpaidAdVideoThirdQuartile() {
        if (listener != null) {
            listener.onThirdQuartile();
        }
    }

    @Override
    public void vpaidAdVideoComplete() {
        if (listener != null) {
            listener.onComplete();
        }
    }

    @Override
    public void vpaidAdUserAcceptInvitation() {

    }

    @Override
    public void vpaidAdUserMinimize() {

    }

    @Override
    public void vpaidAdUserClose() {
        //TODO ?? support
    }

    @Override
    public void vpaidAdPaused() {

    }

    @Override
    public void vpaidAdPlaying() {

    }

    @Override
    public void vpaidAdError(String error) {
        if (listener != null) {
            if (isLoaded()) {
                listener.onError();
            } else {
                listener.onFailedToLoad();
            }
        }
    }

    @Override
    public void vpaidAdLog(String log) {

    }

    @Override
    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
        result.cancel();
        return true;
    }

    @Override
    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
        return false;
    }

    @Override
    public void vpaidAdRemainingTime(String time) {

    }

    @Override
    public void pause() {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null) {
            return;
        }
        VastLog.d("pauseWebView " + adWebView.toString());

        try {
            adWebView.onPause();
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        vpaidBridge.fireAdPauseEvent();
    }

    @Override
    public void resume() {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null) {
            return;
        }
        VastLog.d("resumeWebView " + adWebView.toString());

        try {
            adWebView.onResume();
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        vpaidBridge.fireAdResumeEvent();
    }

    @Override
    public void destroy() {
        vpaidBridge.destroy();
        if (adWebView != null) {
            adWebView.removeAllViews();
            adWebView.setWebChromeClient(null);
            adWebView.setWebViewClient(null);
            adWebView.destroy();
            adWebView = null;
        }
        removeAllViews();

        destroyed = true;
    }

    @Override
    public int getCurrentPosition() {
        currentPosition = currentPosition + PlayerTracker.VIDEO_PROGRESS_TIMER_INTERVAL;
        return currentPosition;
    }

    @Override
    public void start() {
        vpaidBridge.fireStartAdEvent();
    }

    @Override
    public void setVolume(int volume) {
        vpaidBridge.setVolume(volume);
    }

    @Override
    public View getView() {
        return this;
    }

    @VisibleForTesting boolean isDestroyed() {
        return destroyed;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
