package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

@SuppressLint("ViewConstructor")
class VideoView extends TextureView implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
        TextureView.SurfaceTextureListener, PlayerLayerInterface {

    private MediaPlayer mediaPlayer;
    private final Uri mediaFileLocalUri;
    @NonNull private final PlayerLayerListener listener;

    private int videoWidth;
    private int videoHeight;


    VideoView(@NonNull Context context, @NonNull final VastConfig vastConfig, @NonNull Uri mediaFileLocalUri, @NonNull final PlayerLayerListener listener) {
        super(context);
        setSurfaceTextureListener(this);
        this.mediaFileLocalUri = mediaFileLocalUri;
        this.listener = listener;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, mediaFileLocalUri);

        videoWidth = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        videoHeight = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        Extensions extensions = vastConfig.getExtensions();
        if (extensions != null) {
            if (extensions.isVideoClickable()) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(vastConfig.getVideoClicks().getClickThrough());
                    }
                });
            }
        }
    }

    @Override
    public void load() {
        if (isVideoFileSupported()) {
            listener.onLoaded();
        } else {
            listener.onFailedToLoad();
        }
    }

    private boolean isVideoFileSupported() {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mediaFileLocalUri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        return thumb != null;
    }


    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource(getContext(), mediaFileLocalUri);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            VastLog.e(e.getMessage());
            sendError();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void resume() {
        if (mediaPlayer != null) {
            playVideo();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void destroy() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.setOnPreparedListener(null);
            mediaPlayer.setOnVideoSizeChangedListener(null);
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        setVisibility(GONE);
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void setVolume(int volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    private void sendError() {
        listener.onError();
    }


    //MediaPlayer Callbacks

    @Override
    public void onCompletion(MediaPlayer mp) {
        listener.onComplete();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        sendError();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playVideo();
    }

    void playVideo() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            listener.onStarted();
        }
    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;

        Matrix txform = new Matrix();
        getTransform(txform);
        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        txform.postTranslate(xoff, yoff);
        setTransform(txform);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        adjustAspectRatio(videoWidth, videoHeight);
    }

    //SurfaceCallbacks

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        try {
            Surface surface = new Surface(surfaceTexture);
            if (mediaPlayer != null) {
                mediaPlayer.setSurface(surface);
            } else {
                createMediaPlayer();
                mediaPlayer.setSurface(surface);

                prepareMediaPlayer();
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
            sendError();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
