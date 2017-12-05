package com.appodeal.mraid;


public interface MraidViewListener {

    /**
     * Called when mraidView was loaded
     * @param mraidView {@link MraidView}
     */
    void onMraidViewLoaded(MraidView mraidView);

    /**
     * Called if mraidView is fail to load. After that mraid view can't be used.
     * @param mraidView {@link MraidView}
     */
    void onMraidViewFailedToLoad(MraidView mraidView);

    /**
     * Called when mraidView was unloaded. After that mraid view can't be used.
     * @param mraidView {@link MraidView}
     */
    void onMraidViewUnloaded(MraidView mraidView);

    /**
     * Called when mraidView was expanded.
     * @param mraidView {@link MraidView}
     */
    void onMraidViewExpanded(MraidView mraidView);

    /**
     * Called when mraidView was resized.
     * @param mraidView {@link MraidView}
     */
    void onMraidViewResized(MraidView mraidView);

    /**
     * Called when mraidView was clicked.
     * @param mraidView {@link MraidView}
     */
    void onMraidViewClicked(MraidView mraidView);

    /**
     * Called when mraidView was closed from expand or from resize.
     * @param mraidView {@link MraidView}
     */
    void onMraidViewClosed(MraidView mraidView);
}
