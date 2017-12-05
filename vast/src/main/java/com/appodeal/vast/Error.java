package com.appodeal.vast;


class Error {
     static final Error ERROR_NONE = new Error("", 0);

    static final Error ERROR_CODE_XML_PARSING = new Error("XML parsing error", 100);
    static final Error ERROR_CODE_XML_VALIDATE = new Error("VAST schema validation error", 101);
    static final Error ERROR_CODE_VERSION_ERROR = new Error("VAST version of response not supported", 102);
    static final Error ERROR_CODE_TRAFFIC_ERROR = new Error("Trafficking error. Video player received an Ad type that it was not expecting and/or cannot display", 200);
    static final Error ERROR_CODE_LINEARITY_ERROR = new Error("Video player expecting different linearity", 201);
    static final Error ERROR_CODE_DURATION_ERROR = new Error("Video player expecting different duration", 202);
    static final Error ERROR_CODE_PLAYER_SIZE_ERROR = new Error("Video player expecting different size", 203);
    static final Error ERROR_CODE_WRAPPER_ERROR = new Error("General Wrapper error", 300);
    static final Error ERROR_CODE_BAD_URI = new Error("Timeout of VAST URI provided in Wrapper element, or of VAST URI provided in a subsequent Wrapper element. (URI was either unavailable or reached a timeout as defined by the video player.)", 301);
    static final Error ERROR_CODE_EXCEEDED_WRAPPER_LIMIT = new Error("Wrapper limit reached, as defined by the video player. Too many Wrapper responses have been received with no InLine response", 302);
    static final Error ERROR_CODE_NO_VAST_AFTER_WRAPPER_LIMIT = new Error("No VAST response after one or more Wrappers", 303);
    static final Error ERROR_CODE_NO_FILE = new Error("File not found. Unable to find Linear/MediaFile from URI", 401);
    static final Error ERROR_CODE_BAD_FILE = new Error("Couldn’t find MediaFile that is supported by this video player, based on the attributes of the MediaFile element", 403);
    static final Error ERROR_CODE_ERROR_SHOWING = new Error("Problem displaying MediaFile.", 405);
    static final Error ERROR_CODE_COMPANION_NODE_NOT_FOUND = new Error("Unable to fetch CompanionAds/Companion resource", 603);
    static final Error ERROR_CODE_COMPANION_NOT_FOUND = new Error("Couldn’t find Companion resource with supported type", 604);
    static final Error ERROR_CODE_VPAID_ERROR = new Error("General VPAID error", 901);

    private final String description;
    private final int code;

    private Error(String description, int code) {
        this.description = description;
        this.code = code;
    }

    int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("Vast error: %s", description);
    }
}
