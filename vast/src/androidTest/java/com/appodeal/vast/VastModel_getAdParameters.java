package com.appodeal.vast;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VastModel_getAdParameters {

    @Test
    public void getAdParameters_validValue_shouldReturnValue() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<AdParameters>adParameters</AdParameters>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals("adParameters", vastModel.getAdParameters());
    }

    @Test
    public void getAdParameters_emptyValue_shouldEmptyString() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<AdParameters></AdParameters>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals("", vastModel.getAdParameters());
    }

    @Test
    public void getAdParameters_withoutNode_shouldEmptyString() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals("", vastModel.getAdParameters());
    }

    @Test
    public void getAdParameters_wrongHierarchy_shouldEmptyString() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<AdParameters><Params>adParameters</Params></AdParameters>\n" +
                "</Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals("", vastModel.getAdParameters());
    }


    @Test
    public void getAdParameters_validValueAndCompanion_shouldReturnValueFromLinear() throws Exception {
        String nodeString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear>\n" +
                "<AdParameters>adParameters</AdParameters>\n" +
                "</Linear></Creative>" +
                "<Creative>" +
                "<CompanionAds>" +
                " <Companion width=\"300\" height=\"250\">" +
                "  <StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>" +
                "  <AdParameters>adParameters_fom_companion</AdParameters>\n" +
                " </Companion>" +
                "</CompanionAds></Creative></Creatives></InLine></Ad></VAST></VASTS>";
        Document document = VastTools.getDocumentFromString(nodeString);
        VastModel vastModel = new VastModel(document);
        assertEquals("adParameters", vastModel.getAdParameters());
    }
}
