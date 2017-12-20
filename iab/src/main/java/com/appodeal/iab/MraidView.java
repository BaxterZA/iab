package com.appodeal.iab;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.appodeal.iab.MraidEnvironment;
import com.appodeal.iab.mraid.MraidViewController;
import com.appodeal.iab.mraid.MraidViewControllerListener;

import java.util.List;

/**
 * MRAID VIEW see <a href="https://www.iab.com/wp-content/uploads/2017/07/MRAID_3.0_FINAL.pdf">specification</a>
 */
public class MraidView extends FrameLayout implements MraidViewControllerListener {
    @VisibleForTesting
    MraidViewController controller;
    private MraidViewListener mraidViewListener;

    public MraidView(@NonNull Context context) {
        super(context);
        initController();
    }

    public MraidView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initController();
    }

    private void initController() {
        controller = MraidViewController.createInlineController(getContext(), this);
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

    /**
     * Set {@link MraidViewListener}
     * @param mraidViewListener implementation of {@link MraidViewListener}
     */
    public void setMraidViewListener(MraidViewListener mraidViewListener) {
        this.mraidViewListener = mraidViewListener;
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
     * @param debugListener implementation of {@link WebViewDebugListener}
     */
    public void setAdWebViewDebugListener(WebViewDebugListener debugListener) {
        if (controller != null) {
            controller.setAdWebViewDebugListener(debugListener);
        }
    }

    /**
     * Start loading
     */
    public void load() {
        if (controller != null) {
            controller.setMraidViewControllerListener(this);
            controller.load();
        }
    }

    /**
     * Destroy mraid view, after that it can't be used
     */
    public void destroy() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
        mraidViewListener = null;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (controller != null) {
            if (visibility != View.VISIBLE) {
                controller.pause();
            } else {
                controller.resume();
            }
        }
    }

    @Override
    public void onMraidViewControllerLoaded(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewLoaded(this);
        }
    }

    @Override
    public void onMraidViewControllerFailedToLoad(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewFailedToLoad(this);
        }
        destroy();
    }

    @Override
    public void onMraidViewControllerUnloaded(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewUnloaded(this);
        }
        destroy();
    }

    @Override
    public void onMraidViewControllerExpanded(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewExpanded(this);
        }
    }

    @Override
    public void onMraidViewControllerResized(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewResized(this);
        }
    }

    @Override
    public void onMraidViewControllerClicked(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewClicked(this);
        }
    }

    @Override
    public void onMraidViewControllerClosed(MraidViewController mraidViewController) {
        if (mraidViewListener != null) {
            mraidViewListener.onMraidViewClosed(this);
        }
    }

    /**
     * Get {@link MraidViewListener}
     * @return {@link MraidViewListener}
     */
    public MraidViewListener getMraidViewListener() {
        return mraidViewListener;
    }
}
