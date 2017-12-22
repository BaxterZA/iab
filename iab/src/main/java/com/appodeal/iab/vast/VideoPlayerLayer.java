package com.appodeal.iab.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.appodeal.iab.views.LinearCountdownView;
import com.appodeal.iab.views.ViewHelper;

@SuppressLint("ViewConstructor")
class VideoPlayerLayer extends RelativeLayout implements PlayerLayerInterface {
    private final VideoView videoView;
    private final VastConfig vastConfig;
    private LinearCountdownView progressBar;
    private int playerPositionInMills;
    private final PlayerLayerListener listener;

    VideoPlayerLayer(@NonNull Context context, @NonNull final VastConfig vastConfig, @NonNull Uri mediaFileLocalUri, @NonNull final PlayerLayerListener listener) {
        super(context);
        this.vastConfig = vastConfig;
        this.listener = listener;
        videoView = new VideoView(context, vastConfig, mediaFileLocalUri, listener);

        addView(videoView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (vastConfig.canShowProgress()) {
            progressBar = ViewHelper.createProgressBar(context, vastConfig.getAssetsColor());
            addView(progressBar);
        }
    }

    @Override
    public void load() {
        videoView.load();
    }

    @Override
    public int getCurrentPosition() {
        int currentPosition = 0;
        if (videoView != null) {
            currentPosition = videoView.getCurrentPosition();
        }

        if (progressBar != null) {
            int newPercentage = 100 * currentPosition / vastConfig.getDuration();
            progressBar.changePercentage(newPercentage);
        }

        int newPercentage = 100 * currentPosition / vastConfig.getDuration();
        int previousPercentage = 100 * playerPositionInMills / vastConfig.getDuration();

        if (previousPercentage < 25 && newPercentage >= 25) {
            listener.onFirstQuartile();
        } else if (previousPercentage < 50 && newPercentage >= 50) {
            listener.onMidpoint();
        } else if (previousPercentage < 75 && newPercentage >= 75) {
            listener.onThirdQuartile();
        }

        playerPositionInMills = currentPosition;
        return currentPosition;
    }


    @Override
    public void start() {
        if (videoView != null) {
            videoView.start();
        }
    }

    @Override
    public void resume() {
        if (videoView != null) {
            videoView.resume();
        }
    }

    @Override
    public void pause() {
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    public void destroy() {
        removeAllViews();
        if (videoView != null) {
            videoView.destroy();
        }
        progressBar = null;
    }

    @Override
    public void setVolume(int volume) {
        if (videoView != null) {
            videoView.setVolume(volume);
        }
    }

    @Override
    public View getView() {
        return this;
    }
}
