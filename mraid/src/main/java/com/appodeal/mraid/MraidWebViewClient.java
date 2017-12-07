package com.appodeal.mraid;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

class MraidWebViewClient extends AdWebViewClient {
    private static final String MRAID_JS = "mraid.js";
    private static final String MRAID_JS_SCRIPT = "javascript:" + Assets.mraidJs;

    MraidWebViewClient(AdWebViewListener listener) {
        super(listener);
    }

    @SuppressWarnings("deprecation")
    @Override
    public WebResourceResponse shouldInterceptRequest(@NonNull final WebView view, @NonNull final String url) {
        if (matchesInjectionUrl(url)) {
            if (listener != null) {
                listener.onMraidRequested();
            }
            return createMraidInjectionResponse();
        } else {
            return super.shouldInterceptRequest(view, url);
        }
    }


    @VisibleForTesting
    boolean matchesInjectionUrl(@NonNull final String url) {
        final Uri uri = Uri.parse(url.toLowerCase(Locale.US));
        return MRAID_JS.equals(uri.getLastPathSegment());
    }

    private WebResourceResponse createMraidInjectionResponse() {
        InputStream data = new ByteArrayInputStream(MRAID_JS_SCRIPT.getBytes());
        return new WebResourceResponse("text/javascript", "UTF-8", data);
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
