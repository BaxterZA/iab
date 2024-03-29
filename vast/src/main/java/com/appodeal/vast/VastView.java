package com.appodeal.vast;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class VastView extends RelativeLayout implements VastLoader.VastLoaderListener, VastViewControllerListener {
    private VastViewController controller;
    private VastViewListener vastViewListener;

    public VastView(Context context) {
        super(context);
    }

    public VastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setVastViewListener(VastViewListener vastViewListener) {
        this.vastViewListener = vastViewListener;
    }

    public void loadXml(String xml) {
        VastLoader vastLoader = new VastLoader(getContext(), 16f / 9, VastType.VIEW, this);
        vastLoader.loadXml(xml);
    }

    public void loadUrl(String url) {
        VastLoader vastLoader = new VastLoader(getContext(), 16f / 9, VastType.VIEW, this);
        vastLoader.loadUrl(url);
    }

    public void destroy() {
        removeAllViews();
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
        vastViewListener = null;
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
            if (vastViewListener != null) {
                vastViewListener.onVastFailedToLoad(this);
            }
            return;
        }
        controller = vastViewController;
        controller.setListener(this);
        controller.load();
    }

    @Override
    public void onVastControllerLoaded(VastViewController vastViewController) {
        removeAllViews();
        addView(vastViewController.getView(), new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        if (vastViewListener != null) {
            vastViewListener.onVastLoaded(this);
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
            vastViewListener.onVastFailedToLoad(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerFailedToShow(VastViewController vastViewController) {
        if (vastViewListener != null) {
            vastViewListener.onVastFailedToShow(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerShown(VastViewController vastViewController) {
        if (vastViewListener != null) {
            vastViewListener.onVastShown(this);
        }
    }

    @Override
    public void onVastControllerClicked(VastViewController vastViewController, String url) {
        if (vastViewListener != null) {
            vastViewListener.onVastClicked(this, url);
        }
    }

    @Override
    public void onVastControllerCompleted(VastViewController vastViewController) {
        if (vastViewListener != null) {
            vastViewListener.onVastFinished(this);
        }
    }

    @Override
    public void onVastControllerClosed(VastViewController vastViewController) {
        //never going to happen
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
