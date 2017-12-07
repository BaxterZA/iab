package com.appodeal.mraid;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class AdWebChromeClientTest {

    @Test
    public void onConsoleMessage() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final ConsoleMessage message = new ConsoleMessage("text of the message", "source", 12, ConsoleMessage.MessageLevel.DEBUG);
        AdWebChromeClient adWebChromeClient = new AdWebChromeClient(new MraidCommandListener() {
            @Override
            public void mraidViewPageFinished() {

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
                assertEquals(message, consoleMessage);
                countDownLatch.countDown();
                return false;
            }
        });
        adWebChromeClient.onConsoleMessage(message);
        countDownLatch.await();
    }

    @Test
    public void onJsAlert() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AdWebChromeClient adWebChromeClient = new AdWebChromeClient(new MraidCommandListener() {
            @Override
            public void mraidViewPageFinished() {

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
                assertEquals(message, "alert test");
                countDownLatch.countDown();
                return false;
            }

            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                return false;
            }
        });
        adWebChromeClient.onJsAlert(mock(WebView.class), "url", "alert test", mock(JsResult.class));
        countDownLatch.await();
    }
}