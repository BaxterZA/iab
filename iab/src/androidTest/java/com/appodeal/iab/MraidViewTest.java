package com.appodeal.iab;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.appodeal.iab.mraid.MraidViewController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class MraidViewTest {
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void controllerInit() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);
        mraidView.controller = mock(MraidViewController.class);

        mraidView.setHtml("html");
        verify(mraidView.controller).setHtml("html");

        mraidView.setUrl("http://appodeal.com");
        verify(mraidView.controller).setUrl("http://appodeal.com");

        mraidView.setBaseUrl("http://localhost");
        verify(mraidView.controller).setBaseUrl("http://localhost");

        MraidEnvironment mraidEnvironment = new MraidEnvironment.Builder().build();
        mraidView.setMraidEnvironment(mraidEnvironment);
        verify(mraidView.controller).setMraidEnvironment(mraidEnvironment);

        WebViewDebugListener webViewDebugListener = mock(WebViewDebugListener.class);
        mraidView.setAdWebViewDebugListener(webViewDebugListener);
        verify(mraidView.controller).setAdWebViewDebugListener(webViewDebugListener);

        List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
        MraidNativeFeatureListener nativeFeatureListener = mock(MraidNativeFeatureListener.class);
        mraidView.setSupportedFeatures(nativeFeatureList, nativeFeatureListener);
        verify(mraidView.controller).setSupportedFeatures(nativeFeatureList, nativeFeatureListener);

        mraidView.load();
        verify(mraidView.controller).load();
    }

    @Test
    public void destroy() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);
        MraidViewListener mraidViewListener = mock(MraidViewListener.class);
        mraidView.setMraidViewListener(mraidViewListener);

        mraidView.destroy();
        assertNull(mraidView.controller);
        assertNull(mraidView.getMraidViewListener());
    }

    @Test
    public void controllerEvents_shouldCallLoaded() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {

            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        });
        mraidView.onMraidViewControllerLoaded(mraidView.controller);
        countDownLatch.await();
    }

    @Test
    public void controllerEvents_shouldCallFailedToLoad() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        });
        mraidView.onMraidViewControllerFailedToLoad(mraidView.controller);
        countDownLatch.await();
        assertNull(mraidView.controller);
    }

    @Test
    public void controllerEvents_shouldCallUnloaded() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        });
        mraidView.onMraidViewControllerUnloaded(mraidView.controller);
        countDownLatch.await();
        assertNull(mraidView.controller);
    }

    @Test
    public void controllerEvents_shouldCallExpanded() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        });
        mraidView.onMraidViewControllerExpanded(mraidView.controller);
        countDownLatch.await();
    }

    @Test
    public void controllerEvents_shouldCallResized() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        });
        mraidView.onMraidViewControllerResized(mraidView.controller);
        countDownLatch.await();
    }

    @Test
    public void controllerEvents_shouldCallClicked() throws Exception {
        MraidView mraidView = new MraidView(context);
        assertNotNull(mraidView.controller);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {

            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {

            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {

            }
        });
        mraidView.onMraidViewControllerClicked(mraidView.controller);
        countDownLatch.await();
    }
}