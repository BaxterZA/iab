package com.appodeal.iab.vpaid;

import android.webkit.WebView;

import com.appodeal.iab.Logger;
import com.appodeal.iab.webview.AdWebViewClient;
import com.appodeal.iab.webview.AdWebViewListener;


class VpaidWebViewClient extends AdWebViewClient {
    private final static String TAG = "VpaidWebViewClient";

    VpaidWebViewClient(AdWebViewListener listener) {
        super(listener);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.d(TAG, "shouldOverrideUrlLoading: " + url);
        if (listener != null) {
            if (url.startsWith("vpaid://")) {
                listener.onProcessCommand(url);
            } else {
                listener.onProcessCommand(String.format("vpaid://AdClickThru?url=%s", url));
            }
        }
        return true;
    }
}
