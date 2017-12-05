package com.appodeal.vast;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;
import java.util.Map;

class VastConfig {
    @VisibleForTesting List<MediaFile> mediaFiles;
    @VisibleForTesting List<MediaFile> interactiveCreativeFiles;
    @VisibleForTesting List<Companion> companions;
    private Extensions extensions;
    private List<Icon> icons;
    private String adParameters;
    private int skipOffset;
    private int duration;
    private Map<TrackingEventsType, List<String>> trackingEventMap;
    private List<ProgressEvent> progressTrackingList;
    private List<String> errorTracking;
    private List<String> impressionTracking;
    private VideoClicks videoClicks;
    private List<String> viewableViewableImpressions;
    private List<String> notViewableViewableImpression;
    private List<String> viewUndeterminedViewableImpression;

    private MediaFile mediaFile;
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
            CompanionPicker companionPicker = new CompanionPicker(mediaFile.getWidth() / mediaFile.getHeight());
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
        return companion;
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
}
