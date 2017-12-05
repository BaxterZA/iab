package com.appodeal.vast;

import org.junit.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VastModel_CompanionTest {

    @Test
    public void getCompanionsTest_fillAllFields_shouldCreateObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><CompanionAds><Companion width=\"300\" height=\"250\">\n" +
                "\t<IFrameResource><![CDATA[iframe_url]]></IFrameResource>\n" +
                "\t<HTMLResource><![CDATA[html_code]]></HTMLResource>\n" +
                "\t<StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>\n" +
                "\t<TrackingEvents>\n" +
                "\t\t<Tracking event=\"creativeView\">\n" +
                "\t\t  <![CDATA[creative_view_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t\t<Tracking event=\"close\">\n" +
                "\t\t  <![CDATA[close_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t</TrackingEvents>\n" +
                "\t<CompanionClickThrough>click_through_url</CompanionClickThrough>\n" +
                "\t<CompanionClickTracking>click_tracking_1</CompanionClickTracking>\n" +
                "\t<CompanionClickTracking>click_tracking_2</CompanionClickTracking>\n" +
                "\t<AdParameters>adParameters</AdParameters>\n" +
                "</Companion></CompanionAds></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<Companion> companions = vastModel.getCompanions();

        assertEquals(1, companions.size());

        Companion companion = companions.get(0);

        assertNotNull(companion);

        assertEquals(companion.getWidth(), 300);
        assertEquals(companion.getHeight(), 250);

        assertNotNull(companion.getIFrameResource());
        assertEquals(companion.getIFrameResource().getUri(), "iframe_url");

        assertNotNull(companion.getHtmlResource());
        assertNotNull(companion.getHtmlResource().getHtml(), "html_code");

        assertNotNull(companion.getStaticResource());
        assertNotNull(companion.getStaticResource().getUri(), "static_resource_url");
        assertNotNull(companion.getStaticResource().getCreativeType(), "image/jpeg");

        assertEquals(companion.getTracking().size(), 2);
        assertEquals(companion.getTracking().get(TrackingEventsType.creativeView).get(0), "creative_view_url");
        assertEquals(companion.getTracking().get(TrackingEventsType.close).get(0), "close_url");

        assertEquals(companion.getClickThrough(), "click_through_url");

        assertEquals(companion.getClickTracking().size(), 2);
        assertTrue(companion.getClickTracking().contains("click_tracking_1"));
        assertTrue(companion.getClickTracking().contains("click_tracking_2"));

        assertEquals(companion.getAdParameters(), "adParameters");
    }

    @Test
    public void getCompanionsTest_fillAllFieldsInWrapper_shouldCreateObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><Wrapper><Creatives><Creative><CompanionAds><Companion width=\"300\" height=\"250\">\n" +
                "\t<IFrameResource><![CDATA[iframe_url]]></IFrameResource>\n" +
                "\t<HTMLResource><![CDATA[html_code]]></HTMLResource>\n" +
                "\t<StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>\n" +
                "\t<TrackingEvents>\n" +
                "\t\t<Tracking event=\"creativeView\">\n" +
                "\t\t  <![CDATA[creative_view_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t\t<Tracking event=\"close\">\n" +
                "\t\t  <![CDATA[close_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t</TrackingEvents>\n" +
                "\t<CompanionClickThrough>click_through_url</CompanionClickThrough>\n" +
                "\t<CompanionClickTracking>click_tracking_1</CompanionClickTracking>\n" +
                "\t<CompanionClickTracking>click_tracking_2</CompanionClickTracking>\n" +
                "\t<AdParameters>adParameters</AdParameters>\n" +
                "</Companion></CompanionAds></Creative></Creatives></Wrapper></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<Companion> companions = vastModel.getCompanions();

        assertEquals(1, companions.size());

        Companion companion = companions.get(0);

        assertNotNull(companion);

        assertEquals(companion.getWidth(), 300);
        assertEquals(companion.getHeight(), 250);

        assertNotNull(companion.getIFrameResource());
        assertEquals(companion.getIFrameResource().getUri(), "iframe_url");

        assertNotNull(companion.getHtmlResource());
        assertNotNull(companion.getHtmlResource().getHtml(), "html_code");

        assertNotNull(companion.getStaticResource());
        assertNotNull(companion.getStaticResource().getUri(), "static_resource_url");
        assertNotNull(companion.getStaticResource().getCreativeType(), "image/jpeg");

        assertEquals(companion.getTracking().size(), 2);
        assertEquals(companion.getTracking().get(TrackingEventsType.creativeView).get(0), "creative_view_url");
        assertEquals(companion.getTracking().get(TrackingEventsType.close).get(0), "close_url");

        assertEquals(companion.getClickThrough(), "click_through_url");

        assertEquals(companion.getClickTracking().size(), 2);
        assertTrue(companion.getClickTracking().contains("click_tracking_1"));
        assertTrue(companion.getClickTracking().contains("click_tracking_2"));

        assertEquals(companion.getAdParameters(), "adParameters");
    }

    @Test
    public void getCompanionsTest_fillAllFieldsInWrapperAndInInline_shouldCreateTwoObjects() throws Exception {
        String nodeString = "<VASTS>" +
                "<VAST><Ad><Wrapper><Creatives><Creative><CompanionAds><Companion width=\"300\" height=\"250\">\n" +
                "\t<IFrameResource><![CDATA[iframe_url]]></IFrameResource>\n" +
                "\t<HTMLResource><![CDATA[html_code]]></HTMLResource>\n" +
                "\t<StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>\n" +
                "\t<TrackingEvents>\n" +
                "\t\t<Tracking event=\"creativeView\">\n" +
                "\t\t  <![CDATA[creative_view_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t\t<Tracking event=\"close\">\n" +
                "\t\t  <![CDATA[close_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t</TrackingEvents>\n" +
                "\t<CompanionClickThrough>click_through_url</CompanionClickThrough>\n" +
                "\t<CompanionClickTracking>click_tracking_1</CompanionClickTracking>\n" +
                "\t<CompanionClickTracking>click_tracking_2</CompanionClickTracking>\n" +
                "\t<AdParameters>adParameters</AdParameters>\n" +
                "</Companion></CompanionAds></Creative></Creatives></Wrapper></Ad></VAST>" +
                "<VAST><Ad><InLine><Creatives><Creative><CompanionAds><Companion width=\"300\" height=\"250\">\n" +
                "\t<IFrameResource><![CDATA[iframe_url]]></IFrameResource>\n" +
                "\t<HTMLResource><![CDATA[html_code]]></HTMLResource>\n" +
                "\t<StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>\n" +
                "\t<TrackingEvents>\n" +
                "\t\t<Tracking event=\"creativeView\">\n" +
                "\t\t  <![CDATA[creative_view_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t\t<Tracking event=\"close\">\n" +
                "\t\t  <![CDATA[close_url]]>\n" +
                "\t\t</Tracking>\n" +
                "\t</TrackingEvents>\n" +
                "\t<CompanionClickThrough>click_through_url</CompanionClickThrough>\n" +
                "\t<CompanionClickTracking>click_tracking_1</CompanionClickTracking>\n" +
                "\t<CompanionClickTracking>click_tracking_2</CompanionClickTracking>\n" +
                "\t<AdParameters>adParameters</AdParameters>\n" +
                "</Companion></CompanionAds></Creative></Creatives></InLine></Ad></VAST>" +
                "</VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<Companion> companions = vastModel.getCompanions();

        assertEquals(2, companions.size());
    }
}