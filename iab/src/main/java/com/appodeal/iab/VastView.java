package com.appodeal.iab;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.appodeal.iab.vast.VastLoader;
import com.appodeal.iab.vast.VastType;
import com.appodeal.iab.vast.VastViewController;
import com.appodeal.iab.vast.VastViewControllerListener;

/**
 * VAST VIEW see <a href="https://www.iab.com/wp-content/uploads/2016/01/VAST_4-0_2016-01-21.pdf">specification</a>
 */
public class VastView extends RelativeLayout {
    private VastViewController controller;
    private VastViewListener vastViewListener;

    public VastView(Context context) {
        super(context);
    }

    public VastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Set {@link VastViewListener}
     * @param vastViewListener implementation of {@link VastViewListener}
     */
    public void setVastViewListener(VastViewListener vastViewListener) {
        this.vastViewListener = vastViewListener;
    }

    /**
     * Set xml content, and start loading
     * @param xml content
     */
    public void loadXml(String xml) {
        VastLoader vastLoader = new VastLoader(getContext(), 16f / 9, VastType.VIEW, loaderListener());
        vastLoader.loadXml(xml);
    }

    /**
     * Set url link to content, and start loading
     * @param url link to content
     */
    public void loadUrl(String url) {
        VastLoader vastLoader = new VastLoader(getContext(), 16f / 9, VastType.VIEW, loaderListener());
        vastLoader.loadUrl(url);
    }

    /**
     * Destroy vast view, after that it can't be used
     */
    public void destroy() {
        removeAllViews();
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
        vastViewListener = null;
    }

    /**
     * Check if vast view was destroyed
     * @return {@code true} if vast view was destroyed {@code false} if not
     */
    public boolean isDestroyed() {
        return controller == null || controller.isDestroyed();
    }

    /**
     * Check if vast view was loaded
     * @return {@code true} if vast view was loaded {@code false} if not
     */
    public boolean isLoaded() {
        return controller != null && controller.isLoaded();
    }

    private VastLoader.VastLoaderListener loaderListener() {
        return new VastLoader.VastLoaderListener() {
            @Override
            public void onComplete(@Nullable VastViewController vastViewController) {
                if (vastViewController == null) {
                    if (vastViewListener != null) {
                        vastViewListener.onVastFailedToLoad(VastView.this);
                    }
                    return;
                }
                controller = vastViewController;
                controller.setListener(new VastViewControllerListener() {
                    @Override
                    public void onVastControllerLoaded(VastViewController vastViewController) {
                        removeAllViews();
                        addView(vastViewController.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                        if (vastViewListener != null) {
                            vastViewListener.onVastLoaded(VastView.this);
                        }
                        if (isOnScreen()) {
                            if (getContext() instanceof Activity) {
                                controller.attachActivity((Activity) getContext());
                            }
                            controller.start();
                        }
                    }

                    @Override
                    public void onVastControllerFailedToLoad(VastViewController vastViewController) {
                        if (vastViewListener != null) {
                            vastViewListener.onVastFailedToLoad(VastView.this);
                        }
                        destroy();
                    }

                    @Override
                    public void onVastControllerStarted(VastViewController vastViewController) {
                        if (vastViewListener != null) {
                            vastViewListener.onVastStarted(VastView.this);
                        }
                    }

                    @Override
                    public void onVastControllerClicked(VastViewController vastViewController, String url) {
                        if (vastViewListener != null) {
                            vastViewListener.onVastClicked(VastView.this, url);
                        }
                    }

                    @Override
                    public void onVastControllerCompleted(VastViewController vastViewController) {
                        if (vastViewListener != null) {
                            vastViewListener.onVastFinished(VastView.this);
                        }
                    }

                    @Override
                    public void onVastControllerClosed(VastViewController vastViewController) {
                        //never going to happen
                    }

                });
                controller.load();
            }
        };
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (controller != null) {
            if (visibility != View.VISIBLE) {
                controller.pause();
            } else {
                if (getContext() instanceof Activity) {
                    controller.attachActivity((Activity) getContext());
                }
                controller.start();
                controller.resume();
            }
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int measWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int curWidth = getMeasuredWidth();
        final int curHeight = getMeasuredHeight();

        int finalWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            finalWidth = measWidth;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            finalWidth = Math.min(measWidth, curWidth);
        } else {
            finalWidth = curWidth;
        }

        int finalHeight = (int) (9f / 16 * finalWidth);

        if (heightMode == MeasureSpec.EXACTLY && measHeight < finalHeight) {
            finalHeight = measHeight;
            finalWidth = (int) (16f / 9 * finalHeight);
        }

        if (Math.abs(finalHeight - curHeight) >= 2 || Math.abs(finalWidth - curWidth) >= 2) {
            getLayoutParams().width = finalWidth;
            getLayoutParams().height = finalHeight;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean isOnScreen() {
        Rect viewRect = new Rect();
        boolean isAdVisible = getGlobalVisibleRect(viewRect);
        boolean isAdShown = isShown();
        boolean windowHasFocus = hasWindowFocus();
        return isAdVisible && isAdShown && windowHasFocus;
    }
}
