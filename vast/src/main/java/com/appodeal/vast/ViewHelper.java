package com.appodeal.vast;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Pair;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.mraid.CircleCountdownView;

public class ViewHelper {

    static TextView createCtaButton(Context context, Pair<Integer, Integer> position, int assetsColor, int assetsBackgroundColor, String text) {
        RelativeLayout.LayoutParams learnMoreTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
        learnMoreTextParams.addRule(position.first);
        learnMoreTextParams.addRule(position.second);
        ctaButton.setText(text);
        ctaButton.setLayoutParams(learnMoreTextParams);
        return ctaButton;
    }

    private static Drawable getButtonBackground(int assetsBackgroundColor) {
        GradientDrawable backgroundShape =  new GradientDrawable();
        backgroundShape.setShape(GradientDrawable.RECTANGLE);
        backgroundShape.setColor(assetsBackgroundColor);
        backgroundShape.setCornerRadius(100);
        return backgroundShape;
    }

    static CircleCountdownView createCloseButton(Context context, int size, Pair<Integer, Integer> position, int assetsColor, int assetsBackgroundColor) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);
        layoutParams.addRule(position.first);
        layoutParams.addRule(position.second);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        CircleCountdownView closeButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        closeButton.setLayoutParams(layoutParams);
        return closeButton;
    }

    static CircleCountdownView createMuteButton(Context context, int size, Pair<Integer, Integer> position, int assetsColor, int assetsBackgroundColor) {
        CircleCountdownView muteButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        RelativeLayout.LayoutParams muteButtonParams = new RelativeLayout.LayoutParams(size, size);
        muteButtonParams.addRule(position.first);
        muteButtonParams.addRule(position.second);
        muteButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        muteButton.setLayoutParams(muteButtonParams);
        muteButton.setImage(VastTools.getBitmapFromBase64(VastTools.mute));
        return muteButton;
    }

    static CircleCountdownView createRepeatButton(Context context, int size, int assetsColor, int assetsBackgroundColor) {
        CircleCountdownView repeatButton = new CircleCountdownView(context, assetsColor, assetsBackgroundColor);
        RelativeLayout.LayoutParams bannerRepeatButtonParams = new RelativeLayout.LayoutParams(size, size);
        bannerRepeatButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        bannerRepeatButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        bannerRepeatButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        repeatButton.setLayoutParams(bannerRepeatButtonParams);
        repeatButton.setImage(VastTools.getBitmapFromBase64(VastTools.repeat));
        return repeatButton;
    }

    static LinearCountdownView createProgressBar(Context context, int assetsColor) {
        LinearCountdownView progressBar = new LinearCountdownView(context, assetsColor);
        RelativeLayout.LayoutParams vastCountdownParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        vastCountdownParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(vastCountdownParams);
        progressBar.changePercentage(0);
        return progressBar;
    }
}
