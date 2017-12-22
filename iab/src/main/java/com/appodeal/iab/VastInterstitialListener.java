package com.appodeal.iab;


public interface VastInterstitialListener {

    /**
     * Called when interstitial was loaded.
     * @param vastInterstitial {@link VastInterstitial}
     */
    void onVastLoaded(VastInterstitial vastInterstitial);

    /**
     * Called is interstitial is fail to load. After that interstitial can't be used.
     * @param vastInterstitial {@link VastInterstitial}
     */
    void onVastFailedToLoad(VastInterstitial vastInterstitial);

    /**
     * Called when interstitial failed to show. After that interstitial can't be used.
     * @param vastInterstitial {@link VastInterstitial}
     */
    void onVastShown(VastInterstitial vastInterstitial);

    /**
     * Called when interstitial was shown.
     * @param vastInterstitial {@link VastInterstitial}
     */
    void onVastFailedToShow(VastInterstitial vastInterstitial);

    /**
     * Called when interstitial was clicked.
     * @param vastInterstitial {@link VastInterstitial}
     * @param url target link
     */
    void onVastClicked(VastInterstitial vastInterstitial, String url);

    /**
     * Called when interstitial finished.
     * @param vastInterstitial {@link VastInterstitial}
     */
    void onVastFinished(VastInterstitial vastInterstitial);

    /**
     * Called when interstitial was closed. After that interstitial can't be used.
     * @param vastInterstitial {@link VastInterstitial}
     */
    void onVastClosed(VastInterstitial vastInterstitial);
}
