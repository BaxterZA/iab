package com.appodeal.vast;


import android.content.Context;
import android.support.annotation.Nullable;

public class VastInterstitial implements VastLoader.VastLoaderListener, VastViewControllerListener {
    private Context context;
    private VastViewController controller;
    private VastInterstitialListener vastInterstitialListener;

    public VastInterstitial(Context context) {
        this.context = context;
    }

    public void setVastInterstitialListener(VastInterstitialListener vastInterstitialListener) {
        this.vastInterstitialListener = vastInterstitialListener;
    }

    public VastInterstitialListener getVastInterstitialListener() {
        return vastInterstitialListener;
    }

    public void loadXml(String xml) {
        VastLoader vastLoader = new VastLoader(context, VastTools.getDisplayAspectRatio(context), VastType.FULLSCREEN, this);
        vastLoader.loadXml(xml);
    }

    public void loadUrl(String url) {
        VastLoader vastLoader = new VastLoader(context, VastTools.getDisplayAspectRatio(context), VastType.FULLSCREEN, this);
        vastLoader.loadUrl(url);
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
    public void onComplete(@Nullable VastViewController vastViewController) {
        if (vastViewController == null) {
            if (vastInterstitialListener != null) {
                vastInterstitialListener.onVastFailedToLoad(this);
            }
            return;
        }
        controller = vastViewController;
        controller.setListener(this);
        controller.load();
    }

    @Override
    public void onVastControllerLoaded(VastViewController vastViewController) {
        if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastLoaded(this);
        }
    }

    @Override
    public void onVastControllerFailedToLoad(VastViewController vastViewController) {
        if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastFailedToLoad(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerFailedToShow(VastViewController vastViewController) {
        if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastFailedToShow(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerShown(VastViewController vastViewController) {

    }

    @Override
    public void onVastControllerClicked(VastViewController vastViewController, String url) {

    }

    @Override
    public void onVastControllerCompleted(VastViewController vastViewController) {

    }

    @Override
    public void onVastControllerClosed(VastViewController vastViewController) {

    }

    VastViewController getController() {
        return controller;
    }

}
