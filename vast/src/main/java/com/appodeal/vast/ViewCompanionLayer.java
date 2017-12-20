package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appodeal.mraid.CircleCountdownView;
import com.appodeal.mraid.MraidNativeFeature;
import com.appodeal.mraid.MraidNativeFeatureListener;
import com.appodeal.mraid.MraidView;
import com.appodeal.mraid.MraidViewListener;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class ViewCompanionLayer extends CompanionLayer {
    private MraidView mraidView;
    private boolean mraidCompanionLoaded;

    public ViewCompanionLayer(Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionLayer.CompanionListener listener) {
        super(context, vastConfig, listener);

        if (companion != null) {
            mraidView = companion.createMraidView(getContext());
            mraidView.setMraidViewListener(createMraidViewListener());
            mraidView.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), createMraidNativeFeatureListener());
            mraidView.load();
        }
    }

    @Override
    void showCompanion() {
        setVisibility(VISIBLE);
        listener.onCompanionShown(companion);

        if (mraidCompanionLoaded) {
            LayoutParams layoutParams =  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.addView(mraidView, 0, layoutParams);
        } else {
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
        }
    }

    void destroy() {
        super.destroy();
        if (mraidView != null) {
            mraidView.destroy();
            mraidView = null;
        }
    }

    private MraidViewListener createMraidViewListener() {
        return new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {
                mraidCompanionLoaded = true;
            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
                mraidView.setVisibility(View.GONE);
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {
                mraidView.setVisibility(View.GONE);
            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

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
