package com.appodeal.iab.vpaid;

import android.net.Uri;
import android.support.test.filters.SdkSuppress;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.appodeal.iab.webview.AdWebViewListener;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class VpaidWebViewClientTest {


    @Test
    @SdkSuppress(minSdkVersion = 21)
    public void shouldOverrideUrlLoading_vpaidCommandApi21_WithTouch() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        VpaidWebViewClient vpaidWebViewClient = new VpaidWebViewClient(new AdWebViewListener() {
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
                assertEquals(url, "vpaid://AdClickThru?url=http://appodeal.com");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        vpaidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), new WebResourceRequest() {
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
    public void shouldOverrideUrlLoading_vpaidCommand() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        VpaidWebViewClient vpaidWebViewClient = new VpaidWebViewClient(new AdWebViewListener() {
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
                assertEquals(url, "vpaid://AdStarted");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        vpaidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), "vpaid://AdStarted");
        countDownLatch.await();
    }

    @Test
    public void shouldOverrideUrlLoading_redirect() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        VpaidWebViewClient vpaidWebViewClient = new VpaidWebViewClient(new AdWebViewListener() {
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
                assertEquals(url, "vpaid://AdClickThru?url=http://appodeal.com");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        vpaidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), "http://appodeal.com");
        countDownLatch.await();
    }

    @Test
    @SdkSuppress(minSdkVersion = 21)
    public void shouldOverrideUrlLoading_vpaidCommandApi21() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        VpaidWebViewClient vpaidWebViewClient = new VpaidWebViewClient(new AdWebViewListener() {
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
                assertEquals(url, "vpaid://AdPaused");
                countDownLatch.countDown();
            }

            @Override
            public void onMraidRequested() {

            }
        });
        vpaidWebViewClient.shouldOverrideUrlLoading(mock(WebView.class), new WebResourceRequest() {
            @Override
            public Uri getUrl() {
                return Uri.parse("vpaid://AdPaused");
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
        VpaidWebViewClient vpaidWebViewClient = new VpaidWebViewClient(new AdWebViewListener() {
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
        vpaidWebViewClient.onRenderProcessGone(mock(WebView.class), mock(RenderProcessGoneDetail.class));
        countDownLatch.await();
    }


}