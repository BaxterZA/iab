package com.appodeal.mraid;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class MraidMraidBridgeTest {
    private MraidEnvironment mraidEnvironment;
    private AdWebView adWebView;

    @Before
    public void setUp() throws Exception {
        adWebView = mock(AdWebView.class);
        mraidEnvironment = new MraidEnvironment.Builder().build();
    }

    @Test
    public void onRenderProcessGone() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, new MraidCommandListener() {
            @Override
            public void mraidViewPageFinished() {

            }

            @Override
            public void mraidViewRenderProcessGone() {
                countDownLatch.countDown();
            }

            @Override
            public void onAudioVolumeChange(float volumePercentage) {

            }

            @Override
            public void onLoaded() {

            }

            @Override
            public void onFailedToLoad() {

            }

            @Override
            public void onResize(MraidResizeProperties resizeProperties) throws MraidError {

            }

            @Override
            public void onExpand() throws MraidError {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onUnload() {

            }

            @Override
            public void onSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidError {

            }

            @Override
            public void onOpen(String url) {

            }

            @Override
            public void onPlayVideo(String url) {

            }

            @Override
            public void onStorePicture(String url) {

            }

            @Override
            public void onCreateCalendarEvent(String event) {

            }

            @Override
            public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                return false;
            }

            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                return false;
            }
        });

        mraidBridge.onRenderProcessGone();
        countDownLatch.await();
    }

    @Test
    public void onPageFinished() throws Exception {
        final MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, new MraidCommandListener() {
            @Override
            public void mraidViewPageFinished() {
                assertTrue(mraidBridge.isLoaded());
                countDownLatch.countDown();
            }

            @Override
            public void mraidViewRenderProcessGone() {

            }

            @Override
            public void onAudioVolumeChange(float volumePercentage) {

            }

            @Override
            public void onLoaded() {

            }

            @Override
            public void onFailedToLoad() {

            }

            @Override
            public void onResize(MraidResizeProperties resizeProperties) throws MraidError {

            }

            @Override
            public void onExpand() throws MraidError {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onUnload() {

            }

            @Override
            public void onSetOrientationProperties(boolean allowOrientationChange, MraidOrientation forceOrientation) throws MraidError {

            }

            @Override
            public void onOpen(String url) {

            }

            @Override
            public void onPlayVideo(String url) {

            }

            @Override
            public void onStorePicture(String url) {

            }

            @Override
            public void onCreateCalendarEvent(String event) {

            }

            @Override
            public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                return false;
            }

            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                return false;
            }
        });
        assertFalse(mraidBridge.isLoaded());
        mraidBridge.onPageFinished();
        countDownLatch.await();
    }

    @Test
    public void onTouch() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        assertFalse(mraidBridge.wasTouched());
        mraidBridge.onTouch();
        assertTrue(mraidBridge.wasTouched());
    }

    @Test
    public void onMraidRequested() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        assertFalse(mraidBridge.isMraid());
        mraidBridge.onMraidRequested();
        assertFalse(mraidBridge.wasTouched());
    }

    @Test
    public void fireSizeChangeEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireSizeChangeEvent(new MraidScreenMetrics(2));
        verify(adWebView, never()).loadUrl(anyString());
    }

    @Test
    public void fireSizeChangeEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();
        MraidScreenMetrics mraidScreenMetrics = new MraidScreenMetrics(320);
        mraidScreenMetrics.updateMaxSize(1920, 1080);
        mraidScreenMetrics.updateScreenSize(1800, 1000);
        mraidScreenMetrics.updateDefaultSize(10, 10, 640, 100);
        mraidScreenMetrics.updateCurrentSize(20, 20, 320, 50);
        mraidBridge.fireSizeChangeEvent(mraidScreenMetrics);
        verify(adWebView).loadUrl("javascript:mraid.setMaxSize(960, 540);");
        verify(adWebView).loadUrl("javascript:mraid.setScreenSize(900, 500);");
        verify(adWebView).loadUrl("javascript:mraid.setCurrentPosition(10, 10, 160, 25);");
        verify(adWebView).loadUrl("javascript:mraid.setDefaultPosition(5, 5, 320, 50);");
    }

    @Test
    public void fireReadyEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireReadyEvent();
        verify(adWebView, never()).loadUrl("javascript:mraid.fireReadyEvent();");
    }

    @Test
    public void fireReadyEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();
        mraidBridge.fireReadyEvent();
        verify(adWebView).loadUrl("javascript:mraid.fireReadyEvent();");
    }

    @Test
    public void fireStateChangeEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireStateChangeEvent(MraidState.DEFAULT);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(default);");

        mraidBridge.fireStateChangeEvent(MraidState.EXPANDED);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(expanded);");

        mraidBridge.fireStateChangeEvent(MraidState.RESIZED);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(resized);");

        mraidBridge.fireStateChangeEvent(MraidState.HIDDEN);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(hidden);");

        mraidBridge.fireStateChangeEvent(MraidState.LOADING);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(loading);");

    }

    @Test
    public void fireStateChangeEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();
        mraidBridge.fireStateChangeEvent(MraidState.DEFAULT);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(default);");

        mraidBridge.fireStateChangeEvent(MraidState.EXPANDED);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(expanded);");

        mraidBridge.fireStateChangeEvent(MraidState.RESIZED);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(resized);");

        mraidBridge.fireStateChangeEvent(MraidState.HIDDEN);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(hidden);");

        mraidBridge.fireStateChangeEvent(MraidState.LOADING);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireStateChangeEvent(loading);");
    }

    @Test
    public void setPlacementType_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.setPlacementType(MraidPlacementType.INLINE);
        verify(adWebView, never()).loadUrl("javascript:mraid.setPlacementType('inline');");

        mraidBridge.setPlacementType(MraidPlacementType.INTERSTITIAL);
        verify(adWebView, never()).loadUrl("javascript:mraid.setPlacementType('interstitial');");
    }

    @Test
    public void setPlacementType_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();
        mraidBridge.setPlacementType(MraidPlacementType.INLINE);
        verify(adWebView).loadUrl("javascript:mraid.setPlacementType('inline');");

        mraidBridge.setPlacementType(MraidPlacementType.INTERSTITIAL);
        verify(adWebView).loadUrl("javascript:mraid.setPlacementType('interstitial');");
    }

    @Test
    public void fireExposureChangeEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireExposureChangeEvent(new ExposureState(15, null, null));
        verify(adWebView, never()).loadUrl(anyString());
    }

    @Test
    public void fireExposureChangeEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        ExposureState exposureState = new ExposureState(15, null, null);
        mraidBridge.fireExposureChangeEvent(exposureState);
        verify(adWebView).loadUrl("javascript:mraid.fireExposureChangeEvent(" + exposureState.toMraidString() + ");");
    }

    @Test
    public void fireViewableChangeEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireViewableChangeEvent(true);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireViewableChangeEvent(true);");
    }

    @Test
    public void fireViewableChangeEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        mraidBridge.fireViewableChangeEvent(true);
        verify(adWebView).loadUrl("javascript:mraid.fireViewableChangeEvent(true);");
    }

    @Test
    public void fireAudioVolumeChangeEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireAudioVolumeChangeEvent(77.4f);
        verify(adWebView, never()).loadUrl("javascript:mraid.fireAudioVolumeChangeEvent(77.4);");
    }

    @Test
    public void fireAudioVolumeChangeEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        mraidBridge.fireAudioVolumeChangeEvent(77.4f);
        verify(adWebView).loadUrl("javascript:mraid.fireAudioVolumeChangeEvent(77.4);");
    }

    @Test
    public void fireErrorEvent_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.fireErrorEvent(MraidCommand.OPEN, "message");
        verify(adWebView, never()).loadUrl("javascript:mraid.fireErrorEvent('message', 'open');");
    }

    @Test
    public void fireErrorEvent_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        mraidBridge.fireErrorEvent(MraidCommand.OPEN, "message");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('message', 'open');");
    }

    @Test
    public void setCurrentAppOrientation_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        mraidBridge.setCurrentAppOrientation(new MraidAppOrientationProperties("landscape", true));
        verify(adWebView, never()).loadUrl("javascript:mraid.setCurrentAppOrientation('landscape', true);");
    }

    @Test
    public void setCurrentAppOrientation_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        mraidBridge.setCurrentAppOrientation(new MraidAppOrientationProperties("landscape", true));
        verify(adWebView).loadUrl("javascript:mraid.setCurrentAppOrientation('landscape', true);");
    }

    @Test
    public void setLocation_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        Location stubLocation = new Location(LocationManager.GPS_PROVIDER);
        stubLocation.setLatitude(10.1);
        stubLocation.setLongitude(50.123);
        stubLocation.setAccuracy(12.1f);
        mraidBridge.setLocation(stubLocation);
        verify(adWebView, never()).loadUrl("javascript:mraid.setLocation(10.1, 50.123, 1, 12.1, " + System.currentTimeMillis() / 1000 + ", '');");
    }

    @Test
    public void setLocation_mraid_GPS_PROVIDER() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        Location stubLocation = new Location(LocationManager.GPS_PROVIDER);
        stubLocation.setLatitude(10.1);
        stubLocation.setLongitude(50.123);
        stubLocation.setAccuracy(12.1f);
        mraidBridge.setLocation(stubLocation);
        verify(adWebView).loadUrl("javascript:mraid.setLocation(10.1, 50.123, 1, 12.1, " + System.currentTimeMillis() / 1000 + ", '');");
    }

    @Test
    public void setLocation_mraid_NETWORK_PROVIDER() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        Location stubLocation = new Location(LocationManager.NETWORK_PROVIDER);
        stubLocation.setLatitude(10.1);
        stubLocation.setLongitude(50.123);
        stubLocation.setAccuracy(12.1f);
        mraidBridge.setLocation(stubLocation);
        verify(adWebView).loadUrl("javascript:mraid.setLocation(10.1, 50.123, 2, 12.1, " + System.currentTimeMillis() / 1000 + ", 'android');");

    }

    @Test
    public void setLocation_mraid_UNKNOWN() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();
        Location stubLocation = new Location("unknown");
        stubLocation.setLatitude(10.1);
        stubLocation.setLongitude(50.123);
        stubLocation.setAccuracy(12.1f);
        mraidBridge.setLocation(stubLocation);
        verify(adWebView).loadUrl("javascript:mraid.setLocation(10.1, 50.123, 2, 12.1, " + System.currentTimeMillis() / 1000 + ", 'android');");
    }

    @Test
    public void setSupportedServices_jsTag() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);
        mraidBridge.setSupportedServices(nativeFeatureManager);
        verify(adWebView, never()).loadUrl(anyString());
    }

    @Test
    public void setSupportedServices_mraid_nullValue() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        mraidBridge.setSupportedServices(null);
        verify(adWebView, never()).loadUrl(anyString());
    }

    @Test
    public void setSupportedServices_mraid() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mock(MraidCommandListener.class));
        mraidBridge.onMraidRequested();

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);
        doReturn(true).when(nativeFeatureManager).isSmsSupported();
        doReturn(true).when(nativeFeatureManager).isTelSupported();
        doReturn(true).when(nativeFeatureManager).isCalendarSupported();
        doReturn(true).when(nativeFeatureManager).isStorePictureSupported();
        doReturn(true).when(nativeFeatureManager).isInlineVideoSupported();
        doReturn(true).when(nativeFeatureManager).isVpaidSupported();
        doReturn(true).when(nativeFeatureManager).isLocationSupported();
        mraidBridge.setSupportedServices(nativeFeatureManager);
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.SMS, true);");
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.TEL, true);");
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.CALENDAR, true);");
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.STOREPICTURE, true);");
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.INLINEVIDEO, true);");
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.VPAID, true);");
        verify(adWebView).loadUrl("javascript:mraid.setSupports(mraid.SUPPORTED_FEATURES.LOCATION, true);");
    }

    @Test
    public void onProcessCommand_CLOSE() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://close");
        verify(mraidCommandListener).onClose();
    }

    @Test
    public void onProcessCommand_UNLOAD() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://unload");
        verify(mraidCommandListener).onUnload();
    }

    @Test
    public void onProcessCommand_JS_TAG_LOADED() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://loaded");
        verify(mraidCommandListener).onLoaded();
    }

    @Test
    public void onProcessCommand_JS_TAG_NO_FILL() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://noFill");
        verify(mraidCommandListener).onFailedToLoad();
    }

    @Test
    public void onProcessCommand_SET_RESIZE_PROPERTIES() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://setResizeProperties?width=320&height=480&offsetX=15&offsetY=45&customClosePosition=top-left&allowOffscreen=false");
        assertEquals(mraidBridge.resizeProperties.width, 320);
        assertEquals(mraidBridge.resizeProperties.height, 480);
        assertEquals(mraidBridge.resizeProperties.offsetX, 15);
        assertEquals(mraidBridge.resizeProperties.offsetY, 45);
        assertEquals(mraidBridge.resizeProperties.customClosePosition, ClosePosition.TOP_RIGHT);
        assertEquals(mraidBridge.resizeProperties.allowOffscreen, false);
    }

    @Test
    public void onProcessCommand_RESIZE_wo_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.resizeProperties = new MraidResizeProperties(0, 0, 0, 0, ClosePosition.TOP_RIGHT, true);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://resize");
        verify(mraidCommandListener, never()).onResize(mraidBridge.resizeProperties);
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Cannot execute this command, view wasn't click', 'resize');");
    }

    @Test
    public void onProcessCommand_RESIZE_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.resizeProperties = new MraidResizeProperties(0, 0, 0, 0, ClosePosition.TOP_RIGHT, true);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://resize");
        verify(mraidCommandListener).onResize(mraidBridge.resizeProperties);
    }

    @Test
    public void onProcessCommand_RESIZE_error() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.resizeProperties = new MraidResizeProperties(0, 0, 0, 0, ClosePosition.TOP_RIGHT, true);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        doThrow(new MraidError("message")).when(mraidCommandListener).onResize(mraidBridge.resizeProperties);
        mraidBridge.onProcessCommand("mraid://resize");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('message', 'resize');");
    }

    @Test
    public void onProcessCommand_EXPAND_wo_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://expand");
        verify(mraidCommandListener, never()).onExpand();
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Cannot execute this command, view wasn't click', 'expand');");
    }

    @Test
    public void onProcessCommand_EXPAND_two_part() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://expand?url=newUrl");
        verify(mraidCommandListener, never()).onExpand();
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Two-part ads deprecated', 'expand');");
    }

    @Test
    public void onProcessCommand_EXPAND_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://expand");
        verify(mraidCommandListener).onExpand();
    }

    @Test
    public void onProcessCommand_EXPAND_error() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        doThrow(new MraidError("message")).when(mraidCommandListener).onExpand();
        mraidBridge.onProcessCommand("mraid://expand");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('message', 'expand');");
    }

    @Test
    public void onProcessCommand_USE_CUSTOM_CLOSE() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://useCustomClose?useCustomClose=true");
        verify(mraidCommandListener, never()).onExpand();
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('For MRAID 2.0 or older version ads in MRAID 3.0 containers " +
                "useCustomClose() requests will be ignored by the host', 'useCustomClose');");
    }

    @Test
    public void onProcessCommand_OPEN_wo_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://open?url=link");
        verify(mraidCommandListener, never()).onOpen("link");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Cannot execute this command, view wasn't click', 'open');");
    }

    @Test
    public void onProcessCommand_OPEN_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://open?url=link");
        verify(mraidCommandListener).onOpen("link");
    }

    @Test
    public void onProcessCommand_SET_ORIENTATION_PROPERTIES() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://setOrientationProperties?allowOrientationChange=true&forceOrientation=landscape");
        verify(mraidCommandListener).onSetOrientationProperties(true, MraidOrientation.LANDSCAPE);
    }

    @Test
    public void onProcessCommand_SET_ORIENTATION_PROPERTIES_error() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        doThrow(new MraidError("message")).when(mraidCommandListener).onSetOrientationProperties(true, MraidOrientation.LANDSCAPE);
        mraidBridge.onProcessCommand("mraid://setOrientationProperties?allowOrientationChange=true&forceOrientation=landscape");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('message', 'setOrientationProperties');");
    }

    @Test
    public void onProcessCommand_PLAY_VIDEO_wo_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://playVideo?url=link");
        verify(mraidCommandListener, never()).onPlayVideo("link");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Cannot execute this command, view wasn't click', 'playVideo');");
    }

    @Test
    public void onProcessCommand_PLAY_VIDEO_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://playVideo?url=link");
        verify(mraidCommandListener).onPlayVideo("link");
    }

    @Test
    public void onProcessCommand_STORE_PICTURE_wo_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://storePicture?url=link");
        verify(mraidCommandListener, never()).onStorePicture("link");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Cannot execute this command, view wasn't click', 'storePicture');");
    }

    @Test
    public void onProcessCommand_STORE_PICTURE_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://storePicture?url=link");
        verify(mraidCommandListener).onStorePicture("link");
    }

    @Test
    public void onProcessCommand_CREATE_CALENDAR_EVENT_wo_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("mraid://createCalendarEvent?eventJSON={\"description\": \"Mayan Apocalypse/End of World\", \"location\": \"everywhere\", \"start\": \"2012-12-21T00:00-05:00\", \"end\": \"2012-12- 22T00:00-05:00\"}");
        verify(mraidCommandListener, never()).onCreateCalendarEvent("{\"description\": \"Mayan Apocalypse/End of World\", \"location\": \"everywhere\", \"start\": \"2012-12-21T00:00-05:00\", \"end\": \"2012-12- 22T00:00-05:00\"}");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Cannot execute this command, view wasn't click', 'createCalendarEvent');");
    }

    @Test
    public void onProcessCommand_CREATE_CALENDAR_EVENT_click() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onTouch();
        mraidBridge.onProcessCommand("mraid://createCalendarEvent?eventJSON={\"description\": \"Mayan Apocalypse/End of World\", \"location\": \"everywhere\", \"start\": \"2012-12-21T00:00-05:00\", \"end\": \"2012-12- 22T00:00-05:00\"}");
        verify(mraidCommandListener).onCreateCalendarEvent("{\"description\": \"Mayan Apocalypse/End of World\", \"location\": \"everywhere\", \"start\": \"2012-12-21T00:00-05:00\", \"end\": \"2012-12- 22T00:00-05:00\"}");
    }

    @Test
    public void onProcessCommand_UNKNOWN() throws Exception {
        MraidBridge mraidBridge = new MraidBridge(MraidPlacementType.INLINE);
        MraidCommandListener mraidCommandListener = mock(MraidCommandListener.class);
        mraidBridge.initMraidWebView(adWebView, mraidEnvironment, mraidCommandListener);
        mraidBridge.onMraidRequested();

        mraidBridge.onProcessCommand("unknown");
        verify(adWebView).loadUrl("javascript:mraid.fireErrorEvent('Unspecified MRAID command', '');");
    }
}