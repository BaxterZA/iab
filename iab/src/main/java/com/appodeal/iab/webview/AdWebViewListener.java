package com.appodeal.iab.webview;


public interface AdWebViewListener {

    void onRenderProcessGone();
    void onTouch();
    void onPageFinished();
    void onProcessCommand(String url);
    void onMraidRequested();
}
