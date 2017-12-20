package com.appodeal.iab;


public interface VastViewListener {

    void onVastLoaded(VastView view);
    void onVastFailedToLoad(VastView view);
    void onVastFailedToShow(VastView view);
    void onVastShown(VastView view);
    void onVastClicked(VastView view, String url);
    void onVastFinished(VastView view);
}
