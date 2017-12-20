package com.appodeal.iab.webview;


import android.annotation.TargetApi;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appodeal.iab.Logger;

public class AdWebViewClient extends WebViewClient {
    private final static String TAG = "AdWebViewClient";

    protected final AdWebViewListener listener;

    public AdWebViewClient(AdWebViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Logger.d(TAG, "onPageFinished: " + url);
        if (listener != null) {
            listener.onPageFinished();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Logger.d(TAG, "onReceivedError: " + description);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    @TargetApi(26)
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        Logger.d(TAG, String.format("onRenderProcessGone didCrash: %s", detail.didCrash()));
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
