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

public class AdWebViewClient extends WebViewClient {
    protected final AdWebViewListener listener;

    public AdWebViewClient(AdWebViewListener listener) {
        this.listener = listener;
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
}
