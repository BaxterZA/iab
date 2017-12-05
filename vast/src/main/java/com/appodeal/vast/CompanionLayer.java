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

@SuppressLint("ViewConstructor")
public class CompanionLayer extends RelativeLayout {
    private CompanionListener listener;

    interface CompanionListener {
        void onCompanionClicked(@Nullable Companion companion);
        void onCompanionShown(@Nullable Companion companion);
    }

    private Companion companion;

    public CompanionLayer(Context context, @NonNull VastConfig vastConfig, @NonNull final CompanionListener listener) {
        super(context);
        this.listener = listener;
        if (vastConfig.getExtensions() != null && vastConfig.getExtensions().getCompanion() != null) {
            companion = vastConfig.getExtensions().getCompanion();
        } else {
            companion = vastConfig.getCompanion();
        }

        View companionView;
        if (companion == null) {
            companionView = getCompanionFromFrame(context, vastConfig);
        } else {
            companionView = companion.getView(context);

        }
        addView(companionView, new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        companionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCompanionClicked(companion);
            }
        });
    }

    void showCompanion() {
        this.setVisibility(VISIBLE);
        listener.onCompanionShown(companion);
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
    }
}
