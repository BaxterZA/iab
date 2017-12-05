package com.appodeal.mraid;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MraidCommandProcessorTest {
    private MraidCommandProcessor mraidCommandProcessor = new MraidCommandProcessor();

    @Test
    public void parseBoolean() throws Exception {
        assertTrue(mraidCommandProcessor.parseBoolean("true"));
        assertFalse(mraidCommandProcessor.parseBoolean("false"));
        assertFalse(mraidCommandProcessor.parseBoolean(null, false));
    }

    @Test(expected=MraidError.class)
    public void parseBoolean_invalid() throws Exception {
        assertTrue(mraidCommandProcessor.parseBoolean("String"));
    }

    @Test
    public void parseOrientation() throws Exception {
        assertEquals(mraidCommandProcessor.parseOrientation("portrait"), MraidOrientation.PORTRAIT);
        assertEquals(mraidCommandProcessor.parseOrientation("landscape"), MraidOrientation.LANDSCAPE);
        assertEquals(mraidCommandProcessor.parseOrientation("none"), MraidOrientation.NONE);
    }

    @Test(expected=MraidError.class)
    public void parseOrientation_invalid() throws Exception {
        mraidCommandProcessor.parseOrientation("unknown");
    }

    @Test
    public void parseURL() throws Exception {
        assertEquals("http://appodeal.com", mraidCommandProcessor.parseURL("http://appodeal.com"));
        assertEquals("file://storage/1.txt", mraidCommandProcessor.parseURL("file://storage/1.txt"));
        assertEquals("market://com.appodeal.android", mraidCommandProcessor.parseURL("market://com.appodeal.android"));
    }

    @Test(expected=MraidError.class)
    public void parseURL_invalid() throws Exception {
        mraidCommandProcessor.parseURL(null);
    }

    @Test
    public void parseNumber() throws Exception {
        assertEquals(mraidCommandProcessor.parseNumber("100"), 100);
        assertEquals(mraidCommandProcessor.parseNumber("0"), 0);
    }

    @Test(expected=MraidError.class)
    public void parseNumber_invalid() throws Exception {
        assertEquals(mraidCommandProcessor.parseNumber(null), 100);
    }

    @Test(expected=MraidError.class)
    public void parseNumber_invalid2() throws Exception {
        assertEquals(mraidCommandProcessor.parseNumber("string"), 100);
    }

    @Test
    public void checkParamsForCommand_setOrientationProperties() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("allowOrientationChange", "true");
        params.put("forceOrientation", "landscape");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("setOrientationProperties", params));

        params = new HashMap<>();
        params.put("forceOrientation", "landscape");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setOrientationProperties", params));

        params = new HashMap<>();
        params.put("allowOrientationChange", "true");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setOrientationProperties", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setOrientationProperties", params));
    }

    @Test
    public void checkParamsForCommand_setResizeProperties() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("width", "true");
        params.put("height", "landscape");
        params.put("offsetX", "landscape");
        params.put("offsetY", "landscape");
        params.put("customClosePosition", "true");
        params.put("allowOffscreen", "landscape");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));

        params = new HashMap<>();
        params.put("width", "true");
        params.put("height", "landscape");
        params.put("offsetX", "landscape");
        params.put("offsetY", "landscape");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));

        params = new HashMap<>();
        params.put("height", "landscape");
        params.put("offsetX", "landscape");
        params.put("offsetY", "landscape");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));

        params = new HashMap<>();
        params.put("width", "true");
        params.put("offsetX", "landscape");
        params.put("offsetY", "landscape");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));

        params = new HashMap<>();
        params.put("width", "true");
        params.put("height", "landscape");
        params.put("offsetY", "landscape");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));

        params = new HashMap<>();
        params.put("width", "true");
        params.put("height", "landscape");
        params.put("offsetX", "landscape");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("setResizeProperties", params));
    }

    @Test
    public void checkParamsForCommand_storePicture() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("url", "url");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("storePicture", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("storePicture", params));
    }

    @Test
    public void checkParamsForCommand_createCalendarEvent() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("eventJSON", "{}");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("createCalendarEvent", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("createCalendarEvent", params));
    }

    @Test
    public void checkParamsForCommand_playVideo() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("url", "url");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("playVideo", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("playVideo", params));
    }

    @Test
    public void checkParamsForCommand_open() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("url", "url");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("open", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("open", params));
    }

    @Test
    public void checkParamsForCommand_AdError() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("msg", "message");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("AdError", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("AdError", params));
    }

    @Test
    public void checkParamsForCommand_AdClickThru() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("url", "link");
        params.put("id", "0");
        params.put("playerHandles", "true");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("AdClickThru", params));

        params = new HashMap<>();
        params.put("id", "0");
        params.put("playerHandles", "true");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("AdClickThru", params));

        params = new HashMap<>();
        params.put("url", "link");
        params.put("playerHandles", "true");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("AdClickThru", params));

        params = new HashMap<>();
        params.put("url", "link");
        params.put("id", "0");
        assertFalse(mraidCommandProcessor.checkParamsForCommand("AdClickThru", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("AdClickThru", params));
    }


    @Test
    public void checkParamsForCommand_AdDuration() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("time", "15");
        assertTrue(mraidCommandProcessor.checkParamsForCommand("AdDuration", params));

        params = new HashMap<>();
        assertFalse(mraidCommandProcessor.checkParamsForCommand("AdDuration", params));
    }

    @Test
    public void isValidCommand() throws Exception {
        assertTrue(mraidCommandProcessor.isValidCommand("close"));
        assertTrue(mraidCommandProcessor.isValidCommand("unload"));
        assertTrue(mraidCommandProcessor.isValidCommand("open"));
        assertTrue(mraidCommandProcessor.isValidCommand("createCalendarEvent"));
        assertTrue(mraidCommandProcessor.isValidCommand("playVideo"));
        assertTrue(mraidCommandProcessor.isValidCommand("storePicture"));
        assertTrue(mraidCommandProcessor.isValidCommand("setResizeProperties"));
        assertTrue(mraidCommandProcessor.isValidCommand("resize"));
        assertTrue(mraidCommandProcessor.isValidCommand("setOrientationProperties"));
        assertTrue(mraidCommandProcessor.isValidCommand("useCustomClose"));

        assertTrue(mraidCommandProcessor.isValidCommand("noFill"));
        assertTrue(mraidCommandProcessor.isValidCommand("loaded"));

        assertTrue(mraidCommandProcessor.isValidCommand("AdClickThru"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdError"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdImpression"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdPaused"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdPlaying"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdVideoComplete"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdVideoFirstQuartile"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdVideoMidpoint"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdVideoStart"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdVideoThirdQuartile"));
        assertTrue(mraidCommandProcessor.isValidCommand("AdDuration"));


        assertFalse(mraidCommandProcessor.isValidCommand("unknown"));
        assertFalse(mraidCommandProcessor.isValidCommand(null));
    }
}