package com.appodeal.vast;


interface VastControllerListener {

    void onVastControllerLoaded(VastController vastController);

    void onVastControllerFailedToLoad(VastController vastController);

    void onVastControllerFailedToShow(VastController vastController);

    void onVastControllerShown(VastController vastController);

    void onVastControllerClicked(VastController vastController, String url);

    void onVastControllerCompleted(VastController vastController);

    void onVastControllerClosed(VastController vastController);
}
