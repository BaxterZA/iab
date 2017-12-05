package com.appodeal.vast;


import android.os.Handler;

class MediaFileTracker implements Runnable {
    private static final long VIDEO_PROGRESS_TIMER_INTERVAL = 50;

    private final Handler handler;
    private final MediaFileLayerInterface mediaFileLayer;
    private final VastViewController viewController;
    private boolean shouldStop;

    MediaFileTracker(Handler handler, VastViewController viewController, MediaFileLayerInterface mediaFileLayer) {
        this.handler = handler;
        this.viewController = viewController;
        this.mediaFileLayer = mediaFileLayer;
    }

    void start() {
        handler.postDelayed(this, VIDEO_PROGRESS_TIMER_INTERVAL);
    }

    void stop() {
        shouldStop = true;
    }

    @Override
    public void run() {
        viewController.setCurrentProgress(mediaFileLayer.getCurrentPosition());
        if (!shouldStop) {
            start();
        }
    }
}
