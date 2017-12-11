package com.appodeal.vast;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class VastConfigLoaderTest {
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @Test
    public void withXml() throws Exception {
        String inLineString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>";

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        VastConfigLoader loader = new VastConfigLoader.Builder((float) 16/9, "dir", new VastConfigLoader.VastConfigLoaderListener() {
            @Override
            public void onComplete(VastConfig vastConfig) {
                assertNotNull(vastConfig);
                assertEquals(1, vastConfig.getImpressionTracking().size());
                countDownLatch.countDown();
            }
        }).withXml(inLineString).build();

        VastTools.safeExecute(loader);
        countDownLatch.await();
    }

    @Test
    public void withUrl() throws Exception {
        String inLineString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>";


        HttpUrl baseUrl = server.url("/");
        String xmlUrl = baseUrl.toString();

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(inLineString));

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        VastConfigLoader loader = new VastConfigLoader.Builder((float) 16/9, "dir", new VastConfigLoader.VastConfigLoaderListener() {
            @Override
            public void onComplete(VastConfig vastConfig) {
                assertNotNull(vastConfig);
                assertEquals(1, vastConfig.getImpressionTracking().size());
                countDownLatch.countDown();
            }
        }).withUrl(xmlUrl).build();

        VastTools.safeExecute(loader);
        countDownLatch.await();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}