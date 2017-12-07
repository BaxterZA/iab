package com.appodeal.vast;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.appodeal.vast.vpaid.VpaidViewController;

import java.util.ArrayList;
import java.util.List;

class VastViewController implements MediaFileLayerListener, ControlsLayer.ControlsLayerListener, IconsLayer.IconsLayerListener, CompanionLayer.CompanionListener {

    interface VastViewControllerListener {
        void onLoaded();
        void onFailedToLoad();
        void onShown();
        void onClicked(String url);
        void onClosed();
        void onCompleted();
    }

    private final VastConfig vastConfig;
    private final VastType vastType;
    private final VastViewControllerListener listener;
    private MediaFileLayerInterface mediaFileLayer;
    private ControlsLayer controlsLayer;
    private IconsLayer iconsLayer;
    private CompanionLayer companionLayer;
    private RelativeLayout rootView;
    private Context context;
    private MediaFileTracker mediaFileTracker;
    private int playerPositionInMills;
    private boolean muted;
    private int skipTime;
    private boolean mIsProcessedImpressions = false;

    private VastViewControllerState controllerState = VastViewControllerState.CREATED;

    VastViewController(@NonNull Context context, @NonNull VastConfig vastConfig, VastType vastType, @NonNull final VastViewControllerListener listener) {
        this.context = context;
        this.vastConfig = vastConfig;
        this.vastType = vastType;
        this.listener = listener;

        skipTime = VastTools.defaultSkipTime > vastConfig.getSkipOffset() ? VastTools.defaultSkipTime : vastConfig.getSkipOffset();

        rootView = new RelativeLayout(context);
        rootView.setPadding(0, 0, 0, 0);
        rootView.setBackgroundColor(Color.BLACK);
        changeState(VastViewControllerState.LOADING);
    }

    private synchronized void changeState(VastViewControllerState destinationState) {
        final VastViewControllerState currentState = controllerState;
        switch (currentState) {
            case CREATED:
                switch (destinationState) {
                    case LOADING:
                        controllerState = VastViewControllerState.LOADING;
                        createLayers();
                        break;
                    case READY:
                    case VIDEO_SHOWING:
                    case COMPANION_SHOWING:
                        VastLog.d("Must load controller after creation");
                        break;
                    case DESTROYED:
                        destroy();
                        break;
                }
                break;
            case LOADING:
                switch (destinationState) {
                    case READY:
                        controllerState = VastViewControllerState.READY;
                        break;
                    case VIDEO_SHOWING:
                    case COMPANION_SHOWING:
                        VastLog.d("Controller hasn't loaded");
                        break;
                    case DESTROYED:
                        destroy();
                        break;
                }
                break;
            case READY:
                switch (destinationState) {
                    case LOADING:
                        VastLog.d("Controller already loaded");
                        break;
                    case VIDEO_SHOWING:
                        controllerState = VastViewControllerState.VIDEO_SHOWING;
                        break;
                    case COMPANION_SHOWING:
                        break;
                    case DESTROYED:
                        destroy();
                        break;
                }
                break;
            case VIDEO_SHOWING:
                switch (destinationState) {
                    case LOADING:
                        VastLog.d("Controller already loaded");
                        break;
                    case COMPANION_SHOWING:
                        controllerState = VastViewControllerState.COMPANION_SHOWING;
                        break;
                    case DESTROYED:
                        destroy();
                        break;
                }
                break;
            case COMPANION_SHOWING:
                switch (destinationState) {
                    case LOADING:
                        VastLog.d("Try to repeat video");
                        controllerState = VastViewControllerState.LOADING;
                        createLayers();
                        break;
                    case DESTROYED:
                        destroy();
                        break;
                }
                break;
            case DESTROYED:
                VastLog.d("Controller is destroyed");
                break;

        }
    }

    private void createLayers() {
        rootView.removeAllViews();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        mediaFileLayer = createMediaFileLayer();
        rootView.addView(mediaFileLayer.getView());

        if (vastType == VastType.FULLSCREEN) {
            iconsLayer = new IconsLayer(context, vastConfig, this);
            rootView.addView(iconsLayer, params);
        }

        companionLayer = new CompanionLayer(context, vastConfig, this);
        companionLayer.setVisibility(View.GONE);
        rootView.addView(companionLayer, params);

        controlsLayer = new ControlsLayer(context, vastConfig, skipTime, this);
        rootView.addView(controlsLayer, params);
    }

    private MediaFileLayerInterface createMediaFileLayer() {
        playerPositionInMills = 0;
        MediaFileLayerInterface mediaFileLayer;
        if (vastConfig.getMediaFile().isValidVPAIDMediaFile()) {
            mediaFileLayer = new VpaidViewController(context, vastConfig.getMediaFile().getUrl(), vastConfig.getAdParameters(), this);
        } else {
            mediaFileLayer = new MediaFileVideoLayer(context, vastConfig, vastConfig.getMediaFileLocalUri(), this);
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mediaFileLayer.getView().setLayoutParams(layoutParams);
        return mediaFileLayer;
    }

    void start() {
        if (controllerState == VastViewControllerState.READY) {
            mediaFileLayer.start();
        }
    }

    void resume() {
        if (controllerState == VastViewControllerState.VIDEO_SHOWING) {
            mediaFileLayer.resume();
            mediaFileTracker = createMediaFileTracker();
            mediaFileTracker.start();
        }
    }

    void pause() {
        if (controllerState == VastViewControllerState.VIDEO_SHOWING) {
            mediaFileLayer.pause();
            destroyMediaFileTracker();
        }
    }

    void destroy() {
        controllerState = VastViewControllerState.DESTROYED;
        destroyMediaFileTracker();
        mediaFileLayer.destroy();
        rootView.removeAllViews();
        rootView = null;

        if (iconsLayer != null) {
            iconsLayer.destroy();
            iconsLayer = null;
        }

        companionLayer.destroy();
        companionLayer = null;

        if (controlsLayer != null) {
            controlsLayer.destroy();
            controlsLayer = null;
        }
        context = null;
    }

    ViewGroup getView() {
        return rootView;
    }

    void setCurrentProgress(int currentPosition) {
        if (controllerState != VastViewControllerState.DESTROYED) {
            VastLog.d(String.format("Video progress: %s", currentPosition));
            int newPercentage = 100 * currentPosition / vastConfig.getDuration();
            int previousPercentage = 100 * playerPositionInMills / vastConfig.getDuration();

            if (previousPercentage == 0 && newPercentage > 0) {
                VastLog.i("Video at start");
                trackEvents(TrackingEventsType.start);
            } else if (previousPercentage < 25 && newPercentage >= 25) {
                VastLog.i("Video at first quartile");
                trackEvents(TrackingEventsType.firstQuartile);
            } else if (previousPercentage < 50 && newPercentage >= 50) {
                VastLog.i("Video at midpoint");
                trackEvents(TrackingEventsType.midpoint);
            } else if (previousPercentage < 75 && newPercentage >= 75) {
                VastLog.i("Video at third quartile");
                trackEvents(TrackingEventsType.thirdQuartile);
            }

            List<ProgressEvent> progressEventList = vastConfig.getProgressTrackingList();
            List<String> progressEventUrlListToTrack = new ArrayList<>();
            for (ProgressEvent progressEvent : progressEventList) {
                if (progressEvent.getOffsetTime() > playerPositionInMills && progressEvent.getOffsetTime() <= currentPosition) {
                    progressEventUrlListToTrack.add(progressEvent.getTrackingURL());
                }
            }
            fireUrls(progressEventUrlListToTrack);

            controlsLayer.updateButtons(newPercentage, currentPosition);

            if (vastType == VastType.FULLSCREEN && iconsLayer != null) {
                iconsLayer.updateIcons(currentPosition);
            }

            playerPositionInMills = currentPosition;
        } else {
            destroyMediaFileTracker();
        }
    }

    void attemptToClose() {
        switch (controllerState) {
            case VIDEO_SHOWING:
                if (playerPositionInMills >= skipTime) {
                    trackEvents(TrackingEventsType.skip);
                    finishVideo();
                }
                break;
            case COMPANION_SHOWING:
                if (companionLayer == null || companionLayer.canClose()) {
                    listener.onClosed();
                    changeState(VastViewControllerState.DESTROYED);
                }
                break;
        }
    }

    private void finishVideo() {
        destroyMediaFileTracker();
        controlsLayer.videoComplete();
        mediaFileLayer.destroy();
        if (iconsLayer != null) {
            iconsLayer.destroy();
        }

        companionLayer.showCompanion(vastType);
        if (!companionLayer.hasCompanion()) {
            controlsLayer.showCompanionControls();
        }
        changeState(VastViewControllerState.COMPANION_SHOWING);
    }

    @Override
    public void onStarted() {
        mediaFileTracker = createMediaFileTracker();
        mediaFileTracker.start();

        listener.onShown();
        controlsLayer.videoStart(vastType);

        if (!mIsProcessedImpressions) {
            processImpressions();
            processViewableImpression();
        }

        changeState(VastViewControllerState.VIDEO_SHOWING);
    }

    @Override
    public void onLoaded() {
        changeState(VastViewControllerState.READY);
        listener.onLoaded();
    }

    @Override
    public void onFailedToLoad() {
        changeState(VastViewControllerState.DESTROYED);
        listener.onFailedToLoad();
    }

    @Override
    public void onError() {
        List<String> errorTracking = vastConfig.getErrorTracking();
        for (String url : errorTracking) {
            if (!TextUtils.isEmpty(url)) {
                VastTools.fireUrl(VastTools.replaceMacros(url, vastConfig.getMediaFile().getUrl(), playerPositionInMills, Error.ERROR_CODE_ERROR_SHOWING));
            }
        }
        changeState(VastViewControllerState.DESTROYED);
    }

    @Override
    public void onComplete() {
        VastLog.i("Video completed");
        trackEvents(TrackingEventsType.complete);
        if (listener != null) {
            listener.onCompleted();
        }
        finishVideo();
    }

    @Override
    public void onClick(String url) {
        VastLog.i("Player clicked");
        fireUrls(vastConfig.getVideoClicks().getClickTracking());
        if (listener != null) {
            listener.onClicked(url);
        }
    }

    @Override
    public void onMuteButtonClicked() {
        muted = !muted;
        controlsLayer.updateMuteButton(muted);
        VastLog.i("Mute button clicked");

        mediaFileLayer.setVolume(muted ? 0 : 1);
    }

    @Override
    public void onCtaButtonClicked() {
        VastLog.i("CTA button clicked");
        fireUrls(vastConfig.getVideoClicks().getClickTracking());
        if (listener != null) {
            listener.onClicked(vastConfig.getVideoClicks().getClickThrough());
        }
    }

    @Override
    public void onCloseButtonClicked() {
        VastLog.i("Close button clicked");
        attemptToClose();
    }

    @Override
    public void onRepeatButtonClicked() {
        VastLog.i("Repeat button clicked");
        skipTime = 0;
        changeState(VastViewControllerState.LOADING);
    }


    @Override
    public void onIconClicked(Icon icon, @Nullable String url) {
        VastLog.i("Icon clicked");
        fireUrls(icon.getIconClicks().getClickTracking());
        if (listener != null) {
            listener.onClicked(url);
        }

    }

    @Override
    public void onIconShown(Icon icon) {
        VastLog.i("Icon shown");
        fireUrls(icon.getIconViewTracking());
    }


    @Override
    public void onCompanionClicked(Companion companion, String url) {
        VastLog.i("Companion clicked");
        if (companion != null) {
            fireUrls(companion.getClickTracking());
            if (listener != null) {
                listener.onClicked(url);
            }
        } else {
            fireUrls(vastConfig.getVideoClicks().getClickTracking());
            if (listener != null) {
                listener.onClicked(vastConfig.getVideoClicks().getClickThrough());
            }
        }
    }

    @Override
    public void onCompanionClosed(@Nullable Companion companion) {
        VastLog.i("Companion closed");
        attemptToClose();
    }

    @Override
    public void onCompanionShown(@Nullable Companion companion) {
        VastLog.i("Companion shown");
        if (companion != null && companion.getTracking() != null) {
            List<String> trackers = companion.getTracking().get(TrackingEventsType.creativeView);
            fireUrls(trackers);
        }
    }

    private void processImpressions() {
        VastLog.i("Process impressions");

        mIsProcessedImpressions = true;
        List<String> impressions = vastConfig.getImpressionTracking();
        fireUrls(impressions);
    }

    private void processViewableImpression() {
        List<String> urls = vastConfig.getViewableViewableImpressions();
        fireUrls(urls);
    }

    private void trackEvents(TrackingEventsType eventsType) {
        List<String> trackers = vastConfig.getTrackingEventMap().get(eventsType);
        fireUrls(trackers);
    }

    private void fireUrls(List<String> trackingUrls) {
        if (trackingUrls != null) {
            for (String url : trackingUrls) {
                if (!TextUtils.isEmpty(url)) {
                    VastTools.fireUrl(VastTools.replaceMacros(url, vastConfig.getMediaFile().getUrl(), playerPositionInMills, null));
                }
            }
        }
    }

    private void destroyMediaFileTracker() {
        if (mediaFileTracker != null) {
            mediaFileTracker.stop();
            mediaFileTracker = null;
        }
    }

    private MediaFileTracker createMediaFileTracker() {
        destroyMediaFileTracker();
        return new MediaFileTracker(new Handler(Looper.getMainLooper()), this, mediaFileLayer);
    }
}
