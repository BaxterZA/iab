package com.appodeal.vast;


import android.content.Context;

public class VastInterstitial implements VastControllerListener {
    private Context context;
    private VastController controller;
    private VastInterstitialListener vastInterstitialListener;

    public VastInterstitial(Context context) {
        this.context = context;
        this.controller = new VastController(context, VastTools.getDisplayAspectRatio(context), VastType.FULLSCREEN);
        this.controller.setVastControllerListener(this);
    }

    public void setVastInterstitialListener(VastInterstitialListener vastInterstitialListener) {
        this.vastInterstitialListener = vastInterstitialListener;
    }

    public VastInterstitialListener getVastInterstitialListener() {
        return vastInterstitialListener;
    }

    public void loadXml(String xml) {
        controller.loadXml(xml);
    }

    public void loadUrl(String url) {
        controller.loadUrl(url);
    }

    public void show() {
        if (controller != null && controller.isLoaded()) {
            String id = VastInterstitialStorage.save(this);
            VastActivity.startIntent(context, id);
        } else if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastFailedToShow(this);
            destroy();
        }
    }

    public void destroy() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
        vastInterstitialListener = null;
        context = null;
    }

    public boolean isDestroyed() {
        return controller == null || controller.isDestroyed();
    }

    public boolean isLoaded() {
        return controller != null && controller.isLoaded();
    }

    @Override
    public void onVastControllerLoaded(VastController vastController) {
        if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastLoaded(this);
        }
    }

    @Override
    public void onVastControllerFailedToLoad(VastController vastController) {
        if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastFailedToLoad(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerFailedToShow(VastController vastController) {
        if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastFailedToShow(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerShown(VastController vastController) {

    }

    @Override
    public void onVastControllerClicked(VastController vastController, String url) {

    }

    @Override
    public void onVastControllerCompleted(VastController vastController) {

    }

    @Override
    public void onVastControllerClosed(VastController vastController) {

    }

    VastController getController() {
        return controller;
    }

}
