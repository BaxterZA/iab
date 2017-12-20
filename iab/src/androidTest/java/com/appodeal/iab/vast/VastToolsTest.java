package com.appodeal.iab.vast;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class VastToolsTest {
    
    @Test
    public void getSecondsFromTimeStringTest() throws Exception {
        assertEquals(1_000, VastTools.getMillsFromTimeString("00:01"));
        assertEquals(10_000, VastTools.getMillsFromTimeString("00:10"));
        assertEquals(70_000, VastTools.getMillsFromTimeString("01:10"));
        assertEquals(670_000, VastTools.getMillsFromTimeString("11:10"));
        assertEquals(80_010, VastTools.getMillsFromTimeString("01:20.010"));

        assertEquals(1_000, VastTools.getMillsFromTimeString("00:00:01"));
        assertEquals(10_000, VastTools.getMillsFromTimeString("00:00:10"));
        assertEquals(70_000, VastTools.getMillsFromTimeString("00:01:10"));
        assertEquals(670_000, VastTools.getMillsFromTimeString("00:11:10"));
        assertEquals(7270_000, VastTools.getMillsFromTimeString("02:01:10"));
        assertEquals(3600_001, VastTools.getMillsFromTimeString("01:00:00.001"));
        assertEquals(0, VastTools.getMillsFromTimeString("10"));
        assertEquals(0, VastTools.getMillsFromTimeString("0"));
        assertEquals(0, VastTools.getMillsFromTimeString("a"));
        assertEquals(0, VastTools.getMillsFromTimeString("true"));
        assertEquals(0, VastTools.getMillsFromTimeString("null"));
    }
    
    @Test
    public void parseBooleanTest() throws Exception {
        assertTrue(VastTools.parseBoolean("TRUE"));
        assertTrue(VastTools.parseBoolean("true"));
        assertTrue(VastTools.parseBoolean("1"));
        assertFalse(VastTools.parseBoolean("FALSE"));
        assertFalse(VastTools.parseBoolean("false"));
        assertFalse(VastTools.parseBoolean("0"));
        assertFalse(VastTools.parseBoolean("aaaa"));
        assertFalse(VastTools.parseBoolean("21321312"));
        assertFalse(VastTools.parseBoolean("null"));
    }


    @Test
    public void getTimeStringFromMillsTest() throws Exception {
        assertEquals("00:00:00.000", VastTools.getTimeStringFromMills(0));
        assertEquals("00:00:00.001", VastTools.getTimeStringFromMills(1));
        assertEquals("00:00:00.010", VastTools.getTimeStringFromMills(10));
        assertEquals("00:00:00.100", VastTools.getTimeStringFromMills(100));
        assertEquals("00:00:01.010", VastTools.getTimeStringFromMills(1010));
        assertEquals("00:00:11.010", VastTools.getTimeStringFromMills(11010));
        assertEquals("00:01:11.010", VastTools.getTimeStringFromMills(71010));
        assertEquals("00:11:11.010", VastTools.getTimeStringFromMills(671010));
        assertEquals("01:11:11.010", VastTools.getTimeStringFromMills(4271010));
        assertEquals("11:11:11.010", VastTools.getTimeStringFromMills(40271010));
    }

    @Test
    public void isStaticResourceTypeSupportedTest() throws Exception {
        assertTrue(VastTools.isStaticResourceTypeSupported("image/gif"));
        assertTrue(VastTools.isStaticResourceTypeSupported("image/jpeg"));
        assertTrue(VastTools.isStaticResourceTypeSupported("image/jpg"));
        assertTrue(VastTools.isStaticResourceTypeSupported("image/bmp"));
        assertTrue(VastTools.isStaticResourceTypeSupported("image/png"));

        assertFalse(VastTools.isStaticResourceTypeSupported("image/svg+xml"));
        assertFalse(VastTools.isStaticResourceTypeSupported("image/tiff"));
        assertFalse(VastTools.isStaticResourceTypeSupported("image/webp"));
    }

    @Test
    public void getElementValue_string() throws Exception {
        String xmlString = "<Element>value</Element>";
        Document document = VastTools.getDocumentFromString(xmlString);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node node = ((NodeList) xpath.evaluate("//Element", document, XPathConstants.NODESET)).item(0);

        assertEquals("value", VastTools.getElementValue(node));
    }

    @Test
    public void getElementValue_stringWithSpaces() throws Exception {
        String xmlString = "<Element>  value  </Element>";
        Document document = VastTools.getDocumentFromString(xmlString);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node node = ((NodeList) xpath.evaluate("//Element", document, XPathConstants.NODESET)).item(0);

        assertEquals("value", VastTools.getElementValue(node));
    }

    @Test
    public void getElementValue_stringWithNewLines() throws Exception {
        String xmlString = "<Element> \n value \n </Element>";
        Document document = VastTools.getDocumentFromString(xmlString);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node node = ((NodeList) xpath.evaluate("//Element", document, XPathConstants.NODESET)).item(0);

        assertEquals("value", VastTools.getElementValue(node));
    }

    @Test
    public void getElementValue_stringWithCDATA() throws Exception {
        String xmlString = "<Element> \n " +
                "   <![CDATA[value]]>  \n" +
                "\n </Element>";
        Document document = VastTools.getDocumentFromString(xmlString);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node node = ((NodeList) xpath.evaluate("//Element", document, XPathConstants.NODESET)).item(0);

        assertEquals("value", VastTools.getElementValue(node));
    }

    @Test
    public void getElementValue_stringWithCDATAAndNewLine() throws Exception {
        String xmlString = "<Element> \n <![CDATA[value\n ]]>  \n </Element>";
        Document document = VastTools.getDocumentFromString(xmlString);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node node = ((NodeList) xpath.evaluate("//Element", document, XPathConstants.NODESET)).item(0);

        assertEquals("value", VastTools.getElementValue(node));
    }
}