package com.appodeal.vast;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

class Icon {
    private final StaticResource staticResource;
    private final IFrameResource iFrameResource;
    private final HtmlResource htmlResource;
    private final int width;
    private final int height;
    private final int xPosition;
    private final int yPosition;
    private final int duration;
    private final int offset;
    private final int pxratio;
    private final List<String> iconViewTracking;
    private final IconClicks iconClicks;

    private Icon(Builder builder) {
        this.pxratio = builder.pxratio;
        this.staticResource = builder.staticResource;
        this.iFrameResource = builder.iFrameResource;
        this.htmlResource = builder.htmlResource;
        this.width = builder.width / builder.pxratio;
        this.height = builder.height / builder.pxratio;
        if (builder.xPosition != Integer.MAX_VALUE) {
            this.xPosition = builder.xPosition / builder.pxratio;
        } else  {
            this.xPosition = builder.xPosition;
        }
        if (builder.yPosition != Integer.MAX_VALUE) {
            this.yPosition = builder.yPosition / builder.pxratio;
        } else {
            this.yPosition = builder.yPosition;
        }
        this.duration = builder.duration;
        this.offset = builder.offset;
        this.iconViewTracking = builder.iconViewTracking;
        this.iconClicks = builder.iconClicks;
    }

    StaticResource getStaticResource() {
        return staticResource;
    }

    IFrameResource getIFrameResource() {
        return iFrameResource;
    }

    HtmlResource getHtmlResource() {
        return htmlResource;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int getXPosition() {
        return xPosition;
    }

    int getYPosition() {
        return yPosition;
    }

    int getDuration() {
        return duration;
    }

    int getOffset() {
        return offset;
    }

    int getPxRatio() {
        return pxratio;
    }

    List<String> getIconViewTracking() {
        return iconViewTracking;
    }

    IconClicks getIconClicks() {
        return iconClicks;
    }

    boolean isValid() {
        return width > 0 && height > 0 && duration > 0 && isResourceEnabled();
    }

    private boolean isResourceEnabled() {
        return hasValidStaticResource() || htmlResource != null || iFrameResource != null;
    }

    private boolean hasValidStaticResource() {
        return (staticResource != null && VastTools.isStaticResourceTypeSupported(staticResource.getCreativeType()));
    }

    boolean shouldShow(int playerPositionInMills) {
        return playerPositionInMills > offset && playerPositionInMills < (offset + duration);
    }

    View getView(Context context, final IconsLayer.IconsLayerListener listener) {
        IconViewBuilder viewBuilder = new IconViewBuilder();
        View mraidView;
        if (htmlResource != null) {
            mraidView = viewBuilder.createView(context, htmlResource, new IconViewBuilder.onClickListener() {
                @Override
                public void onClick(String url) {
                    listener.onIconClicked(Icon.this, url);
                }
            });
        } else if (staticResource != null && hasValidStaticResource()) {
            mraidView = viewBuilder.createView(context, staticResource, iconClicks.getClickThrough(), new IconViewBuilder.onClickListener() {
                @Override
                public void onClick(String url) {
                    listener.onIconClicked(Icon.this, url);
                }
            });
        } else {
            mraidView = viewBuilder.createView(context, iFrameResource, new IconViewBuilder.onClickListener() {
                @Override
                public void onClick(String url) {
                    listener.onIconClicked(Icon.this, url);
                }
            });
        }
        int viewWidth = Math.round(width * VastTools.getScreenDensity(context));
        int viewHeight = Math.round(height * VastTools.getScreenDensity(context));
        RelativeLayout.LayoutParams iconViewParams = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        switch (xPosition) {
            case 0:
                iconViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case Integer.MAX_VALUE:
                iconViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
            default:
                iconViewParams.leftMargin = xPosition;
        }
        switch (yPosition) {
            case 0:
                iconViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case Integer.MAX_VALUE:
                iconViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            default:
                iconViewParams.topMargin = yPosition;
        }
        mraidView.setLayoutParams(iconViewParams);
        return mraidView;
    }

    static class Builder {
        private StaticResource staticResource;
        private IFrameResource iFrameResource;
        private HtmlResource htmlResource;
        private int width;
        private int height;
        private int xPosition;
        private int yPosition;
        private int duration;
        private int offset;
        private int pxratio = 1;
        private List<String> iconViewTracking = new ArrayList<>();
        private IconClicks iconClicks = new IconClicks();

        Builder() {
        }

        void setStaticResource(StaticResource staticResource) {
            this.staticResource = staticResource;
        }

        void setIFrameResource(IFrameResource iFrameResource) {
            this.iFrameResource = iFrameResource;
        }

        void setHtmlResource(HtmlResource htmlResource) {
            this.htmlResource = htmlResource;
        }

        void setWidth(int width) {
            this.width = width;
        }

        void setHeight(int height) {
            this.height = height;
        }

        void setXPosition(int xPosition) {
            this.xPosition = xPosition;
        }

        void setYPosition(int yPosition) {
            this.yPosition = yPosition;
        }

        void setDuration(int duration) {
            this.duration = duration;
        }

        void setOffset(int offset) {
            this.offset = offset;
        }

        void setPxratio(int pxratio) {
            this.pxratio = pxratio;
        }

        void setIconViewTracking(List<String> iconViewTracking) {
            this.iconViewTracking = iconViewTracking;
        }

        void setIconClicks(IconClicks iconClicks) {
            this.iconClicks = iconClicks;
        }

        Icon build() {
            return new Icon(this);
        }
    }
}
