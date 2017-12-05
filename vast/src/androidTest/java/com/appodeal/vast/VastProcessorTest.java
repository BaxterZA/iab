package com.appodeal.vast;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class VastProcessorTest {
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @Test
    public void loadFromXmlTest_validXml_shouldReturnModel() throws Exception {
        String inLineString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
               "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>";

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = processor.loadXml(inLineString);
        assertNotNull(vastModel);
        assertEquals(1, vastModel.getImpressions().size());
        assertTrue(vastModel.getImpressions().contains("inline_impression"));
    }

    @Test
    public void loadFromXmlTest_invalidXml_shouldReturnEmptyModel() throws Exception {
        String inLineString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                " </Ad>\n" +
                "</VAST>";

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = processor.loadXml(inLineString);
        assertNotNull(vastModel);
        assertEquals(0, vastModel.getImpressions().size());
    }

    @Test
    public void loadFromUrlTest_validXml_shouldReturnModel() throws Exception {
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

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = processor.loadUrl(xmlUrl);
        assertNotNull(vastModel);
        assertEquals(1, vastModel.getImpressions().size());
        assertTrue(vastModel.getImpressions().contains("inline_impression"));
    }

    @Test
    public void loadFromUrlTest_204Response_shouldReturnEmptyModel() throws Exception {
        HttpUrl baseUrl = server.url("/");
        String xmlUrl = baseUrl.toString();

        server.enqueue(new MockResponse()
                .setResponseCode(204));

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = processor.loadUrl(xmlUrl);
        assertNotNull(vastModel);
        assertEquals(0, vastModel.getImpressions().size());
    }
    
    @Test
    public void loadFromXmlTest_withWrapper_shouldReturnModel() throws Exception {
        HttpUrl baseUrl = server.url("/");
        String xmlUrl = baseUrl.toString();

        String wrapperString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <Wrapper>\n" +
                "   <Impression>wrapper_impression</Impression>\n" +
                "   <VASTAdTagURI>" + xmlUrl + "</VASTAdTagURI>\n" +
                "  </Wrapper>\n" +
                " </Ad>\n" +
                "</VAST>";
        String inLineString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>";

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(inLineString));

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = processor.loadXml(wrapperString);
        assertNotNull(vastModel);
        assertEquals(2, vastModel.getImpressions().size());
        assertTrue(vastModel.getImpressions().contains("inline_impression"));
        assertTrue(vastModel.getImpressions().contains("wrapper_impression"));
    }

    @Test
    public void loadFromUrlTest_withWrapperOutOfLimit_shouldReturnModelWithOnlyWrappers() throws Exception {
        HttpUrl baseUrl = server.url("/");
        String xmlUrl = baseUrl.toString();

        String wrapperString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <Wrapper>\n" +
                "   <Impression>wrapper_impression</Impression>\n" +
                "   <VASTAdTagURI>" + xmlUrl + "</VASTAdTagURI>\n" +
                "  </Wrapper>\n" +
                " </Ad>\n" +
                "</VAST>";
        String inLineString = "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>";

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(wrapperString));
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(wrapperString));
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(wrapperString));
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(wrapperString));
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(wrapperString));


        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(inLineString));

        VastProcessor processor = new VastProcessor();
        VastModel vastModel = processor.loadUrl(xmlUrl);
        assertNotNull(vastModel);
        assertEquals(5, vastModel.getImpressions().size());
        assertFalse(vastModel.getImpressions().contains("inline_impression"));
        assertTrue(vastModel.getImpressions().contains("wrapper_impression"));
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

}