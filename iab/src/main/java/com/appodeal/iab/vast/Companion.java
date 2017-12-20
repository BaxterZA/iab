package com.appodeal.iab.vast;

import android.content.Context;

import com.appodeal.iab.MraidInterstitial;
import com.appodeal.iab.MraidView;
import com.appodeal.iab.mraid.MraidViewController;

import java.util.HashMap;
import java.util.List;


class Companion implements ResourceHolder {
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

    @Override
    public StaticResource getStaticResource() {
        return staticResource;
    }

    @Override
    public IFrameResource getIFrameResource() {
        return iFrameResource;
    }

    @Override
    public HtmlResource getHtmlResource() {
        return htmlResource;
    }

    @Override
    public String getClickThrough() {
        return clickThrough;
    }

    HashMap<TrackingEventsType, List<String>> getTracking() {
        return tracking;
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

    @Override
    public boolean hasValidStaticResource() {
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

    MraidViewController createMraidViewController(Context context) {
        MraidViewController mraidViewController = MraidViewController.createInterstitialController(context);
        if (htmlResource != null) {
            mraidViewController.setHtml(htmlResource.getHtml());
        } else if (staticResource != null && hasValidStaticResource()) {
            mraidViewController.setHtml(staticResource.getHtml(getClickThrough()));
        } else {
            mraidViewController.setUrl(iFrameResource.getUri());
        }
        return mraidViewController;
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
