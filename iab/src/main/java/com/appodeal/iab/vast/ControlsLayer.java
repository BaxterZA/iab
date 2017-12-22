package com.appodeal.iab.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.iab.views.CircleCountdownView;
import com.appodeal.iab.views.ViewHelper;

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

    public ControlsLayer(Context context, @NonNull VastConfig vastConfig, int skipTime, boolean isNonSkippable, @NonNull final ControlsLayerListener listener) {
        super(context);
        this.vastConfig = vastConfig;
        this.skipTime = skipTime;

        int buttonSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
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

        if (!isNonSkippable) {
            closeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCloseButtonClicked();
                }
            });
            addView(closeButton);
        }
    }

    void updateMuteButton(boolean muted) {
        if (muteButton != null) {
            if (muted) {
                muteButton.setImage(ViewHelper.getBitmapFromBase64(ViewHelper.unmute));
            } else {
                muteButton.setImage(ViewHelper.getBitmapFromBase64(ViewHelper.mute));
            }
        }
    }

    void updateButtons(int currentPosition) {
        if (closeButton != null) {
            if (currentPosition >= skipTime) {
                closeButton.setImage(ViewHelper.getBitmapFromBase64(ViewHelper.close));
            } else {
                int percent = skipTime != 0 ? 100 * currentPosition / skipTime : 100;
                int counterValue = (int) Math.ceil((skipTime - currentPosition) / 1000F);
                closeButton.changePercentage(percent, counterValue);
            }
        }
    }

    void videoStart(VastType vastType) {
        if (muteButton != null) {
            if (vastConfig.canShowMute()) {
                muteButton.setVisibility(VISIBLE);
            } else {
                muteButton.setVisibility(GONE);
            }
        }

        if (ctaButton != null) {
            if (vastConfig.canShowCta()) {
                ctaButton.setVisibility(VISIBLE);
            } else {
                ctaButton.setVisibility(GONE);
            }
        }

        if (closeButton != null) {
            switch (vastType) {
                case VIEW:
                    closeButton.setVisibility(GONE);
                    break;

                case FULLSCREEN:
                    closeButton.setVisibility(VISIBLE);
                    break;
            }
        }
    }

    void destroy() {
        removeAllViews();
        muteButton = null;
        closeButton = null;
        ctaButton = null;
    }
}
