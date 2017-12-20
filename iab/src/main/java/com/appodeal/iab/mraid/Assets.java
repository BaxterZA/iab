package com.appodeal.iab.mraid;

class Assets {
    static final String mraidJs = "//\n" +
            "//  mraid.js\n" +
            "//\n" +
            "\n" +
            "(function() {\n" +
            "\n" +
            "    console.log(\"MRAID object loading...\");\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * console logging helper\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    var LogLevelEnum = {\n" +
            "        \"DEBUG\"   : 0,\n" +
            "        \"INFO\"    : 1,\n" +
            "        \"WARNING\" : 2,\n" +
            "        \"ERROR\"   : 3,\n" +
            "        \"NONE\"    : 4\n" +
            "    };\n" +
            "\n" +
            "    var logLevel = LogLevelEnum.DEBUG;\n" +
            "    var log = {};\n" +
            "\n" +
            "    log.d = function(msg) {\n" +
            "        if (logLevel <= LogLevelEnum.DEBUG) {\n" +
            "            console.log(\"(D-mraid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    log.i = function(msg) {\n" +
            "        if (logLevel <= LogLevelEnum.INFO) {\n" +
            "            console.log(\"(I-mraid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    log.w = function(msg) {\n" +
            "        if (logLevel <= LogLevelEnum.WARNING) {\n" +
            "            console.log(\"(W-mraid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    log.e = function(msg) {\n" +
            "        if (logLevel <= LogLevelEnum.ERROR) {\n" +
            "            console.log(\"(E-mraid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * MRAID declaration\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    var mraid = window.mraid = {};\n" +
            "\n" +
            "    var STATES = mraid.STATES = {\n" +
            "        \"LOADING\" : \"loading\",\n" +
            "        \"DEFAULT\" : \"default\",\n" +
            "        \"EXPANDED\" : \"expanded\",\n" +
            "        \"RESIZED\" : \"resized\",\n" +
            "        \"HIDDEN\" : \"hidden\"\n" +
            "    };\n" +
            "\n" +
            "    var PLACEMENT_TYPES = mraid.PLACEMENT_TYPES = {\n" +
            "        \"INLINE\" : \"inline\",\n" +
            "        \"INTERSTITIAL\" : \"interstitial\"\n" +
            "    };\n" +
            "\n" +
            "    var RESIZE_PROPERTIES_CUSTOM_CLOSE_POSITION = mraid.RESIZE_PROPERTIES_CUSTOM_CLOSE_POSITION = {\n" +
            "        \"TOP_LEFT\" : \"top-left\",\n" +
            "        \"TOP_CENTER\" : \"top-center\",\n" +
            "        \"TOP_RIGHT\" : \"top-right\",\n" +
            "        \"CENTER\" : \"center\",\n" +
            "        \"BOTTOM_LEFT\" : \"bottom-left\",\n" +
            "        \"BOTTOM_CENTER\" : \"bottom-center\",\n" +
            "        \"BOTTOM_RIGHT\" : \"bottom-right\"\n" +
            "    };\n" +
            "\n" +
            "    var ORIENTATION_PROPERTIES_FORCE_ORIENTATION = mraid.ORIENTATION_PROPERTIES_FORCE_ORIENTATION = {\n" +
            "        \"PORTRAIT\" : \"portrait\",\n" +
            "        \"LANDSCAPE\" : \"landscape\",\n" +
            "        \"NONE\" : \"none\"\n" +
            "    };\n" +
            "\n" +
            "    var EVENTS = mraid.EVENTS = {\n" +
            "        \"ERROR\" : \"error\",\n" +
            "        \"READY\" : \"ready\",\n" +
            "        \"SIZECHANGE\" : \"sizeChange\",\n" +
            "        \"STATECHANGE\" : \"stateChange\",\n" +
            "        \"EXPOSURECHANGE\" : \"exposureChange\",\n" +
            "        \"AUDIOVOLUMECHANGE\" : \"audioVolumeChange\",\n" +
            "        \"VIEWABLECHANGE\" : \"viewableChange\"\n" +
            "    };\n" +
            "\n" +
            "    var SUPPORTED_FEATURES = mraid.SUPPORTED_FEATURES = {\n" +
            "        \"SMS\" : \"sms\",\n" +
            "        \"TEL\" : \"tel\",\n" +
            "        \"CALENDAR\" : \"calendar\",\n" +
            "        \"STOREPICTURE\" : \"storePicture\",\n" +
            "        \"INLINEVIDEO\" : \"inlineVideo\",\n" +
            "        \"VPAID\" : \"vpaid\",\n" +
            "        \"LOCATION\" : \"location\"\n" +
            "    };\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * state\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    var state = STATES.LOADING;\n" +
            "    var placementType = PLACEMENT_TYPES.INLINE;\n" +
            "    var supportedFeatures = {};\n" +
            "    var isViewable = false;\n" +
            "    var isExpandPropertiesSet = false;\n" +
            "    var isResizeReady = false;\n" +
            "\n" +
            "    var expandProperties = {\n" +
            "        \"width\" : 0,\n" +
            "        \"height\" : 0,\n" +
            "        \"useCustomClose\" : false,\n" +
            "        \"isModal\" : true\n" +
            "    };\n" +
            "\n" +
            "    var orientationProperties = {\n" +
            "        \"allowOrientationChange\" : true,\n" +
            "        \"forceOrientation\" : ORIENTATION_PROPERTIES_FORCE_ORIENTATION.NONE\n" +
            "    };\n" +
            "\n" +
            "    var currentAppOrientation = {\n" +
            "        \"orientation\": \"\",\n" +
            "        \"locked\": false\n" +
            "    }\n" +
            "\n" +
            "    var resizeProperties = {\n" +
            "        \"width\" : 0,\n" +
            "        \"height\" : 0,\n" +
            "        \"customClosePosition\" : RESIZE_PROPERTIES_CUSTOM_CLOSE_POSITION.TOP_RIGHT,\n" +
            "        \"offsetX\" : 0,\n" +
            "        \"offsetY\" : 0,\n" +
            "        \"allowOffscreen\" : true\n" +
            "    };\n" +
            "\n" +
            "    var currentPosition = {\n" +
            "        \"x\" : 0,\n" +
            "        \"y\" : 0,\n" +
            "        \"width\" : 0,\n" +
            "        \"height\" : 0\n" +
            "    };\n" +
            "\n" +
            "    var defaultPosition = {\n" +
            "        \"x\" : 0,\n" +
            "        \"y\" : 0,\n" +
            "        \"width\" : 0,\n" +
            "        \"height\" : 0\n" +
            "    };\n" +
            "\n" +
            "    var maxSize = {\n" +
            "        \"width\" : 0,\n" +
            "        \"height\" : 0\n" +
            "    };\n" +
            "\n" +
            "    var screenSize = {\n" +
            "        \"width\" : 0,\n" +
            "        \"height\" : 0\n" +
            "    };\n" +
            "\n" +
            "    var location = {\n" +
            "        \"lat\": 0,\n" +
            "        \"lon\": 0,\n" +
            "        \"type\": 0,\n" +
            "        \"accuracy\": 0,\n" +
            "        \"lastfix\": 0,\n" +
            "        \"ipservice\": \"\",\n" +
            "    }\n" +
            "\n" +
            "    var currentOrientation = 0;\n" +
            "\n" +
            "    var listeners = {};\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * \"official\" API: methods called by creative\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    mraid.addEventListener = function(event, listener) {\n" +
            "        log.i(\"mraid.addEventListener \" + event + \": \" + String(listener));\n" +
            "        if (!event || !listener) {\n" +
            "            mraid.fireErrorEvent(\"Both event and listener are required.\", \"mraid.addEventListener\");\n" +
            "            return;\n" +
            "        }\n" +
            "        if (!contains(event, EVENTS)) {\n" +
            "            mraid.fireErrorEvent(\"Unknown MRAID event: \" + event, \"mraid.addEventListener\");\n" +
            "            return;\n" +
            "        }\n" +
            "        var listenersForEvent = listeners[event] = listeners[event] || [];\n" +
            "        // check to make sure that the listener isn't already registered\n" +
            "        for (var i = 0; i < listenersForEvent.length; i++) {\n" +
            "            var str1 = String(listener);\n" +
            "            var str2 = String(listenersForEvent[i]);\n" +
            "            if (listener === listenersForEvent[i] || str1 === str2) {\n" +
            "                log.i(\"listener \" + str1 + \" is already registered for event \" + event);\n" +
            "                return;\n" +
            "            }\n" +
            "        }\n" +
            "        listenersForEvent.push(listener);\n" +
            "    };\n" +
            "\n" +
            "    mraid.createCalendarEvent = function(parameters) {\n" +
            "        log.i(\"mraid.createCalendarEvent with \" + parameters);\n" +
            "        if (supportedFeatures[mraid.SUPPORTED_FEATURES.CALENDAR]) {\n" +
            "            callNative(\"createCalendarEvent?eventJSON=\"\t+ JSON.stringify(parameters));\n" +
            "        } else {\n" +
            "            log.e(\"createCalendarEvent is not supported\");\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.close = function() {\n" +
            "        log.i(\"mraid.close\");\n" +
            "        if (state === STATES.LOADING\n" +
            "            || (state === STATES.DEFAULT && placementType === PLACEMENT_TYPES.INLINE)\n" +
            "            || state === STATES.HIDDEN) {\n" +
            "            mraid.fireErrorEvent(\"mraid.close can only be called on inline placements in a resized or expanded state, or by interstitials in a default state\", \"mraid.close\");\n" +
            "            return;\n" +
            "        }\n" +
            "        callNative(\"close\");\n" +
            "    };\n" +
            "\n" +
            "    mraid.unload = function() {\n" +
            "        log.i(\"mraid.unload\");\n" +
            "        callNative(\"unload\");\n" +
            "    };\n" +
            "\n" +
            "    mraid.expand = function(url) {\n" +
            "        if (url === undefined) {\n" +
            "            log.i(\"mraid.expand (1-part)\");\n" +
            "        } else {\n" +
            "            mraid.fireErrorEvent(\"Two-part ads deprecated\", \"mraid.expand\");\n" +
            "            return;\n" +
            "        }\n" +
            "        // The only time it is valid to call expand is when the ad is\n" +
            "        // a banner currently in either default or resized state.\n" +
            "        if (placementType !== PLACEMENT_TYPES.INLINE\n" +
            "            || (state !== STATES.DEFAULT && state !== STATES.RESIZED)) {\n" +
            "            return;\n" +
            "        }\n" +
            "        callNative(\"expand\");\n" +
            "    };\n" +
            "\n" +
            "    mraid.getCurrentPosition = function() {\n" +
            "        log.i(\"mraid.getCurrentPosition\");\n" +
            "        return currentPosition;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getDefaultPosition = function() {\n" +
            "        log.i(\"mraid.getDefaultPosition\");\n" +
            "        return defaultPosition;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getExpandProperties = function() {\n" +
            "        log.i(\"mraid.getExpandProperties\");\n" +
            "        return expandProperties;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getMaxSize = function() {\n" +
            "        log.i(\"mraid.getMaxSize\");\n" +
            "        return maxSize;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getOrientationProperties = function() {\n" +
            "        log.i(\"mraid.getOrientationProperties\");\n" +
            "        return orientationProperties;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getPlacementType = function() {\n" +
            "        log.i(\"mraid.getPlacementType\");\n" +
            "        return placementType;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getResizeProperties = function() {\n" +
            "        log.i(\"mraid.getResizeProperties\");\n" +
            "        return resizeProperties;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getScreenSize = function() {\n" +
            "        log.i(\"mraid.getScreenSize\");\n" +
            "        return screenSize;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getCurrentAppOrientation = function() {\n" +
            "        log.i(\"mraid.getCurrentAppOrientation\");\n" +
            "        return currentAppOrientation;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getLocation = function() {\n" +
            "        log.i(\"mraid.getLocation\");\n" +
            "        return location;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getState = function() {\n" +
            "        log.i(\"mraid.getState\");\n" +
            "        return state;\n" +
            "    };\n" +
            "\n" +
            "    mraid.getVersion = function() {\n" +
            "        log.i(\"mraid.getVersion\");\n" +
            "        return window.MRAID_ENV.version;\n" +
            "    };\n" +
            "\n" +
            "    mraid.isViewable = function() {\n" +
            "        log.i(\"mraid.isViewable\");\n" +
            "        return isViewable;\n" +
            "    };\n" +
            "\n" +
            "    mraid.open = function(url) {\n" +
            "        log.i(\"mraid.open \" + url);\n" +
            "        callNative(\"open?url=\" + encodeURIComponent(url));\n" +
            "    };\n" +
            "\n" +
            "    mraid.playVideo = function(url) {\n" +
            "        log.i(\"mraid.playVideo \" + url);\n" +
            "        callNative(\"playVideo?url=\" + encodeURIComponent(url));\n" +
            "    };\n" +
            "\n" +
            "    mraid.removeEventListener = function(event, listener) {\n" +
            "        log.i(\"mraid.removeEventListener \" + event + \" : \" + String(listener));\n" +
            "        if (!event) {\n" +
            "            mraid.fireErrorEvent(\"Event is required.\", \"removeEventListener\");\n" +
            "            return;\n" +
            "        }\n" +
            "        if (!contains(event, EVENTS)) {\n" +
            "            mraid.fireErrorEvent(\"Unknown MRAID event: \" + event, \"removeEventListener\");\n" +
            "            return;\n" +
            "        }\n" +
            "        if (listeners.hasOwnProperty(event)) {\n" +
            "            if (listener) {\n" +
            "                var listenersForEvent = listeners[event];\n" +
            "                // try to find the given listener\n" +
            "                var len = listenersForEvent.length;\n" +
            "                for (var i = 0; i < len; i++) {\n" +
            "                    var registeredListener = listenersForEvent[i];\n" +
            "                    var str1 = String(listener);\n" +
            "                    var str2 = String(registeredListener);\n" +
            "                    if (listener === registeredListener || str1 === str2) {\n" +
            "                        listenersForEvent.splice(i, 1);\n" +
            "                        break;\n" +
            "                    }\n" +
            "                }\n" +
            "                if (i === len) {\n" +
            "                    log.i(\"listener \" + str1 + \" not found for event \" + event);\n" +
            "                }\n" +
            "                if (listenersForEvent.length === 0) {\n" +
            "                    delete listeners[event];\n" +
            "                }\n" +
            "            } else {\n" +
            "                // no listener to remove was provided, so remove all listeners\n" +
            "                // for given event\n" +
            "                delete listeners[event];\n" +
            "            }\n" +
            "        } else {\n" +
            "            log.i(\"no listeners registered for event \" + event);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.resize = function() {\n" +
            "        log.i(\"mraid.resize\");\n" +
            "        // The only time it is valid to call resize is when the ad is\n" +
            "        // a banner currently in either default or resized state.\n" +
            "        // Trigger an error if the current state is expanded.\n" +
            "        if (placementType === PLACEMENT_TYPES.INTERSTITIAL || state === STATES.LOADING || state === STATES.HIDDEN) {\n" +
            "            // do nothing\n" +
            "            return;\n" +
            "        }\n" +
            "        if (state === STATES.EXPANDED) {\n" +
            "            mraid.fireErrorEvent(\"mraid.resize called when ad is in expanded state\", \"mraid.resize\");\n" +
            "            return;\n" +
            "        }\n" +
            "        if (!isResizeReady) {\n" +
            "            mraid.fireErrorEvent(\"mraid.resize is not ready to be called\", \"mraid.resize\");\n" +
            "            return;\n" +
            "        }\n" +
            "        callNative(\"resize\");\n" +
            "    };\n" +
            "\n" +
            "    mraid.setExpandProperties = function(properties) {\n" +
            "        log.i(\"mraid.setExpandProperties\");\n" +
            "\n" +
            "        if (!validate(properties, \"setExpandProperties\")) {\n" +
            "            log.e(\"failed validation\");\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        var oldUseCustomClose = expandProperties.useCustomClose;\n" +
            "\n" +
            "        // expandProperties contains 3 read-write properties: width, height, and useCustomClose;\n" +
            "        // the isModal property is read-only\n" +
            "        var rwProps = [ \"width\", \"height\", \"useCustomClose\" ];\n" +
            "        for (var i = 0; i < rwProps.length; i++) {\n" +
            "            var propname = rwProps[i];\n" +
            "            if (properties.hasOwnProperty(propname)) {\n" +
            "                expandProperties[propname] = properties[propname];\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        // In MRAID v3.0, all expanded ads by definition cover the entire screen,\n" +
            "        // so the only property that the native side has to know about is useCustomClose.\n" +
            "        // (That is, the width and height properties are not needed by the native code.)\n" +
            "        if (expandProperties.useCustomClose !== oldUseCustomClose) {\n" +
            "            callNative(\"useCustomClose?useCustomClose=\"\t+ expandProperties.useCustomClose);\n" +
            "        }\n" +
            "\n" +
            "        isExpandPropertiesSet = true;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setOrientationProperties = function(properties) {\n" +
            "        log.i(\"mraid.setOrientationProperties\");\n" +
            "\n" +
            "        if (!validate(properties, \"setOrientationProperties\")) {\n" +
            "            log.e(\"failed validation\");\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        var newOrientationProperties = {};\n" +
            "        newOrientationProperties.allowOrientationChange = orientationProperties.allowOrientationChange,\n" +
            "            newOrientationProperties.forceOrientation = orientationProperties.forceOrientation;\n" +
            "\n" +
            "        // orientationProperties contains 2 read-write properties:\n" +
            "        // allowOrientationChange and forceOrientation\n" +
            "        var rwProps = [ \"allowOrientationChange\", \"forceOrientation\" ];\n" +
            "        for (var i = 0; i < rwProps.length; i++) {\n" +
            "            var propname = rwProps[i];\n" +
            "            if (properties.hasOwnProperty(propname)) {\n" +
            "                newOrientationProperties[propname] = properties[propname];\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        // Setting allowOrientationChange to true while setting forceOrientation\n" +
            "        // to either portrait or landscape\n" +
            "        // is considered an error condition.\n" +
            "        if (newOrientationProperties.allowOrientationChange\n" +
            "            && newOrientationProperties.forceOrientation !== mraid.ORIENTATION_PROPERTIES_FORCE_ORIENTATION.NONE) {\n" +
            "            mraid.fireErrorEvent(\n" +
            "                \"allowOrientationChange is true but forceOrientation is \"\n" +
            "                + newOrientationProperties.forceOrientation,\n" +
            "                \"setOrientationProperties\");\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        orientationProperties.allowOrientationChange = newOrientationProperties.allowOrientationChange;\n" +
            "        orientationProperties.forceOrientation = newOrientationProperties.forceOrientation;\n" +
            "\n" +
            "        var params = \"allowOrientationChange=\"\n" +
            "            + orientationProperties.allowOrientationChange\n" +
            "            + \"&forceOrientation=\" + orientationProperties.forceOrientation;\n" +
            "\n" +
            "        callNative(\"setOrientationProperties?\" + params);\n" +
            "    };\n" +
            "\n" +
            "    mraid.setResizeProperties = function(properties) {\n" +
            "        log.i(\"mraid.setResizeProperties\");\n" +
            "\n" +
            "        isResizeReady = false;\n" +
            "\n" +
            "        // resizeProperties contains 6 read-write properties:\n" +
            "        // width, height, offsetX, offsetY, customClosePosition, allowOffscreen\n" +
            "\n" +
            "        // The properties object passed into this function must contain width, height, offsetX, offsetY.\n" +
            "        // The remaining two properties are optional.\n" +
            "        var requiredProps = [ \"width\", \"height\", \"offsetX\", \"offsetY\" ];\n" +
            "        for (var i = 0; i < requiredProps.length; i++) {\n" +
            "            var propname = requiredProps[i];\n" +
            "            if (!properties.hasOwnProperty(propname)) {\n" +
            "                mraid.fireErrorEvent(\n" +
            "                    \"required property \" + propname + \" is missing\",\n" +
            "                    \"mraid.setResizeProperties\");\n" +
            "                return;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        if (!validate(properties, \"setResizeProperties\")) {\n" +
            "            mraid.fireErrorEvent(\"failed validation\", \"mraid.setResizeProperties\");\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        var rwProps = [ \"width\", \"height\", \"offsetX\", \"offsetY\", \"customClosePosition\", \"allowOffscreen\" ];\n" +
            "        for (var i = 0; i < rwProps.length; i++) {\n" +
            "            var propname = rwProps[i];\n" +
            "            if (properties.hasOwnProperty(propname)) {\n" +
            "                resizeProperties[propname] = properties[propname];\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        var params =\n" +
            "            \"width=\" + resizeProperties.width +\n" +
            "            \"&height=\" + resizeProperties.height +\n" +
            "            \"&offsetX=\" + resizeProperties.offsetX +\n" +
            "            \"&offsetY=\" + resizeProperties.offsetY +\n" +
            "            \"&customClosePosition=\" + resizeProperties.customClosePosition +\n" +
            "            \"&allowOffscreen=\" + resizeProperties.allowOffscreen;\n" +
            "\n" +
            "        callNative(\"setResizeProperties?\" + params);\n" +
            "\n" +
            "        isResizeReady = true;\n" +
            "    };\n" +
            "\n" +
            "    mraid.storePicture = function(url) {\n" +
            "        log.i(\"mraid.storePicture \" + url);\n" +
            "        if (supportedFeatures[mraid.SUPPORTED_FEATURES.STOREPICTURE]) {\n" +
            "            callNative(\"storePicture?url=\" + encodeURIComponent(url));\n" +
            "        } else {\n" +
            "            log.e(\"storePicture is not supported\");\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.supports = function(feature) {\n" +
            "        log.i(\"mraid.supports \" + feature + \" \" + supportedFeatures[feature]);\n" +
            "        var retval = supportedFeatures[feature];\n" +
            "        if (typeof retval === \"undefined\") {\n" +
            "            retval = false;\n" +
            "        }\n" +
            "        return retval;\n" +
            "    };\n" +
            "    mraid.useCustomClose = function(isCustomClose) {\n" +
            "        log.i(\"mraid.useCustomClose \" + isCustomClose);\n" +
            "        if (expandProperties.useCustomClose !== isCustomClose) {\n" +
            "            expandProperties.useCustomClose = isCustomClose;\n" +
            "            callNative(\"useCustomClose?useCustomClose=\" + expandProperties.useCustomClose);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * helper methods called by SDK\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "        // setters to change state\n" +
            "    mraid.setCurrentPosition = function(x, y, width, height) {\n" +
            "        log.i(\"mraid.setCurrentPosition \" + x + \",\" + y + \",\" + width + \",\"\t+ height);\n" +
            "\n" +
            "        var previousSize = {};\n" +
            "        previousSize.width = currentPosition.width;\n" +
            "        previousSize.height = currentPosition.height;\n" +
            "        log.i(\"previousSize \" + previousSize.width + \",\" + previousSize.height);\n" +
            "\n" +
            "        currentPosition.x = x;\n" +
            "        currentPosition.y = y;\n" +
            "        currentPosition.width = width;\n" +
            "        currentPosition.height = height;\n" +
            "\n" +
            "        if (width !== previousSize.width || height !== previousSize.height) {\n" +
            "            mraid.fireSizeChangeEvent(width, height);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.setDefaultPosition = function(x, y, width, height) {\n" +
            "        log.i(\"mraid.setDefaultPosition \" + x + \",\" + y + \",\" + width + \",\"\t+ height);\n" +
            "        defaultPosition.x = x;\n" +
            "        defaultPosition.y = y;\n" +
            "        defaultPosition.width = width;\n" +
            "        defaultPosition.height = height;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setExpandSize = function(width, height) {\n" +
            "        log.i(\"mraid.setExpandSize \" + width + \"x\" + height);\n" +
            "        expandProperties.width = width;\n" +
            "        expandProperties.height = height;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setMaxSize = function(width, height) {\n" +
            "        log.i(\"mraid.setMaxSize \" + width + \"x\" + height);\n" +
            "        maxSize.width = width;\n" +
            "        maxSize.height = height;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setPlacementType = function(pt) {\n" +
            "        log.i(\"mraid.setPlacementType \" + pt);\n" +
            "        placementType = pt;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setScreenSize = function(width, height) {\n" +
            "        log.i(\"mraid.setScreenSize \" + width + \"x\" + height);\n" +
            "        screenSize.width = width;\n" +
            "        screenSize.height = height;\n" +
            "        if (!isExpandPropertiesSet) {\n" +
            "            expandProperties.width = width;\n" +
            "            expandProperties.height = height;;\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.setCurrentAppOrientation = function(orientation, locked) {\n" +
            "        log.i(\"mraid.setCurrentAppOrientation \" + orientation + \", \" + locked);\n" +
            "        currentAppOrientation.orientation = orientation;\n" +
            "        currentAppOrientation.locked = locked;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setSupports = function(feature, supported) {\n" +
            "        log.i(\"mraid.setSupports \" + feature + \" \" + supported);\n" +
            "        supportedFeatures[feature] = supported;\n" +
            "    };\n" +
            "\n" +
            "    mraid.setLocation = function(lat, lon, type, accuracy, lastfix, ipservice) {\n" +
            "        log.i(\"mraid.setLocation \" + lat + \", \" + lon + \", \" + type + \", \" + accuracy + \", \" + lastfix + \", \" + ipservice);\n" +
            "        location.lat = lat;\n" +
            "        location.lon = lon;\n" +
            "        location.type = type;\n" +
            "        location.accuracy = accuracy;\n" +
            "        location.lastfix = lastfix;\n" +
            "        location.ipservice = ipservice;\n" +
            "    };\n" +
            "\n" +
            "    // methods to fire events\n" +
            "\n" +
            "    mraid.fireErrorEvent = function(message, action) {\n" +
            "        log.i(\"mraid.fireErrorEvent \" + message + \" \" + action);\n" +
            "        fireEvent(mraid.EVENTS.ERROR, message, action);\n" +
            "    };\n" +
            "\n" +
            "    mraid.fireReadyEvent = function() {\n" +
            "        log.i(\"mraid.fireReadyEvent\");\n" +
            "        fireEvent(mraid.EVENTS.READY);\n" +
            "    };\n" +
            "\n" +
            "    mraid.fireSizeChangeEvent = function(width, height) {\n" +
            "        log.i(\"mraid.fireSizeChangeEvent \" + width + \"x\" + height);\n" +
            "        if (state !== mraid.STATES.LOADING) {\n" +
            "            fireEvent(mraid.EVENTS.SIZECHANGE, width, height);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.fireStateChangeEvent = function(newState) {\n" +
            "        log.i(\"mraid.fireStateChangeEvent \" + newState);\n" +
            "        if (state !== newState) {\n" +
            "            state = newState;\n" +
            "            fireEvent(mraid.EVENTS.STATECHANGE, state);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.fireViewableChangeEvent = function(newIsViewable) {\n" +
            "        log.i(\"mraid.fireViewableChangeEvent \" + newIsViewable);\n" +
            "        if (isViewable !== newIsViewable) {\n" +
            "            isViewable = newIsViewable;\n" +
            "            fireEvent(mraid.EVENTS.VIEWABLECHANGE, isViewable);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.fireAudioVolumeChangeEvent = function(volumePercentage) {\n" +
            "        log.i(\"mraid.fireAudioVolumeChangeEvent \" + volumePercentage);\n" +
            "        fireEvent(mraid.EVENTS.AUDIOVOLUMECHANGE, volumePercentage);\n" +
            "    };\n" +
            "\n" +
            "    mraid.fireExposureChangeEvent = function(exposedPercentage, visibleRectangle, occlusionRectangles) {\n" +
            "        fireEvent(mraid.EVENTS.EXPOSURECHANGE, exposedPercentage, visibleRectangle, occlusionRectangles);\n" +
            "    };\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * Appodeal methods for js tag supporting\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    mraid.adLoaded = false;\n" +
            "\n" +
            "    mraid.noFill = function() {\n" +
            "        if (!mraid.adLoaded) {\n" +
            "            log.i(\"mraid.noFill\");\n" +
            "            callNative(\"noFill\");\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    mraid.loaded = function() {\n" +
            "        log.i(\"mraid.loaded\");\n" +
            "        mraid.adLoaded = true;\n" +
            "        callNative(\"loaded\");\n" +
            "    };\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * internal helper methods\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    function callNative(command) {\n" +
            "        var iframe = document.createElement(\"IFRAME\");\n" +
            "        iframe.setAttribute(\"src\", \"mraid://\" + command);\n" +
            "        document.documentElement.appendChild(iframe);\n" +
            "        iframe.parentNode.removeChild(iframe);\n" +
            "        iframe = null;\n" +
            "    };\n" +
            "\n" +
            "    function fireEvent(event) {\n" +
            "        var args = Array.prototype.slice.call(arguments);\n" +
            "        args.shift();\n" +
            "        log.i(\"fireEvent \" + event + \" [\" + args.toString() + \"]\");\n" +
            "        var tempEventListeners = listeners[event];\n" +
            "        if (tempEventListeners) {\n" +
            "            var eventListeners = tempEventListeners.slice();\n" +
            "            var len = eventListeners.length;\n" +
            "            log.i(len + \" listener(s) found\");\n" +
            "            for (var i = 0; i < len; i++) {\n" +
            "                eventListeners[i].apply(null, args);\n" +
            "            }\n" +
            "        } else {\n" +
            "            log.i(\"no listeners found for \" + event);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    function contains(value, array) {\n" +
            "        for ( var i in array) {\n" +
            "            if (array[i] === value) {\n" +
            "                return true;\n" +
            "            }\n" +
            "        }\n" +
            "        return false;\n" +
            "    };\n" +
            "\n" +
            "    // The action parameter is a string which is the name of the setter function\n" +
            "    // which called this function\n" +
            "    // (in other words, setExpandPropeties, setOrientationProperties, or\n" +
            "    // setResizeProperties).\n" +
            "    // It serves both as the key to get the the appropriate set of validating\n" +
            "    // functions from the allValidators object\n" +
            "    // as well as the action parameter of any error event that may be thrown.\n" +
            "    function validate(properties, action) {\n" +
            "        var retval = true;\n" +
            "        var validators = allValidators[action];\n" +
            "        for (var prop in properties) {\n" +
            "            var validator = validators[prop];\n" +
            "            var value = properties[prop];\n" +
            "            if (validator && !validator(value)) {\n" +
            "                mraid.fireErrorEvent(\"Value of property \" + prop + \" (\" + value\t+ \") is invalid\", \"mraid.\" + action);\n" +
            "                retval = false;\n" +
            "            }\n" +
            "        }\n" +
            "        return retval;\n" +
            "    };\n" +
            "\n" +
            "    var allValidators = {\n" +
            "        \"setExpandProperties\" : {\n" +
            "            // In MRAID 3.0, the only property in expandProperties we actually care about is useCustomClose.\n" +
            "            // Still, we'll do a basic sanity check on the width and height properties, too.\n" +
            "            \"width\" : function(width) {\n" +
            "                return !isNaN(width);\n" +
            "            },\n" +
            "            \"height\" : function(height) {\n" +
            "                return !isNaN(height);\n" +
            "            },\n" +
            "            \"useCustomClose\" : function(useCustomClose) {\n" +
            "                return (typeof useCustomClose === \"boolean\");\n" +
            "            }\n" +
            "        },\n" +
            "        \"setOrientationProperties\" : {\n" +
            "            \"allowOrientationChange\" : function(allowOrientationChange) {\n" +
            "                return (typeof allowOrientationChange === \"boolean\");\n" +
            "            },\n" +
            "            \"forceOrientation\" : function(forceOrientation) {\n" +
            "                var validValues = [ \"portrait\", \"landscape\", \"none\" ];\n" +
            "                return (typeof forceOrientation === \"string\" && validValues.indexOf(forceOrientation) !== -1);\n" +
            "            }\n" +
            "        },\n" +
            "        \"setResizeProperties\" : {\n" +
            "            \"width\" : function(width) {\n" +
            "                return !isNaN(width) && 50 <= width;\n" +
            "            },\n" +
            "            \"height\" : function(height) {\n" +
            "                return !isNaN(height) && 50 <= height;\n" +
            "            },\n" +
            "            \"offsetX\" : function(offsetX) {\n" +
            "                return !isNaN(offsetX);\n" +
            "            },\n" +
            "            \"offsetY\" : function(offsetY) {\n" +
            "                return !isNaN(offsetY);\n" +
            "            },\n" +
            "            \"customClosePosition\" : function(customClosePosition) {\n" +
            "                var validPositions = [ \"top-left\", \"top-center\", \"top-right\",\n" +
            "                    \"center\",\n" +
            "                    \"bottom-left\", \"bottom-center\",\t\"bottom-right\" ];\n" +
            "                return (typeof customClosePosition === \"string\" && validPositions.indexOf(customClosePosition) !== -1);\n" +
            "            },\n" +
            "            \"allowOffscreen\" : function(allowOffscreen) {\n" +
            "                return (typeof allowOffscreen === \"boolean\");\n" +
            "            }\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * VPAID\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    var vpaid = window.vpaid = {};\n" +
            "\n" +
            "    vpaid.fireEvent = function(event) {\n" +
            "        log.i(\"vpaid event: \" + event);\n" +
            "        callNative(event);\n" +
            "    };\n" +
            "\n" +
            "    vpaid.AdClickThru = function(event, url, id, playerHandles) {\n" +
            "        log.i(\"vpaid event: AdClickThru (\" + url + \")\");\n" +
            "        callNative(\"AdClickThru?url=\" + encodeURIComponent(url) + \"&id=\" + id + \"&playerHandles=\" + playerHandles);\n" +
            "    };\n" +
            "\n" +
            "    vpaid.AdError = function(event, str) {\n" +
            "        log.i(\"vpaid event: AdError ('\" + str + \"')\");\n" +
            "        callNative(\"AdError?msg=\" + str);\n" +
            "    };\n" +
            "\n" +
            "    var vpaidCallbacks = {\n" +
            "        AdClickThru : vpaid.AdClickThru,\n" +
            "        AdError : vpaid.AdError,\n" +
            "        AdImpression : vpaid.fireEvent,\n" +
            "        AdPaused : vpaid.fireEvent,\n" +
            "        AdPlaying : vpaid.fireEvent,\n" +
            "        AdVideoComplete : vpaid.fireEvent,\n" +
            "        AdVideoFirstQuartile : vpaid.fireEvent,\n" +
            "        AdVideoMidpoint : vpaid.fireEvent,\n" +
            "        AdVideoStart : vpaid.fireEvent,\n" +
            "        AdVideoThirdQuartile : vpaid.fireEvent\n" +
            "    };\n" +
            "\n" +
            "    mraid.initVpaid = function(vpaidObject) {\n" +
            "        if(typeof vpaidObject !== 'undefined') {\n" +
            "            vpaid.creative = vpaidObject;\n" +
            "            log.i(\"vpaid initVpaid\");\n" +
            "            for (var eventName in vpaidCallbacks) {\n" +
            "                vpaidObject.subscribe(vpaidCallbacks[eventName], eventName);\n" +
            "            }\n" +
            "            var duration = vpaidObject.getAdDuration();\n" +
            "            callNative(\"AdDuration?time=\" + duration);\n" +
            "            vpaidObject.startAd();\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    console.log(\"Current environment: \" + JSON.stringify(window.MRAID_ENV));\n" +
            "\n" +
            "    console.log(\"MRAID object loaded\");\n" +
            "\n" +
            "})();";
}
