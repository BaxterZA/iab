package com.appodeal.iab;


import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class VastInterstitialStorage {
    private static final Map<String, WeakReference<VastInterstitial>> storage = new HashMap<>();

    static synchronized void saveWithId(@NonNull String id, @NonNull VastInterstitial interstitial) {
        storage.put(id, new WeakReference<>(interstitial));
    }

    static synchronized String save(@NonNull VastInterstitial interstitial) {
        String id = UUID.randomUUID().toString();
        storage.put(id, new WeakReference<>(interstitial));
        return id;
    }

    static synchronized VastInterstitial get(String id) {
        if (id == null) {
            return null;
        }
        WeakReference<VastInterstitial> objectWeakReference = storage.get(id);
        if (objectWeakReference != null) {
            storage.remove(id);
            return objectWeakReference.get();
        } else {
            return null;
        }
    }
}
