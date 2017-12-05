package com.appodeal.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VastModel_getSkipOffset {

    @Test
    public void getSkipOffset_validValue_shouldReturnValue() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear skipOffset=\"00:00:10\">\n" +
                "<Duration>00:00:30</Duration>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(10, vastModel.getSkipOffset());
    }

    @Test
    public void getSkipOffset_withoutNode_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getSkipOffset());
    }

    @Test
    public void getSkipOffset_emptyNode_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear skipOffset=\"\">\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getSkipOffset());
    }

    @Test
    public void getSkipOffset_invalidValue_shouldReturnZero() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear skipOffset=\"Time\">\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals(0, vastModel.getSkipOffset());
    }
}
