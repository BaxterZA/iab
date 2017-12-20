package com.appodeal.iab.vast;


class ProgressEvent {
    private final int offsetTime;
    private final String trackingURL;

    ProgressEvent(int offsetTime, String trackingURL) {
        this.offsetTime = offsetTime;
        this.trackingURL = trackingURL;
    }

    int getOffsetTime() {
        return offsetTime;
    }

    String getTrackingURL() {
        return trackingURL;
    }
}
