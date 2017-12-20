package com.appodeal.iab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.appodeal.iab.vast.VastTools;
import com.appodeal.iab.vast.VastViewController;
import com.appodeal.iab.vast.VastViewControllerListener;


public class VastActivity extends Activity implements VastViewControllerListener {
    private final static String TAG = "VastActivity";
    public static final String ID = "com.appodeal.vast.VastViewController.id";
    private VastInterstitial vastInterstitial;
    private VastViewController controller;
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
            Logger.e(TAG, String.format("Vast interstitial with id %s not found", interstitialId));
            finish();
            return;
        }

        if (vastInterstitial.isDestroyed()) {
            Logger.e(TAG, String.format("Vast interstitial with id %s was destroyed", interstitialId));
            finish();
            return;
        }

        interstitialListener = vastInterstitial.getVastInterstitialListener();
        controller = vastInterstitial.getController();
        if (controller == null) {
            Logger.e(TAG, "Vast controller not found");
            closeActivity();
            return;
        }

        controller.attachActivity(this);
        controller.setListener(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        ViewGroup vastView = controller.getView();
        VastTools.removeFromParent(vastView);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setContentView(vastView, layoutParams);

        controller.start();
    }

    private void closeActivity() {
        if (interstitialListener != null) {
            interstitialListener.onVastClosed(vastInterstitial);
        }
        vastInterstitial.destroy();
        vastInterstitial = null;
        controller = null;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (controller != null) {
            controller.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (controller != null) {
            controller.resume();
        }
    }

    @Override
    public void onBackPressed() {
        if (controller != null) {
            controller.attemptToClose();
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
    public void onVastControllerLoaded(VastViewController vastViewController) {
        //repeat clicked
        vastViewController.start();
    }

    @Override
    public void onVastControllerFailedToLoad(VastViewController vastViewController) {

    }

    @Override
    public void onVastControllerFailedToShow(VastViewController vastViewController) {
        closeActivity();
    }

    @Override
    public void onVastControllerShown(VastViewController vastViewController) {
        if (interstitialListener != null) {
            interstitialListener.onVastShown(vastInterstitial);
        }
    }

    @Override
    public void onVastControllerClicked(VastViewController vastViewController, String url) {
        if (interstitialListener != null) {
            interstitialListener.onVastClicked(vastInterstitial, url);
        }
    }

    @Override
    public void onVastControllerCompleted(VastViewController vastViewController) {
        if (interstitialListener != null) {
            interstitialListener.onVastFinished(vastInterstitial);
        }
    }

    @Override
    public void onVastControllerClosed(VastViewController vastViewController) {
        closeActivity();
    }
}
