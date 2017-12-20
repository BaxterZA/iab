package com.appodeal.iab.mraid;


import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

import com.appodeal.iab.Logger;

class AudioVolumeContentObserver extends ContentObserver {
    private final static String TAG = "AudioVolumeContentObserver";

    private float lastVolumePercentage = -1;
    private final Context context;
    private final MraidCommandListener listener;

    AudioVolumeContentObserver(Handler handler, Context context, MraidCommandListener listener) {
        super(handler);
        this.context = context.getApplicationContext();
        this.listener = listener;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        float currentVolumePercentage = getCurrentPercentage();

        if (currentVolumePercentage != lastVolumePercentage) {
            lastVolumePercentage = currentVolumePercentage;
            Logger.d(TAG, String.format("Volume change, current value: %s", lastVolumePercentage));
            if (listener != null) {
                listener.onAudioVolumeChange(lastVolumePercentage);
            }
        }
    }

    float getCurrentPercentage() {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return currentVolume / maxVolume * 100;
    }
}
