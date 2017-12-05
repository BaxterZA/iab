package com.appodeal.mraid;


import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.Map;

class MraidBridge implements MraidWebViewListener {
    private final MraidPlacementType placementType;
    private MraidWebView mraidWebView;
    private MraidCommandListener listener;
    private MraidEnvironment environment;
    private boolean isLoaded;
    private boolean wasTouched;
    private boolean isMraid;
    @VisibleForTesting MraidResizeProperties resizeProperties;

    MraidBridge(@NonNull MraidPlacementType placementType) {
        this.placementType = placementType;
        this.resizeProperties = new MraidResizeProperties();
    }

    void initMraidWebView(MraidWebView mraidWebView, @NonNull MraidEnvironment environment, MraidCommandListener listener) {
        this.listener = listener;
        this.mraidWebView = mraidWebView;
        this.environment = environment;
        this.mraidWebView.setWebViewClient(new MraidWebViewClient(this));
        this.mraidWebView.setWebChromeClient(new MraidWebChromeClient(listener));
        mraidWebView.setListener(this);
    }

    void loadContentHtml(@NonNull String htmlData, @Nullable String baseUrl) {
        isLoaded = false;
        mraidWebView.loadDataWithBaseURL(baseUrl, MraidHtmlProcessor.processRawHtml(htmlData, environment), "text/html", "UTF-8", null);
    }

    void loadContentUrl(String url) {
        isLoaded = false;
        mraidWebView.loadUrl(url);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    @VisibleForTesting boolean wasTouched() {
        return wasTouched;
    }

    void destroy() {
        listener = null;
        mraidWebView = null;
    }

    @Override
    public void onRenderProcessGone() {
        if (listener != null) {
            listener.mraidViewRenderProcessGone();
        }
    }

    @Override
    public void onTouch() {
        wasTouched = true;
    }

    @Override
    public void onPageFinished() {
        isLoaded = true;
        if (listener != null) {
            listener.mraidViewPageFinished();
        }
    }

    @Override
    public void onProcessCommand(String url) {
        MraidCommandProcessor mraidCommandProcessor = new MraidCommandProcessor();
        if (mraidCommandProcessor.isCommandValid(url)) {
            MraidCommand mraidCommand = mraidCommandProcessor.getMraidCommand();
            Map<String, String> params = mraidCommandProcessor.getParams();

            try {
                runCommand(mraidCommand, params, mraidCommandProcessor);
            } catch (MraidError e) {
                MraidLog.e(e.getMessage());
                fireErrorEvent(mraidCommand, e.getMessage());
            }
        } else {
            fireErrorEvent(MraidCommand.UNKNOWN, String.format("Invalid command: %s", url));
        }
    }

    private void runCommand(@NonNull final MraidCommand command, @NonNull Map<String, String> params, @NonNull MraidCommandProcessor mraidCommandProcessor) throws MraidError {
        if (command.requiresClick(placementType) && !wasTouched()) {
            throw new MraidError("Cannot execute this command, view wasn't click");
        }

        if (listener == null) {
            throw new MraidError("Can't find listener");
        }

        if (mraidWebView == null) {
            throw new MraidError("The current WebView is being destroyed");
        }

        switch (command) {
            case CLOSE:
                listener.onClose();
                break;
            case UNLOAD:
                listener.onUnload();
                break;
            case JS_TAG_LOADED:
                listener.onLoaded();
                break;
            case JS_TAG_NO_FILL:
                listener.onFailedToLoad();
                break;
            case SET_RESIZE_PROPERTIES:
                int width = mraidCommandProcessor.parseNumber(params.get("width"));
                int height = mraidCommandProcessor.parseNumber(params.get("height"));
                int offsetX = mraidCommandProcessor.parseNumber(params.get("offsetX"));
                int offsetY = mraidCommandProcessor.parseNumber(params.get("offsetY"));
                //closePosition is deprecated in MRAID 3.0. The host will always add close indicator in top right corner.
                ClosePosition closePosition = ClosePosition.TOP_RIGHT;
                boolean allowOffscreen = mraidCommandProcessor.parseBoolean(params.get("allowOffscreen"), true);
                resizeProperties = new MraidResizeProperties(width, height, offsetX, offsetY, closePosition, allowOffscreen);
                break;
            case RESIZE:
                listener.onResize(resizeProperties);
                break;
            case EXPAND:
                String url = params.get("url");
                if (url != null) {
                    fireErrorEvent(MraidCommand.EXPAND, "Two-part ads deprecated");
                    return;
                }
                listener.onExpand();
                break;
            case USE_CUSTOM_CLOSE:
                fireErrorEvent(MraidCommand.USE_CUSTOM_CLOSE, "For MRAID 2.0 or older version ads in MRAID 3.0 containers " +
                        "useCustomClose() requests will be ignored by the host");
                break;
            case OPEN:
                listener.onOpen(params.get("url"));
                break;
            case SET_ORIENTATION_PROPERTIES:
                boolean allowOrientationChange = mraidCommandProcessor.parseBoolean(params.get("allowOrientationChange"));
                MraidOrientation forceOrientation = mraidCommandProcessor.parseOrientation(params.get("forceOrientation"));
                listener.onSetOrientationProperties(allowOrientationChange, forceOrientation);
                break;
            case PLAY_VIDEO:
                url = mraidCommandProcessor.parseURL(params.get("url"));
                listener.onPlayVideo(url);
                break;
            case STORE_PICTURE:
                url = mraidCommandProcessor.parseURL(params.get("url"));
                listener.onStorePicture(url);
                break;
            case CREATE_CALENDAR_EVENT:
                String eventJSON = params.get("eventJSON");
                listener.onCreateCalendarEvent(eventJSON);
                break;
            case VPAID_AD_CLICK_THRU:
            case VPAID_AD_ERROR:
            case VPAID_AD_IMPRESSION:
            case VPAID_AD_PAUSED:
            case VPAID_AD_PLAYING:
            case VPAID_AD_VIDEO_COMPLETE:
            case VPAID_AD_VIDEO_FIRST_QUARTILE:
            case VPAID_AD_VIDEO_MIDPOINT:
            case VPAID_AD_VIDEO_START:
            case VPAID_AD_VIDEO_THIRD_QUARTILE:
            case VPAID_AD_DURATION:
                MraidLog.d(String.format("Vpaid event: %s, with params: %s", command.getName(), params));
                break;
            case UNKNOWN:
                throw new MraidError("Unspecified MRAID command");
        }
    }

    @Override
    public void onMraidRequested() {
        isMraid = true;
    }

    private void injectJavaScript(@NonNull String javascript) {
        MraidLog.d("evaluating js: " + javascript);
        mraidWebView.loadUrl("javascript:" + javascript);
    }


    void fireSizeChangeEvent(MraidScreenMetrics mraidScreenMetrics) {
        if (isMraid) {
            MraidLog.d("setScreenSize " + mraidScreenMetrics.getScreenSizeString());
            injectJavaScript("mraid.setScreenSize(" + mraidScreenMetrics.getScreenSizeString() + ");");

            MraidLog.d("setMaxSize " + mraidScreenMetrics.getMaxSizeString());
            injectJavaScript("mraid.setMaxSize(" + mraidScreenMetrics.getMaxSizeString() + ");");

            MraidLog.d("setCurrentPosition " + mraidScreenMetrics.setCurrentPositionString());
            injectJavaScript("mraid.setCurrentPosition(" + mraidScreenMetrics.setCurrentPositionString() + ");");

            MraidLog.d("setDefaultPosition " + mraidScreenMetrics.getDefaultPositionString());
            injectJavaScript("mraid.setDefaultPosition(" + mraidScreenMetrics.getDefaultPositionString() + ");");
        }
    }

    void fireReadyEvent() {
        if (isMraid) {
            MraidLog.d("fireReadyEvent");
            injectJavaScript("mraid.fireReadyEvent();");
        }
    }

    void fireStateChangeEvent(MraidState state) {
        if (isMraid) {
            MraidLog.d("fireStateChangeEvent");
            injectJavaScript("mraid.fireStateChangeEvent('" + state.toMraidString() + "');");
        }
    }

    void setPlacementType(MraidPlacementType placementType) {
        if (isMraid) {
            MraidLog.d("setPlacementType");
            injectJavaScript("mraid.setPlacementType('" + placementType.toMraidString() + "');");
        }
    }

    void fireExposureChangeEvent(ExposureState exposureState) {
        if (isMraid) {
            MraidLog.d("fireExposureChangeEvent");
            injectJavaScript("mraid.fireExposureChangeEvent(" + exposureState.toMraidString() + ");");
        }
    }

    void fireViewableChangeEvent(boolean isViewable) {
        if (isMraid) {
            MraidLog.d("fireViewableChangeEvent");
            injectJavaScript("mraid.fireViewableChangeEvent(" + isViewable + ");");
        }
    }

    void fireAudioVolumeChangeEvent(float percent) {
        if (isMraid) {
            MraidLog.d("audioVolumeChange");
            injectJavaScript("mraid.fireAudioVolumeChangeEvent(" + percent + ");");
        }
    }

    void fireErrorEvent(@NonNull MraidCommand command, @NonNull String message) {
        if (isMraid) {
            MraidLog.d("fireErrorEvent");
            injectJavaScript("mraid.fireErrorEvent('" + message + "', '" + command.getName() + "');");
        }
    }

    void setCurrentAppOrientation(@NonNull MraidAppOrientationProperties mraidAppOrientationProperties) {
        if (isMraid) {
            MraidLog.d("setCurrentAppOrientation");
            injectJavaScript("mraid.setCurrentAppOrientation('" + mraidAppOrientationProperties.orientation + "', " + mraidAppOrientationProperties.locked + ");");
        }
    }

    void setLocation(Location location) {
        if (isMraid && location != null) {
            long lastFix = (System.currentTimeMillis() - location.getTime()) / 1000;
            int type = location.getProvider().equals(LocationManager.GPS_PROVIDER) ? 1 : 2;
            String ipservice = "";
            if (type == 2) {
                ipservice = "android";
            }
            injectJavaScript("mraid.setLocation(" + location.getLatitude() + ", " + location.getLongitude() + ", "
                    + type + ", " + location.getAccuracy() + ", " + lastFix + ", '" + ipservice + "');");
        }
    }

    void setSupportedServices(@Nullable MraidNativeFeatureManager nativeFeatureManager) {
        if (isMraid && nativeFeatureManager != null) {
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.SMS, " + nativeFeatureManager.isSmsSupported() + ");");
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.TEL, " + nativeFeatureManager.isTelSupported() + ");");
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.CALENDAR, " + nativeFeatureManager.isCalendarSupported() + ");");
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.STOREPICTURE, " + nativeFeatureManager.isStorePictureSupported() + ");");
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.INLINEVIDEO, " + nativeFeatureManager.isInlineVideoSupported() + ");");
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.VPAID, " + nativeFeatureManager.isVpaidSupported() + ");");
            injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.LOCATION, " + nativeFeatureManager.isLocationSupported() + ");");
        }
    }

    public boolean isMraid() {
        return isMraid;
    }
}
