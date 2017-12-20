package com.appodeal.iab.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class VastModel_getVideoClicks {

    @Test
    public void getVideoClicks_clickThroughAndTwoClickTrackingAndTwoCustomClick_shouldReturnValues() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<VideoClicks>" +
                "  <ClickThrough>clickThrough_url</ClickThrough>" +
                "  <ClickTracking>clickTracking_url_1</ClickTracking>" +
                "  <ClickTracking>clickTracking_url_2</ClickTracking>" +
                "  <CustomClick>customClick_url_1</CustomClick>" +
                "  <CustomClick>customClick_url_2</CustomClick>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals("clickThrough_url", videoClicks.getClickThrough());
        assertEquals(2, videoClicks.getClickTracking().size());
        assertTrue(videoClicks.getClickTracking().contains("clickTracking_url_1"));
        assertTrue(videoClicks.getClickTracking().contains("clickTracking_url_2"));
        assertEquals(2, videoClicks.getCustomClick().size());
        assertTrue(videoClicks.getCustomClick().contains("customClick_url_1"));
        assertTrue(videoClicks.getCustomClick().contains("customClick_url_2"));
    }

    @Test
    public void getVideoClicks_clickThroughAndTwoClickTrackingAndTwoCustomClickAndOneInWrapper_shouldReturnValues() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<VideoClicks>" +
                "  <ClickThrough>clickThrough_url</ClickThrough>" +
                "  <ClickTracking>clickTracking_url_1</ClickTracking>" +
                "  <ClickTracking>clickTracking_url_2</ClickTracking>" +
                "  <CustomClick>customClick_url_1</CustomClick>" +
                "  <CustomClick>customClick_url_2</CustomClick>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST>" +
                "<VAST><Ad><Wrapper><Creatives><Creative><Linear>" +
                "<VideoClicks>" +
                "  <ClickThrough>clickThrough_url_wrapper</ClickThrough>" +
                "  <ClickTracking>clickTracking_url_wrapper</ClickTracking>" +
                "  <CustomClick>customClick_url_wrapper</CustomClick>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></Wrapper></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals("clickThrough_url", videoClicks.getClickThrough());
        assertEquals(3, videoClicks.getClickTracking().size());
        assertTrue(videoClicks.getClickTracking().contains("clickTracking_url_1"));
        assertTrue(videoClicks.getClickTracking().contains("clickTracking_url_2"));
        assertTrue(videoClicks.getClickTracking().contains("clickTracking_url_wrapper"));
        assertEquals(3, videoClicks.getCustomClick().size());
        assertTrue(videoClicks.getCustomClick().contains("customClick_url_1"));
        assertTrue(videoClicks.getCustomClick().contains("customClick_url_2"));
        assertTrue(videoClicks.getCustomClick().contains("customClick_url_wrapper"));
    }

    @Test
    public void getVideoClicks_emptyValues_shouldReturnEmptyObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<VideoClicks>" +
                "  <ClickThrough></ClickThrough>" +
                "  <ClickTracking></ClickTracking>" +
                "  <CustomClick></CustomClick>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals(null, videoClicks.getClickThrough());
        assertEquals(0, videoClicks.getClickTracking().size());
        assertEquals(0, videoClicks.getCustomClick().size());
    }

    @Test
    public void getVideoClicks_emptyNode_shouldReturnEmptyObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<VideoClicks>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals(null, videoClicks.getClickThrough());
        assertEquals(0, videoClicks.getClickTracking().size());
        assertEquals(0, videoClicks.getCustomClick().size());
    }

    @Test
    public void getVideoClicks_withoutNode_shouldReturnEmptyObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals(null, videoClicks.getClickThrough());
        assertEquals(0, videoClicks.getClickTracking().size());
        assertEquals(0, videoClicks.getCustomClick().size());
    }


    @Test
    public void getVideoClicks_invalidHierarchyInClickThrough_shouldReturnValidObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<VideoClicks>" +
                "  <ClickThrough><Url>url1</Url></ClickThrough>" +
                "  <ClickTracking>clickTracking_url</ClickTracking>" +
                "  <CustomClick>customClick_url</CustomClick>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals(null, videoClicks.getClickThrough());
        assertEquals(1, videoClicks.getClickTracking().size());
        assertTrue(videoClicks.getClickTracking().contains("clickTracking_url"));
        assertEquals(1, videoClicks.getCustomClick().size());
        assertTrue(videoClicks.getCustomClick().contains("customClick_url"));
    }

    @Test
    public void getVideoClicks_invalidHierarchyInClickTracking_shouldReturnValidObject() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<VideoClicks>" +
                "  <ClickThrough>clickThrough_url</ClickThrough>" +
                "  <ClickTracking><Url>url1</Url></ClickTracking>" +
                "  <CustomClick>customClick_url</CustomClick>" +
                "</VideoClicks>" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        VideoClicks videoClicks = vastModel.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals("clickThrough_url", videoClicks.getClickThrough());
        assertEquals(0, videoClicks.getClickTracking().size());
        assertEquals(1, videoClicks.getCustomClick().size());
        assertTrue(videoClicks.getCustomClick().contains("customClick_url"));
    }

}
