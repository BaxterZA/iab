package com.appodeal.vast;


interface MediaFileLayerListener {

    void onStarted();
    void onLoaded();
    void onFailedToLoad();
    void onError();
    void onComplete();

}
