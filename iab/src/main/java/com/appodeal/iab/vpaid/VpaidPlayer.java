package com.appodeal.iab.vpaid;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.appodeal.iab.Logger;
import com.appodeal.iab.vast.PlayerLayerInterface;
import com.appodeal.iab.vast.PlayerLayerListener;
import com.appodeal.iab.vast.PlayerTracker;
import com.appodeal.iab.webview.AdWebView;


@SuppressLint("ViewConstructor")
public class VpaidPlayer extends RelativeLayout implements VpaidCommandListener, PlayerLayerInterface {
    private final static String TAG = "VpaidPlayer";

    private final VpaidBridge vpaidBridge;
    private final String adParameters;
    private AdWebView adWebView;
    private int currentPosition;
    private final String fileUrl;
    private final PlayerLayerListener listener;

    private boolean destroyed;
    private boolean loaded;

    public static VpaidPlayer createVpaidPlayer(Context context, String fileUrl, String adParameters, PlayerLayerListener listener) {
        AdWebView adWebView = new AdWebView(context);
        adWebView.allowMediaPlayback();
        VpaidBridge vpaidBridge = new VpaidBridge();
        return new VpaidPlayer(context, adWebView, vpaidBridge, fileUrl, adParameters, listener);
    }

    @VisibleForTesting VpaidPlayer(Context context, AdWebView adWebView, VpaidBridge vpaidBridge, String fileUrl, String adParameters, PlayerLayerListener listener) {
        super(context);
        this.vpaidBridge = vpaidBridge;
        this.fileUrl = fileUrl;
        this.listener = listener;
        this.adParameters = adParameters;
        this.adWebView = adWebView;

        addView(adWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void load() {
        vpaidBridge.initVpaidWebView(adWebView,this);
        vpaidBridge.loadContentHtml(fileUrl, "http://localhost");
    }

    @Override
    public void vpaidViewPageFinished() {
        if (isDestroyed()) {
            return;
        }

        vpaidBridge.setCreativeDataAndLoad(adParameters);
    }

    @Override
    public void vpaidViewRenderProcessGone() {
        if (isDestroyed()) {
            return;
        }
        Logger.d(TAG, "Render process gone");
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
        Logger.d(TAG, "pauseWebView " + adWebView.toString());

        try {
            adWebView.onPause();
        } catch (Exception e) {
            Logger.e(TAG, e);
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
        Logger.d(TAG, "resumeWebView " + adWebView.toString());

        try {
            adWebView.onResume();
        } catch (Exception e) {
            Logger.e(TAG, e);
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
        if (isDestroyed()) {
            return;
        }

        vpaidBridge.fireStartAdEvent();
    }

    @Override
    public void setVolume(int volume) {
        if (isDestroyed()) {
            return;
        }

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
