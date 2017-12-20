package com.appodeal.iab.vast;


public enum VastViewControllerState {
    /**
     * Controller was created
     */
    CREATED,

    /**
     * Controller is loading
     */
    LOADING,

    /**
     * Controller is ready to show.
     */
    READY,

    /**
     * Video is showing.
     */
    VIDEO_SHOWING,

    /**
     * Companion is showing.
     */
    COMPANION_SHOWING,

    /**
     * Controller is destroyed.
     */
    DESTROYED
}
