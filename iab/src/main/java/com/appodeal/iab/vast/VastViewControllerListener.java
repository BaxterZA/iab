package com.appodeal.iab.vast;


public interface VastViewControllerListener {

    void onVastControllerLoaded(VastViewController vastViewController);

    void onVastControllerFailedToLoad(VastViewController vastViewController);

    void onVastControllerStarted(VastViewController vastViewController);

    void onVastControllerClicked(VastViewController vastViewController, String url);

    void onVastControllerCompleted(VastViewController vastViewController);

    void onVastControllerClosed(VastViewController vastViewController);
}
