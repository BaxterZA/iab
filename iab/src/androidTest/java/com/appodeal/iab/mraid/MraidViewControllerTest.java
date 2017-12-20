package com.appodeal.iab.mraid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;

import com.appodeal.iab.MraidActivity;
import com.appodeal.iab.MraidEnvironment;
import com.appodeal.iab.MraidNativeFeature;
import com.appodeal.iab.MraidNativeFeatureListener;
import com.appodeal.iab.MraidView;
import com.appodeal.iab.WebViewDebugListener;
import com.appodeal.iab.views.CloseableLayout;
import com.appodeal.iab.webview.AdWebView;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class MraidViewControllerTest {

    @Rule
    public ActivityTestRule<MraidActivity> activityRule = new ActivityTestRule<>(MraidActivity.class);

    @Test
    public void getMraidEnvironment_defaultValues() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidEnvironment mraidEnvironment = mraidViewController.getMraidEnvironment();
        JSONObject jsonObject = new JSONObject(mraidEnvironment.toMraidString());
        assertEquals(jsonObject.getString("version"), MraidEnvironment.version);
        assertEquals(jsonObject.getString("sdk"), MraidEnvironment.sdk);
        assertEquals(jsonObject.getString("sdkVersion"), MraidEnvironment.sdkVersion);
    }

    @Test
    public void getMraidEnvironment_userData() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidEnvironment mraidEnvironment = new MraidEnvironment.Builder().setIfa("ifa").setAppId("com.appodeal.mraid").setLimitAdTracking(true).setCoppa(true).build();
        mraidViewController.setMraidEnvironment(mraidEnvironment);
        assertEquals(mraidEnvironment, mraidViewController.getMraidEnvironment());
    }

    @Test
    public void getActivity_withActivity_shouldReturnActivity() {
        Activity mockActivity = activityRule.getActivity();
        MraidViewController mraidViewController = new MraidViewController(mockActivity,
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        assertEquals(mraidViewController.getActivity(), mockActivity);
    }

    @Test
    public void getActivity_withContext_shouldReturnNull() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        assertEquals(mraidViewController.getActivity(), null);
    }

    @Test
    public void attachActivity_shouldReturnActivityAfter() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        assertEquals(mraidViewController.getActivity(), null);

        Activity mockActivity = activityRule.getActivity();
        mraidViewController.attachActivity(mockActivity);
        assertEquals(mraidViewController.getActivity(), mockActivity);
    }

    @Test
    public void destroy_shouldSetNull() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.setSupportedFeatures(new ArrayList<MraidNativeFeature>(), mock(MraidNativeFeatureListener.class));

        assertNotNull(mraidViewController);
        assertNotNull(mraidViewController.mraidNativeFeatureManager);
        assertNotNull(mraidViewController.getMraidView());
        assertFalse(mraidViewController.isDestroyed());

        mraidViewController.destroy();

        assertNull(mraidViewController.mraidNativeFeatureManager);
        assertNull(mraidViewController.getMraidView());
        assertTrue(mraidViewController.isDestroyed());
    }

    @Test
    public void mraidViewPageFinished_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.destroy();

        mraidViewController.mraidViewPageFinished();
        assertFalse(mraidViewController.isLoaded());
    }

    @Test
    public void mraidViewPageFinished_jsTag_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.setJsTag(true);

        mraidViewController.mraidViewPageFinished();
        assertFalse(mraidViewController.isLoaded());
    }

    @Test
    public void mraidViewPageFinished_shouldCallLoaded() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.mraidViewPageFinished();
        assertTrue(mraidViewController.isLoaded());
        verify(mraidViewControllerListener).onMraidViewControllerLoaded(mraidViewController);
    }

    @Test
    public void mraidViewRenderProcessGone_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);
        mraidViewController.destroy();
        assertTrue(mraidViewController.isDestroyed());

        mraidViewController.mraidViewRenderProcessGone();

        verify(mraidViewControllerListener, never()).onMraidViewControllerUnloaded(mraidViewController);
        assertTrue(mraidViewController.isDestroyed());
    }

    @Test
    public void mraidViewRenderProcessGone_shouldCallUnloaded() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        assertFalse(mraidViewController.isDestroyed());

        mraidViewController.mraidViewRenderProcessGone();

        verify(mraidViewControllerListener).onMraidViewControllerUnloaded(mraidViewController);
        assertTrue(mraidViewController.isDestroyed());
    }

    @Test
    public void onAudioVolumeChange_destroyed_shouldDoNothing() throws Exception {
        MraidBridge mraidBridge = mock(MraidBridge.class);
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mraidBridge,
                mock(MraidOrientationProperties.class));

        mraidViewController.destroy();

        mraidViewController.onAudioVolumeChange(15f);

        verify(mraidBridge, never()).fireAudioVolumeChangeEvent(15f);
    }

    @Test
    public void onAudioVolumeChange_shouldCallFireViewableChangeEvent() throws Exception {
        MraidBridge mraidBridge = mock(MraidBridge.class);
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mraidBridge,
                mock(MraidOrientationProperties.class));

        mraidViewController.onAudioVolumeChange(15f);

        verify(mraidBridge).fireAudioVolumeChangeEvent(15f);
    }

    @Test
    public void onLoaded_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.destroy();

        mraidViewController.onLoaded();

        assertFalse(mraidViewController.isLoaded());
        verify(mraidViewControllerListener, never()).onMraidViewControllerLoaded(mraidViewController);
    }

    @Test
    public void onLoaded_notJsTag_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.destroy();

        mraidViewController.onLoaded();

        assertFalse(mraidViewController.isLoaded());
        verify(mraidViewControllerListener, never()).onMraidViewControllerLoaded(mraidViewController);
    }

    @Test
    public void onLoaded_jsTag_shouldCallLoaded() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.setJsTag(true);

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onLoaded();

        assertTrue(mraidViewController.isLoaded());
        verify(mraidViewControllerListener).onMraidViewControllerLoaded(mraidViewController);
    }

    @Test
    public void onFailedToLoad_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.destroy();

        mraidViewController.onFailedToLoad();

        assertFalse(mraidViewController.isLoaded());
        verify(mraidViewControllerListener, never()).onMraidViewControllerFailedToLoad(mraidViewController);
    }

    @Test
    public void onFailedToLoad_notJsTag_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.destroy();

        mraidViewController.onFailedToLoad();

        assertFalse(mraidViewController.isLoaded());
        verify(mraidViewControllerListener, never()).onMraidViewControllerFailedToLoad(mraidViewController);
    }

    @Test
    public void onFailedToLoad_jsTag_shouldCallFailToLoad() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.setJsTag(true);

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onFailedToLoad();

        assertFalse(mraidViewController.isLoaded());
        verify(mraidViewControllerListener).onMraidViewControllerFailedToLoad(mraidViewController);
    }

    @Test
    public void onResize_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));

        mraidViewController.destroy();

        mraidViewController.onResize(mock(MraidResizeProperties.class));

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test
    public void onResize_stateLOADING_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.LOADING;

        mraidViewController.onResize(mock(MraidResizeProperties.class));

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test
    public void onResize_stateHIDDEN_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.HIDDEN;

        mraidViewController.onResize(mock(MraidResizeProperties.class));

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test(expected = MraidError.class)
    public void onResize_stateEXPANDED_shouldThrowError() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.EXPANDED;

        mraidViewController.onResize(mock(MraidResizeProperties.class));

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test(expected = MraidError.class)
    public void onResize_placementTypeINTERSTITIAL_shouldThrowError() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INTERSTITIAL,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.DEFAULT;

        mraidViewController.onResize(mock(MraidResizeProperties.class));

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test(expected = MraidError.class)
    public void onResize_offScreenNotAllowed_shouldThrowError() throws Exception {
        MraidScreenMetrics mraidScreenMetrics = mock(MraidScreenMetrics.class);
        doReturn(new Size(1920, 1080)).when(mraidScreenMetrics).getMaxSize();
        doReturn(new Rect(0, 0, 320, 50)).when(mraidScreenMetrics).getDefaultSize();
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mraidScreenMetrics,
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.DEFAULT;

        MraidResizeProperties mraidResizeProperties = spy(new MraidResizeProperties(3840, 2160, 0, 0, ClosePosition.TOP_RIGHT, false));
        doReturn(new Rect(0, 0, 3840, 2160)).when(mraidResizeProperties).getResizeRect(any(Context.class), anyInt(), anyInt());
        mraidViewController.onResize(mraidResizeProperties);

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test(expected = MraidError.class)
    public void onResize_closeButtonOffScreen_shouldThrowError() throws Exception {
        MraidScreenMetrics mraidScreenMetrics = mock(MraidScreenMetrics.class);
        doReturn(new Size(1920, 1080)).when(mraidScreenMetrics).getMaxSize();
        doReturn(new Rect(0, 0, 320, 50)).when(mraidScreenMetrics).getDefaultSize();
        doReturn(new Rect(0, 0, 1920, 1080)).when(mraidScreenMetrics).getMasSizeRect();
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mraidScreenMetrics,
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        doNothing().when(mraidViewController).resize(any(Rect.class));

        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.DEFAULT;

        MraidResizeProperties mraidResizeProperties = spy(new MraidResizeProperties(3840, 2160, 0, 0, ClosePosition.TOP_RIGHT, true));
        doReturn(new Rect(0, 0, 3840, 2160)).when(mraidResizeProperties).getResizeRect(any(Context.class), anyInt(), anyInt());
        mraidViewController.onResize(mraidResizeProperties);

        verify(mraidViewController, never()).resize(any(Rect.class));
    }

    @Test
    public void onResize_offScreenNotAllowed_shouldCallResize() throws Exception {
        MraidScreenMetrics mraidScreenMetrics = mock(MraidScreenMetrics.class);
        doReturn(new Size(1920, 1080)).when(mraidScreenMetrics).getMaxSize();
        doReturn(new Rect(0, 0, 320, 50)).when(mraidScreenMetrics).getDefaultSize();
        doReturn(new Rect(0, 0, 1920, 1080)).when(mraidScreenMetrics).getMasSizeRect();
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mraidScreenMetrics,
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));
        doNothing().when(mraidViewController).resize(any(Rect.class));

        mraidViewController.closeableLayout = mock(CloseableLayout.class);
        mraidViewController.adWebView = mock(AdWebView.class);
        mraidViewController.mraidState = MraidState.DEFAULT;

        MraidResizeProperties mraidResizeProperties = spy(new MraidResizeProperties(300, 250, 0, 0, ClosePosition.TOP_LEFT, false));
        doReturn(new Rect(0, 0, 300, 250)).when(mraidResizeProperties).getResizeRect(any(Context.class), anyInt(), anyInt());
        mraidViewController.onResize(mraidResizeProperties);

        verify(mraidViewController).resize(any(Rect.class));
    }


    //TODO Expand, close, updateSize

    @Test
    public void onClose_destroyed_shouldHandleClose() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INTERSTITIAL,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onClose();

        verify(mraidViewController).onCloseEvent();
    }

    @Test
    public void onUnload_shouldCallUnloaded() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INTERSTITIAL,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onUnload();

        verify(mraidViewControllerListener).onMraidViewControllerUnloaded(mraidViewController);
        assertTrue(mraidViewController.isDestroyed());
    }

    @Test
    public void onUnload_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INTERSTITIAL,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.destroy();

        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onUnload();

        verify(mraidViewControllerListener, never()).onMraidViewControllerUnloaded(mraidViewController);
    }

    @Test
    public void onSetOrientationProperties_interstitial_shouldApply() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(false, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INTERSTITIAL,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(true).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.PORTRAIT);
        doNothing().when(mraidViewController).applyOrientationProperties();
        assertEquals(mraidViewController.mraidOrientationProperties.allowOrientationChange, false);
        assertEquals(mraidViewController.mraidOrientationProperties.forceOrientation, MraidOrientation.LANDSCAPE);

        mraidViewController.onSetOrientationProperties(true, MraidOrientation.PORTRAIT);

        assertEquals(mraidViewController.mraidOrientationProperties.allowOrientationChange, true);
        assertEquals(mraidViewController.mraidOrientationProperties.forceOrientation, MraidOrientation.PORTRAIT);
        verify(mraidViewController).applyOrientationProperties();
    }

    @Test
    public void onSetOrientationProperties_stateExpanded_shouldApply() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(false, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        mraidViewController.mraidState = MraidState.EXPANDED;

        doReturn(true).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.PORTRAIT);
        doNothing().when(mraidViewController).applyOrientationProperties();
        assertEquals(mraidViewController.mraidOrientationProperties.allowOrientationChange, false);
        assertEquals(mraidViewController.mraidOrientationProperties.forceOrientation, MraidOrientation.LANDSCAPE);

        mraidViewController.onSetOrientationProperties(true, MraidOrientation.PORTRAIT);

        assertEquals(mraidViewController.mraidOrientationProperties.allowOrientationChange, true);
        assertEquals(mraidViewController.mraidOrientationProperties.forceOrientation, MraidOrientation.PORTRAIT);
        verify(mraidViewController).applyOrientationProperties();
    }

    @Test
    public void onSetOrientationProperties_stateDefault_shouldSavePropertiesWithoutApply() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(false, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(true).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.PORTRAIT);
        assertEquals(mraidViewController.mraidOrientationProperties.allowOrientationChange, false);
        assertEquals(mraidViewController.mraidOrientationProperties.forceOrientation, MraidOrientation.LANDSCAPE);

        mraidViewController.onSetOrientationProperties(true, MraidOrientation.PORTRAIT);

        assertEquals(mraidViewController.mraidOrientationProperties.allowOrientationChange, true);
        assertEquals(mraidViewController.mraidOrientationProperties.forceOrientation, MraidOrientation.PORTRAIT);
        verify(mraidViewController, never()).applyOrientationProperties();
    }

    @Test(expected = MraidError.class)
    public void onSetOrientationProperties_disabledByShouldAllowForceOrientation_shouldThrowError() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));

        doReturn(false).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.PORTRAIT);

        mraidViewController.onSetOrientationProperties(true, MraidOrientation.PORTRAIT);

        verify(mraidViewController, never()).applyOrientationProperties();
    }

    @Test
    public void onSetOrientationProperties_destroyed_shouldDoNothing() throws Exception {
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class)));

        mraidViewController.destroy();

        mraidViewController.shouldAllowForceOrientation(MraidOrientation.PORTRAIT);

        verify(mraidViewController, never()).applyOrientationProperties();
    }

    @Test
    public void onOpen_shouldOpenUrl() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);
        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onOpen("http://appodeal.com");

        verify(nativeFeatureManager).open("http://appodeal.com");
        verify(mraidViewControllerListener).onMraidViewControllerClicked(mraidViewController);
    }

    @Test
    public void onOpen_shouldSendSms() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);
        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onOpen("sms://123456");

        verify(nativeFeatureManager).sendSms("sms://123456");
        verify(mraidViewControllerListener).onMraidViewControllerClicked(mraidViewController);
    }

    @Test
    public void onOpen_shouldCallTel() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);
        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.onOpen("tel://123456");

        verify(nativeFeatureManager).calTell("tel://123456");
        verify(mraidViewControllerListener).onMraidViewControllerClicked(mraidViewController);
    }

    @Test
    public void onOpen_destroyed_shouldDoNothing() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);
        MraidViewControllerListener mraidViewControllerListener = mock(MraidViewControllerListener.class);
        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;
        mraidViewController.setMraidViewControllerListener(mraidViewControllerListener);

        mraidViewController.destroy();

        mraidViewController.onOpen("http://appodeal.com");

        verify(nativeFeatureManager, never()).open("http://appodeal.com");
        verify(mraidViewControllerListener, never()).onMraidViewControllerClicked(mraidViewController);
    }

    @Test
    public void onPlayVideo_destroyed_shouldDoNothing() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);

        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;

        mraidViewController.destroy();

        mraidViewController.onPlayVideo("url");

        verify(nativeFeatureManager, never()).playVideo("url");
    }

    @Test
    public void onPlayVideo_shouldCall() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);

        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;

        mraidViewController.onPlayVideo("url");

        verify(nativeFeatureManager).playVideo("url");
    }

    @Test
    public void onStorePicture_shouldCall() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);

        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;

        mraidViewController.onStorePicture("url");

        verify(nativeFeatureManager).storePicture("url");
    }

    @Test
    public void onStorePicture_destroyed_shouldDoNothing() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);

        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;

        mraidViewController.destroy();

        mraidViewController.onStorePicture("url");

        verify(nativeFeatureManager, never()).storePicture("url");
    }

    @Test
    public void onCreateCalendarEvent_shouldCall() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);

        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;

        mraidViewController.onCreateCalendarEvent("event");

        verify(nativeFeatureManager).createCalendarEvent("event");
    }

    @Test
    public void onCreateCalendarEvent_destroyed_shouldDoNothing() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        MraidNativeFeatureManager nativeFeatureManager = mock(MraidNativeFeatureManager.class);

        mraidViewController.mraidNativeFeatureManager = nativeFeatureManager;

        mraidViewController.destroy();

        mraidViewController.onCreateCalendarEvent("event");

        verify(nativeFeatureManager, never()).createCalendarEvent("event");
    }

    @Test
    public void onJsAlert() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        WebViewDebugListener webViewDebugListener = mock(WebViewDebugListener.class);
        mraidViewController.setAdWebViewDebugListener(webViewDebugListener);

        JsResult jsResult = mock(JsResult.class);
        mraidViewController.onJsAlert("error message", jsResult);

        verify(webViewDebugListener).onJsAlert("error message", jsResult);
    }

    @Test
    public void onConsoleMessage() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        WebViewDebugListener webViewDebugListener = mock(WebViewDebugListener.class);
        mraidViewController.setAdWebViewDebugListener(webViewDebugListener);

        ConsoleMessage consoleMessage = mock(ConsoleMessage.class);
        mraidViewController.onConsoleMessage(consoleMessage);

        verify(webViewDebugListener).onConsoleMessage(consoleMessage);
    }

    @Test
    public void applyOrientationProperties_allowChangeWithNone_shouldRestoreOriginal() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.NONE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doNothing().when(mraidViewController).lockOrientation(anyInt());
        doNothing().when(mraidViewController).restoreOrientation();
        mraidViewController.applyOrientationProperties();

        verify(mraidViewController).restoreOrientation();
    }

    @Test
    public void applyOrientationProperties_lockChangeWithNoneAndCurrentPortrait_shouldLockCurrentPortraitOrientation() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(false, MraidOrientation.NONE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(activityRule.getActivity()).when(mraidViewController).getActivity();
        mraidViewController.applyOrientationProperties();

        verify(mraidViewController).lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void applyOrientationProperties_lockChangeWithNone_shouldLockCurrentOrientation() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(false, MraidOrientation.NONE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(activityRule.getActivity()).when(mraidViewController).getActivity();
        mraidViewController.applyOrientationProperties();

        verify(mraidViewController).lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test(expected = MraidError.class)
    public void applyOrientationProperties_lockChangeWithNoneWithoutActivity_shouldThrowError() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(false, MraidOrientation.NONE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(null).when(mraidViewController).getActivity();
        mraidViewController.applyOrientationProperties();

        verify(mraidViewController).lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void applyOrientationProperties_portraitWithForceOrientation_shouldLockOrientation() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.PORTRAIT);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));
        doNothing().when(mraidViewController).lockOrientation(anyInt());
        mraidViewController.applyOrientationProperties();

        verify(mraidViewController).lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void applyOrientationProperties_landscapeWithForceOrientation_shouldLockOrientation() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));
        doNothing().when(mraidViewController).lockOrientation(anyInt());
        mraidViewController.applyOrientationProperties();

        verify(mraidViewController).lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Test
    public void lockOrientation_portrait_shouldRequestOrientationOrientation() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.PORTRAIT);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        Activity activity = mock(Activity.class);
        doReturn(activity).when(mraidViewController).getActivity();
        doReturn(true).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.PORTRAIT);

        mraidViewController.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        verify(activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void lockOrientation_landscape_shouldRequestOrientation() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        Activity activity = mock(Activity.class);
        doReturn(activity).when(mraidViewController).getActivity();
        doReturn(true).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.LANDSCAPE);

        mraidViewController.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        verify(activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Test(expected = MraidError.class)
    public void lockOrientation_activityIsNull_shouldThrowError() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(null).when(mraidViewController).getActivity();

        mraidViewController.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Test(expected = MraidError.class)
    public void lockOrientation_forceOrientationDisabled_shouldThrowError() throws Exception {
        MraidOrientationProperties mraidOrientationProperties = new MraidOrientationProperties(true, MraidOrientation.LANDSCAPE);
        MraidViewController mraidViewController = spy(new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mraidOrientationProperties));

        doReturn(activityRule.getActivity()).when(mraidViewController).getActivity();
        doReturn(false).when(mraidViewController).shouldAllowForceOrientation(MraidOrientation.LANDSCAPE);

        mraidViewController.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Test
    public void restoreOrientation_shouldRequestOriginal() throws Exception {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.originalRequestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        Activity activity = mock(Activity.class);
        mraidViewController.attachActivity(activity);

        mraidViewController.restoreOrientation();

        verify(activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Test
    public void resume_shouldResumeWebView() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.adWebView = mock(AdWebView.class);

        mraidViewController.resume();

        verify(mraidViewController.adWebView).onResume();
    }

    @Test
    public void resume_destroyed_shouldDoNothing() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.destroy();
        mraidViewController.adWebView = mock(AdWebView.class);

        mraidViewController.resume();

        verify(mraidViewController.adWebView, never()).onResume();
    }

    @Test
    public void pause_shouldPauseWebView() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.adWebView = mock(AdWebView.class);

        mraidViewController.pause();

        verify(mraidViewController.adWebView).onPause();
    }

    @Test
    public void pause_destroyed_shouldDoNothing() {
        MraidViewController mraidViewController = new MraidViewController(InstrumentationRegistry.getTargetContext(),
                mock(MraidView.class),
                mock(MraidScreenMetrics.class),
                MraidPlacementType.INLINE,
                mock(MraidBridge.class),
                mock(MraidOrientationProperties.class));

        mraidViewController.destroy();
        mraidViewController.adWebView = mock(AdWebView.class);

        mraidViewController.pause();

        verify(mraidViewController.adWebView, never()).onPause();
    }


}