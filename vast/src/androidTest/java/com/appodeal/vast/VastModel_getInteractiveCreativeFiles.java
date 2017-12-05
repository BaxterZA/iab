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
public class VastModel_getInteractiveCreativeFiles {

    @Test
    public void getInteractiveCreativeFiles_oneElement_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<InteractiveCreativeFile type=\"application/javascript\" apiFramework=\"VPAID\">url</InteractiveCreativeFile>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getInteractiveCreativeFiles();
        assertNotNull(mediaFiles);
        assertEquals(1, mediaFiles.size());
        assertEquals("url", mediaFiles.get(0).getUrl());
        assertEquals("application/javascript", mediaFiles.get(0).getType());
        assertEquals("VPAID", mediaFiles.get(0).getApiFramework());
    }

    @Test
    public void getInteractiveCreativeFiles_wrongHierarchy_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<InteractiveCreativeFile type=\"application/javascript\" apiFramework=\"VPAID\"><Url>url</Url></InteractiveCreativeFile>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getInteractiveCreativeFiles();
        assertNotNull(mediaFiles);
        assertEquals(0, mediaFiles.size());
    }

    @Test
    public void getInteractiveCreativeFiles_emptyElement_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<InteractiveCreativeFile type=\"application/javascript\" apiFramework=\"VPAID\"></InteractiveCreativeFile>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getInteractiveCreativeFiles();
        assertNotNull(mediaFiles);
        assertEquals(0, mediaFiles.size());
    }

    @Test
    public void getInteractiveCreativeFiles_withoutNode_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getInteractiveCreativeFiles();
        assertNotNull(mediaFiles);
        assertEquals(0, mediaFiles.size());
    }
}
