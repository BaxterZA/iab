package com.appodeal.iab;


import android.content.Context;
import android.support.annotation.Nullable;

import com.appodeal.iab.vast.VastLoader;
import com.appodeal.iab.vast.VastType;
import com.appodeal.iab.vast.VastViewController;
import com.appodeal.iab.vast.VastViewControllerListener;
import com.appodeal.iab.views.ViewHelper;

/**
 * Used for showing fullscreen VAST ads
 */
public class VastInterstitial {
    private Context context;
    private VastViewController controller;
    private VastInterstitialListener vastInterstitialListener;
    private boolean isNonSkippable;
    private boolean isDestroyed;

    public VastInterstitial(Context context) {
        this.context = context;
    }

    /**
     * Set {@link VastInterstitialListener}
     * @param vastInterstitialListener implementation of {@link VastInterstitialListener}
     */
    public void setVastInterstitialListener(VastInterstitialListener vastInterstitialListener) {
        this.vastInterstitialListener = vastInterstitialListener;
    }

    /**
     * Set skip possibility
     * @param nonSkippable {@code true} to disable close button
     */
    public void setNonSkippable(boolean nonSkippable) {
        isNonSkippable = nonSkippable;
    }

    VastInterstitialListener getVastInterstitialListener() {
        return vastInterstitialListener;
    }

    /**
     * Set xml content, and start loading
     * @param xml content
     */
    public void loadXml(String xml) {
        if (isDestroyed) {
            return;
        }
        VastLoader vastLoader = new VastLoader(context, ViewHelper.getDisplayAspectRatio(context), VastType.FULLSCREEN, loaderListener());
        vastLoader.loadXml(xml);
    }

    /**
     * Set url link to content, and start loading
     * @param url link to content
     */
    public void loadUrl(String url) {
        if (isDestroyed) {
            return;
        }
        VastLoader vastLoader = new VastLoader(context, ViewHelper.getDisplayAspectRatio(context), VastType.FULLSCREEN, loaderListener());
        vastLoader.loadUrl(url);
    }

    /**
     * Start showing
     */
    public void show() {
        if (isDestroyed) {
            return;
        }
        if (controller != null && controller.isLoaded()) {
            String id = VastInterstitialStorage.save(this);
            VastActivity.startIntent(context, id);
        } else if (vastInterstitialListener != null) {
            vastInterstitialListener.onVastFailedToShow(this);
            destroy();
        }
    }

    /**
     * Destroy vast interstitial, after that it can't be used
     */
    public void destroy() {
        isDestroyed = true;
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
        vastInterstitialListener = null;
        context = null;
    }

    /**
     * Check if vast view was destroyed
     * @return {@code true} if vast view was destroyed {@code false} if not
     */
    public boolean isDestroyed() {
        return isDestroyed || controller == null || controller.isDestroyed();
    }

    /**
     * Check if vast view was loaded
     * @return {@code true} if vast view was loaded {@code false} if not
     */
    public boolean isLoaded() {
        return !isDestroyed && controller != null && controller.isLoaded();
    }

    private VastLoader.VastLoaderListener loaderListener() {
        return new VastLoader.VastLoaderListener() {
            @Override
            public void onComplete(@Nullable VastViewController vastViewController) {
                if (isDestroyed) {
                    return;
                }
                if (vastViewController == null) {
                    if (vastInterstitialListener != null) {
                        vastInterstitialListener.onVastFailedToLoad(VastInterstitial.this);
                    }
                    return;
                }
                controller = vastViewController;
                controller.setNonSkippable(isNonSkippable);
                controller.setListener(new VastViewControllerListener() {

                    @Override
                    public void onVastControllerLoaded(VastViewController vastViewController) {
                        if (vastInterstitialListener != null) {
                            vastInterstitialListener.onVastLoaded(VastInterstitial.this);
                        }
                    }

                    @Override
                    public void onVastControllerFailedToLoad(VastViewController vastViewController) {
                        if (vastInterstitialListener != null) {
                            vastInterstitialListener.onVastFailedToLoad(VastInterstitial.this);
                        }
                        destroy();
                    }

                    @Override
                    public void onVastControllerStarted(VastViewController vastViewController) {

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
                });
                controller.load();
            }
        };
    }

    VastViewController getController() {
        return controller;
    }
}
