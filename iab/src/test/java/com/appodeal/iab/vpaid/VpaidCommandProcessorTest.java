package com.appodeal.iab.vpaid;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class VpaidCommandProcessorTest {
    private final VpaidCommandProcessor vpaidCommandProcessor = new VpaidCommandProcessor();

    @Test
    public void checkParamsForCommand_AdLog() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdLog", params));

        params = new HashMap<>();
        params.put("msg", "message");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdLog", params));
    }

    @Test
    public void checkParamsForCommand_AdError() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdError", params));

        params = new HashMap<>();
        params.put("msg", "message");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdError", params));
    }

    @Test
    public void checkParamsForCommand_AdClickThru() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdClickThru", params));

        params = new HashMap<>();
        params.put("url", "url");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdClickThru", params));
    }

    @Test
    public void checkParamsForCommand_AdVolumeChange() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdVolumeChange", params));

        params = new HashMap<>();
        params.put("state", "state");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdVolumeChange", params));
    }

    @Test
    public void checkParamsForCommand_AdDurationChange() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdDurationChange", params));

        params = new HashMap<>();
        params.put("state", "state");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdDurationChange", params));
    }

    @Test
    public void checkParamsForCommand_AdSkippableStateChange() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdSkippableStateChange", params));

        params = new HashMap<>();
        params.put("state", "state");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdSkippableStateChange", params));
    }

    @Test
    public void checkParamsForCommand_AdRemainingTime() throws Exception {
        Map<String, String> params = new HashMap<>();
        assertFalse(vpaidCommandProcessor.checkParamsForCommand("AdRemainingTime", params));

        params = new HashMap<>();
        params.put("time", "time");
        assertTrue(vpaidCommandProcessor.checkParamsForCommand("AdRemainingTime", params));
    }

    @Test
    public void isValidCommand() throws Exception {
        assertTrue(vpaidCommandProcessor.isValidCommand("AdStarted"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdStopped"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdSkipped"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdLoaded"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdLinearChange"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdSizeChange"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdExpandedChange"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdSkippableStateChange"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdDurationChange"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdVolumeChange"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdImpression"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdClickThru"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdInteraction"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdVideoStart"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdVideoFirstQuartile"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdVideoMidpoint"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdVideoThirdQuartile"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdVideoComplete"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdUserAcceptInvitation"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdUserMinimize"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdUserClose"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdPaused"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdPlaying"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdError"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdLog"));
        assertTrue(vpaidCommandProcessor.isValidCommand("AdRemainingTime"));

        assertFalse(vpaidCommandProcessor.isValidCommand("unknown"));
        assertFalse(vpaidCommandProcessor.isValidCommand(null));
    }

}