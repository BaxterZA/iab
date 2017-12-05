package com.appodeal.vast;


public interface VastInterstitialListener {

    void onVastLoaded(VastInterstitial vastInterstitial);
    void onVastFailedToLoad(VastInterstitial vastInterstitial);
    void onVastFailedToShow(VastInterstitial vastInterstitial);
    void onVastShown(VastInterstitial vastInterstitial);
    void onVastClicked(VastInterstitial vastInterstitial, String url);
    void onVastFinished(VastInterstitial vastInterstitial);
    void onVastClosed(VastInterstitial vastInterstitial);
}
