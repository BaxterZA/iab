package com.appodeal.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class VastModel_getNotViewableViewableImpression {

    @Test
    public void getNotViewableViewableImpression_oneElement_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<ViewableImpression><NotViewable>url1</NotViewable></ViewableImpression>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> impressions = vastModel.getNotViewableViewableImpression();
        assertNotNull(impressions);
        assertEquals(1, impressions.size());
        assertEquals("url1", impressions.get(0));
    }

    @Test
    public void getNotViewableViewableImpression_emptyElement_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<ViewableImpression><NotViewable></NotViewable></ViewableImpression>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> impressions = vastModel.getNotViewableViewableImpression();
        assertNotNull(impressions);
        assertEquals(0, impressions.size());
    }

    @Test
    public void getNotViewableViewableImpression_invalidHierarchy_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<ViewableImpression><Viewable<Url>url</Url></NotViewable></ViewableImpression>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> impressions = vastModel.getNotViewableViewableImpression();
        assertNotNull(impressions);
        assertEquals(0, impressions.size());
    }

    @Test
    public void getNotViewableViewableImpression_twoElements_shouldReturnTwoElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<ViewableImpression><NotViewable>url1</NotViewable></ViewableImpression>\n" +
                "<ViewableImpression><NotViewable>url2</NotViewable></ViewableImpression>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> impressions = vastModel.getNotViewableViewableImpression();
        assertNotNull(impressions);
        assertEquals(2, impressions.size());
        assertTrue(impressions.contains("url1"));
        assertTrue(impressions.contains("url2"));
    }

    @Test
    public void getNotViewableViewableImpression_twoElementsWithOneInWrapper_shouldReturnThreeElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<ViewableImpression><NotViewable>url1</NotViewable></ViewableImpression>\n" +
                "<ViewableImpression><NotViewable>url2</NotViewable></ViewableImpression>\n" +
                "</InLine></Ad></VAST>" +
                "<VAST><Ad><Wrapper>" +
                "<ViewableImpression><NotViewable>wrapper_url</NotViewable></ViewableImpression>\n" +
                "</Wrapper></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> impressions = vastModel.getNotViewableViewableImpression();
        assertNotNull(impressions);
        assertEquals(3, impressions.size());
        assertTrue(impressions.contains("url1"));
        assertTrue(impressions.contains("url2"));
        assertTrue(impressions.contains("wrapper_url"));
    }
}
