package com.appodeal.mraid;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.pm.ActivityInfo.CONFIG_ORIENTATION;
import static android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE;


class MraidViewController implements MraidCommandListener, MraidViewabilityTracker.MraidViewabilityListener {
    private final Context context;
    private final MraidScreenMetrics mraidScreenMetrics;
    private final MraidBridge mraidBridge;
    private final Handler handler;
    private WeakReference<Activity> activityWeakReference;
    private String html;
    private String url;
    private String baseUrl = "http://localhost";
    private boolean isJsTag;
    private MraidPlacementType placementType = MraidPlacementType.INLINE;
    @VisibleForTesting MraidState mraidState = MraidState.LOADING;
    private MraidEnvironment mraidEnvironment;
    private AudioVolumeContentObserver audioVolumeContentObserver;
    private AdWebViewDebugListener debugListener;
    private MraidViewabilityTracker mraidViewabilityTracker;
    private boolean isVisible;

    @VisibleForTesting MraidViewControllerListener mraidViewControllerListener;
    private FrameLayout mraidView;
    @VisibleForTesting
    AdWebView adWebView;
    @VisibleForTesting CloseableLayout closeableLayout;

    @VisibleForTesting MraidOrientationProperties mraidOrientationProperties;
    @VisibleForTesting Integer originalRequestedOrientation;
    private int lastOrientation = -1;

    private boolean isFullScreen;
    private boolean isForceNotFullScreen;
    private boolean isWindowHardwareAccelerated;

    private boolean destroyed;
    private boolean loaded;
    private View.OnLayoutChangeListener onLayoutChangeListener;

    @VisibleForTesting
    @Nullable MraidNativeFeatureManager mraidNativeFeatureManager;

    static MraidViewController createInlineController(@NonNull Context context, @NonNull MraidView mraidView) {
        MraidScreenMetrics mraidScreenMetrics = new MraidScreenMetrics(ViewHelper.getDensity(context));
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties();
        return new MraidViewController(context, mraidView, mraidScreenMetrics, MraidPlacementType.INLINE, mraidBridge, mraidOrientationProperties);
    }

    static MraidViewController createInterstitialController(@NonNull Context context) {
        MraidScreenMetrics mraidScreenMetrics = new MraidScreenMetrics(ViewHelper.getDensity(context));
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INTERSTITIAL);
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties();
        FrameLayout mraidView = new FrameLayout(context);
        return new MraidViewController(context, mraidView, mraidScreenMetrics, MraidPlacementType.INTERSTITIAL, mraidBridge, mraidOrientationProperties);
    }

    @VisibleForTesting MraidViewController(@NonNull Context context,
                        @NonNull FrameLayout mraidView,
                        @NonNull MraidScreenMetrics mraidScreenMetrics,
                        @NonNull MraidPlacementType placementType,
                        @NonNull MraidBridge mraidBridge,
                        @NonNull MraidOrientationProperties mraidOrientationProperties) {
        this.context = context.getApplicationContext();
        if (context instanceof Activity) {
            activityWeakReference = new WeakReference<>((Activity) context);
        } else {
            activityWeakReference = new WeakReference<>(null);
        }
        this.mraidView = mraidView;
        this.onLayoutChangeListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (v instanceof MraidView) {
                    int orientation = v.getContext().getResources().getConfiguration().orientation;
                    if (orientation != lastOrientation) {
                        lastOrientation = orientation;
                        MraidLog.d(String.format("Orientation changed: %s", lastOrientation == Configuration.ORIENTATION_PORTRAIT ? "portrait" : "landscape"));
                        updateSizesAndOrientation();
                    }

                }
            }
        };
        this.mraidView.addOnLayoutChangeListener(onLayoutChangeListener);
        this.mraidScreenMetrics = mraidScreenMetrics;
        this.placementType = placementType;
        this.mraidBridge = mraidBridge;
        handler = new Handler(Looper.getMainLooper());
        closeableLayout = new CloseableLayout(context.getApplicationContext());
        closeableLayout.setOnCloseListener(new CloseableLayout.OnCloseListener() {
            @Override
            public void onClose() {
                onCloseEvent();
            }
        });
        this.mraidOrientationProperties = mraidOrientationProperties;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    void setJsTag(boolean jsTag) {
        isJsTag = jsTag;
    }

    void setMraidEnvironment(MraidEnvironment mraidEnvironment) {
        this.mraidEnvironment = mraidEnvironment;
    }

    void setMraidViewControllerListener(MraidViewControllerListener mraidViewControllerListener) {
        this.mraidViewControllerListener = mraidViewControllerListener;
    }

    void setSupportedFeatures(List<MraidNativeFeature> supportedFeatures, MraidNativeFeatureListener mraidNativeFeatureListener) {
        if (supportedFeatures != null && mraidNativeFeatureListener != null) {
            mraidNativeFeatureManager = new MraidNativeFeatureManager(context, supportedFeatures, mraidNativeFeatureListener);
        }
    }

    void setAdWebViewDebugListener(AdWebViewDebugListener debugListener) {
        this.debugListener = debugListener;
    }

    void load() {
        adWebView = new AdWebView(context);
        if (placementType == MraidPlacementType.INTERSTITIAL && mraidNativeFeatureManager != null && mraidNativeFeatureManager.isInlineVideoSupported()) {
            adWebView.allowMediaPlayback();
        }
        mraidBridge.initMraidWebView(adWebView, getMraidEnvironment(), this);
        if (html != null) {
            mraidBridge.loadContentHtml(html, baseUrl);
        } else if (url != null) {
            mraidBridge.loadContentUrl(url);
        } else {
            if (mraidViewControllerListener != null) {
                mraidViewControllerListener.onMraidViewControllerFailedToLoad(this);
            }
        }
        mraidView.addView(adWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @VisibleForTesting MraidEnvironment getMraidEnvironment() {
        if (mraidEnvironment == null) {
            mraidEnvironment = new MraidEnvironment.Builder().build();
        }
        return mraidEnvironment;
    }

    @VisibleForTesting Activity getActivity() {
        if (activityWeakReference != null) {
             return activityWeakReference.get();
        } else {
            return null;
        }
    }

    void attachActivity(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    FrameLayout getMraidView() {
        return mraidView;
    }

    private void updateSizesAndOrientation() {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null) {
            return;
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        mraidScreenMetrics.updateDensity(displayMetrics.densityDpi);

        mraidScreenMetrics.updateScreenSize(width, height);

        int[] location = new int[2];
        View rootView = ViewHelper.getTopRootView(mraidView, getActivity());
        if (rootView != null) {
            rootView.getLocationOnScreen(location);
            mraidScreenMetrics.updateMaxSize(rootView.getWidth(), rootView.getHeight());
        }

        mraidView.getLocationOnScreen(location);
        mraidScreenMetrics.updateDefaultSize(location[0], location[1], mraidView.getWidth(), mraidView.getHeight());

        adWebView.getLocationOnScreen(location);
        mraidScreenMetrics.updateCurrentSize(location[0], location[1], adWebView.getWidth(), adWebView.getHeight());

        mraidBridge.setCurrentAppOrientation(new MraidAppOrientationProperties(context, getActivity()));

        mraidBridge.fireSizeChangeEvent(mraidScreenMetrics);
    }


    @Override
    public void mraidViewPageFinished() {
        if (isDestroyed()) {
            return;
        }

        if (!isJsTag) {
            loaded = true;
            if (mraidViewControllerListener != null) {
                mraidViewControllerListener.onMraidViewControllerLoaded(this);
            }
            new MraidPreDrawTracker(adWebView, new MraidPreDrawTracker.MraidDrawListener() {
                @Override
                public void onPreDraw() {
                    changeWebViewContext();
                    mraidBridge.setSupportedServices(mraidNativeFeatureManager);
                    mraidState = MraidState.DEFAULT;
                    mraidBridge.setPlacementType(placementType);
                    mraidBridge.fireStateChangeEvent(mraidState);
                    updateSizesAndOrientation();
                    startAudioVolumeListener();
                    if (mraidNativeFeatureManager != null && mraidNativeFeatureManager.isLocationSupported()) {
                        mraidBridge.setLocation(mraidNativeFeatureManager.getLocation());
                    }
                    mraidBridge.fireReadyEvent();

                    startExposureTracking();
                }
            });
        }
    }

    private void changeWebViewContext() {
        if (adWebView != null && adWebView.getContext() instanceof MutableContextWrapper) {
            Activity activity = ViewHelper.getActivityForView(mraidView);
            if (activity != null) {
                ((MutableContextWrapper) adWebView.getContext()).setBaseContext(activity);
            }
        }
    }

    @Override
    public void onExposureChanged(ExposureState exposureState) {
        if (isVisible != exposureState.isVisible()) {
            isVisible = exposureState.isVisible();
            handleVisibilityChanged(isVisible);
        }
        mraidBridge.fireExposureChangeEvent(exposureState);
    }

    @Override
    public void mraidViewRenderProcessGone() {
        if (isDestroyed()) {
            return;
        }
        MraidLog.d("Render process gone");
        if (mraidViewControllerListener != null) {
            mraidViewControllerListener.onMraidViewControllerUnloaded(this);
        }
        destroy();
    }

    private void handleVisibilityChanged(boolean isViewable) {
        if (isDestroyed()) {
            return;
        }

        mraidBridge.fireViewableChangeEvent(isViewable);
    }

    @Override
    public void onAudioVolumeChange(float volumePercentage) {
        if (isDestroyed()) {
            return;
        }

        mraidBridge.fireAudioVolumeChangeEvent(volumePercentage);
    }

    @Override
    public void onLoaded() {
        if (isDestroyed()) {
            return;
        }

        if (isJsTag) {
            loaded = true;
            if (mraidViewControllerListener != null) {
                mraidViewControllerListener.onMraidViewControllerLoaded(this);
            }

            new MraidPreDrawTracker(adWebView, new MraidPreDrawTracker.MraidDrawListener() {
                @Override
                public void onPreDraw() {
                    changeWebViewContext();
                }
            });
        }
    }

    @Override
    public void onFailedToLoad() {
        if (isDestroyed()) {
            return;
        }

        if (isJsTag) {
            if (mraidViewControllerListener != null) {
                mraidViewControllerListener.onMraidViewControllerFailedToLoad(this);
            }
        }
    }

    @Override
    public void onResize(MraidResizeProperties resizeProperties) throws MraidError {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null || closeableLayout == null) {
            throw new MraidError("WebView is null");
        }

        if (mraidState == MraidState.LOADING || mraidState == MraidState.HIDDEN) {
            return;
        } else if (mraidState == MraidState.EXPANDED) {
            throw new MraidError("Not allowed to resize from an already expanded ad");
        }

        if (placementType == MraidPlacementType.INTERSTITIAL) {
            throw new MraidError("Not allowed to resize from an interstitial ad");
        }

        Rect resizeRect = resizeProperties.getResizeRect(context, mraidScreenMetrics.getDefaultSize().left, mraidScreenMetrics.getDefaultSize().top);
        MraidLog.d(String.format("Max size: %s", mraidScreenMetrics.getMaxSize()));
        MraidLog.d(String.format("Resized rect: %s", resizeRect));

        if (!resizeProperties.allowOffscreen) {
            Size maxsize = mraidScreenMetrics.getMaxSize();
            if (maxsize.width() < resizeRect.width() || maxsize.height() < resizeRect.height()) {
                throw new MraidError("Resized view would not appear on screen");
            }
        }

        Rect closeButtonRect = CloseableLayout.getCloseButtonRect(resizeProperties.customClosePosition, resizeRect);
        MraidLog.d(String.format("Close button rect: %s", closeButtonRect));
        if (!mraidScreenMetrics.getMasSizeRect().contains(closeButtonRect)) {
            throw new MraidError("Close area would not appear on screen");
        }

        resize(resizeRect);
    }

    @VisibleForTesting void resize(Rect resizeRect) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(resizeRect.width(), resizeRect.height());
        layoutParams.leftMargin = resizeRect.left;
        layoutParams.topMargin = resizeRect.top;
        if (mraidState == MraidState.DEFAULT) {
            mraidView.removeView(adWebView);
            mraidView.setVisibility(View.INVISIBLE);
            closeableLayout.addView(adWebView, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            closeableLayout.showCloseButton();
            ViewGroup rootView = ViewHelper.getTopRootView(mraidView, getActivity());
            if (rootView != null) {
                rootView.addView(closeableLayout, layoutParams);
            } else {
                if (getActivity() != null) {
                    getActivity().addContentView(closeableLayout, layoutParams);
                }
            }
        } else if (mraidState == MraidState.RESIZED && closeableLayout != null) {
            closeableLayout.setLayoutParams(layoutParams);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                mraidState = MraidState.RESIZED;
                mraidBridge.fireStateChangeEvent(mraidState);
                updateSizesAndOrientation();
                if (mraidViewControllerListener != null) {
                    mraidViewControllerListener.onMraidViewControllerResized(MraidViewController.this);
                }
                startExposureTracking();
            }
        });

    }

    @Override
    public void onExpand() throws MraidError {
        if (isDestroyed()) {
            return;
        }

        if (placementType == MraidPlacementType.INTERSTITIAL) {
            return;
        }

        if (mraidState != MraidState.DEFAULT && mraidState != MraidState.RESIZED) {
            return;
        }

        if (adWebView == null || closeableLayout == null) {
            throw new MraidError("WebView is null");
        }

        applyOrientationProperties();

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if (mraidState == MraidState.DEFAULT) {
            mraidView.removeView(adWebView);
            mraidView.setVisibility(View.INVISIBLE);
            closeableLayout.addView(adWebView, 0, layoutParams);
            closeableLayout.showCloseButton();
            ViewGroup rootView = ViewHelper.getTopRootView(mraidView, getActivity());
            if (rootView != null) {
                rootView.addView(closeableLayout, layoutParams);
            } else {
                if (getActivity() != null) {
                    getActivity().addContentView(closeableLayout, layoutParams);
                }
            }
            if (getActivity() != null) {
                int flags = getActivity().getWindow().getAttributes().flags;
                isFullScreen = ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0);
                isForceNotFullScreen = ((flags & WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN) != 0);

                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

                if (mraidNativeFeatureManager != null && mraidNativeFeatureManager.isInlineVideoSupported() && getActivity() != null) {
                    isWindowHardwareAccelerated = (getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) != 0;

                    if (!isWindowHardwareAccelerated) {
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
                    }
                }
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                mraidState = MraidState.EXPANDED;
                mraidBridge.fireStateChangeEvent(mraidState);
                updateSizesAndOrientation();
                if (mraidViewControllerListener != null) {
                    mraidViewControllerListener.onMraidViewControllerExpanded(MraidViewController.this);
                }
                startExposureTracking();
            }
        });
    }

    @Override
    public void onClose() {
        onCloseEvent();
    }

    @Override
    public void onUnload() {
        if (isDestroyed()) {
            return;
        }

        MraidLog.d("Unload called, destroy controller");
        if (mraidViewControllerListener != null) {
            mraidViewControllerListener.onMraidViewControllerUnloaded(this);
        }
        destroy();
    }

    @Override
    public void onSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidError {
        if (isDestroyed()) {
            return;
        }

        if (!shouldAllowForceOrientation(forceOrientation)) {
            throw new MraidError("Unable to force orientation to " + forceOrientation);
        }
        mraidOrientationProperties = new MraidOrientationProperties(allowOrientationChange, forceOrientation);
        if (mraidState == MraidState.EXPANDED || placementType == MraidPlacementType.INTERSTITIAL) {
            applyOrientationProperties();
        }
    }

    @Override
    public void onOpen(String url) {
        if (isDestroyed()) {
            return;
        }

        if (mraidViewControllerListener != null) {
            mraidViewControllerListener.onMraidViewControllerClicked(this);
        }

        if (mraidNativeFeatureManager != null) {
            if (url.startsWith("sms")) {
                mraidNativeFeatureManager.sendSms(url);
            } else if (url.startsWith("tel")) {
                mraidNativeFeatureManager.calTell(url);
            } else {
                mraidNativeFeatureManager.open(url);
            }
        } else {
            MraidLog.d("Native feature listener is null");
        }
    }

    @Override
    public void onPlayVideo(String url) {
        if (isDestroyed()) {
            return;
        }

        if (mraidNativeFeatureManager != null) {
            mraidNativeFeatureManager.playVideo(url);
        } else {
            MraidLog.d("Native feature listener is null");
        }
    }

    @Override
    public void onStorePicture(String url) {
        if (isDestroyed()) {
            return;
        }

        if (mraidNativeFeatureManager != null) {
            mraidNativeFeatureManager.storePicture(url);
        } else {
            MraidLog.d("Native feature listener is null");
        }
    }

    @Override
    public void onCreateCalendarEvent(String event) {
        if (isDestroyed()) {
            return;
        }

        if (mraidNativeFeatureManager != null) {
            mraidNativeFeatureManager.createCalendarEvent(event);
        } else {
            MraidLog.d("Native feature listener is null");
        }
    }

    @Override
    public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
        if (debugListener != null) {
            return debugListener.onJsAlert(message, result);
        }
        result.cancel();
        return true;
    }

    @Override
    public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
        return debugListener == null || debugListener.onConsoleMessage(consoleMessage);
    }

    @VisibleForTesting void onCloseEvent() {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null) {
            return;
        }

        if (mraidState == MraidState.LOADING || mraidState == MraidState.HIDDEN) {
            return;
        }

        if (placementType != MraidPlacementType.INTERSTITIAL && getActivity() != null) {
            if (!isFullScreen) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            if (isForceNotFullScreen) {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
            if (!isWindowHardwareAccelerated) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        }

        if (mraidState == MraidState.EXPANDED || placementType == MraidPlacementType.INTERSTITIAL) {
            restoreOrientation();
        }

        if (mraidState == MraidState.RESIZED || mraidState == MraidState.EXPANDED) {
            closeableLayout.removeView(adWebView);
            mraidView.addView(adWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            mraidView.setVisibility(View.VISIBLE);
            ViewHelper.removeViewFromParent(closeableLayout);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mraidState = MraidState.DEFAULT;
                    mraidBridge.fireStateChangeEvent(mraidState);
                    updateSizesAndOrientation();
                    if (mraidViewControllerListener != null) {
                        mraidViewControllerListener.onMraidViewControllerClosed(MraidViewController.this);
                    }
                    startExposureTracking();
                }
            });
        } else if (mraidState == MraidState.DEFAULT) {
            mraidView.setVisibility(View.INVISIBLE);
            mraidState = MraidState.HIDDEN;
            mraidBridge.fireStateChangeEvent(mraidState);
            if (mraidViewControllerListener != null) {
                mraidViewControllerListener.onMraidViewControllerClosed(MraidViewController.this);
            }
            if (mraidViewabilityTracker != null) {
                mraidViewabilityTracker.stopTracking();
            }
        }

    }

    @VisibleForTesting boolean shouldAllowForceOrientation(final MraidOrientation newOrientation) {
        if (newOrientation == MraidOrientation.NONE) {
            return true;
        }

        if (getActivity() == null) {
            return false;
        }

        Activity activity = getActivity();
        final ActivityInfo activityInfo;
        try {
            activityInfo = activity.getPackageManager().getActivityInfo(new ComponentName(activity, activity.getClass()), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        final int activityOrientation = activityInfo.screenOrientation;
        if (activityOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return activityOrientation == newOrientation.getActivityInfoOrientation();
        }

        return (activityInfo.configChanges & CONFIG_ORIENTATION) != 0 && (activityInfo.configChanges & CONFIG_SCREEN_SIZE) != 0;
    }

    @VisibleForTesting void lockOrientation(final int screenOrientation) throws MraidError {
        if (getActivity() == null || !shouldAllowForceOrientation(mraidOrientationProperties.forceOrientation)) {
            throw new MraidError("Attempted to lock orientation to unsupported value: " + mraidOrientationProperties.forceOrientation.name());
        }

        if (originalRequestedOrientation == null) {
            originalRequestedOrientation = getActivity().getRequestedOrientation();
        }

        getActivity().setRequestedOrientation(screenOrientation);
    }

    @VisibleForTesting void applyOrientationProperties() throws MraidError {
        if (mraidOrientationProperties.forceOrientation == MraidOrientation.NONE) {
            if (mraidOrientationProperties.allowOrientationChange) {
                restoreOrientation();
            } else {
                if (getActivity() == null) {
                    throw new MraidError("Unable to set MRAID orientation Context is not an Activity.");
                }
                int currentOrientation = context.getResources().getConfiguration().orientation;
                switch (currentOrientation) {
                    case Configuration.ORIENTATION_PORTRAIT:
                        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                }
            }
        } else {
            lockOrientation(mraidOrientationProperties.forceOrientation.getActivityInfoOrientation());
        }
    }

    @VisibleForTesting void restoreOrientation() {
        if (getActivity() != null && originalRequestedOrientation != null) {
            getActivity().setRequestedOrientation(originalRequestedOrientation);
        }
        originalRequestedOrientation = null;
    }


    void pause() {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null) {
            return;
        }
        MraidLog.d("pauseWebView " + adWebView.toString());
        if (mraidViewabilityTracker != null) {
            mraidViewabilityTracker.stopTracking();
        }
        stopAudioVolumeListener();
        if (mraidBridge != null) {
            mraidBridge.fireViewableChangeEvent(false);
            mraidBridge.fireExposureChangeEvent(ExposureState.empty());
        }
        try {
            adWebView.onPause();
        } catch (Exception e) {
            MraidLog.e(e.getMessage());
        }
    }

    void resume() {
        if (isDestroyed()) {
            return;
        }

        if (adWebView == null) {
            return;
        }
        MraidLog.d("resumeWebView " + adWebView.toString());
        startExposureTracking();
        startAudioVolumeListener();
        try {
            adWebView.onResume();
        } catch (Exception e) {
            MraidLog.e(e.getMessage());
        }
    }

    void destroy() {
        mraidNativeFeatureManager = null;
        ViewHelper.removeViewFromParent(closeableLayout);
        mraidBridge.destroy();
        if (adWebView != null) {
            adWebView.removeAllViews();
            adWebView.setWebChromeClient(null);
            adWebView.setWebViewClient(null);
            adWebView.destroy();
            adWebView = null;
        }
        if (mraidView != null) {
            mraidView.removeOnLayoutChangeListener(onLayoutChangeListener);
            mraidView = null;
        }
        closeableLayout = null;
        stopAudioVolumeListener();
        if (mraidViewabilityTracker != null) {
            mraidViewabilityTracker.stopTracking();
            mraidViewabilityTracker = null;
        }

        destroyed = true;
    }

    boolean isDestroyed() {
        return destroyed;
    }

    public boolean isLoaded() {
        return loaded;
    }

    private void startExposureTracking() {
        if (isDestroyed() || isJsTag) {
            return;
        }

        if (mraidState == MraidState.LOADING || mraidState == MraidState.HIDDEN || adWebView == null) {
            return;
        }

        if (mraidViewabilityTracker != null) {
            mraidViewabilityTracker.stopTracking();
        }

        mraidViewabilityTracker = new MraidViewabilityTracker(adWebView, this);
    }

    private void stopAudioVolumeListener() {
        if (audioVolumeContentObserver != null) {
            context.getContentResolver().unregisterContentObserver(audioVolumeContentObserver);
            audioVolumeContentObserver = null;
        }
    }

    private void startAudioVolumeListener() {
        if (isDestroyed() || isJsTag) {
            return;
        }

        if (mraidState == MraidState.LOADING || mraidState == MraidState.HIDDEN || adWebView == null) {
            return;
        }

        if (audioVolumeContentObserver != null) {
            stopAudioVolumeListener();
        }
        audioVolumeContentObserver = new AudioVolumeContentObserver(handler, context.getApplicationContext(), this);

        context.getApplicationContext().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true, audioVolumeContentObserver);
    }
}
