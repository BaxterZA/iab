package com.appodeal.iab.vpaid;

import android.support.annotation.NonNull;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;

import com.appodeal.iab.webview.AdWebView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class VpaidBridgeTest {
    private AdWebView adWebView;

    @Before
    public void setUp() throws Exception {
        adWebView = mock(AdWebView.class);
    }

    @Test
    public void onRenderProcessGone() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        vpaidBridge.initVpaidWebView(adWebView, new VpaidCommandListener() {
            @Override
            public void vpaidViewPageFinished() {
                
            }

            @Override
            public void vpaidViewRenderProcessGone() {
                countDownLatch.countDown();
            }

            @Override
            public void vpaidAdStarted() {

            }

            @Override
            public void vpaidAdStopped() {

            }

            @Override
            public void vpaidAdSkipped() {

            }

            @Override
            public void vpaidAdLoaded() {

            }

            @Override
            public void vpaidAdLinearChange() {

            }

            @Override
            public void vpaidAdSizeChange() {

            }

            @Override
            public void vpaidAdExpandedChange() {

            }

            @Override
            public void vpaidAdSkippableStateChange(String state) {

            }

            @Override
            public void vpaidAdDurationChange(String duration) {

            }

            @Override
            public void vpaidAdVolumeChange(String volume) {

            }

            @Override
            public void vpaidAdImpression() {

            }

            @Override
            public void vpaidAdClickThru(String url) {

            }

            @Override
            public void vpaidAdInteraction() {

            }

            @Override
            public void vpaidAdVideoStart() {

            }

            @Override
            public void vpaidAdVideoFirstQuartile() {

            }

            @Override
            public void vpaidAdVideoMidpoint() {

            }

            @Override
            public void vpaidAdVideoThirdQuartile() {

            }

            @Override
            public void vpaidAdVideoComplete() {

            }

            @Override
            public void vpaidAdUserAcceptInvitation() {

            }

            @Override
            public void vpaidAdUserMinimize() {

            }

            @Override
            public void vpaidAdUserClose() {

            }

            @Override
            public void vpaidAdPaused() {

            }

            @Override
            public void vpaidAdPlaying() {

            }

            @Override
            public void vpaidAdError(String error) {

            }

            @Override
            public void vpaidAdLog(String log) {

            }

            @Override
            public void vpaidAdRemainingTime(String time) {

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

        vpaidBridge.onRenderProcessGone();
        countDownLatch.await();
    }

    @Test
    public void onPageFinished() throws Exception {
        final VpaidBridge vpaidBridge = new VpaidBridge();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        vpaidBridge.initVpaidWebView(adWebView, new VpaidCommandListener() {
            @Override
            public void vpaidViewPageFinished() {
                assertTrue(vpaidBridge.isLoaded());
                countDownLatch.countDown();
            }

            @Override
            public void vpaidViewRenderProcessGone() {
                
            }

            @Override
            public void vpaidAdStarted() {

            }

            @Override
            public void vpaidAdStopped() {

            }

            @Override
            public void vpaidAdSkipped() {

            }

            @Override
            public void vpaidAdLoaded() {

            }

            @Override
            public void vpaidAdLinearChange() {

            }

            @Override
            public void vpaidAdSizeChange() {

            }

            @Override
            public void vpaidAdExpandedChange() {

            }

            @Override
            public void vpaidAdSkippableStateChange(String state) {

            }

            @Override
            public void vpaidAdDurationChange(String duration) {

            }

            @Override
            public void vpaidAdVolumeChange(String volume) {

            }

            @Override
            public void vpaidAdImpression() {

            }

            @Override
            public void vpaidAdClickThru(String url) {

            }

            @Override
            public void vpaidAdInteraction() {

            }

            @Override
            public void vpaidAdVideoStart() {

            }

            @Override
            public void vpaidAdVideoFirstQuartile() {

            }

            @Override
            public void vpaidAdVideoMidpoint() {

            }

            @Override
            public void vpaidAdVideoThirdQuartile() {

            }

            @Override
            public void vpaidAdVideoComplete() {

            }

            @Override
            public void vpaidAdUserAcceptInvitation() {

            }

            @Override
            public void vpaidAdUserMinimize() {

            }

            @Override
            public void vpaidAdUserClose() {

            }

            @Override
            public void vpaidAdPaused() {

            }

            @Override
            public void vpaidAdPlaying() {

            }

            @Override
            public void vpaidAdError(String error) {

            }

            @Override
            public void vpaidAdLog(String log) {

            }

            @Override
            public void vpaidAdRemainingTime(String time) {

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

        assertFalse(vpaidBridge.isLoaded());
        vpaidBridge.onPageFinished();
        countDownLatch.await();
    }

    @Test
    public void onTouch() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        assertFalse(vpaidBridge.wasTouched());
        vpaidBridge.onTouch();
        assertTrue(vpaidBridge.wasTouched());
    }
    
    @Test
    public void setCreativeDataAndLoad() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        vpaidBridge.setCreativeDataAndLoad("{data}");
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.setCreativeData({data});");
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.loadAd();");
    }
    
    @Test
    public void fireStartAdEvent() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        vpaidBridge.fireStartAdEvent();
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.fireStartAdEvent();");
    }    
    
    @Test
    public void fireAdResumeEvent() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        vpaidBridge.fireAdResumeEvent();
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.fireAdResumeEvent();");
    }    
    
    @Test
    public void fireAdPauseEvent() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        vpaidBridge.fireAdPauseEvent();
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.fireAdPauseEvent();");
    }    
    
    @Test
    public void setVolume_0() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        vpaidBridge.setVolume(0);
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.setVolume(0);");
    }  
    
    @Test
    public void setVolume_1() throws Exception {
        VpaidBridge vpaidBridge = new VpaidBridge();
        vpaidBridge.initVpaidWebView(adWebView, mock(VpaidCommandListener.class));
        vpaidBridge.setVolume(1);
        Mockito.verify(adWebView).loadUrl("javascript:vpaid.setVolume(1);");
    }
}