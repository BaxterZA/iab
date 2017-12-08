package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
class VideoPlayerLayer extends RelativeLayout implements PlayerLayerInterface{
    private VideoView videoView;
    private VastConfig vastConfig;
    private LinearCountdownView progressBar;

    VideoPlayerLayer(@NonNull Context context, @NonNull final VastConfig vastConfig, @NonNull Uri mediaFileLocalUri, @NonNull final PlayerLayerListener listener) {
        super(context);
        this.vastConfig = vastConfig;
        videoView = new VideoView(context, vastConfig, mediaFileLocalUri, listener);

        addView(videoView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int assetsColor = VastTools.assetsColor;
        Extensions extensions = vastConfig.getExtensions();
        if (extensions != null) {
            assetsColor = extensions.getAssetsColor();
        }
        progressBar = createProgressBar(context, assetsColor);
        addView(progressBar);
    }

    private LinearCountdownView createProgressBar(Context context, int assetsColor) {
        LinearCountdownView progressBar = new LinearCountdownView(context, assetsColor);
        LayoutParams vastCountdownParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        vastCountdownParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(vastCountdownParams);
        progressBar.changePercentage(0);
        return progressBar;
    }

    @Override
    public int getCurrentPosition() {
        int currentPosition = 0;
        if (videoView != null) {
            currentPosition =  videoView.getCurrentPosition();
        }

        if (progressBar != null) {
            int newPercentage = 100 * currentPosition / vastConfig.getDuration();
            progressBar.changePercentage(newPercentage);
        }
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
