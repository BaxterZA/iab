package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appodeal.mraid.CloseableLayout;
import com.appodeal.mraid.MraidView;

@SuppressLint("ViewConstructor")
public class CompanionLayer extends RelativeLayout {
    private CompanionListener listener;
    private int closeTime;
    private CloseableLayout closeableLayout;
    private boolean canClose;
    private View companionView;

    interface CompanionListener {
        void onCompanionShown(@Nullable Companion companion);
        void onCompanionClicked(@Nullable Companion companion, String url);
        void onCompanionClosed(@Nullable Companion companion);
    }

    private Companion companion;

    public CompanionLayer(Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionListener listener) {
        super(context);
        this.listener = listener;
        Extensions extensions = vastConfig.getExtensions();
        int assetsColor = VastTools.assetsColor;
        int assetsBackgroundColor = VastTools.backgroundColor;
        if (extensions != null) {
            assetsColor = extensions.getAssetsColor();
            assetsBackgroundColor = extensions.getAssetsBackgroundColor();
            if (extensions.getCompanion() != null){
                companion = extensions.getCompanion();
            } else{
                companion = vastConfig.getCompanion();
            }
            closeTime = extensions.getCompanionCloseTime();
        } else {
            companion = vastConfig.getCompanion();
        }

        closeableLayout = new CloseableLayout(context, assetsColor, assetsBackgroundColor);
        if (companion == null) {
            companionView = getCompanionFromFrame(context, vastConfig);
            companionView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompanionClicked(companion, vastConfig.getVideoClicks().getClickThrough());
                }
            });
        } else {
            companionView = companion.getView(context, listener);
        }
        RelativeLayout.LayoutParams layoutParams =  new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        closeableLayout.addView(companionView, 0, layoutParams);
        closeableLayout.setOnCloseListener(new CloseableLayout.OnCloseListener() {
            @Override
            public void onClose() {
                listener.onCompanionClosed(companion);
            }
        });
        addView(closeableLayout, layoutParams);

    }

    boolean canClose() {
        return canClose;
    }

    void showCompanion(VastType vastType) {
        setVisibility(VISIBLE);
        listener.onCompanionShown(companion);

        if (vastType == VastType.FULLSCREEN) {
            if (closeTime > 0) {
                closeableLayout.startTimer(closeTime);
                closeableLayout.setSkippableStateListener(new CloseableLayout.SkippableStateListener() {
                    @Override
                    public void onSkippableStateChange() {
                        canClose = true;
                    }
                });
            } else {
                canClose = true;
                closeableLayout.showCloseButton();
            }
        }
    }

    boolean hasCompanion() {
        return companion != null;
    }

    View getCompanionFromFrame(Context context, VastConfig vastConfig) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, vastConfig.getMediaFileLocalUri());
        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(vastConfig.getDuration() / 2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if (bmFrame != null) {
            ImageView frame = new ImageView(context);
            frame.setImageBitmap(bmFrame);
            frame.setAdjustViewBounds(true);
            frame.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LayoutParams lastFrameParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lastFrameParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            frame.setLayoutParams(lastFrameParams);
            return frame;
        }
        return new View(context);
    }

    void destroy() {
        listener = null;
        removeAllViews();
        if (companionView != null && companionView instanceof MraidView) {
            ((MraidView) companionView).destroy();
            companionView = null;
        }
    }
}
