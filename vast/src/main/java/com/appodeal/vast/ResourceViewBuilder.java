package com.appodeal.vast;


import android.content.Context;
import android.location.Location;
import android.view.View;

import com.appodeal.mraid.MraidNativeFeature;
import com.appodeal.mraid.MraidNativeFeatureListener;
import com.appodeal.mraid.MraidView;
import com.appodeal.mraid.MraidViewListener;

import java.util.ArrayList;

class ResourceViewBuilder {

    interface onClickListener {
        void onClick(String url);
    }

    View createView(Context context, StaticResource resource, final String url, final onClickListener listener) {
        MraidView mraidView = new MraidView(context);
        mraidView.setHtml(resource.getHtml(url));
        mraidView.load();


        mraidView.setMraidViewListener(createMraidViewListener());
        mraidView.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), createMraidNativeFeatureListener(listener));

        return mraidView;
    }

    View createView(Context context, HtmlResource resource, final onClickListener listener) {
        MraidView mraidView = new MraidView(context);
        mraidView.setHtml(resource.getHtml());
        mraidView.load();


        mraidView.setMraidViewListener(createMraidViewListener());
        mraidView.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), createMraidNativeFeatureListener(listener));

        return mraidView;
    }

    View createView(Context context, IFrameResource resource, final onClickListener listener) {
        MraidView mraidView = new MraidView(context);
        mraidView.setUrl(resource.getUri());
        mraidView.load();


        mraidView.setMraidViewListener(createMraidViewListener());
        mraidView.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), createMraidNativeFeatureListener(listener));

        return mraidView;
    }

    private MraidViewListener createMraidViewListener() {
        return new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
                mraidView.setVisibility(View.GONE);
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {
                mraidView.setVisibility(View.GONE);
            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        };
    }

    private MraidNativeFeatureListener createMraidNativeFeatureListener(final onClickListener listener) {
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
