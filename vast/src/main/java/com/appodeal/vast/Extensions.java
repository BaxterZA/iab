package com.appodeal.vast;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.widget.RelativeLayout;

class Extensions {
    private final String ctaText;
    private final boolean showCta;
    private final boolean showMute;
    private final boolean showCompanion;
    private final boolean showProgress;
    private final boolean videoClickable;
    private final int companionCloseTime;
    private final int CtaXPosition;
    private final int CtaYPosition;
    private final int CloseXPosition;
    private final int CloseYPosition;
    private final int MuteXPosition;
    private final int MuteYPosition;
    private final int assetsColor;
    private final int assetsBackgroundColor;
    private final Companion companion;

    private Extensions(Builder builder) {
        this.ctaText = builder.ctaText;
        this.showCta = builder.showCta;
        this.showMute = builder.showMute;
        this.showCompanion = builder.showCompanion;
        this.showProgress = builder.showProgress;
        this.videoClickable = builder.videoClickable;
        this.companionCloseTime = builder.companionCloseTime;
        this.CtaXPosition = builder.CtaXPosition;
        this.CtaYPosition = builder.CtaYPosition;
        this.CloseXPosition = builder.CloseXPosition;
        this.CloseYPosition = builder.CloseYPosition;
        this.MuteXPosition = builder.MuteXPosition;
        this.MuteYPosition = builder.MuteYPosition;
        this.assetsColor = builder.assetsColor;
        this.assetsBackgroundColor = builder.assetsBackgroundColor;
        this.companion = builder.companion;
    }

    String getCtaText() {
        return ctaText;
    }

    boolean canShowCta() {
        return showCta;
    }

    boolean canShowMute() {
        return showMute;
    }

    boolean canShowCompanion() {
        return showCompanion;
    }

    boolean canShowProgress() {
        return showProgress;
    }

    int getCompanionCloseTime() {
        return companionCloseTime;
    }

    boolean isVideoClickable() {
        return videoClickable;
    }

    Pair<Integer, Integer> getCtaPosition() {
        return new Pair<>(CtaXPosition, CtaYPosition);
    }

    @NonNull Pair<Integer, Integer> getClosePosition() {
        return new Pair<>(CloseXPosition, CloseYPosition);
    }

    Pair<Integer, Integer> getMutePosition() {
        return new Pair<>(MuteXPosition, MuteYPosition);
    }

    int getAssetsColor() {
        return assetsColor;
    }

    int getAssetsBackgroundColor() {
        return assetsBackgroundColor;
    }

    Companion getCompanion() {
        return companion;
    }

    static class Builder {
        private String ctaText;
        private boolean showCta = true;
        private boolean showMute = true;
        private boolean showCompanion = true;
        private boolean showProgress = true;
        private boolean videoClickable;
        private int companionCloseTime;
        private int CtaXPosition = RelativeLayout.ALIGN_PARENT_RIGHT;
        private int CtaYPosition = RelativeLayout.ALIGN_PARENT_BOTTOM;
        private int CloseXPosition = RelativeLayout.ALIGN_PARENT_RIGHT;
        private int CloseYPosition = RelativeLayout.ALIGN_PARENT_TOP;
        private int MuteXPosition = RelativeLayout.ALIGN_PARENT_LEFT;
        private int MuteYPosition = RelativeLayout.ALIGN_PARENT_TOP;
        private int assetsColor = VastTools.assetsColor;
        private int assetsBackgroundColor = Color.TRANSPARENT;
        private Companion companion;

        Builder() {
        }

        void setCtaText(String ctaText) {
            this.ctaText = ctaText;
        }

        void setShowCta(boolean showCta) {
            this.showCta = showCta;
        }

        void setShowMute(boolean showMute) {
            this.showMute = showMute;
        }

        void setShowCompanion(boolean showCompanion) {
            this.showCompanion = showCompanion;
        }

        void setShowProgress(boolean showProgress) {
            this.showProgress = showProgress;
        }

        void setVideoClickable(boolean videoClickable) {
            this.videoClickable = videoClickable;
        }

        void setCompanionCloseTime(int companionCloseTime) {
            this.companionCloseTime = companionCloseTime;
        }

        void setCtaXPosition(int ctaXPosition) {
            CtaXPosition = ctaXPosition;
        }

        void setCtaYPosition(int ctaYPosition) {
            CtaYPosition = ctaYPosition;
        }

        void setCloseXPosition(int closeXPosition) {
            CloseXPosition = closeXPosition;
        }

        void setCloseYPosition(int closeYPosition) {
            CloseYPosition = closeYPosition;
        }

        void setMuteXPosition(int muteXPosition) {
            MuteXPosition = muteXPosition;
        }

        void setMuteYPosition(int muteYPosition) {
            MuteYPosition = muteYPosition;
        }

        void setAssetsColor(int assetsColor) {
            this.assetsColor = assetsColor;
        }

        void setAssetsBackgroundColor(int assetsBackgroundColor) {
            this.assetsBackgroundColor = assetsBackgroundColor;
        }

        void setCompanion(Companion companion) {
            this.companion = companion;
        }

        Extensions build() {
            return new Extensions(this);
        }
    }
}
