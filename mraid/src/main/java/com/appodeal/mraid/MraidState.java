package com.appodeal.mraid;


import java.util.Locale;

enum MraidState {
    LOADING,
    DEFAULT,
    RESIZED,
    EXPANDED,
    HIDDEN;

    public String toMraidString() {
        return toString().toLowerCase(Locale.US);
    }
}
