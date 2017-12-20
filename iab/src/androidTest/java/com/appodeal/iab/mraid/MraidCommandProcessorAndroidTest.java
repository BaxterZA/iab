package com.appodeal.iab.mraid;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MraidCommandProcessorAndroidTest {
    private MraidCommandProcessor mraidCommandProcessor;

    @Test
    public void isCommandValid_bad_values() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid(null));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);

        //Check all unknown command later
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid(""));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("unknown"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://close"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.CLOSE);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://unload"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNLOAD);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://expand"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.EXPAND);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://resize"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.RESIZE);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://noFill"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.JS_TAG_NO_FILL);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://loaded"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.JS_TAG_LOADED);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdImpression"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_IMPRESSION);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdPaused"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_PAUSED);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdPlaying"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_PLAYING);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdVideoComplete"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_VIDEO_COMPLETE);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdVideoFirstQuartile"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_VIDEO_FIRST_QUARTILE);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdVideoMidpoint"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_VIDEO_MIDPOINT);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdVideoStart"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_VIDEO_START);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdVideoThirdQuartile"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_VIDEO_THIRD_QUARTILE);
    }

    @Test
    public void isCommandValid_setOrientationProperties() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://setOrientationProperties?allowOrientationChange=true&forceOrientation=landscape"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.SET_ORIENTATION_PROPERTIES);
        assertEquals(mraidCommandProcessor.getParams().size(), 2);
        assertEquals(mraidCommandProcessor.getParams().get("allowOrientationChange"), "true");
        assertEquals(mraidCommandProcessor.getParams().get("forceOrientation"), "landscape");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setOrientationProperties?forceOrientation=landscape"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setOrientationProperties?allowOrientationChange=true"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setOrientationProperties"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_setResizeProperties() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://setResizeProperties?width=320&height=480&offsetX=15&offsetY=45&customClosePosition=top-left&allowOffscreen=false"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.SET_RESIZE_PROPERTIES);
        assertEquals(mraidCommandProcessor.getParams().size(), 6);
        assertEquals(mraidCommandProcessor.getParams().get("width"), "320");
        assertEquals(mraidCommandProcessor.getParams().get("height"), "480");
        assertEquals(mraidCommandProcessor.getParams().get("offsetX"), "15");
        assertEquals(mraidCommandProcessor.getParams().get("offsetY"), "45");
        assertEquals(mraidCommandProcessor.getParams().get("customClosePosition"), "top-left");
        assertEquals(mraidCommandProcessor.getParams().get("allowOffscreen"), "false");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://setResizeProperties?width=320&height=480&offsetX=15&offsetY=45"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.SET_RESIZE_PROPERTIES);
        assertEquals(mraidCommandProcessor.getParams().size(), 4);
        assertEquals(mraidCommandProcessor.getParams().get("width"), "320");
        assertEquals(mraidCommandProcessor.getParams().get("height"), "480");
        assertEquals(mraidCommandProcessor.getParams().get("offsetX"), "15");
        assertEquals(mraidCommandProcessor.getParams().get("offsetY"), "45");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setResizeProperties?width=320&height=480&offsetX=15"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setResizeProperties?width=320&height=480&offsetY=45"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setResizeProperties?width=320&offsetX=15&offsetY=45"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://setResizeProperties?height=480&offsetX=15&offsetY=45"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_storePicture() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://storePicture?url=http://appodeal.com"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.STORE_PICTURE);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "http://appodeal.com");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://storePicture?url=file://appodeal.txt"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.STORE_PICTURE);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "file://appodeal.txt");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://storePicture"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }


    @Test
    public void isCommandValid_createCalendarEvent() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://createCalendarEvent?eventJSON={\"description\": \"Mayan Apocalypse/End of World\", \"location\": \"everywhere\", \"start\": \"2012-12-21T00:00-05:00\", \"end\": \"2012-12- 22T00:00-05:00\"}"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.CREATE_CALENDAR_EVENT);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("eventJSON"), "{\"description\": \"Mayan Apocalypse/End of World\", \"location\": \"everywhere\", \"start\": \"2012-12-21T00:00-05:00\", \"end\": \"2012-12- 22T00:00-05:00\"}");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://createCalendarEvent"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_playVideo() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://playVideo?url=http://appodeal.com"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.PLAY_VIDEO);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "http://appodeal.com");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://playVideo?url=file://appodeal.mp4"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.PLAY_VIDEO);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "file://appodeal.mp4");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://playVideo"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_open() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://open?url=http://appodeal.com"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.OPEN);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "http://appodeal.com");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://open?url=file://appodeal.txt"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.OPEN);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "file://appodeal.txt");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://open?url=market://com.appodeal.android"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.OPEN);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "market://com.appodeal.android");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://open"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdError() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdError?msg=error message"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_ERROR);
        assertEquals(mraidCommandProcessor.getParams().size(), 1);
        assertEquals(mraidCommandProcessor.getParams().get("msg"), "error message");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://AdError"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdClickThru() throws Exception {
        mraidCommandProcessor = new MraidCommandProcessor();
        assertTrue(mraidCommandProcessor.isCommandValid("mraid://AdClickThru?url=link&id=0&playerHandles=true"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.VPAID_AD_CLICK_THRU);
        assertEquals(mraidCommandProcessor.getParams().size(), 3);
        assertEquals(mraidCommandProcessor.getParams().get("url"), "link");
        assertEquals(mraidCommandProcessor.getParams().get("id"), "0");
        assertEquals(mraidCommandProcessor.getParams().get("playerHandles"), "true");

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://AdClickThru?url=link&id=0"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://AdClickThru?url=link&playerHandles=true"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://AdClickThru?id=0&playerHandles=true"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);

        mraidCommandProcessor = new MraidCommandProcessor();
        assertFalse(mraidCommandProcessor.isCommandValid("mraid://AdClickThru"));
        assertEquals(mraidCommandProcessor.getMraidCommand(), MraidCommand.UNKNOWN);
    }

}