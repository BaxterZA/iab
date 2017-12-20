package com.appodeal.iab.vast;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appodeal.iab.Logger;

import java.io.File;
import java.util.Arrays;

public class VastLoader implements VastConfigLoader.VastConfigLoaderListener {
    private final static String TAG = "VastLoader";

    public interface VastLoaderListener {
        void onComplete(@Nullable VastViewController vastViewController);
    }

    private final static int cacheSize = 5;

    private float adAspectRatio;
    private VastLoaderListener vastLoaderListener;
    private final String cacheDir;
    private Context context;
    private VastType vastType;

    public VastLoader(@NonNull Context context, float adAspectRatio, VastType vastType, @NonNull VastLoaderListener vastLoaderListener) {
        this.adAspectRatio = adAspectRatio;
        this.cacheDir = getCacheDir(context.getApplicationContext());
        this.context = context.getApplicationContext();
        this.vastType = vastType;
        this.vastLoaderListener = vastLoaderListener;
    }

    public void loadXml(String xml) {
        if (cacheDir == null) {
            vastLoaderListener.onComplete(null);
            destroy();
            return;
        }

        VastConfigLoader vastConfigLoader = new VastConfigLoader.Builder(adAspectRatio, cacheDir, this).withXml(xml).build();
        VastTools.safeExecute(vastConfigLoader);
    }

    public void loadUrl(String url) {
        if (cacheDir == null) {
            vastLoaderListener.onComplete(null);
            destroy();
            return;
        }

        VastConfigLoader vastConfigLoader = new VastConfigLoader.Builder(adAspectRatio, cacheDir, this).withUrl(url).build();
        VastTools.safeExecute(vastConfigLoader);
    }

    void destroy() {
        context = null;
    }

    private String getCacheDir(Context context) {
        File externalStorage = context.getExternalFilesDir(null);
        if (externalStorage != null) {
            return externalStorage.getPath() + "/vast_cache/";
        }
        return null;
    }


    @Override
    public void onComplete(VastConfig vastConfig) {
        if (vastConfig == null || !vastConfig.isPlayable()) {
            vastLoaderListener.onComplete(null);
            destroy();
            return;
        }
        clearCache(vastConfig.getMediaFileLocalUri());

        vastLoaderListener.onComplete(new VastViewController(context, vastConfig, vastType));
    }

    class FileData implements Comparable {
        long mLastModified;
        File mFile;

        FileData(File file) {
            mFile = file;
            mLastModified = file.lastModified();
        }

        public int compareTo(@NonNull Object o) {
            FileData file = ((FileData) o);
            return mLastModified > file.mLastModified ? -1 : mLastModified == file.mLastModified ? 0 : 1;
        }
    }

    private void clearCache(@NonNull Uri currentFileUri) {
        try {
            if (cacheDir == null) {
                return;
            }
            File dir = new File(cacheDir);
            File[] files = dir.listFiles();
            if (files != null && files.length > cacheSize) {
                FileData[] pairs = new FileData[files.length];
                for (int i = 0; i < files.length; i++) {
                    pairs[i] = new FileData(files[i]);
                }

                Arrays.sort(pairs);

                for (int i = 0; i < files.length; i++) {
                    files[i] = pairs[i].mFile;
                }

                for (int i = cacheSize; i < files.length; i++) {
                    if (!Uri.fromFile(files[i]).equals(currentFileUri)) {
                        //noinspection ResultOfMethodCallIgnored
                        files[i].delete();
                    }
                }

            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
