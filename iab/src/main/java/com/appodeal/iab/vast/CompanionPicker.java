package com.appodeal.iab.vast;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

class CompanionPicker {
    private final float videoAspectRatio;

    CompanionPicker(float videoAspectRatio) {
        this.videoAspectRatio = videoAspectRatio;
    }

    @Nullable
    Companion getBestCompanion(@NonNull List<Companion> companions) {
        Companion bestCompanion = null;
        double bestAspectRatio = 0;

        for (Companion companion : companions) {
            if (!companion.isValid()) {
                continue;
            }

            float companionAspectRatio = (float) companion.getWidth() / companion.getHeight();
            if (Math.min(companion.getWidth(), companion.getHeight()) >= 250 && companionAspectRatio <= 2.5) {
                if (bestCompanion == null || Math.abs(bestAspectRatio - videoAspectRatio) > Math.abs(companionAspectRatio - videoAspectRatio)) {
                    bestCompanion = companion;
                    bestAspectRatio = companionAspectRatio;
                }
            }
        }

        return bestCompanion;
    }
}
