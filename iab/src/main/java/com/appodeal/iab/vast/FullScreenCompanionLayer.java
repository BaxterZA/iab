package com.appodeal.iab.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appodeal.iab.mraid.MraidViewController;
import com.appodeal.iab.views.CircleCountdownView;
import com.appodeal.iab.views.CloseableLayout;
import com.appodeal.iab.views.ViewHelper;

@SuppressLint("ViewConstructor")
public class FullScreenCompanionLayer extends CompanionLayer {
    private MraidViewController mraidViewController;
    private boolean canClose;

    public FullScreenCompanionLayer(final Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionListener listener) {
        super(context, vastConfig, listener);

        if (companion != null) {
            mraidViewController = ResourceViewHelper.createInterstitialController(context, companion, new ResourceViewHelper.ResourceListener() {
                @Override
                public void onClick(String url) {
                    listener.onCompanionClicked(companion, url);
                }

                @Override
                public void onClose() {
                    listener.onCompanionClosed(companion);
                }
            });
            this.mraidViewController.load();
        }
    }

    boolean canClose() {
        return canClose;
    }

    void showCompanion() {
        setVisibility(VISIBLE);
        listener.onCompanionShown(companion);

        if (mraidViewController != null && mraidViewController.isLoaded()) {
            FrameLayout mraidView = mraidViewController.getMraidView();
            if (mraidView.getParent() != null) {
                ((ViewGroup) mraidView.getParent()).removeView(mraidView);
            }

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            CloseableLayout interstitialView = new CloseableLayout(getContext());
            interstitialView.addView(mraidView, 0, layoutParams);
            interstitialView.setOnCloseListener(new CloseableLayout.OnCloseListener() {
                @Override
                public void onClose() {
                    mraidViewController.onClose();
                }
            });
            interstitialView.setSkippableStateListener(new CloseableLayout.SkippableStateListener() {
                @Override
                public void onSkippableStateChange() {
                    canClose = true;
                }
            });
            interstitialView.startTimer(vastConfig.getCompanionCloseTime());
            addView(interstitialView, layoutParams);
        } else {
            canClose = true;
            showFullscreenCompanionFromFrame(getContext());
        }
    }

    void showFullscreenCompanionFromFrame(Context context) {
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

            CircleCountdownView closeButton = ViewHelper.createCloseButton(context, buttonSize, new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor());
            closeButton.setImage(ViewHelper.getBitmapFromBase64(ViewHelper.close));
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompanionClosed(null);
                }
            });
            this.addView(closeButton);
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
