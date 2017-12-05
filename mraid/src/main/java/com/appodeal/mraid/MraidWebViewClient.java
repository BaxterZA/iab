package com.appodeal.mraid;


import android.annotation.TargetApi;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

class MraidWebViewClient extends WebViewClient {

    private static final String MRAID_JS = "mraid.js";
    private static final String MRAID_JS_SCRIPT = "javascript:" + Assets.mraidJs;
    private final MraidWebViewListener listener;

    MraidWebViewClient(MraidWebViewListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("deprecation")
    @Override
    public WebResourceResponse shouldInterceptRequest(@NonNull final WebView view,  @NonNull final String url) {
        if (matchesInjectionUrl(url)) {
            if (listener != null) {
                listener.onMraidRequested();
            }
            return createMraidInjectionResponse();
        } else {
            return super.shouldInterceptRequest(view, url);
        }
    }

    @VisibleForTesting boolean matchesInjectionUrl(@NonNull final String url) {
        final Uri uri = Uri.parse(url.toLowerCase(Locale.US));
        return MRAID_JS.equals(uri.getLastPathSegment());
    }

    private WebResourceResponse createMraidInjectionResponse() {
        InputStream data = new ByteArrayInputStream(MRAID_JS_SCRIPT.getBytes());
        return new WebResourceResponse("text/javascript", "UTF-8", data);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        MraidLog.d("onPageFinished: " + url);
        if (listener != null) {
            listener.onPageFinished();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        MraidLog.d("onReceivedError: " + description);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    @TargetApi(26)
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        MraidLog.d(String.format("onRenderProcessGone didCrash: %s", detail.didCrash()));
        if (listener != null) {
            listener.onRenderProcessGone();
        }
        return true;
    }

    @Override
    @TargetApi(24)
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.hasGesture()) {
            if (listener != null) {
                listener.onTouch();
            }
        }
        return shouldOverrideUrlLoading(view, request.getUrl().toString());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        MraidLog.d("shouldOverrideUrlLoading: " + url);
        if (listener != null) {
            if (url.startsWith("mraid://")) {
                listener.onProcessCommand(url);
            } else {
                listener.onProcessCommand(String.format("mraid://open?url=%s", url));
            }
        }
        return true;
    }
}
