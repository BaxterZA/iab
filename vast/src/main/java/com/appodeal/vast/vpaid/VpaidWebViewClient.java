package com.appodeal.vast.vpaid;

import android.webkit.WebView;

import com.appodeal.mraid.AdWebViewClient;
import com.appodeal.mraid.AdWebViewListener;
import com.appodeal.vast.VastLog;


public class VpaidWebViewClient extends AdWebViewClient {

    public VpaidWebViewClient(AdWebViewListener listener) {
        super(listener);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        VastLog.d("shouldOverrideUrlLoading: " + url);
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
