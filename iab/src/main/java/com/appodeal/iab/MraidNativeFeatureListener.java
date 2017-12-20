package com.appodeal.iab;


import android.location.Location;

/**
 * Interface to handle native feature events
 */
public interface MraidNativeFeatureListener {

    /**
     * Handle sms send event
     * @param url event param
     */
    void mraidNativeFeatureSendSms(String url);

    /**
     * Handle call event
     * @param url event param
     */
    void mraidNativeFeatureCallTel(String url);

    /**
     * Handle create calendar event
     * @param eventJSON event param
     */
    void mraidNativeFeatureCreateCalendarEvent(String eventJSON);

    /**
     * Handle play video event
     * @param url video url
     */
    void mraidNativeFeaturePlayVideo(String url);

    /**
     * Handle store picture event
     * @param url picture url
     */
    void mraidNativeFeatureStorePicture(String url);

    /**
     * Handle Mraid open event
     * @param url target url
     */
    void mraidNativeFeatureOpenBrowser(String url);

    /**
     * Pass location to ad creative
     * @return current location
     */
    Location mraidNativeFeatureGetLocation();
}
