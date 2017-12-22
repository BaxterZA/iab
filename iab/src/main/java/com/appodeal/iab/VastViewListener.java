package com.appodeal.iab;


public interface VastViewListener {

    /**
     * Called when vast view was loaded
     * @param view {@link VastView}
     */
    void onVastLoaded(VastView view);

    /**
     * Called if vast view is fail to load. After that mraid view can't be used.
     * @param view {@link VastView}
     */
    void onVastFailedToLoad(VastView view);

    /**
     * Called when vast view start playing.
     * @param view {@link VastView}
     */
    void onVastStarted(VastView view);

    /**
     * Called if vast view was clicked.
     * @param view {@link VastView}
     */
    void onVastClicked(VastView view, String url);

    /**
     * Called when vast view finished.
     * @param view {@link VastView}
     */
    void onVastFinished(VastView view);
}
