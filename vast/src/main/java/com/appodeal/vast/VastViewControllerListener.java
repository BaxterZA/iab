package com.appodeal.vast;


interface VastViewControllerListener {

    void onVastControllerLoaded(VastViewController vastViewController);

    void onVastControllerFailedToLoad(VastViewController vastViewController);

    void onVastControllerFailedToShow(VastViewController vastViewController);

    void onVastControllerShown(VastViewController vastViewController);

    void onVastControllerClicked(VastViewController vastViewController, String url);

    void onVastControllerCompleted(VastViewController vastViewController);

    void onVastControllerClosed(VastViewController vastViewController);
}
