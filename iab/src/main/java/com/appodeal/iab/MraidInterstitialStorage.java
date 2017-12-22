package com.appodeal.iab;


import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class MraidInterstitialStorage {
    private static final Map<String, WeakReference<MraidInterstitial>> storage = new HashMap<>();

    static synchronized void saveWithId(@NonNull String id, @NonNull MraidInterstitial interstitial) {
        storage.put(id, new WeakReference<>(interstitial));
    }

    static synchronized String save(@NonNull MraidInterstitial interstitial) {
        String id = UUID.randomUUID().toString();
        storage.put(id, new WeakReference<>(interstitial));
        return id;
    }

    static synchronized MraidInterstitial get(String id) {
        if (id == null) {
            return null;
        }
        WeakReference<MraidInterstitial> objectWeakReference = storage.get(id);
        if (objectWeakReference != null) {
            storage.remove(id);
            return objectWeakReference.get();
        } else {
            return null;
        }
    }
}
