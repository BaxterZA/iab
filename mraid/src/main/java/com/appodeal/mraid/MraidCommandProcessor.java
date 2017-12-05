package com.appodeal.mraid;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class MraidCommandProcessor {
    private Map<String, String> params = new HashMap<>();
    private MraidCommand mraidCommand = MraidCommand.UNKNOWN;


    boolean isCommandValid(String commandUrl) {
        MraidLog.d("parseCommandUrl " + commandUrl);
        Uri uri;
        try {
            uri = Uri.parse(commandUrl);
        } catch (Exception e) {
            MraidLog.d("Invalid MRAID URL: " + commandUrl);
            return false;
        }
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if ("mraid".equals(scheme)) {

            Set<String> args = uri.getQueryParameterNames();
            for (String paramName : args) {
                params.put(paramName, uri.getQueryParameter(paramName));
            }

            // Check for valid command.
            if (!isValidCommand(host)) {
                MraidLog.d("command " + host + " is unknown");
                return false;
            }

            // Check for valid parameters for the given command.
            if (!checkParamsForCommand(host, params)) {
                MraidLog.d("command URL " + commandUrl + " is missing parameters");
                return false;
            }

            for (MraidCommand command : MraidCommand.values()) {
                if (command.getName().equals(host)) {
                    mraidCommand = command;
                }
            }
        }
        return true;
    }

    Map<String, String> getParams() {
        return params;
    }

    MraidCommand getMraidCommand() {
        return mraidCommand;
    }

    @VisibleForTesting boolean isValidCommand(String command) {
        final String[] commands = {"close", "unload", "createCalendarEvent", "expand", "open", "playVideo", "resize", "setOrientationProperties", "setResizeProperties", "storePicture", "useCustomClose", "noFill", "loaded",
                "AdClickThru", "AdError", "AdImpression", "AdPaused", "AdPlaying", "AdVideoComplete", "AdVideoFirstQuartile", "AdVideoMidpoint", "AdVideoStart", "AdVideoThirdQuartile",
                "AdDuration"};
        return (Arrays.asList(commands).contains(command));
    }

    @VisibleForTesting boolean checkParamsForCommand(String command, @NonNull Map<String, String> params) {
        switch (command) {
            case "createCalendarEvent":
                return params.containsKey("eventJSON");
            case "open":
            case "playVideo":
            case "storePicture":
                return params.containsKey("url");
            case "setOrientationProperties":
                return params.containsKey("allowOrientationChange") && params.containsKey("forceOrientation");
            case "setResizeProperties":
                return params.containsKey("width") &&
                        params.containsKey("height") &&
                        params.containsKey("offsetX") &&
                        params.containsKey("offsetY");
            case "useCustomClose":
                return params.containsKey("useCustomClose");
            case "AdError":
                return params.containsKey("msg");
            case "AdClickThru":
                return params.containsKey("url") &&
                        params.containsKey("id") &&
                        params.containsKey("playerHandles");
            case "AdDuration":
                return params.containsKey("time");
        }
        return true;
    }

    int parseNumber(String value) throws MraidError {
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new MraidError("Invalid numeric parameter: " + value);
        }
        return result;
    }

    boolean parseBoolean(@Nullable String value, boolean defaultValue) throws MraidError {
        if (value == null) {
            return defaultValue;
        }
        return parseBoolean(value);
    }

    boolean parseBoolean(final String value) throws MraidError {
        if ("true".equals(value)) {
            return true;
        } else if ("false".equals(value)) {
            return false;
        }
        throw new MraidError("Invalid boolean parameter: " + value);
    }

    MraidOrientation parseOrientation(String value) throws MraidError {
        switch (value) {
            case "portrait":
                return MraidOrientation.PORTRAIT;
            case "landscape":
                return MraidOrientation.LANDSCAPE;
            case "none":
                return MraidOrientation.NONE;
            default:
                throw new MraidError("Invalid orientation: " + value);
        }
    }

    String parseURL(String url) throws MraidError {
        if (url == null) {
            throw new MraidError("URL cannot be null");
        }
        return url;
    }
}
