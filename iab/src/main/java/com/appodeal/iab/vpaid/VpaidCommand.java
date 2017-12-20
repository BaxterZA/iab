package com.appodeal.iab.vpaid;


import android.support.annotation.NonNull;


enum  VpaidCommand {
    VPAID_AD_STARTED("AdStarted"),
    VPAID_AD_STOPPED("AdStopped"),
    VPAID_AD_SKIPPED("AdSkipped"),
    VPAID_AD_LOADED("AdLoaded"),
    VPAID_AD_LINEAR_CHANGE("AdLinearChange"),
    VPAID_AD_SIZE_CHANGE("AdSizeChange"),
    VPAID_AD_EXPANDED_CHANGE("AdExpandedChange"),
    VPAID_AD_SKIPPABLE_STATE_CHANGE("AdSkippableStateChange"),
    VPAID_AD_DURATION_CHANGE("AdDurationChange"),
    VPAID_AD_VOLUME_CHANGE("AdVolumeChange"),
    VPAID_AD_IMPRESSION("AdImpression"),
    VPAID_AD_CLICK_THRU("AdClickThru") {
        @Override
        boolean requiresClick() {
            return true;
        }
    },
    VPAID_AD_INTERACTION("AdInteraction"),
    VPAID_AD_VIDEO_START("AdVideoStart"),
    VPAID_AD_VIDEO_FIRST_QUARTILE("AdVideoFirstQuartile"),
    VPAID_AD_VIDEO_MIDPOINT("AdVideoMidpoint"),
    VPAID_AD_VIDEO_THIRD_QUARTILE("AdVideoThirdQuartile"),
    VPAID_AD_VIDEO_COMPLETE("AdVideoComplete"),
    VPAID_AD_USER_ACCEPT_INVITATION("AdUserAcceptInvitation"),
    VPAID_AD_USER_MINIMIZE("AdUserMinimize"),
    VPAID_AD_USER_CLOSE("AdUserClose"){
        @Override
        boolean requiresClick() {
            return true;
        }
    },
    VPAID_AD_PAUSED("AdPaused"),
    VPAID_AD_PLAYING("AdPlaying"),
    VPAID_AD_ERROR("AdError"),
    VPAID_AD_LOG("AdLog"),
    VPAID_AD_REMAINING_TIME("AdRemainingTime"),
    UNKNOWN("");

    @NonNull
    private final String name;

    VpaidCommand(@NonNull String nameString) {
        name = nameString;
    }

    @NonNull
    public String getName() {
        return name;
    }

    boolean requiresClick() {
        return false;
    }
}
