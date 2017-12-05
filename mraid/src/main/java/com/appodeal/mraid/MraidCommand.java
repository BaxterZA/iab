package com.appodeal.mraid;


import android.support.annotation.NonNull;

enum  MraidCommand {
    CLOSE("close"),
    UNLOAD("unload"),
    EXPAND("expand") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return placementType == MraidPlacementType.INLINE;
        }
    },
    USE_CUSTOM_CLOSE("useCustomClose"),
    OPEN("open") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return true;
        }
    },
    SET_RESIZE_PROPERTIES("setResizeProperties"),
    RESIZE("resize") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return true;
        }
    },
    SET_ORIENTATION_PROPERTIES("setOrientationProperties"),
    PLAY_VIDEO("playVideo") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return placementType == MraidPlacementType.INLINE;
        }
    },
    STORE_PICTURE("storePicture") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return true;
        }
    },
    CREATE_CALENDAR_EVENT("createCalendarEvent") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return true;
        }
    },
    JS_TAG_LOADED("loaded"),
    JS_TAG_NO_FILL("noFill"),
    VPAID_AD_CLICK_THRU("AdClickThru") {
        @Override
        boolean requiresClick(@NonNull final MraidPlacementType placementType) {
            return true;
        }
    },
    VPAID_AD_ERROR("AdError"),
    VPAID_AD_IMPRESSION("AdImpression"),
    VPAID_AD_PAUSED("AdPaused"),
    VPAID_AD_PLAYING("AdPlaying"),
    VPAID_AD_VIDEO_COMPLETE("AdVideoComplete"),
    VPAID_AD_VIDEO_FIRST_QUARTILE("AdVideoFirstQuartile"),
    VPAID_AD_VIDEO_MIDPOINT("AdVideoMidpoint"),
    VPAID_AD_VIDEO_START("AdVideoStart"),
    VPAID_AD_VIDEO_THIRD_QUARTILE("AdVideoThirdQuartile"),
    VPAID_AD_DURATION("AdDuration"),
    UNKNOWN("");

    @NonNull private final String name;

    MraidCommand(@NonNull String nameString) {
        name = nameString;
    }

    @NonNull
    public String getName() {
        return name;
    }

    boolean requiresClick(@NonNull MraidPlacementType placementType) {
        return false;
    }
}
