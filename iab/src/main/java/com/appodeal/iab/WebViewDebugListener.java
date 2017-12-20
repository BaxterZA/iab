package com.appodeal.iab;


import android.support.annotation.NonNull;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;

/**
 * Listener can be used to debug creative, see {@link android.webkit.WebChromeClient}
 */
public interface WebViewDebugListener {

    /**
     * Called when javascript try to display alert dialog
     * @param message alert message
     * @param result {@link JsResult}
     * @return see {@link android.webkit.WebChromeClient}
     */
    boolean onJsAlert(@NonNull String message, @NonNull JsResult result);

    /**
     * Called when javascript log to console
     * @param consoleMessage log message
     * @return see {@link android.webkit.WebChromeClient}
     */
    boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage);
}
