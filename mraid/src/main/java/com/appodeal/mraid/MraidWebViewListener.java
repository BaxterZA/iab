package com.appodeal.mraid;


interface MraidWebViewListener {

    void onRenderProcessGone();
    void onTouch();
    void onPageFinished();
    void onProcessCommand(String url);
    void onMraidRequested();
}
