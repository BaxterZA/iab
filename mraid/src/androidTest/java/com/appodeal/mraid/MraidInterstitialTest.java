package com.appodeal.mraid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class MraidInterstitialTest {
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }


    @Test
    public void controllerInit() throws Exception {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);
        assertNotNull(mraidInterstitial.getController());
        mraidInterstitial.controller = mock(MraidViewController.class);

        mraidInterstitial.setHtml("html");
        verify(mraidInterstitial.controller).setHtml("html");

        mraidInterstitial.setUrl("http://appodeal.com");
        verify(mraidInterstitial.controller).setUrl("http://appodeal.com");

        mraidInterstitial.setBaseUrl("http://localhost");
        verify(mraidInterstitial.controller).setBaseUrl("http://localhost");

        MraidEnvironment mraidEnvironment = new MraidEnvironment.Builder().build();
        mraidInterstitial.setMraidEnvironment(mraidEnvironment);
        verify(mraidInterstitial.controller).setMraidEnvironment(mraidEnvironment);

        MraidWebViewDebugListener mraidWebViewDebugListener = mock(MraidWebViewDebugListener.class);
        mraidInterstitial.setMraidWebViewDebugListener(mraidWebViewDebugListener);
        verify(mraidInterstitial.controller).setMraidWebViewDebugListener(mraidWebViewDebugListener);

        List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
        MraidNativeFeatureListener nativeFeatureListener = mock(MraidNativeFeatureListener.class);
        mraidInterstitial.setSupportedFeatures(nativeFeatureList, nativeFeatureListener);
        verify(mraidInterstitial.controller).setSupportedFeatures(nativeFeatureList, nativeFeatureListener);

        mraidInterstitial.load();
        verify(mraidInterstitial.controller).load();
    }


    @Test
    public void destroy() throws Exception {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);
        assertNotNull(mraidInterstitial.getController());
        mraidInterstitial.controller = mock(MraidViewController.class);
        MraidInterstitialListener mraidInterstitialListener = mock(MraidInterstitialListener.class);
        mraidInterstitial.setMraidInterstitialListener(mraidInterstitialListener);

        mraidInterstitial.destroy();
        assertNull(mraidInterstitial.controller);
        assertNull(mraidInterstitial.getMraidInterstitialListener());
        assertTrue(mraidInterstitial.isDestroyed());
    }

    @Test
    public void controllerEvents_shouldCallLoaded() throws Exception {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);
        assertNotNull(mraidInterstitial.getController());
        mraidInterstitial.controller = spy(mraidInterstitial.controller);
        doNothing().when(mraidInterstitial.controller).load();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidInterstitial.setMraidInterstitialListener(new MraidInterstitialListener() {
            @Override
            public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {

            }
        });
        mraidInterstitial.load();
        mraidInterstitial.getController().mraidViewControllerListener.onMraidViewControllerLoaded(mraidInterstitial.getController());
        countDownLatch.await();
    }

    @Test
    public void controllerEvents_shouldCallFailedToLoad() throws Exception {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);
        assertNotNull(mraidInterstitial.getController());
        mraidInterstitial.controller = spy(mraidInterstitial.controller);
        doNothing().when(mraidInterstitial.controller).load();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidInterstitial.setMraidInterstitialListener(new MraidInterstitialListener() {
            @Override
            public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {

            }
        });
        mraidInterstitial.load();
        mraidInterstitial.getController().mraidViewControllerListener.onMraidViewControllerFailedToLoad(mraidInterstitial.getController());
        countDownLatch.await();
        assertTrue(mraidInterstitial.isDestroyed());
    }

    @Test
    public void controllerEvents_shouldCallUnloaded() throws Exception {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);
        assertNotNull(mraidInterstitial.getController());
        mraidInterstitial.controller = spy(mraidInterstitial.controller);
        doNothing().when(mraidInterstitial.controller).load();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidInterstitial.setMraidInterstitialListener(new MraidInterstitialListener() {
            @Override
            public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {

            }
        });
        mraidInterstitial.load();
        mraidInterstitial.getController().mraidViewControllerListener.onMraidViewControllerUnloaded(mraidInterstitial.getController());
        countDownLatch.await();
        assertTrue(mraidInterstitial.isDestroyed());
    }

    @Test
    public void showBeforeLoaded_shouldCallFailedToShow() throws Exception {
        MraidInterstitial mraidInterstitial = new MraidInterstitial(context);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mraidInterstitial.setMraidInterstitialListener(new MraidInterstitialListener() {
            @Override
            public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {
                countDownLatch.countDown();
            }

            @Override
            public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {

            }

            @Override
            public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {

            }
        });
        mraidInterstitial.show();
        countDownLatch.await();
    }
}