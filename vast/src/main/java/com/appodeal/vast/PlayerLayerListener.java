package com.appodeal.vast;


public interface PlayerLayerListener {

    void onLoaded();
    void onFailedToLoad();
    void onError();
    void onStarted();
    void onFirstQuartile();
    void onMidpoint();
    void onThirdQuartile();
    void onComplete();
    void onClick(String url);

}
