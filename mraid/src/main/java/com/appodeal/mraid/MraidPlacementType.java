package com.appodeal.mraid;


import java.util.Locale;

enum MraidPlacementType {
    INLINE,
    INTERSTITIAL;

    String toMraidString() {
        return toString().toLowerCase(Locale.US);
    }
}
