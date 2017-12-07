package com.appodeal.vast.vpaid;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.appodeal.vast.VastLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class VpaidCommandProcessor {
    private Map<String, String> params = new HashMap<>();
    private VpaidCommand vpaidCommand = VpaidCommand.UNKNOWN;


    boolean isCommandValid(String commandUrl) {
        VastLog.d("parseCommandUrl " + commandUrl);
        Uri uri;
        try {
            uri = Uri.parse(commandUrl);
        } catch (Exception e) {
            VastLog.d("Invalid VPAID URL: " + commandUrl);
            return false;
        }
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if ("vpaid".equals(scheme)) {

            Set<String> args = uri.getQueryParameterNames();
            for (String paramName : args) {
                params.put(paramName, uri.getQueryParameter(paramName));
            }

            // Check for valid command.
            if (!isValidCommand(host)) {
                VastLog.d("command " + host + " is unknown");
                return false;
            }

            // Check for valid parameters for the given command.
            if (!checkParamsForCommand(host, params)) {
                VastLog.d("command URL " + commandUrl + " is missing parameters");
                return false;
            }

            for (VpaidCommand command : VpaidCommand.values()) {
                if (command.getName().equals(host)) {
                    vpaidCommand = command;
                }
            }
        }
        return true;
    }

    Map<String, String> getParams() {
        return params;
    }

    VpaidCommand getVpaidCommand() {
        return vpaidCommand;
    }

    @VisibleForTesting
    boolean isValidCommand(String command) {
        final String[] commands = {"AdStarted", "AdStopped", "AdSkipped", "AdLoaded", "AdLinearChange", "AdSizeChange", "AdExpandedChange",
                "AdSkippableStateChange", "AdDurationChange", "AdVolumeChange", "AdImpression", "AdClickThru", "AdInteraction",
                "AdVideoStart", "AdVideoFirstQuartile", "AdVideoMidpoint", "AdVideoThirdQuartile", "AdVideoComplete", "AdUserAcceptInvitation",
                "AdUserMinimize", "AdUserClose", "AdPaused", "AdPlaying", "AdError", "AdLog", "AdRemainingTime"};
        return (Arrays.asList(commands).contains(command));
    }

    @VisibleForTesting boolean checkParamsForCommand(String command, @NonNull Map<String, String> params) {
        switch (command) {
            case "AdLog":
            case "AdError":
                return params.containsKey("msg");
            case "AdClickThru":
                return params.containsKey("url");
            case "AdVolumeChange":
            case "AdDurationChange":
            case "AdSkippableStateChange":
                return params.containsKey("state");
            case "AdRemainingTime":
                return params.containsKey("time");
        }
        return true;
    }

    int parseNumber(String value) throws VpaidError {
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new VpaidError("Invalid numeric parameter: " + value);
        }
        return result;
    }

    boolean parseBoolean(@Nullable String value, boolean defaultValue) throws VpaidError {
        if (value == null) {
            return defaultValue;
        }
        return parseBoolean(value);
    }

    boolean parseBoolean(final String value) throws VpaidError {
        if ("true".equals(value)) {
            return true;
        } else if ("false".equals(value)) {
            return false;
        }
        throw new VpaidError("Invalid boolean parameter: " + value);
    }

    String parseURL(String url) throws VpaidError {
        if (url == null) {
            throw new VpaidError("URL cannot be null");
        }
        return url;
    }

}
