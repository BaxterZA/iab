package com.appodeal.iab.vast;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

class MediaFilePicker {
    private final float adAspectRatio;

    MediaFilePicker(float adAspectRatio) {
        this.adAspectRatio = adAspectRatio;
    }

    @Nullable
    MediaFile getBestMediaFile(@NonNull List<MediaFile> mediaFiles, @NonNull List<MediaFile> interactiveFiles) {
        MediaFile bestMediaFile = null;
        double bestAspectRatio = 0;
        for (MediaFile mediaFile : mediaFiles) {
            if (mediaFile.isValidVideoFile()) {
                float mediaFileAspectRatio = (float) mediaFile.getWidth() / mediaFile.getHeight();
                if (bestMediaFile == null || Math.abs(bestAspectRatio - adAspectRatio) > Math.abs(mediaFileAspectRatio - adAspectRatio)) {
                    bestMediaFile = mediaFile;
                    bestAspectRatio = mediaFileAspectRatio;
                }
            }
        }

        if (bestMediaFile == null && interactiveFiles.size() > 0) {
            for (MediaFile interactiveFile : interactiveFiles) {
                if (interactiveFile.isValidVPAIDMediaFile()) {
                    bestMediaFile = interactiveFile;
                    break;
                }
            }
        }

        return bestMediaFile;
    }
}
