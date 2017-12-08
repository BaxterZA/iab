package com.appodeal.vast;


public interface PlayerLayerListener {

    void onStarted();
    void onLoaded();
    void onFailedToLoad();
    void onError();
    void onComplete();
    void onClick(String url);

}
