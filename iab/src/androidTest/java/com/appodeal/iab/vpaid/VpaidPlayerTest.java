package com.appodeal.iab.vpaid;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.appodeal.iab.vast.PlayerLayerListener;
import com.appodeal.iab.webview.AdWebView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class VpaidPlayerTest {

    @Test
    public void loadTest() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                mock(PlayerLayerListener.class));

        vpaidPlayer.load();

        verify(vpaidBridge).initVpaidWebView(adWebView, vpaidPlayer);
        verify(vpaidBridge).loadContentHtml("url", "http://localhost");
    }

    @Test
    public void destroy() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                mock(PlayerLayerListener.class));
        vpaidPlayer.destroy();

        assertTrue(vpaidPlayer.isDestroyed());
    }

    @Test
    public void vpaidViewPageFinished_isDestroyed_shouldDoNothing() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                mock(PlayerLayerListener.class));
        vpaidPlayer.destroy();

        vpaidPlayer.vpaidViewPageFinished();

        verify(vpaidBridge, never()).setCreativeDataAndLoad("adParameters");

    }

    @Test
    public void vpaidViewPageFinished_shouldSetCreativeDataAndLoad() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                mock(PlayerLayerListener.class));
        vpaidPlayer.vpaidViewPageFinished();

        verify(vpaidBridge).setCreativeDataAndLoad("adParameters");
    }

    @Test
    public void vpaidViewRenderProcessGone_shouldDestroy() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                mock(PlayerLayerListener.class));
        assertFalse(vpaidPlayer.isDestroyed());

        vpaidPlayer.vpaidViewRenderProcessGone();

        assertTrue(vpaidPlayer.isDestroyed());
    }

    @Test
    public void vpaidAdSkipped_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdSkipped();

        verify(playerLayerListener).onComplete();
    }

    @Test
    public void vpaidAdLoaded_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdLoaded();

        verify(playerLayerListener).onLoaded();

        assertTrue(vpaidPlayer.isLoaded());
    }

    @Test
    public void vpaidAdClickThru_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdClickThru("link");

        verify(playerLayerListener).onClick("link");
    }

    @Test
    public void vpaidAdVideoStart_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdVideoStart();

        verify(playerLayerListener).onStarted();
    }

    @Test
    public void vpaidAdVideoFirstQuartile_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdVideoFirstQuartile();

        verify(playerLayerListener).onFirstQuartile();
    }

    @Test
    public void vpaidAdVideoMidpoint_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdVideoMidpoint();

        verify(playerLayerListener).onMidpoint();
    }

    @Test
    public void vpaidAdVideoThirdQuartile_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdVideoThirdQuartile();

        verify(playerLayerListener).onThirdQuartile();
    }

    @Test
    public void vpaidAdVideoComplete_shouldCallListener() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdVideoComplete();

        verify(playerLayerListener).onComplete();
    }

    @Test
    public void vpaidAdError_beforeLoaded_shouldCallFailToLoad() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.vpaidAdError("error");

        verify(playerLayerListener).onFailedToLoad();
    }

    @Test
    public void vpaidAdError_afterLoaded_shouldCallError() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);
        vpaidPlayer.vpaidAdLoaded();
        vpaidPlayer.vpaidAdError("error");

        verify(playerLayerListener).onError();
    }

    @Test
    public void pause_destroyed_shouldDoNothing() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.destroy();

        vpaidPlayer.pause();

        verify(adWebView, never()).onPause();
        verify(vpaidBridge, never()).fireAdPauseEvent();
    }

    @Test
    public void pause_shouldPause() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.pause();

        verify(adWebView).onPause();
        verify(vpaidBridge).fireAdPauseEvent();
    }

    @Test
    public void resume_destroyed_shouldDoNothing() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.destroy();

        vpaidPlayer.resume();

        verify(adWebView, never()).onResume();
        verify(vpaidBridge, never()).fireAdResumeEvent();
    }

    @Test
    public void resume_shouldResume() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.resume();

        verify(adWebView).onResume();
        verify(vpaidBridge).fireAdResumeEvent();
    }

    @Test
    public void start_destroyed_shouldDoNothing() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);
        vpaidPlayer.destroy();

        vpaidPlayer.start();

        verify(vpaidBridge, never()).fireStartAdEvent();
    }

    @Test
    public void start_shouldStart() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.start();

        verify(vpaidBridge).fireStartAdEvent();
    }

    @Test
    public void setVolume_destroyed_shouldDoNothing() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);
        vpaidPlayer.destroy();

        vpaidPlayer.setVolume(1);

        verify(vpaidBridge, never()).setVolume(1);
    }

    @Test
    public void setVolume_shouldStart() throws Exception {
        AdWebView adWebView = mock(AdWebView.class);
        VpaidBridge vpaidBridge = mock(VpaidBridge.class);
        PlayerLayerListener playerLayerListener = mock(PlayerLayerListener.class);
        VpaidPlayer vpaidPlayer = new VpaidPlayer(InstrumentationRegistry.getTargetContext(),
                adWebView,
                vpaidBridge,
                "url",
                "adParameters",
                playerLayerListener);

        vpaidPlayer.setVolume(1);

        verify(vpaidBridge).setVolume(1);
    }

}