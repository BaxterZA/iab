package com.appodeal.iab.vpaid;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;


import com.appodeal.iab.Logger;
import com.appodeal.iab.webview.AdWebChromeClient;
import com.appodeal.iab.webview.AdWebView;
import com.appodeal.iab.webview.AdWebViewListener;

import java.util.Map;

public class VpaidBridge implements AdWebViewListener {
    private final static String TAG = "VpaidBridge";
    private AdWebView adWebView;
    private VpaidCommandListener listener;
    private boolean isLoaded;
    private boolean wasTouched;

    VpaidBridge() {
    }

    void initVpaidWebView(AdWebView adWebView, VpaidCommandListener listener) {
        this.listener = listener;
        this.adWebView = adWebView;
        this.adWebView.setWebViewClient(new VpaidWebViewClient(this));
        this.adWebView.setWebChromeClient(new AdWebChromeClient(listener));
        adWebView.setListener(this);
    }

    void loadContentHtml(@NonNull String html, @Nullable String baseUrl) {
        isLoaded = false;
        adWebView.loadDataWithBaseURL(baseUrl, VpaidHtmlProcessor.processHtml(html), "text/html", "UTF-8", null);
    }

    boolean isLoaded() {
        return isLoaded;
    }

    @VisibleForTesting
    boolean wasTouched() {
        return wasTouched;
    }

    void destroy() {
        listener = null;
        adWebView = null;
    }

    @Override
    public void onRenderProcessGone() {
        if (listener != null) {
            listener.vpaidViewRenderProcessGone();
        }
    }

    @Override
    public void onTouch() {
        wasTouched = true;
    }

    @Override
    public void onPageFinished() {
        isLoaded = true;
        if (listener != null) {
            listener.vpaidViewPageFinished();
        }
    }

    @Override
    public void onProcessCommand(String url) {
        VpaidCommandProcessor vpaidCommandProcessor = new VpaidCommandProcessor();
        if (vpaidCommandProcessor.isCommandValid(url)) {
            VpaidCommand vpaidCommand = vpaidCommandProcessor.getVpaidCommand();
            Map<String, String> params = vpaidCommandProcessor.getParams();

            try {
                runCommand(vpaidCommand, params, vpaidCommandProcessor);
            } catch (VpaidError e) {
                Logger.e(TAG, e);
            }
        } else {
            Logger.d(TAG, String.format("Invalid VAPID command: %s", url));
        }
    }

    @Override
    public void onMraidRequested() {

    }

    private void runCommand(@NonNull final VpaidCommand command, @NonNull Map<String, String> params, @NonNull VpaidCommandProcessor vpaidCommandProcessor) throws VpaidError {
        if (command.requiresClick() && !wasTouched()) {
            throw new VpaidError("Cannot execute this command, view wasn't click");
        }

        if (listener == null) {
            throw new VpaidError("Can't find listener");
        }

        if (adWebView == null) {
            throw new VpaidError("The current WebView is being destroyed");
        }

        switch (command) {
            case VPAID_AD_STARTED:
                listener.vpaidAdStarted();
                break;
            case VPAID_AD_STOPPED:
                listener.vpaidAdStopped();
                break;
            case VPAID_AD_SKIPPED:
                listener.vpaidAdSkipped();
                break;
            case VPAID_AD_LOADED:
                listener.vpaidAdLoaded();
                break;
            case VPAID_AD_LINEAR_CHANGE:
                listener.vpaidAdLinearChange();
                break;
            case VPAID_AD_SIZE_CHANGE:
                listener.vpaidAdSizeChange();
                break;
            case VPAID_AD_EXPANDED_CHANGE:
                listener.vpaidAdExpandedChange();
                break;
            case VPAID_AD_SKIPPABLE_STATE_CHANGE:
                listener.vpaidAdSkippableStateChange(params.get("state"));
                break;
            case VPAID_AD_DURATION_CHANGE:
                listener.vpaidAdDurationChange(params.get("state"));
                break;
            case VPAID_AD_VOLUME_CHANGE:
                listener.vpaidAdDurationChange(params.get("state"));
                break;
            case VPAID_AD_IMPRESSION:
                listener.vpaidAdImpression();
                break;
            case VPAID_AD_CLICK_THRU:
                listener.vpaidAdClickThru(params.get("url"));
                break;
            case VPAID_AD_INTERACTION:
                listener.vpaidAdInteraction();
                break;
            case VPAID_AD_VIDEO_START:
                listener.vpaidAdVideoStart();
                break;
            case VPAID_AD_VIDEO_FIRST_QUARTILE:
                listener.vpaidAdVideoFirstQuartile();
                break;
            case VPAID_AD_VIDEO_MIDPOINT:
                listener.vpaidAdVideoMidpoint();
                break;
            case VPAID_AD_VIDEO_THIRD_QUARTILE:
                listener.vpaidAdVideoThirdQuartile();
                break;
            case VPAID_AD_VIDEO_COMPLETE:
                listener.vpaidAdVideoComplete();
                break;
            case VPAID_AD_USER_ACCEPT_INVITATION:
                listener.vpaidAdUserAcceptInvitation();
                break;
            case VPAID_AD_USER_MINIMIZE:
                listener.vpaidAdUserMinimize();
                break;
            case VPAID_AD_USER_CLOSE:
                listener.vpaidAdUserClose();
                break;
            case VPAID_AD_PAUSED:
                listener.vpaidAdPaused();
                break;
            case VPAID_AD_PLAYING:
                listener.vpaidAdPlaying();
                break;
            case VPAID_AD_ERROR:
                listener.vpaidAdError(params.get("msg"));
                break;
            case VPAID_AD_LOG:
                Logger.d(TAG, String.format("Vpaid event: %s, with params: %s", command.getName(), params));
                break;
            case VPAID_AD_REMAINING_TIME:
                listener.vpaidAdRemainingTime(params.get("time"));
                break;
            case UNKNOWN:
                throw new VpaidError("Unspecified VPAID command");
        }
    }

    private void injectJavaScript(@NonNull String javascript) {
        Logger.d(TAG, "evaluating js: " + javascript);
        adWebView.loadUrl("javascript:" + javascript);
    }

    void setCreativeDataAndLoad(String adParameters) {
        if (adParameters != null) {
            String js = "vpaid.setCreativeData(" + adParameters + ");";
            injectJavaScript(js);
        }
        injectJavaScript("vpaid.loadAd();");
    }

    void fireStartAdEvent() {
        injectJavaScript("vpaid.fireStartAdEvent();");
    }

    void fireAdResumeEvent() {
        injectJavaScript("vpaid.fireAdResumeEvent();");
    }

    void fireAdPauseEvent() {
        injectJavaScript("vpaid.fireAdPauseEvent();");
    }

    void setVolume(int value) {
        injectJavaScript("vpaid.setVolume(" + value + ");");
    }
}

