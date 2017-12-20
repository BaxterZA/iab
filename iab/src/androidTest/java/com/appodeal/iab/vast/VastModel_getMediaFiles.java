package com.appodeal.iab.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class VastModel_getMediaFiles {

    @Test
    public void getMediaFiles_oneElement_shouldReturnOneElement() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><MediaFiles>\n" +
                "<MediaFile delivery=\"progressive\" type=\"application/javascript\" apiFramework=\"VPAID\" width=\"480\" height=\"320\">url</MediaFile>\n" +
                "</MediaFiles></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getMediaFiles();
        assertNotNull(mediaFiles);
        assertEquals(1, mediaFiles.size());
        assertEquals("url", mediaFiles.get(0).getUrl());
        assertEquals("application/javascript", mediaFiles.get(0).getType());
        assertEquals("VPAID", mediaFiles.get(0).getApiFramework());
        assertEquals(Integer.valueOf(480), mediaFiles.get(0).getWidth());
        assertEquals(Integer.valueOf(320), mediaFiles.get(0).getHeight());
        assertEquals("progressive", mediaFiles.get(0).getDelivery());
    }

    @Test
    public void getMediaFiles_twoElements_shouldReturnTwoElements() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><MediaFiles>\n" +
                "<MediaFile delivery=\"progressive\" type=\"application/javascript\" apiFramework=\"VPAID\" width=\"480\" height=\"320\">url</MediaFile>\n" +
                "<MediaFile delivery=\"streaming\" type=\"video/mp4\" width=\"1920\" height=\"1080\">url2</MediaFile>\n" +
                "</MediaFiles></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getMediaFiles();
        assertNotNull(mediaFiles);
        assertEquals(2, mediaFiles.size());
        assertEquals("url", mediaFiles.get(0).getUrl());
        assertEquals("application/javascript", mediaFiles.get(0).getType());
        assertEquals("VPAID", mediaFiles.get(0).getApiFramework());
        assertEquals(Integer.valueOf(480), mediaFiles.get(0).getWidth());
        assertEquals(Integer.valueOf(320), mediaFiles.get(0).getHeight());
        assertEquals("progressive", mediaFiles.get(0).getDelivery());

        assertEquals("url2", mediaFiles.get(1).getUrl());
        assertEquals("video/mp4", mediaFiles.get(1).getType());
        assertEquals(null, mediaFiles.get(1).getApiFramework());
        assertEquals(Integer.valueOf(1920), mediaFiles.get(1).getWidth());
        assertEquals(Integer.valueOf(1080), mediaFiles.get(1).getHeight());
        assertEquals("streaming", mediaFiles.get(1).getDelivery());
    }

    @Test
    public void getMediaFiles_emptyNode_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><MediaFiles>\n" +
                "<MediaFile delivery=\"progressive\" type=\"application/javascript\" apiFramework=\"VPAID\" width=\"480\" height=\"320\"></MediaFile>\n" +
                "</MediaFiles></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getMediaFiles();
        assertNotNull(mediaFiles);
        assertEquals(0, mediaFiles.size());
    }

    @Test
    public void getMediaFiles_withoutNode_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><MediaFiles>\n" +
                "</MediaFiles></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getMediaFiles();
        assertNotNull(mediaFiles);
        assertEquals(0, mediaFiles.size());
    }

    @Test
    public void getMediaFiles_wrongHierarchy_shouldReturnEmptyList() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><MediaFiles>\n" +
                "<MediaFile delivery=\"progressive\" type=\"application/javascript\" apiFramework=\"VPAID\" width=\"480\" height=\"320\"><Url>url</Url></MediaFile>\n" +
                "</MediaFiles></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        List<MediaFile> mediaFiles = vastModel.getMediaFiles();
        assertNotNull(mediaFiles);
        assertEquals(0, mediaFiles.size());
    }
}
