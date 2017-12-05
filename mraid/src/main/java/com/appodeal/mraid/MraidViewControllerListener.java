package com.appodeal.mraid;


interface MraidViewControllerListener {

    void onMraidViewControllerLoaded(MraidViewController mraidViewController);

    void onMraidViewControllerFailedToLoad(MraidViewController mraidViewController);

    void onMraidViewControllerUnloaded(MraidViewController mraidViewController);

    void onMraidViewControllerExpanded(MraidViewController mraidViewController);

    void onMraidViewControllerResized(MraidViewController mraidViewController);

    void onMraidViewControllerClicked(MraidViewController mraidViewController);

    void onMraidViewControllerClosed(MraidViewController mraidViewController);

}
