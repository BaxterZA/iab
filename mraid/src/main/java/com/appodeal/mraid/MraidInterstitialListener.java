package com.appodeal.mraid;


public interface MraidInterstitialListener {

    /**
     * Called when interstitial was loaded.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial);

    /**
     * Called is interstitial is fail to load. After that interstitial can't be used.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial);

    /**
     * Called when interstitial was shown.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialShown(MraidInterstitial mraidInterstitial);
    
    /**
     * Called when interstitial failed to show. After that interstitial can't be used.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial);

    /**
     * Called when interstitial was unloaded. After that interstitial can't be used.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial);

    /**
     * Called when interstitial was clicked.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial);
    
    /**
     * Called when interstitial was closed. After that interstitial can't be used.
     * @param mraidInterstitial {@link MraidInterstitial}
     */
    void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial);
}
