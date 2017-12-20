package com.appodeal.vast;

import android.content.Context;
import android.view.View;

import com.appodeal.mraid.MraidInterstitial;
import com.appodeal.mraid.MraidView;

import java.util.HashMap;
import java.util.List;


class Companion {
    private final StaticResource staticResource;
    private final IFrameResource iFrameResource;
    private final HtmlResource htmlResource;
    private final HashMap<TrackingEventsType, List<String>> tracking;
    private final String clickThrough;
    private final List<String> clickTracking;
    private final int width;
    private final int height;
    private final String adParameters;

    private Companion(Builder builder) {
        this.staticResource = builder.staticResource;
        this.iFrameResource = builder.iFrameResource;
        this.htmlResource = builder.htmlResource;
        this.clickThrough = builder.clickThrough;
        this.clickTracking = builder.clickTracking;
        this.tracking = builder.tracking;
        this.width = builder.width;
        this.height = builder.height;
        this.adParameters = builder.adParameters;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
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

    HashMap<TrackingEventsType, List<String>> getTracking() {
        return tracking;
    }

    String getClickThrough() {
        return clickThrough;
    }

    List<String> getClickTracking() {
        return clickTracking;
    }

    String getAdParameters() {
        return adParameters;
    }

    boolean isValid() {
        return hasValidSizes() && (hasValidStaticResource() || iFrameResource != null || htmlResource != null);
    }

    private boolean hasValidStaticResource() {
        return (staticResource != null && VastTools.isStaticResourceTypeSupported(staticResource.getCreativeType()));
    }

    private boolean hasValidSizes() {
        return getWidth() > 0 && getHeight() > 0;
    }

    MraidView createMraidView(Context context) {
        MraidView mraidView = new MraidView(context);
        if (htmlResource != null) {
            mraidView.setHtml(htmlResource.getHtml());
        } else if (staticResource != null && hasValidStaticResource()) {
            mraidView.setHtml(staticResource.getHtml(getClickThrough()));
        } else {
            mraidView.setUrl(iFrameResource.getUri());
        }
        return mraidView;
    }

    MraidInterstitial createMraidInterstitial(Context context) {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);
        if (htmlResource != null) {
            mraidInterstitial.setHtml(htmlResource.getHtml());
        } else if (staticResource != null && hasValidStaticResource()) {
            mraidInterstitial.setHtml(staticResource.getHtml(getClickThrough()));
        } else {
            mraidInterstitial.setUrl(iFrameResource.getUri());
        }
        return mraidInterstitial;
    }

    static class Builder {
        private StaticResource staticResource;
        private IFrameResource iFrameResource;
        private HtmlResource htmlResource;
        private HashMap<TrackingEventsType, List<String>> tracking;
        private String clickThrough;
        private List<String> clickTracking;
        private final int width;
        private final int height;
        private String adParameters;

        Builder(int width, int height) {
            this.width = width;
            this.height = height;
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

        void setTracking(HashMap<TrackingEventsType, List<String>> tracking) {
            this.tracking = tracking;
        }

        void setClickThrough(String clickThrough) {
            this.clickThrough = clickThrough;
        }

        void setClickTracking(List<String> clickTracking) {
            this.clickTracking = clickTracking;
        }

        void setAdParameters(String adParameters) {
            this.adParameters = adParameters;
        }

        Companion build() {
            return new Companion(this);
        }
    }
}
