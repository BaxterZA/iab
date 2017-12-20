package com.appodeal.iab.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class VastModel_getErrorUrls {

    @Test
    public void getErrorUrls_oneElement_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<Error>url1</Error>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> errors = vastModel.getErrorUrls();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("url1", errors.get(0));
    }

    @Test
    public void getErrorUrls_emptyElement_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<Error></Error>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> errors = vastModel.getErrorUrls();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void getErrorUrls_invalidHierarchy_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<Error><Url>url</Url></Error>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> errors = vastModel.getErrorUrls();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void getErrorUrls_twoElements_shouldReturnTwoElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<Error>url1</Error>\n" +
                "<Error>url2</Error>\n" +
                "</InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> errors = vastModel.getErrorUrls();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.contains("url1"));
        assertTrue(errors.contains("url2"));
    }

    @Test
    public void getErrorUrls_twoElementsWithOneInWrapper_shouldReturnThreeElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine>\n" +
                "<Error>url1</Error>\n" +
                "<Error>url2</Error>\n" +
                "</InLine></Ad></VAST>" +
                "<VAST><Ad><Wrapper>" +
                "<Error>wrapper_url</Error>\n" +
                "</Wrapper></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<String> errors = vastModel.getErrorUrls();
        assertNotNull(errors);
        assertEquals(3, errors.size());
        assertTrue(errors.contains("url1"));
        assertTrue(errors.contains("url2"));
        assertTrue(errors.contains("wrapper_url"));
    }
}
