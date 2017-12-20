package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.mraid.CircleCountdownView;

@SuppressLint("ViewConstructor")
public class ControlsLayer extends RelativeLayout {

    interface ControlsLayerListener {
        void onMuteButtonClicked();
        void onCtaButtonClicked();
        void onCloseButtonClicked();
    }

    private final VastConfig vastConfig;
    private final int skipTime;
    private CircleCountdownView closeButton;
    private CircleCountdownView muteButton;
    private TextView ctaButton;

    public ControlsLayer(Context context, @NonNull VastConfig vastConfig, int skipTime, @NonNull final ControlsLayerListener listener) {
        super(context);
        this.vastConfig = vastConfig;
        this.skipTime = skipTime;

        int buttonSize = Math.round(50 * VastTools.getScreenDensity(context));
        closeButton = ViewHelper.createCloseButton(context, buttonSize, vastConfig.getCloseButtonPosition(), vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor());
        muteButton = ViewHelper.createMuteButton(context, buttonSize, vastConfig.getMuteButtonPosition(), vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor());
        ctaButton = ViewHelper.createCtaButton(context, vastConfig.getCtaButtonPosition(), vastConfig.getAssetsColor(), vastConfig.getAssetsBackgroundColor(), vastConfig.getCtaText());

        ctaButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCtaButtonClicked();
            }
        });
        addView(ctaButton);

        muteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMuteButtonClicked();
            }
        });
        addView(muteButton);

        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseButtonClicked();
            }
        });
        addView(closeButton);
    }

    void updateMuteButton(boolean muted) {
        if (muteButton != null) {
            if (muted) {
                muteButton.setImage(VastTools.getBitmapFromBase64(VastTools.unmute));
            } else {
                muteButton.setImage(VastTools.getBitmapFromBase64(VastTools.mute));
            }
        }
    }

    void updateButtons(int currentPosition) {
        if (closeButton != null) {
            if (currentPosition >= skipTime) {
                closeButton.setImage(VastTools.getBitmapFromBase64(VastTools.close));
            } else {
                int percent = skipTime != 0 ? 100 * currentPosition / skipTime : 100;
                int counterValue = (int) Math.ceil((skipTime - currentPosition) / 1000F);
                closeButton.changePercentage(percent, counterValue);
            }
        }
    }

    void videoStart(VastType vastType) {
        if (vastConfig.canShowMute()) {
            muteButton.setVisibility(VISIBLE);
        } else {
            muteButton.setVisibility(GONE);
        }

        if (vastConfig.canShowCta()) {
            ctaButton.setVisibility(VISIBLE);
        } else {
            ctaButton.setVisibility(GONE);
        }

        switch (vastType) {
            case VIEW:
                closeButton.setVisibility(GONE);
                break;

            case FULLSCREEN:
                closeButton.setVisibility(VISIBLE);
                break;
        }
    }

    void destroy() {
        removeAllViews();
        muteButton = null;
        closeButton = null;
        ctaButton = null;
    }
}
