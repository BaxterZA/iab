package com.appodeal.vast;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.Arrays;

class VastController implements VastLoader.LoaderListener {
    private final static int cacheSize = 5;

    private float adAspectRatio;
    private VastControllerListener vastControllerListener;
    private final String cacheDir;
    private Context context;
    private VastViewController vastViewController;
    private boolean loaded;
    private boolean destroyed;
    private VastConfig vastConfig;
    private ViewGroup vastView;
    private VastType vastType;

    VastController(Context context, float adAspectRatio, VastType vastType) {
        this.adAspectRatio = adAspectRatio;
        this.cacheDir = getCacheDir(context.getApplicationContext());
        this.context = context.getApplicationContext();
        this.vastType = vastType;

        vastView = new RelativeLayout(context.getApplicationContext());
    }

    void setVastControllerListener(VastControllerListener vastControllerListener) {
        this.vastControllerListener = vastControllerListener;
    }

    void loadXml(String xml) {
        if (cacheDir == null) {
            if (vastControllerListener != null) {
                vastControllerListener.onVastControllerFailedToLoad(this);
            }
            destroy();
            return;
        }

        VastLoader vastLoader = new VastLoader.Builder(adAspectRatio, cacheDir).withXml(xml).withListener(this).build();
        VastTools.safeExecute(vastLoader);
    }

    void loadUrl(String url) {
        if (cacheDir == null) {
            if (vastControllerListener != null) {
                vastControllerListener.onVastControllerFailedToLoad(this);
            }
            destroy();
            return;
        }

        VastLoader vastLoader = new VastLoader.Builder(adAspectRatio, cacheDir).withUrl(url).withListener(this).build();
        VastTools.safeExecute(vastLoader);
    }


    ViewGroup getVastView() {
        return vastView;
    }

    void start() {
        if (vastViewController != null) {
            vastViewController.start();
        }
    }

    void pause() {
        if (vastViewController != null) {
            vastViewController.pause();
        }
    }

    void resume() {
        if (vastViewController != null) {
            vastViewController.resume();
        }
    }

    void destroy() {
        if (vastViewController != null) {
            vastViewController.destroy();
        }
        if (vastView != null) {
            vastView.removeAllViews();
            vastView = null;
        }
        context = null;
        vastControllerListener = null;
        vastViewController = null;
        loaded = false;
        destroyed = true;
    }

    void attachActivity(Activity activity) {
        if (vastViewController != null) {
            vastViewController.attachActivity(activity);
        }
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
        clearCache();
        if (vastConfig == null || !vastConfig.isPlayable()) {
            if (vastControllerListener != null) {
                vastControllerListener.onVastControllerFailedToLoad(this);
            }
            destroy();
            return;
        }
        this.vastConfig = vastConfig;
        vastViewController = new VastViewController(context, vastConfig, vastType, new VastViewController.VastViewControllerListener() {
            @Override
            public void onLoaded() {
                loaded = true;
                if (vastControllerListener != null) {
                    vastControllerListener.onVastControllerLoaded(VastController.this);
                }
            }

            @Override
            public void onShown() {
                if (vastControllerListener != null) {
                    vastControllerListener.onVastControllerShown(VastController.this);
                }
            }

            @Override
            public void onFailedToLoad() {
                if (vastControllerListener != null) {
                    vastControllerListener.onVastControllerFailedToLoad(VastController.this);
                }
            }

            @Override
            public void onClicked(@Nullable String url) {
                if (vastControllerListener != null && url != null) {
                    vastControllerListener.onVastControllerClicked(VastController.this, url);
                }
            }

            @Override
            public void onClosed() {
                if (vastControllerListener != null) {
                    vastControllerListener.onVastControllerClosed(VastController.this);
                }
            }

            @Override
            public void onCompleted() {
                if (vastControllerListener != null) {
                    vastControllerListener.onVastControllerCompleted(VastController.this);
                }
            }
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        vastView.removeAllViews();
        vastView.addView(vastViewController.getView(), layoutParams);
        vastViewController.load();
    }

    boolean isLoaded() {
        return loaded;
    }

    boolean isDestroyed() {
        return destroyed;
    }

    int getAdOrientation() {
        return vastConfig.getVideoOrientation();
    }

    void attemptToClose() {
        if (vastViewController != null) {
            vastViewController.attemptToClose();
        }
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

    private void clearCache() {
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
                    if (vastConfig == null || !Uri.fromFile(files[i]).equals(vastConfig.getMediaFileLocalUri())) {
                        //noinspection ResultOfMethodCallIgnored
                        files[i].delete();
                    }
                }

            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }
    }
}
