package com.appodeal.mraid;

import android.net.Uri;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MraidWebViewClientTest {

    @Test
    public void shouldInterceptRequest() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {

            }

            @Override
            public void onTouch() {

            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void onProcessCommand(String url) {

            }

            @Override
            public void onMraidRequested() {
                countDownLatch.countDown();
            }
        });
        MraidWebViewClient.shouldInterceptRequest(mock(WebView.class), "mraid.js");
        countDownLatch.await();
    }

    @Test
    public void onPageFinished() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {

            }

            @Override
            public void onTouch() {

            }

            @Override
            public void onPageFinished() {
                countDownLatch.countDown();
            }

            @Override
            public void onProcessCommand(String url) {

            }

            @Override
            public void onMraidRequested() {

            }
        });
        MraidWebViewClient.onPageFinished(mock(WebView.class), "url");
        countDownLatch.await();
    }

    @Test
    @SdkSuppress(minSdkVersion = 21)
    public void shouldOverrideUrlLoading_mraidCommandApi21_WithTouch() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {

            }

            @Override
            public void onTouch() {
                countDownLatch.countDown();
            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void onProcessCommand(String url) {
                assertEquals(url, "mraid://open?url=http://appodeal.com");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        MraidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), new WebResourceRequest() {
            @Override
            public Uri getUrl() {
                return Uri.parse("http://appodeal.com");
            }

            @Override
            public boolean isForMainFrame() {
                return false;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }

            @Override
            public boolean hasGesture() {
                return true;
            }

            @Override
            public String getMethod() {
                return "post";
            }

            @Override
            public Map<String, String> getRequestHeaders() {
                return null;
            }
        });
        countDownLatch.await();
    }

    @Test
    public void shouldOverrideUrlLoading_mraidCommand() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {

            }

            @Override
            public void onTouch() {
                fail();
            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void onProcessCommand(String url) {
                assertEquals(url, "mraid://resize");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        MraidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), "mraid://resize");
        countDownLatch.await();
    }

    @Test
    public void shouldOverrideUrlLoading_redirect() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {

            }

            @Override
            public void onTouch() {
                fail();
            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void onProcessCommand(String url) {
                assertEquals(url, "mraid://open?url=http://appodeal.com");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        MraidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), "http://appodeal.com");
        countDownLatch.await();
    }

    @Test
    @SdkSuppress(minSdkVersion = 21)
    public void shouldOverrideUrlLoading_mraidCommandApi21() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {

            }

            @Override
            public void onTouch() {
                fail();
            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void onProcessCommand(String url) {
                assertEquals(url, "mraid://expand");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        MraidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), new WebResourceRequest() {
            @Override
            public Uri getUrl() {
                return Uri.parse("mraid://expand");
            }

            @Override
            public boolean isForMainFrame() {
                return false;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }

            @Override
            public boolean hasGesture() {
                return false;
            }

            @Override
            public String getMethod() {
                return "post";
            }

            @Override
            public Map<String, String> getRequestHeaders() {
                return null;
            }
        });
        countDownLatch.await();
    }

    @Test
    @SdkSuppress(minSdkVersion = 26)
    public void onRenderProcessGone() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(new AdWebViewListener() {
            @Override
            public void onRenderProcessGone() {
                countDownLatch.countDown();
            }

            @Override
            public void onTouch() {

            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void onProcessCommand(String url) {

            }

            @Override
            public void onMraidRequested() {

            }
        });
        MraidWebViewClient.onRenderProcessGone(mock(WebView.class), mock(RenderProcessGoneDetail.class));
        countDownLatch.await();
    }

    @Test
    public void matchesInjectionUrl_simpleJs() throws Exception {
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(null);
        assertTrue(MraidWebViewClient.matchesInjectionUrl("mraid.js"));
    }

    @Test
    public void matchesInjectionUrl_jsWithHost() throws Exception {
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(null);
        assertTrue(MraidWebViewClient.matchesInjectionUrl("http://appodeal.com/mraid.js"));
    }

    @Test
    public void matchesInjectionUrl_localJs() throws Exception {
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(null);
        assertTrue(MraidWebViewClient.matchesInjectionUrl("file://android_asset/appodeal.com/mraid.js"));
    }

    @Test
    public void matchesInjectionUrl_jsWithParams() throws Exception {
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(null);
        assertTrue(MraidWebViewClient.matchesInjectionUrl("http://appodeal.com/mraid.js?v=3.0"));
    }

    @Test
    public void matchesInjectionUrl_invalidName() throws Exception {
        MraidWebViewClient MraidWebViewClient = new MraidWebViewClient(null);
        assertFalse(MraidWebViewClient.matchesInjectionUrl("mraidd.js"));
    }
}