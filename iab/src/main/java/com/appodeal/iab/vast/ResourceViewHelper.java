package com.appodeal.iab.vast;


import android.content.Context;
import android.location.Location;

import com.appodeal.iab.MraidNativeFeature;
import com.appodeal.iab.MraidNativeFeatureListener;
import com.appodeal.iab.MraidView;
import com.appodeal.iab.mraid.MraidViewController;
import com.appodeal.iab.mraid.MraidViewControllerListener;

import java.util.ArrayList;

class ResourceViewHelper {

    interface ResourceListener {
        void onClick(String url);
        void onClose();
    }

    static MraidViewController createInterstitialController(Context context, ResourceHolder resourceHolder, ResourceListener listener) {
        MraidViewController mraidViewController = MraidViewController.createInterstitialController(context);
        loadViewController(mraidViewController, resourceHolder, listener);

        return mraidViewController;
    }

    static MraidViewController createViewController(Context context, ResourceHolder resourceHolder, ResourceListener listener) {
        MraidView mraidView = new MraidView(context);
        MraidViewController mraidViewController = MraidViewController.createInlineController(context, mraidView);
        loadViewController(mraidViewController, resourceHolder, listener);

        return mraidViewController;
    }

    private static void loadViewController(MraidViewController mraidViewController, ResourceHolder resourceHolder, ResourceListener listener) {
        if (resourceHolder.getHtmlResource() != null) {
            mraidViewController.setHtml(resourceHolder.getHtmlResource().getHtml());
        } else if (resourceHolder.getStaticResource() != null && resourceHolder.hasValidStaticResource()) {
            mraidViewController.setHtml(resourceHolder.getStaticResource().getHtml(resourceHolder.getClickThrough()));
        } else {
            mraidViewController.setUrl(resourceHolder.getIFrameResource().getUri());
        }
        mraidViewController.setMraidViewControllerListener(createMraidViewControllerListener(listener));
        mraidViewController.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), createMraidNativeFeatureListener(listener));
        mraidViewController.load();
    }

    private static MraidViewControllerListener createMraidViewControllerListener(final ResourceListener listener) {
        return new MraidViewControllerListener() {
            @Override
            public void onMraidViewControllerLoaded(MraidViewController mraidViewController) {

            }

            @Override
            public void onMraidViewControllerFailedToLoad(MraidViewController mraidViewController) {

            }

            @Override
            public void onMraidViewControllerUnloaded(MraidViewController mraidViewController) {
                listener.onClose();
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
                listener.onClose();
            }
        };
    }

    private static MraidNativeFeatureListener createMraidNativeFeatureListener(final ResourceListener listener) {
        return new MraidNativeFeatureListener() {
            @Override
            public void mraidNativeFeatureSendSms(String url) {

            }

            @Override
            public void mraidNativeFeatureCallTel(String url) {

            }

            @Override
            public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {

            }

            @Override
            public void mraidNativeFeaturePlayVideo(String url) {

            }

            @Override
            public void mraidNativeFeatureStorePicture(String url) {

            }

            @Override
            public void mraidNativeFeatureOpenBrowser(String url) {
                listener.onClick(url);
            }

            @Override
            public Location mraidNativeFeatureGetLocation() {
                return null;
            }
        };
    }
}
