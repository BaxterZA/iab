package com.appodeal.vast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


public class VastActivity extends Activity implements VastControllerListener {
    public static final String ID = "com.appodeal.vast.VastViewController.id";
    private VastInterstitial vastInterstitial;
    private VastController vastController;
    private VastInterstitialListener interstitialListener;

    public static void startIntent(Context context, String id) {
        Intent intent = new Intent(context, VastActivity.class);
        intent.putExtra(ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressWarnings("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String interstitialId = getIntent().getStringExtra(ID);
        vastInterstitial = VastInterstitialStorage.get(interstitialId);

        if (vastInterstitial == null) {
            VastLog.e(String.format("Vast interstitial with id %s not found", interstitialId));
            finish();
            return;
        }

        if (vastInterstitial.isDestroyed()) {
            VastLog.e(String.format("Vast interstitial with id %s was destroyed", interstitialId));
            finish();
            return;
        }

        interstitialListener = vastInterstitial.getVastInterstitialListener();
        vastController = vastInterstitial.getController();
        if (vastController == null) {
            VastLog.e("Vast controller not found");
            closeActivity();
            return;
        }

        vastController.attachActivity(this);
        vastController.setVastControllerListener(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        ViewGroup vastView = vastController.getVastView();
        if (vastView.getParent() != null) {
            ((ViewGroup) vastView.getParent()).removeView(vastView);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setContentView(vastView, layoutParams);

        vastController.start();
    }

    private void closeActivity() {
        if (interstitialListener != null) {
            interstitialListener.onVastClosed(vastInterstitial);
        }
        vastInterstitial.destroy();
        vastInterstitial = null;
        vastController = null;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vastController != null) {
            vastController.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vastController != null) {
            vastController.resume();
        }
    }

    @Override
    public void onBackPressed() {
        if (vastController != null) {
            vastController.attemptToClose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vastInterstitial != null && !vastInterstitial.isDestroyed()) {
            VastInterstitialStorage.saveWithId(getIntent().getStringExtra(ID), vastInterstitial);
        }
    }

    @Override
    public void onVastControllerLoaded(VastController vastController) {
        //repeat clicked
        vastController.start();
    }

    @Override
    public void onVastControllerFailedToLoad(VastController vastController) {

    }

    @Override
    public void onVastControllerFailedToShow(VastController vastController) {
        closeActivity();
    }

    @Override
    public void onVastControllerShown(VastController vastController) {
        if (interstitialListener != null) {
            interstitialListener.onVastShown(vastInterstitial);
        }
    }

    @Override
    public void onVastControllerClicked(VastController vastController, String url) {
        if (interstitialListener != null) {
            interstitialListener.onVastClicked(vastInterstitial, url);
        }
    }

    @Override
    public void onVastControllerCompleted(VastController vastController) {
        if (interstitialListener != null) {
            interstitialListener.onVastFinished(vastInterstitial);
        }
    }

    @Override
    public void onVastControllerClosed(VastController vastController) {
        closeActivity();
    }
}
