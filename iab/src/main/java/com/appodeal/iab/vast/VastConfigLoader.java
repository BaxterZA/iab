package com.appodeal.iab.vast;


import android.os.AsyncTask;
import android.support.annotation.NonNull;

class VastConfigLoader extends AsyncTask<Void, Void, VastConfig> {
    interface VastConfigLoaderListener {
        void onComplete(VastConfig vastConfig);
    }

    private final String url;
    private final String xml;
    private final float adAspectRatio;
    private final VastConfigLoaderListener vastConfigLoaderListener;
    private final String cacheDir;

    private VastConfigLoader(Builder builder) {
        this.url = builder.url;
        this.xml = builder.xml;
        this.adAspectRatio = builder.adAspectRatio;
        this.vastConfigLoaderListener = builder.vastConfigLoaderListener;
        this.cacheDir = builder.cacheDir;
    }

    @Override
    protected VastConfig doInBackground(Void... params) {
        if (xml == null && url == null) {
            return null;
        }

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = xml != null ? processor.loadXml(xml) : processor.loadUrl(url);
        if (vastModel != null) {
            return new VastConfig(vastModel, adAspectRatio, cacheDir);
        }
        return null;
    }

    @Override
    protected void onPostExecute(VastConfig vastConfig) {
        super.onPostExecute(vastConfig);
        if (vastConfigLoaderListener != null) {
            vastConfigLoaderListener.onComplete(vastConfig);
        }
    }


    static class Builder {
        private String url;
        private String xml;
        private final float adAspectRatio;
        private final VastConfigLoaderListener vastConfigLoaderListener;
        private final String cacheDir;

        Builder(float adAspectRatio, @NonNull String cacheDir, @NonNull VastConfigLoaderListener vastConfigLoaderListener) {
            this.adAspectRatio = adAspectRatio;
            this.cacheDir = cacheDir;
            this.vastConfigLoaderListener = vastConfigLoaderListener;
        }

        Builder withUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        Builder withXml(@NonNull String xml) {
            this.xml = xml;
            return this;
        }

        VastConfigLoader build() {
            return new VastConfigLoader(this);
        }
    }
}
