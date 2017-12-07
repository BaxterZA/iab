package com.appodeal.mraid;

import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class AdWebChromeClient extends WebChromeClient {
    private AdWebViewDebugListener debugListener;

    public AdWebChromeClient(AdWebViewDebugListener debugListener) {
        super();
        this.debugListener = debugListener;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (consoleMessage == null || consoleMessage.message() == null) {
            return false;
        }
        if (!consoleMessage.message().contains("Uncaught ReferenceError")) {
            MraidLog.i("JS console", consoleMessage.message() + (consoleMessage.sourceId() == null ? "" : " at " + consoleMessage.sourceId()) + ":" + consoleMessage.lineNumber());
        }
        return debugListener == null || debugListener.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (debugListener != null) {
            return debugListener.onJsAlert(message, result);
        }
        MraidLog.d("JS alert", message);
        return handlePopups(result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        MraidLog.d("JS confirm", message);
        return handlePopups(result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        MraidLog.d("JS prompt", message);
        return handlePopups(result);
    }

    private boolean handlePopups(JsResult result) {
        // Cancel all popups
        result.cancel();
        return true;
    }
}
