package com.appodeal.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VastModel_getDuration {

    @Test
    public void getDuration_validValue_shouldReturnValue() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration>00:00:30</Duration>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(30, vastModel.getDuration());
    }

    @Test
    public void getDuration_withoutNode_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getDuration());
    }

    @Test
    public void getDuration_emptyNode_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration></Duration>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getDuration());
    }

    @Test
    public void getDuration_invalidValue_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration>Time</Duration>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getDuration());
    }

    @Test
    public void getDuration_invalidHierarchy_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<Duration><Time>00:00:15</Time></Duration>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getDuration());
    }
}
