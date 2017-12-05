package com.appodeal.mraid;


import android.content.Context;
import android.support.annotation.VisibleForTesting;

import java.util.List;

/**
 * Used for showing fullscreen MRAID ads
 */
public class MraidInterstitial {
    @VisibleForTesting MraidViewController controller;
    private Context context;
    private MraidInterstitialListener mraidInterstitialListener;
    private boolean destroyed;
    private int closeTimeInterval = 3000;

    public MraidInterstitial(Context context) {
        this.context = context;
        this.controller = MraidViewController.createInterstitialController(context);
    }

    /**
     * Set html content
     * @param html content
     */
    public void setHtml(String html) {
        if (controller != null) {
            controller.setHtml(html);
        }
    }

    /**
     * Set url link to content
     * @param url link to content
     */
    public void setUrl(String url) {
        if (controller != null) {
            controller.setUrl(url);
        }
    }

    /**
     * Set base url to use in webview with html content
     * @param baseUrl url
     */
    public void setBaseUrl(String baseUrl) {
        if (controller != null) {
            controller.setBaseUrl(baseUrl);
        }
    }

    /**
     * Use js tag specification instead mraid
     * @param jsTag {@code true} if it is a js tag
     */
    public void setJsTag(boolean jsTag) {
        if (controller != null) {
            controller.setJsTag(jsTag);
        }
    }

    public void setMraidInterstitialListener(MraidInterstitialListener mraidInterstitialListener) {
        this.mraidInterstitialListener = mraidInterstitialListener;
    }

    MraidInterstitialListener getMraidInterstitialListener() {
        return mraidInterstitialListener;
    }

    /**
     * Set native features than must be supported anf {@link MraidNativeFeatureListener} to work with native features
     * @param supportedFeatures list of supported native features
     * @param mraidNativeFeatureListener implementation of {@link MraidNativeFeatureListener}
     */
    public void setSupportedFeatures(List<MraidNativeFeature> supportedFeatures, MraidNativeFeatureListener mraidNativeFeatureListener) {
        if (controller != null) {
            controller.setSupportedFeatures(supportedFeatures, mraidNativeFeatureListener);
        }
    }

    /**
     * Set MRAID environment
     * @param mraidEnvironment {@link MraidEnvironment}
     */
    public void setMraidEnvironment(MraidEnvironment mraidEnvironment) {
        if (controller != null) {
            controller.setMraidEnvironment(mraidEnvironment);
        }
    }

    /**
     * Set debug listener to debug creative
     * @param debugListener implementation of {@link MraidWebViewDebugListener}
     */
    public void setMraidWebViewDebugListener(MraidWebViewDebugListener debugListener) {
        if (controller != null) {
            controller.setMraidWebViewDebugListener(debugListener);
        }
    }

    /**
     * Set a timeout after which ad can  be closed
     * @param closeTimeInterval timeout in mills
     */
    public void setCloseTimeInterval(int closeTimeInterval) {
        this.closeTimeInterval = closeTimeInterval;
    }

    /**
     * Get current timeout after which ad can  be closed
     * @return timeout in mills
     */
    public int getCloseTimeInterval() {
        return closeTimeInterval;
    }

    /**
     * Start loading
     */
    public void load() {
        if (controller != null) {
            controller.setMraidViewControllerListener(new MraidViewControllerListener() {
                @Override
                public void onMraidViewControllerLoaded(MraidViewController mraidViewController) {
                    if (mraidInterstitialListener != null) {
                        mraidInterstitialListener.onMraidInterstitialLoaded(MraidInterstitial.this);
                    }
                }

                @Override
                public void onMraidViewControllerFailedToLoad(MraidViewController mraidViewController) {
                    if (mraidInterstitialListener != null) {
                        mraidInterstitialListener.onMraidInterstitialFailedToLoad(MraidInterstitial.this);
                    }
                    destroy();
                }

                @Override
                public void onMraidViewControllerUnloaded(MraidViewController mraidViewController) {
                    if (mraidInterstitialListener != null) {
                        mraidInterstitialListener.onMraidInterstitialUnloaded(MraidInterstitial.this);
                    }
                    destroy();
                }

                @Override
                public void onMraidViewControllerExpanded(MraidViewController mraidViewController) {

                }

                @Override
                public void onMraidViewControllerResized(MraidViewController mraidViewController) {

                }

                @Override
                public void onMraidViewControllerClicked(MraidViewController mraidViewController) {

                }

                @Override
                public void onMraidViewControllerClosed(MraidViewController mraidViewController) {

                }
            });
            controller.load();
        } else if (mraidInterstitialListener != null) {
            mraidInterstitialListener.onMraidInterstitialFailedToLoad(this);
        }
    }

    /**
     * Start showing
     */
    public void show() {
        if (controller != null && controller.isLoaded()) {
            String id = MraidInterstitialStorage.save(this);
            MraidActivity.startIntent(context, id);
        } else if (mraidInterstitialListener != null) {
            mraidInterstitialListener.onMraidInterstitialFailedToShow(this);
            destroy();
        }
    }

    /**
     * Destroy mraid interstitial, after that it can't be used
     */
    public void destroy() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
        mraidInterstitialListener = null;
        context = null;
        destroyed = true;
    }

    boolean isDestroyed() {
        return destroyed;
    }

    MraidViewController getController() {
        return controller;
    }
}
