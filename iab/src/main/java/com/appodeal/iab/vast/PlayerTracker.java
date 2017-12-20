package com.appodeal.iab.vast;


import android.os.Handler;

import com.appodeal.iab.Logger;

public class PlayerTracker implements Runnable {
    private final static String TAG = "PlayerTracker";
    public static final int VIDEO_PROGRESS_TIMER_INTERVAL = 50;

    private final Handler handler;
    private final PlayerLayerInterface playerLayer;
    private final VastViewController viewController;
    private boolean shouldStop;

    PlayerTracker(Handler handler, VastViewController viewController, PlayerLayerInterface playerLayer) {
        this.handler = handler;
        this.viewController = viewController;
        this.playerLayer = playerLayer;
        Logger.d(TAG, "PlayerTracker created");
    }

    void start() {
        handler.postDelayed(this, VIDEO_PROGRESS_TIMER_INTERVAL);
    }

    void stop() {
        Logger.d(TAG, "PlayerTracker stopped");
        shouldStop = true;
    }

    @Override
    public void run() {
        viewController.setCurrentProgress(playerLayer.getCurrentPosition());
        if (!shouldStop) {
            start();
        }
    }
}
