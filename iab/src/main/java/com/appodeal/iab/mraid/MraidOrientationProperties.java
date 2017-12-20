package com.appodeal.iab.mraid;


class MraidOrientationProperties {
    public boolean allowOrientationChange;
    public MraidOrientation forceOrientation;

    MraidOrientationProperties() {
        this(true, MraidOrientation.NONE);
    }

    MraidOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) {
        this.allowOrientationChange = allowOrientationChange;
        this.forceOrientation = forceOrientation;
    }
}
