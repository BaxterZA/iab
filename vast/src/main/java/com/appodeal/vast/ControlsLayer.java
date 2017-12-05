package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class ControlsLayer extends RelativeLayout {

    interface ControlsLayerListener {
        void onMuteButtonClicked();
        void onCtaButtonClicked();
        void onCloseButtonClicked();
        void onRepeatButtonClicked();
    }

    private static final String defaultCtaText = "Learn more";
    private CircleCountdownView closeButton;
    private CircleCountdownView repeatButton;
    private CircleCountdownView muteButton;
    private TextView ctaButton;
    private LinearCountdownView progressBar;
    private Extensions extensions;
    private int skipTime;

    public ControlsLayer(Context context, @NonNull VastConfig vastConfig, int skipTime, @NonNull final ControlsLayerListener listener) {
        super(context);
        this.skipTime = skipTime;

        extensions = vastConfig.getExtensions();
        int buttonSize = Math.round(50 * VastTools.getScreenDensity(context));
        if (extensions != null) {
            int assetsColor = extensions.getAssetsColor();
            int assetsBackgroundColor = extensions.getAssetsBackgroundColor();
            Pair<Integer, Integer> skipButtonAlignment = extensions.getClosePosition();
            closeButton = createCloseButton(context, buttonSize, skipButtonAlignment.first, skipButtonAlignment.second, assetsColor, assetsBackgroundColor);
            repeatButton = createRepeatButton(context, buttonSize, assetsColor, assetsBackgroundColor);

            Pair<Integer, Integer> muteButtonAlignment = extensions.getMutePosition();
            muteButton = createMuteButton(context, buttonSize, muteButtonAlignment.first, muteButtonAlignment.second, assetsColor, assetsBackgroundColor);

            Pair<Integer, Integer> ctaButtonAlignment = extensions.getCtaPosition();
            String ctaText = extensions.getCtaText() != null ? extensions.getCtaText() : defaultCtaText;
            ctaButton = createCtaButton(context, ctaButtonAlignment.first, ctaButtonAlignment.second, assetsColor, assetsBackgroundColor, ctaText);

            if (extensions.isVideoClickable()) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCtaButtonClicked();
                    }
                });
            }

            progressBar = createProgressBar(context, assetsColor);
        } else {
            int assetsColor = VastTools.assetsColor;
            int assetsBackgroundColor = VastTools.backgroundColor;
            closeButton = createCloseButton(context, buttonSize, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP, assetsColor, assetsBackgroundColor);
            repeatButton = createRepeatButton(context, buttonSize, assetsColor, assetsBackgroundColor);
            muteButton = createMuteButton(context, buttonSize, RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT, assetsColor, assetsBackgroundColor);
            ctaButton = createCtaButton(context, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM, assetsColor, assetsBackgroundColor, defaultCtaText);
            progressBar = createProgressBar(context, assetsColor);
        }

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

        repeatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRepeatButtonClicked();
            }
        });
        addView(repeatButton);

        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseButtonClicked();
            }
        });
        addView(closeButton);

        addView(progressBar);
    }

    private CircleCountdownView createCloseButton(Context context, int size, int horizontalPosition, int verticalPosition, int assetsColor, int assetsBackgroundColor) {
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);
        layoutParams.addRule(horizontalPosition);
        layoutParams.addRule(verticalPosition);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        CircleCountdownView closeButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        closeButton.setLayoutParams(layoutParams);
        return closeButton;
    }
    
    private CircleCountdownView createRepeatButton(Context context, int size, int assetsColor, int assetsBackgroundColor) {
        CircleCountdownView repeatButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        LayoutParams bannerRepeatButtonParams = new RelativeLayout.LayoutParams(size, size);
        bannerRepeatButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        bannerRepeatButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        bannerRepeatButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        repeatButton.setLayoutParams(bannerRepeatButtonParams);
        repeatButton.setImage(VastTools.getBitmapFromBase64(VastTools.repeat));
        return repeatButton;
    }

    private CircleCountdownView createMuteButton(Context context, int size, int horizontalPosition, int verticalPosition, int assetsColor, int assetsBackgroundColor) {
        CircleCountdownView muteButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        LayoutParams muteButtonParams = new RelativeLayout.LayoutParams(size, size);
        muteButtonParams.addRule(horizontalPosition);
        muteButtonParams.addRule(verticalPosition);
        muteButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        muteButton.setLayoutParams(muteButtonParams);
        muteButton.setImage(VastTools.getBitmapFromBase64(VastTools.mute));
        return muteButton;
    }
    
    private TextView createCtaButton(Context context, int verticalPosition, int horizontalPosition, int assetsColor, int assetsBackgroundColor, String text) {
        LayoutParams learnMoreTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        learnMoreTextParams.setMargins(0, 0, 5, 5);
        TextView ctaButton = new TextView(context);

        ctaButton.setTextColor(assetsColor);
        ctaButton.setGravity(Gravity.CENTER_VERTICAL);
        ctaButton.setShadowLayer(6.0f, 0.0f, 0.0f, VastTools.shadowColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ctaButton.setBackground(getButtonBackground(assetsBackgroundColor));
        } else {
            //noinspection deprecation
            ctaButton.setBackgroundDrawable(getButtonBackground(assetsBackgroundColor));
        }
        ctaButton.setPadding(30, 10, 30, 10);
        learnMoreTextParams.addRule(verticalPosition);
        learnMoreTextParams.addRule(horizontalPosition);
        ctaButton.setText(text);
        ctaButton.setLayoutParams(learnMoreTextParams);
        return ctaButton;
    }
    
    private LinearCountdownView createProgressBar(Context context, int assetsColor) {
        LinearCountdownView progressBar = new LinearCountdownView(context, assetsColor);
        LayoutParams vastCountdownParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        vastCountdownParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(vastCountdownParams);
        progressBar.changePercentage(0);
        return progressBar;
    }

    private Drawable getButtonBackground(int assetsBackgroundColor) {
        GradientDrawable backgroundShape =  new GradientDrawable();
        backgroundShape.setShape(GradientDrawable.RECTANGLE);
        backgroundShape.setColor(assetsBackgroundColor);
        backgroundShape.setCornerRadius(100);
        return backgroundShape;
    }

    void updateMuteButton(boolean muted) {
        if (muted) {
            muteButton.setImage(VastTools.getBitmapFromBase64(VastTools.unmute));
        } else {
            muteButton.setImage(VastTools.getBitmapFromBase64(VastTools.mute));
        }
    }

    void updateButtons(int newPercentage, int currentPosition) {
        progressBar.changePercentage(newPercentage);

        if (currentPosition >= skipTime) {
            closeButton.setImage(VastTools.getBitmapFromBase64(VastTools.close));
        } else {
            int percent = skipTime != 0 ? 100 * currentPosition / skipTime : 100;
            int counterValue = (int) Math.ceil((skipTime - currentPosition) / 1000F);
            closeButton.changePercentage(percent, counterValue);
        }
    }

    void videoStart(VastType vastType) {
        switch (vastType) {
            case VIEW:
                repeatButton.setVisibility(GONE);
                closeButton.setVisibility(GONE);
                progressBar.setVisibility(GONE);

                if (extensions != null && !extensions.canShowMute()) {
                    muteButton.setVisibility(GONE);
                } else {
                    muteButton.setVisibility(VISIBLE);
                }

                if (extensions != null && !extensions.canShowCta()) {
                    ctaButton.setVisibility(GONE);
                } else {
                    ctaButton.setVisibility(VISIBLE);
                }

                break;

            case FULLSCREEN:
                repeatButton.setVisibility(GONE);
                closeButton.setVisibility(VISIBLE);
                progressBar.setVisibility(VISIBLE);

                if (extensions != null && !extensions.canShowMute()) {
                    muteButton.setVisibility(GONE);
                } else {
                    muteButton.setVisibility(VISIBLE);
                }

                if (extensions != null && !extensions.canShowCta()) {
                    ctaButton.setVisibility(GONE);
                } else {
                    closeButton.setVisibility(VISIBLE);
                }

                break;
        }
    }

    void videoComplete() {
        repeatButton.setVisibility(VISIBLE);
        muteButton.setVisibility(GONE);
        closeButton.setVisibility(GONE);
        ctaButton.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

    void showCompanionControls(VastType vastType) {
        ctaButton.setVisibility(VISIBLE);
        if (vastType == VastType.FULLSCREEN) {
            closeButton.setVisibility(VISIBLE);
        }
    }

    void destroy() {
        repeatButton = null;
        muteButton = null;
        closeButton = null;
        ctaButton = null;
        progressBar = null;
    }
}
