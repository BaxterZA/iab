package com.appodeal.iab.vpaid;

import org.junit.Test;

import static org.junit.Assert.*;

public class VpaidCommandProcessorAndroidTest {
    private VpaidCommandProcessor vpaidCommandProcessor;

    @Test
    public void isCommandValid_bad_values() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid(null));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);

        //Check all unknown command later
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid(""));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("unknown"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }


    @Test
    public void isCommandValid() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdStarted"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_STARTED);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdStopped"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_STOPPED);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdSkipped"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_SKIPPED);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdLoaded"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_LOADED);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdLinearChange"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_LINEAR_CHANGE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdSizeChange"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_SIZE_CHANGE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdExpandedChange"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_EXPANDED_CHANGE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdImpression"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_IMPRESSION);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdInteraction"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_INTERACTION);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdVideoStart"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_VIDEO_START);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdVideoFirstQuartile"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_VIDEO_FIRST_QUARTILE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdVideoMidpoint"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_VIDEO_MIDPOINT);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdVideoThirdQuartile"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_VIDEO_THIRD_QUARTILE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdVideoComplete"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_VIDEO_COMPLETE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdUserAcceptInvitation"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_USER_ACCEPT_INVITATION);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdUserMinimize"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_USER_MINIMIZE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdUserClose"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_USER_CLOSE);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdPaused"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_PAUSED);

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdPlaying"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_PLAYING);
    }

    @Test
    public void isCommandValid_AdLog() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdLog?msg=message"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_LOG);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("msg"), "message");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdLog"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdError() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdError?msg=message"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_ERROR);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("msg"), "message");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdError"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdClickThru() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdClickThru?url=link"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_CLICK_THRU);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("url"), "link");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdClickThru"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdVolumeChange() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdVolumeChange?state=0"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_VOLUME_CHANGE);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("state"), "0");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdVolumeChange"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdDurationChange() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdDurationChange?state=0"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_DURATION_CHANGE);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("state"), "0");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdDurationChange"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdSkippableStateChange() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdSkippableStateChange?state=0"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_SKIPPABLE_STATE_CHANGE);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("state"), "0");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdSkippableStateChange"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }

    @Test
    public void isCommandValid_AdRemainingTime() throws Exception {
        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertTrue(vpaidCommandProcessor.isCommandValid("vpaid://AdRemainingTime?time=10"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.VPAID_AD_REMAINING_TIME);
        assertEquals(vpaidCommandProcessor.getParams().size(), 1);
        assertEquals(vpaidCommandProcessor.getParams().get("time"), "10");

        vpaidCommandProcessor = new VpaidCommandProcessor();
        assertFalse(vpaidCommandProcessor.isCommandValid("vpaid://AdRemainingTime"));
        assertEquals(vpaidCommandProcessor.getVpaidCommand(), VpaidCommand.UNKNOWN);
    }
}