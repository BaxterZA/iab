package com.appodeal.iab.vast;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Pair;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Map;

class VastConfig {
    @VisibleForTesting
    final List<MediaFile> mediaFiles;
    @VisibleForTesting
    final List<MediaFile> interactiveCreativeFiles;
    @VisibleForTesting
    final List<Companion> companions;
    private final Extensions extensions;
    private final List<Icon> icons;
    private final String adParameters;
    private final int skipOffset;
    private final int duration;
    private final Map<TrackingEventsType, List<String>> trackingEventMap;
    private final List<ProgressEvent> progressTrackingList;
    private final List<String> errorTracking;
    private final List<String> impressionTracking;
    private final VideoClicks videoClicks;
    private final List<String> viewableViewableImpressions;
    private final List<String> notViewableViewableImpression;
    private final List<String> viewUndeterminedViewableImpression;

    private final MediaFile mediaFile;
    private Companion companion;
    private Uri mediaFileLocalUri;

    VastConfig(@NonNull VastModel vastModel, float adAspectRatio, @NonNull String cacheDir) {
        mediaFiles = vastModel.getMediaFiles();
        companions = vastModel.getCompanions();
        extensions = vastModel.getExtensions();
        icons = vastModel.getIcons();
        duration = vastModel.getDuration();
        adParameters = vastModel.getAdParameters();
        skipOffset = vastModel.getSkipOffset();
        trackingEventMap = vastModel.getTrackingUrls();
        progressTrackingList = vastModel.getProgressTracking();
        errorTracking = vastModel.getErrorUrls();
        videoClicks = vastModel.getVideoClicks();
        impressionTracking = vastModel.getImpressions();
        viewableViewableImpressions = vastModel.getViewableViewableImpression();
        notViewableViewableImpression = vastModel.getNotViewableViewableImpression();
        viewUndeterminedViewableImpression = vastModel.getViewUndeterminedViewableImpression();
        interactiveCreativeFiles = vastModel.getInteractiveCreativeFiles();

        MediaFilePicker mediaFilePicker = new MediaFilePicker(adAspectRatio);
        mediaFile = mediaFilePicker.getBestMediaFile(mediaFiles, interactiveCreativeFiles);

        if (mediaFile != null) {
            CompanionPicker companionPicker = new CompanionPicker((float) mediaFile.getWidth() / mediaFile.getHeight());
            companion = companionPicker.getBestCompanion(companions);

            MediaFileLoader mediaFileLoader = new MediaFileLoader(cacheDir, mediaFile.getUrl());
            mediaFileLocalUri = mediaFileLoader.getLocalFileUri();
        }
    }

    boolean isPlayable() {
        return mediaFile != null && mediaFileLocalUri != null && duration > 0;
    }

    Extensions getExtensions() {
        return extensions;
    }

    List<Icon> getIcons() {
        return icons;
    }

    String getAdParameters() {
        return adParameters;
    }

    int getSkipOffset() {
        return skipOffset;
    }

    int getDuration() {
        return duration;
    }

    Map<TrackingEventsType, List<String>> getTrackingEventMap() {
        return trackingEventMap;
    }

    List<ProgressEvent> getProgressTrackingList() {
        return progressTrackingList;
    }

    List<String> getErrorTracking() {
        return errorTracking;
    }

    List<String> getImpressionTracking() {
        return impressionTracking;
    }

    VideoClicks getVideoClicks() {
        return videoClicks;
    }

    List<String> getViewableViewableImpressions() {
        return viewableViewableImpressions;
    }

    List<String> getNotViewableViewableImpression() {
        return notViewableViewableImpression;
    }

    List<String> getViewUndeterminedViewableImpression() {
        return viewUndeterminedViewableImpression;
    }

    MediaFile getMediaFile() {
        return mediaFile;
    }

    Companion getCompanion() {
        if (extensions != null && extensions.getCompanion() != null) {
            return extensions.getCompanion();
        } else {
            return companion;
        }
    }

    int getCompanionCloseTime() {
        if (extensions != null) {
            return extensions.getCompanionCloseTime();
        } else {
            return 0;
        }
    }

    Uri getMediaFileLocalUri() {
        return mediaFileLocalUri;
    }

    int getVideoOrientation() {
        if (mediaFile.getWidth() > mediaFile.getHeight()) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    int getCompanionOrientation() {
        if (companion != null) {
            if (companion.getWidth() > companion.getHeight()) {
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else {
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
        }
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    int getAssetsColor() {
        if (extensions != null) {
            return extensions.getAssetsColor();
        } else {
            return VastTools.assetsColor;
        }
    }

    int getAssetsBackgroundColor() {
        if (extensions != null) {
            return extensions.getAssetsBackgroundColor();
        } else {
            return VastTools.backgroundColor;
        }
    }

    Pair<Integer, Integer> getMuteButtonPosition() {
        if (extensions != null) {
            return extensions.getMutePosition();
        } else {
            return new Pair<>(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP);
        }
    }

    Pair<Integer, Integer> getCtaButtonPosition() {
        if (extensions != null) {
            return extensions.getCtaPosition();
        } else {
            return new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }

    Pair<Integer, Integer> getCloseButtonPosition() {
        if (extensions != null) {
            return extensions.getClosePosition();
        } else {
            return new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP);
        }
    }

    String getCtaText() {
        if (extensions != null) {
            return extensions.getCtaText();
        } else {
            return VastTools.defaultCtaText;
        }
    }

    boolean canShowCta() {
        return extensions == null || extensions.canShowCta();
    }

    boolean canShowMute() {
        return extensions == null || extensions.canShowMute();
    }

    boolean canShowCompanion() {
        return extensions == null || extensions.canShowCompanion();
    }

    boolean canShowProgress() {
        return extensions == null || extensions.canShowProgress();
    }
}
