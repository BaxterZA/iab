package com.appodeal.iab.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class VastModel_getTrackingUrls {


    @Test
    public void getTrackingUrls_allTrackings_shouldReturnTracking() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><TrackingEvents>\n" +
                "<Tracking event=\"complete\"></Tracking>" +
                "</TrackingEvents></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        Map<TrackingEventsType, List<String>> trackingEvents = vastModel.getTrackingUrls();
        assertNotNull(trackingEvents);
        assertEquals(0, trackingEvents.size());
    }

    @Test
    public void getTrackingUrls_withoutNode_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><TrackingEvents>\n" +
                "</TrackingEvents></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        Map<TrackingEventsType, List<String>> trackingEvents = vastModel.getTrackingUrls();
        assertNotNull(trackingEvents);
        assertEquals(0, trackingEvents.size());
    }

    @Test
    public void getTrackingUrls_validValue_shouldReturnList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><TrackingEvents>\n" +
                "<Tracking event=\"mute\">mute</Tracking>" +
                "<Tracking event=\"unmute\">unmute</Tracking>" +
                "<Tracking event=\"pause\">pause</Tracking>" +
                "<Tracking event=\"resume\">resume</Tracking>" +
                "<Tracking event=\"rewind\">rewind</Tracking>" +
                "<Tracking event=\"skip\">skip</Tracking>" +
                "<Tracking event=\"start\">start</Tracking>" +
                "<Tracking event=\"firstQuartile\">firstQuartile</Tracking>" +
                "<Tracking event=\"midpoint\">midpoint</Tracking>" +
                "<Tracking event=\"thirdQuartile\">thirdQuartile</Tracking>" +
                "<Tracking event=\"complete\">complete</Tracking>" +
                "</TrackingEvents></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        Map<TrackingEventsType, List<String>> trackingEvents = vastModel.getTrackingUrls();
        assertNotNull(trackingEvents);
        assertEquals(11, trackingEvents.size());

        assertEquals(1, trackingEvents.get(TrackingEventsType.mute).size());
        assertTrue(trackingEvents.get(TrackingEventsType.mute).contains("mute"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.unmute).size());
        assertTrue(trackingEvents.get(TrackingEventsType.unmute).contains("unmute"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.pause).size());
        assertTrue(trackingEvents.get(TrackingEventsType.pause).contains("pause"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.resume).size());
        assertTrue(trackingEvents.get(TrackingEventsType.resume).contains("resume"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.rewind).size());
        assertTrue(trackingEvents.get(TrackingEventsType.rewind).contains("rewind"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.skip).size());
        assertTrue(trackingEvents.get(TrackingEventsType.skip).contains("skip"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.start).size());
        assertTrue(trackingEvents.get(TrackingEventsType.start).contains("start"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.firstQuartile).size());
        assertTrue(trackingEvents.get(TrackingEventsType.firstQuartile).contains("firstQuartile"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.midpoint).size());
        assertTrue(trackingEvents.get(TrackingEventsType.midpoint).contains("midpoint"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.thirdQuartile).size());
        assertTrue(trackingEvents.get(TrackingEventsType.thirdQuartile).contains("thirdQuartile"));
        assertEquals(1, trackingEvents.get(TrackingEventsType.complete).size());
        assertTrue(trackingEvents.get(TrackingEventsType.complete).contains("complete"));
    }

    @Test
    public void getTrackingUrls_inInlineAndWrapper_shouldReturnList() throws Exception {
        String nodeString = "<VASTS>" +
                "<VAST><Ad><InLine><Creatives><Creative><Linear><TrackingEvents>\n" +
                "<Tracking event=\"mute\">mute</Tracking>" +
                "<Tracking event=\"unmute\">unmute</Tracking>" +
                "<Tracking event=\"pause\">pause</Tracking>" +
                "<Tracking event=\"resume\">resume</Tracking>" +
                "<Tracking event=\"rewind\">rewind</Tracking>" +
                "<Tracking event=\"skip\">skip</Tracking>" +
                "<Tracking event=\"start\">start</Tracking>" +
                "<Tracking event=\"firstQuartile\">firstQuartile</Tracking>" +
                "<Tracking event=\"midpoint\">midpoint</Tracking>" +
                "<Tracking event=\"thirdQuartile\">thirdQuartile</Tracking>" +
                "<Tracking event=\"complete\">complete</Tracking>" +
                "</TrackingEvents></Linear></Creative></Creatives></InLine></Ad></VAST>" +
                "<VAST><Ad><Wrapper><Creatives><Creative><Linear><TrackingEvents>\n" +
                "<Tracking event=\"mute\">mute_wrapper</Tracking>" +
                "<Tracking event=\"unmute\">unmute_wrapper</Tracking>" +
                "<Tracking event=\"pause\">pause_wrapper</Tracking>" +
                "<Tracking event=\"resume\">resume_wrapper</Tracking>" +
                "<Tracking event=\"rewind\">rewind_wrapper</Tracking>" +
                "<Tracking event=\"skip\">skip_wrapper</Tracking>" +
                "<Tracking event=\"start\">start_wrapper</Tracking>" +
                "<Tracking event=\"firstQuartile\">firstQuartile_wrapper</Tracking>" +
                "<Tracking event=\"midpoint\">midpoint_wrapper</Tracking>" +
                "<Tracking event=\"thirdQuartile\">thirdQuartile_wrapper</Tracking>" +
                "<Tracking event=\"complete\">complete_wrapper</Tracking>" +
                "</TrackingEvents></Linear></Creative></Creatives></Wrapper></Ad></VAST>" +
                "</VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        Map<TrackingEventsType, List<String>> trackingEvents = vastModel.getTrackingUrls();
        assertNotNull(trackingEvents);
        assertEquals(11, trackingEvents.size());

        assertEquals(2, trackingEvents.get(TrackingEventsType.mute).size());
        assertTrue(trackingEvents.get(TrackingEventsType.mute).contains("mute"));
        assertTrue(trackingEvents.get(TrackingEventsType.mute).contains("mute_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.unmute).size());
        assertTrue(trackingEvents.get(TrackingEventsType.unmute).contains("unmute"));
        assertTrue(trackingEvents.get(TrackingEventsType.unmute).contains("unmute_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.pause).size());
        assertTrue(trackingEvents.get(TrackingEventsType.pause).contains("pause"));
        assertTrue(trackingEvents.get(TrackingEventsType.pause).contains("pause_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.resume).size());
        assertTrue(trackingEvents.get(TrackingEventsType.resume).contains("resume"));
        assertTrue(trackingEvents.get(TrackingEventsType.resume).contains("resume_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.rewind).size());
        assertTrue(trackingEvents.get(TrackingEventsType.rewind).contains("rewind"));
        assertTrue(trackingEvents.get(TrackingEventsType.rewind).contains("rewind_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.skip).size());
        assertTrue(trackingEvents.get(TrackingEventsType.skip).contains("skip"));
        assertTrue(trackingEvents.get(TrackingEventsType.skip).contains("skip_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.start).size());
        assertTrue(trackingEvents.get(TrackingEventsType.start).contains("start"));
        assertTrue(trackingEvents.get(TrackingEventsType.start).contains("start_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.firstQuartile).size());
        assertTrue(trackingEvents.get(TrackingEventsType.firstQuartile).contains("firstQuartile"));
        assertTrue(trackingEvents.get(TrackingEventsType.firstQuartile).contains("firstQuartile_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.midpoint).size());
        assertTrue(trackingEvents.get(TrackingEventsType.midpoint).contains("midpoint"));
        assertTrue(trackingEvents.get(TrackingEventsType.midpoint).contains("midpoint_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.thirdQuartile).size());
        assertTrue(trackingEvents.get(TrackingEventsType.thirdQuartile).contains("thirdQuartile"));
        assertTrue(trackingEvents.get(TrackingEventsType.thirdQuartile).contains("thirdQuartile_wrapper"));
        assertEquals(2, trackingEvents.get(TrackingEventsType.complete).size());
        assertTrue(trackingEvents.get(TrackingEventsType.complete).contains("complete"));
        assertTrue(trackingEvents.get(TrackingEventsType.complete).contains("complete_wrapper"));
    }

}
