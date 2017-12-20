package com.appodeal.iab.vast;


import org.junit.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VastModel_ProgressEvent {

    @Test
    public void getProgressTracking_withTimeAndDuration_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration>00:00:30</Duration>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"00:00:10\">url</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(1, progressEvents.size());
        assertEquals("url", progressEvents.get(0).getTrackingURL());
        assertEquals(10_000, progressEvents.get(0).getOffsetTime());
    }

    @Test
    public void getProgressTracking_withTimeAndWithoutDuration_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"00:00:15\">url</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(1, progressEvents.size());
        assertEquals("url", progressEvents.get(0).getTrackingURL());
        assertEquals(15_000, progressEvents.get(0).getOffsetTime());
    }

    @Test
    public void getProgressTracking_twoElements_shouldReturnTwoElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"00:00:10\">url</Tracking>\n" +
                "  <Tracking event=\"progress\" offset=\"00:00:15\">url2</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(2, progressEvents.size());
        assertEquals("url", progressEvents.get(0).getTrackingURL());
        assertEquals(10_000, progressEvents.get(0).getOffsetTime());
        assertEquals("url2", progressEvents.get(1).getTrackingURL());
        assertEquals(15_000, progressEvents.get(1).getOffsetTime());
    }

    @Test
    public void getProgressTracking_withPercentAndDuration_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration>00:00:30</Duration>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"50%\">url</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(1, progressEvents.size());
        assertEquals("url", progressEvents.get(0).getTrackingURL());
        assertEquals(15_000, progressEvents.get(0).getOffsetTime());
    }

    @Test
    public void getProgressTracking_twoElementWithPercentAndDuration_shouldReturnTwoElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration>00:00:30</Duration>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"50%\">url</Tracking>\n" +
                "  <Tracking event=\"progress\" offset=\"10%\">url2</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(2, progressEvents.size());
        assertEquals("url", progressEvents.get(0).getTrackingURL());
        assertEquals(15_000, progressEvents.get(0).getOffsetTime());
        assertEquals("url2", progressEvents.get(1).getTrackingURL());
        assertEquals(3_000, progressEvents.get(1).getOffsetTime());
    }

    @Test
    public void getProgressTracking_withPercentAndWithoutDuration_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"50%\">url</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(0, progressEvents.size());
    }


    @Test
    public void getProgressTracking_inlineAndWrapper_shouldReturnTwoElements() throws Exception {
        String nodeString = "<VASTS>" +
                "<VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration>00:00:30</Duration>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"50%\">url</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST>" +
                "<VAST><Ad><Wrapper><Creatives><Creative><Linear>\n" +
                "<TrackingEvents>\n" +
                "  <Tracking event=\"progress\" offset=\"00:00:10\">url2</Tracking>\n" +
                "</TrackingEvents>\n" +
                "</Linear></Creative></Creatives></Wrapper></Ad></VAST>" +
                "</VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<ProgressEvent> progressEvents = vastModel.getProgressTracking();
        assertNotNull(progressEvents);
        assertEquals(2, progressEvents.size());
        assertEquals("url", progressEvents.get(0).getTrackingURL());
        assertEquals(15_000, progressEvents.get(0).getOffsetTime());
        assertEquals("url2", progressEvents.get(1).getTrackingURL());
        assertEquals(10_000, progressEvents.get(1).getOffsetTime());
    }
}
