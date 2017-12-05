package com.appodeal.vast;

import org.junit.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.*;

public class VastModel_IconTest {

    @Test
    public void ParsingIconsTest_shouldReturnOne() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" width=\"200\" pxratio=\"2\" xPosition=\"left\" yPosition=\"top\" duration=\"00:00:05\" offset=\"00:00:05\">\n" +
                "  <IFrameResource><![CDATA[iframe_url]]></IFrameResource>\n" +
                "  <HTMLResource><![CDATA[html_code]]></HTMLResource>\n" +
                "  <StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>\n" +
                "   <IconViewTracking >\n" +
                "       <![CDATA[IconViewTracking_1]]>" +
                "   </IconViewTracking>\n" +
                "   <IconViewTracking >\n" +
                "       <![CDATA[IconViewTracking_2]]>" +
                "   </IconViewTracking>\n" +
                "   <IconClicks >\n" +
                "       <IconClickThrough >\n" +
                "           <![CDATA[IconClickThrough]]>" +
                "       </IconClickThrough>\n" +
                "       <IconClickTracking >\n" +
                "           <![CDATA[IconClickTracking_1]]>" +
                "       </IconClickTracking>\n" +
                "       <IconClickTracking >\n" +
                "           <![CDATA[IconClickTracking_2]]>" +
                "       </IconClickTracking>\n" +
                "   </IconClicks>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertTrue(icon.isValid());

        assertEquals(50, icon.getHeight());
        assertEquals(100, icon.getWidth());
        assertEquals(2, icon.getPxRatio());
        assertEquals(0, icon.getXPosition());
        assertEquals(0, icon.getYPosition());
        assertEquals(5, icon.getDuration());
        assertEquals(5, icon.getOffset());

        assertNotNull(icon.getIFrameResource());
        assertEquals(icon.getIFrameResource().getUri(), "iframe_url");

        assertNotNull(icon.getHtmlResource());
        assertNotNull(icon.getHtmlResource().getHtml(), "html_code");

        assertNotNull(icon.getStaticResource());
        assertNotNull(icon.getStaticResource().getUri(), "static_resource_url");
        assertNotNull(icon.getStaticResource().getCreativeType(), "image/jpeg");

        IconClicks iconClicks = icon.getIconClicks();

        assertNotNull(iconClicks);

        assertEquals("IconClickThrough", iconClicks.getClickThrough());
        assertEquals(2, iconClicks.getClickTracking().size());
        assertTrue(iconClicks.getClickTracking().contains("IconClickTracking_1"));
        assertTrue(iconClicks.getClickTracking().contains("IconClickTracking_2"));
        assertEquals(2, icon.getIconViewTracking().size());
        assertTrue(icon.getIconViewTracking().contains("IconViewTracking_1"));
        assertTrue(icon.getIconViewTracking().contains("IconViewTracking_2"));
    }

    @Test
    public void ParsingIconsTestFromWrapper_shouldReturnOne() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><Wrapper><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" width=\"200\" pxratio=\"2\" xPosition=\"left\" yPosition=\"top\" duration=\"00:00:05\" offset=\"00:00:05\">\n" +
                "  <IFrameResource><![CDATA[iframe_url]]></IFrameResource>\n" +
                "  <HTMLResource><![CDATA[html_code]]></HTMLResource>\n" +
                "  <StaticResource creativeType=\"image/jpeg\"><![CDATA[static_resource_url]]></StaticResource>\n" +
                "   <IconViewTracking >\n" +
                "       <![CDATA[IconViewTracking_1]]>" +
                "   </IconViewTracking>\n" +
                "   <IconViewTracking >\n" +
                "       <![CDATA[IconViewTracking_2]]>" +
                "   </IconViewTracking>\n" +
                "   <IconClicks >\n" +
                "       <IconClickThrough >\n" +
                "           <![CDATA[IconClickThrough]]>" +
                "       </IconClickThrough>\n" +
                "       <IconClickTracking >\n" +
                "           <![CDATA[IconClickTracking_1]]>" +
                "       </IconClickTracking>\n" +
                "       <IconClickTracking >\n" +
                "           <![CDATA[IconClickTracking_2]]>" +
                "       </IconClickTracking>\n" +
                "   </IconClicks>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></Wrapper></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertTrue(icon.isValid());

        assertEquals(50, icon.getHeight());
        assertEquals(100, icon.getWidth());
        assertEquals(2, icon.getPxRatio());
        assertEquals(0, icon.getXPosition());
        assertEquals(0, icon.getYPosition());
        assertEquals(5, icon.getDuration());
        assertEquals(5, icon.getOffset());

        assertNotNull(icon.getIFrameResource());
        assertEquals(icon.getIFrameResource().getUri(), "iframe_url");

        assertNotNull(icon.getHtmlResource());
        assertNotNull(icon.getHtmlResource().getHtml(), "html_code");

        assertNotNull(icon.getStaticResource());
        assertNotNull(icon.getStaticResource().getUri(), "static_resource_url");
        assertNotNull(icon.getStaticResource().getCreativeType(), "image/jpeg");

        IconClicks iconClicks = icon.getIconClicks();

        assertNotNull(iconClicks);

        assertEquals("IconClickThrough", iconClicks.getClickThrough());
        assertEquals(2, iconClicks.getClickTracking().size());
        assertTrue(iconClicks.getClickTracking().contains("IconClickTracking_1"));
        assertTrue(iconClicks.getClickTracking().contains("IconClickTracking_2"));
        assertEquals(2, icon.getIconViewTracking().size());
        assertTrue(icon.getIconViewTracking().contains("IconViewTracking_1"));
        assertTrue(icon.getIconViewTracking().contains("IconViewTracking_2"));
    }

    @Test
    public void ParsingIconsTest_defaultValues_shouldReturnOne() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" width=\"200\" xPosition=\"right\" yPosition=\"bottom\" duration=\"00:00:05\">\n" +
                "   <StaticResource creativeType=\"image/png\">\n" +
                "       <![CDATA[url]]>" +
                "   </StaticResource>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertTrue(icon.isValid());

        assertEquals(100, icon.getHeight());
        assertEquals(200, icon.getWidth());
        assertEquals(1, icon.getPxRatio());
        assertEquals(Integer.MAX_VALUE, icon.getXPosition());
        assertEquals(Integer.MAX_VALUE, icon.getYPosition());
        assertEquals(5, icon.getDuration());
    }

    @Test
    public void ParsingIconsTest_withPosition_shouldReturnOne() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" width=\"200\" xPosition=\"10\" yPosition=\"20\" duration=\"00:00:05\">\n" +
                "   <StaticResource creativeType=\"image/png\">\n" +
                "       <![CDATA[url]]>" +
                "   </StaticResource>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertTrue(icon.isValid());

        assertEquals(100, icon.getHeight());
        assertEquals(200, icon.getWidth());
        assertEquals(1, icon.getPxRatio());
        assertEquals(10, icon.getXPosition());
        assertEquals(20, icon.getYPosition());
        assertEquals(5, icon.getDuration());
    }

    @Test
    public void ParsingIconsTest_withoutResource_shouldReturnOneInvalid() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" width=\"200\" xPosition=\"10\" yPosition=\"20\" duration=\"00:00:05\">\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";


        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertFalse(icon.isValid());
    }

    @Test
    public void ParsingIconsTest_withoutWidth_shouldReturnOneInvalid() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" xPosition=\"10\" yPosition=\"20\" duration=\"00:00:05\">\n" +
                "   <StaticResource creativeType=\"image/png\">\n" +
                "       <![CDATA[url]]>" +
                "   </StaticResource>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertFalse(icon.isValid());
    }

    @Test
    public void ParsingIconsTest_withoutHeight_shouldReturnOneInvalid() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon width=\"200\" xPosition=\"10\" yPosition=\"20\" duration=\"00:00:05\">\n" +
                "   <StaticResource creativeType=\"image/png\">\n" +
                "       <![CDATA[url]]>" +
                "   </StaticResource>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertFalse(icon.isValid());
    }

    @Test
    public void ParsingIconsTest_withoutDuration_shouldReturnOneInvalid() throws Exception {
        String xmlString = "<VASTS><VAST><Ad><InLine><Creatives><Creative><Linear><Icons>\n" +
                "<Icon height=\"100\" width=\"200\" xPosition=\"10\" yPosition=\"20\">\n" +
                "   <StaticResource creativeType=\"image/png\">\n" +
                "       <![CDATA[url]]>" +
                "   </StaticResource>\n" +
                "</Icon>\n" +
                "</Icons></Linear></Creative></Creatives></InLine></Ad></VAST></VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);
        List<Icon> icons = vastModel.getIcons();

        assertNotNull(icons);

        assertEquals(1, icons.size());

        Icon icon = icons.get(0);
        assertNotNull(icon);

        assertFalse(icon.isValid());
    }
}