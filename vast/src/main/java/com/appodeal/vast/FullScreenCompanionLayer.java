package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appodeal.mraid.CircleCountdownView;
import com.appodeal.mraid.MraidInterstitial;
import com.appodeal.mraid.MraidInterstitialListener;
import com.appodeal.mraid.MraidNativeFeature;
import com.appodeal.mraid.MraidNativeFeatureListener;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class FullScreenCompanionLayer extends CompanionLayer {
    private MraidInterstitial mraidInterstitial;
    private boolean canClose;
    private boolean mraidCompanionLoaded;

    public FullScreenCompanionLayer(Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionListener listener) {
        super(context, vastConfig, listener);

        if (companion != null) {
            mraidInterstitial = companion.createMraidInterstitial(context);
            mraidInterstitial.setMraidInterstitialListener(createMraidInterstitialListener());
            mraidInterstitial.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), createMraidNativeFeatureListener());
            mraidInterstitial.setCloseTimeInterval(vastConfig.getCompanionCloseTime());
            mraidInterstitial.load();
        }

    }

    boolean canClose() {
        return canClose;
    } //TODO attemptToClose from activity

    void showCompanion() {
        setVisibility(VISIBLE);
        listener.onCompanionShown(companion);

        if (mraidCompanionLoaded) {
            mraidInterstitial.showInActivity((Activity) getContext());
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
            LayoutParams lastFrameParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lastFrameParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            frame.setLayoutParams(lastFrameParams);
            frame.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompanionClicked(companion, vastConfig.getVideoClicks().getClickThrough());
                }
            });
            LayoutParams layoutParams =  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.addView(frame, 0, layoutParams);

            int buttonSize = Math.round(50 * VastTools.getScreenDensity(context));
            CircleCountdownView repeatButton = ViewHelper.createRepeatButton(context, buttonSize, vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor());
            repeatButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRepeatButtonClicked();
                }
            });
            this.addView(repeatButton);

            CircleCountdownView closeButton = ViewHelper.createCloseButton(context, buttonSize, new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor());
            closeButton.setImage(VastTools.getBitmapFromBase64(VastTools.close));
            closeButton.setOnClickListener(new OnClickListener() {
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
        if (mraidInterstitial != null) {
            mraidInterstitial.destroy();
            mraidInterstitial = null;
        }
    }

    private MraidInterstitialListener createMraidInterstitialListener() {
        return new MraidInterstitialListener() {
            @Override
            public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {
                mraidCompanionLoaded = true;
            }

            @Override
            public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {
                canClose = true;
                listener.onCompanionClosed(companion);
            }
        };
    }

    private MraidNativeFeatureListener createMraidNativeFeatureListener() {
        return new MraidNativeFeatureListener() {
            @Override
            public void mraidNativeFeatureSendSms(String url) {

            }

            @Override
            public void mraidNativeFeatureCallTel(String url) {

            }

            @Override
            public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {

            }

            @Override
            public void mraidNativeFeaturePlayVideo(String url) {

            }

            @Override
            public void mraidNativeFeatureStorePicture(String url) {

            }

            @Override
            public void mraidNativeFeatureOpenBrowser(String url) {
                listener.onCompanionClicked(companion, url);
            }

            @Override
            public Location mraidNativeFeatureGetLocation() {
                return null;
            }
        };
    }
}
