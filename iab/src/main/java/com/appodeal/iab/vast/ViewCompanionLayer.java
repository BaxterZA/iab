package com.appodeal.iab.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appodeal.iab.Logger;
import com.appodeal.iab.mraid.MraidViewController;
import com.appodeal.iab.views.CircleCountdownView;
import com.appodeal.iab.views.ViewHelper;

@SuppressLint("ViewConstructor")
public class ViewCompanionLayer extends CompanionLayer {
    private final static String TAG = "ViewCompanionLayer";

    private MraidViewController mraidViewController;

    public ViewCompanionLayer(Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionLayer.CompanionListener listener) {
        super(context, vastConfig, listener);

        if (companion != null) {
            mraidViewController = ResourceViewHelper.createViewController(context, companion, new ResourceViewHelper.ResourceListener() {
                @Override
                public void onClick(String url) {
                    listener.onCompanionClicked(companion, url);
                }

                @Override
                public void onClose() {
                }
            });
            mraidViewController.load();
        }
    }

    @Override
    void showCompanion() {
        setVisibility(VISIBLE);
        listener.onCompanionShown(companion);

        if (mraidViewController != null && mraidViewController.isLoaded()) {
            RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            this.addView(mraidViewController.getMraidView(), 0, layoutParams);
        } else {
            Logger.d(TAG, "companion is missing or not ready, show frame");
            showInlineCompanionFromFrame(getContext());
        }
    }

    @Override
    boolean canClose() {
        return true;
    }

    void showInlineCompanionFromFrame(Context context) {
        removeAllViews();

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, vastConfig.getMediaFileLocalUri());
        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(vastConfig.getDuration() / 2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if (bmFrame == null) {
            listener.onCompanionClosed(null);
        } else {
            ImageView frame = new ImageView(context);
            frame.setImageBitmap(bmFrame);
            frame.setAdjustViewBounds(true);
            frame.setScaleType(ImageView.ScaleType.FIT_CENTER);
            RelativeLayout.LayoutParams lastFrameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lastFrameParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            frame.setLayoutParams(lastFrameParams);
            frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompanionClicked(companion, vastConfig.getVideoClicks().getClickThrough());
                }
            });
            RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            this.addView(frame, 0, layoutParams);

            int buttonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
            CircleCountdownView repeatButton = ViewHelper.createRepeatButton(context, buttonSize, vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor());
            repeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRepeatButtonClicked();
                }
            });
            this.addView(repeatButton);
        }
    }

    void destroy() {
        super.destroy();
        if (mraidViewController != null) {
            mraidViewController.destroy();
            mraidViewController = null;
        }
    }
}
