package com.appodeal.vast;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class VastView extends RelativeLayout implements VastControllerListener {
    private VastController controller;
    private VastViewListener vastViewListener;

    public VastView(Context context) {
        super(context);
        init(context);
    }

    public VastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.controller = new VastController(context, 16f / 9, VastType.VIEW);
        this.controller.setVastControllerListener(this);
    }

    public void setVastViewListener(VastViewListener vastViewListener) {
        this.vastViewListener = vastViewListener;
    }

    public void loadXml(String xml) {
        if (controller != null && !controller.isDestroyed()) {
            controller.loadXml(xml);
        }
    }

    public void loadUrl(String url) {
        if (controller != null && !controller.isDestroyed()) {
            controller.loadUrl(url);
        }
    }

    public void destroy() {
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
    public void onVastControllerLoaded(VastController vastController) {
        removeAllViews();
        addView(vastController.getVastView(), new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        if (vastViewListener != null) {
            vastViewListener.onVastLoaded(this);
        }
        if (isOnScreen()) {
            controller.start();
        }
    }

    @Override
    public void onVastControllerFailedToLoad(VastController vastController) {
        if (vastViewListener != null) {
            vastViewListener.onVastFailedToLoad(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerFailedToShow(VastController vastController) {
        if (vastViewListener != null) {
            vastViewListener.onVastFailedToShow(this);
        }
        destroy();
    }

    @Override
    public void onVastControllerShown(VastController vastController) {
        if (vastViewListener != null) {
            vastViewListener.onVastShown(this);
        }
    }

    @Override
    public void onVastControllerClicked(VastController vastController, String url) {
        if (vastViewListener != null) {
            vastViewListener.onVastClicked(this, url);
        }
    }

    @Override
    public void onVastControllerCompleted(VastController vastController) {
        if (vastViewListener != null) {
            vastViewListener.onVastFinished(this);
        }
    }

    @Override
    public void onVastControllerClosed(VastController vastController) {
        //never going to happen
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (controller != null) {
            if (visibility != View.VISIBLE) {
                controller.pause();
            } else {
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
