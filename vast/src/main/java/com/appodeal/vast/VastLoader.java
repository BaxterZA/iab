package com.appodeal.vast;


import android.os.AsyncTask;
import android.support.annotation.NonNull;

class VastLoader extends AsyncTask<Void, Void, VastConfig> {
    interface LoaderListener {
        void onComplete(VastConfig vastConfig);
    }

    private final String url;
    private final String xml;
    private float adAspectRatio;
    private LoaderListener loaderListener;
    private String cacheDir;

    private VastLoader(Builder builder) {
        this.url = builder.url;
        this.xml = builder.xml;
        this.adAspectRatio = builder.adAspectRatio;
        this.loaderListener = builder.loaderListener;
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
        if (loaderListener != null) {
            loaderListener.onComplete(vastConfig);
        }
    }


    static class Builder {
        private String url;
        private String xml;
        private float adAspectRatio;
        private LoaderListener loaderListener;
        private String cacheDir;

        Builder(float adAspectRatio, @NonNull String cacheDir) {
            this.adAspectRatio = adAspectRatio;
            this.cacheDir = cacheDir;
        }

        Builder withListener(@NonNull LoaderListener loaderListener) {
            this.loaderListener = loaderListener;
            return this;
        }

        Builder withUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        Builder withXml(@NonNull String xml) {
            this.xml = xml;
            return this;
        }

        VastLoader build() {
            return new VastLoader(this);
        }
    }
}
