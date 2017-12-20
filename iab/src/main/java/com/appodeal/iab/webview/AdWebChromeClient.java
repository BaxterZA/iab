package com.appodeal.iab.webview;

import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.appodeal.iab.WebViewDebugListener;
import com.appodeal.iab.Logger;

public class AdWebChromeClient extends WebChromeClient {
    private final static String TAG = "AdWebChromeClient";

    private WebViewDebugListener debugListener;

    public AdWebChromeClient(WebViewDebugListener debugListener) {
        super();
        this.debugListener = debugListener;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (consoleMessage == null || consoleMessage.message() == null) {
            return false;
        }
        if (!consoleMessage.message().contains("Uncaught ReferenceError")) {
            Logger.i(TAG, "onConsoleMessage: " + consoleMessage.message() + (consoleMessage.sourceId() == null ? "" : " at " + consoleMessage.sourceId()) + ":" + consoleMessage.lineNumber());
        }
        return debugListener == null || debugListener.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (debugListener != null) {
            return debugListener.onJsAlert(message, result);
        }
        Logger.i(TAG, "onJsAlert: " + message);
        return handlePopups(result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Logger.i(TAG, "onJsConfirm: " + message);
        return handlePopups(result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Logger.i(TAG, "onJsPrompt: " + message);
        return handlePopups(result);
    }

    private boolean handlePopups(JsResult result) {
        // Cancel all popups
        result.cancel();
        return true;
    }
}
