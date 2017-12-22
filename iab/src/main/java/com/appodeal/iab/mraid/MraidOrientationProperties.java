package com.appodeal.iab.mraid;


class MraidOrientationProperties {
    public final boolean allowOrientationChange;
    public final MraidOrientation forceOrientation;

    MraidOrientationProperties() {
        this(true, MraidOrientation.NONE);
    }

    MraidOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) {
        this.allowOrientationChange = allowOrientationChange;
        this.forceOrientation = forceOrientation;
    }
}
