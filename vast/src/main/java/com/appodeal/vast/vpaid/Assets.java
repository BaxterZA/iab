package com.appodeal.vast.vpaid;


class Assets {
    static final String vpaidJs = "(function() {\n" +
            "\n" +
            "    console.log(\"VPAID object loading...\");\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * console logging helper\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "    var LogLevelEnum = {\n" +
            "        \"DEBUG\": 0,\n" +
            "        \"INFO\": 1,\n" +
            "        \"WARNING\": 2,\n" +
            "        \"ERROR\": 3,\n" +
            "        \"NONE\": 4\n" +
            "    };\n" +
            "\n" +
            "    var logLevel = LogLevelEnum.DEBUG;\n" +
            "    var log = {};\n" +
            "\n" +
            "    log.d = function (msg) {\n" +
            "        if (logLevel <= LogLevelEnum.DEBUG) {\n" +
            "            console.log(\"(D-vpaid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    log.i = function (msg) {\n" +
            "        if (logLevel <= LogLevelEnum.INFO) {\n" +
            "            console.log(\"(I-vpaid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    log.w = function (msg) {\n" +
            "        if (logLevel <= LogLevelEnum.WARNING) {\n" +
            "            console.log(\"(W-vpaid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    log.e = function (msg) {\n" +
            "        if (logLevel <= LogLevelEnum.ERROR) {\n" +
            "            console.log(\"(E-vpaid.js) \" + msg);\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    var vpaid = window.vpaid = {};\n" +
            "\n" +
            "    var VERSION = \"2.0\";\n" +
            "\n" +
            "    /***************************************************************************\n" +
            "     * helper methods called by SDK\n" +
            "     **************************************************************************/\n" +
            "\n" +
            "        // setters to change state\n" +
            "    vpaid.setVolume = function(value) {\n" +
            "        log.i(\"vpaid.setVolume \" + value);\n" +
            "\n" +
            "        vpaid.VPAIDCreative.setAdVolume(value);\n" +
            "    };\n" +
            "\n" +
            "\n" +
            "    vpaid.checkVPAIDInterface = function (VPAIDCreative) {\n" +
            "        if (\n" +
            "            VPAIDCreative.handshakeVersion && typeof VPAIDCreative.handshakeVersion == \"function\" &&\n" +
            "            VPAIDCreative.initAd && typeof VPAIDCreative.initAd == \"function\" &&\n" +
            "            VPAIDCreative.startAd && typeof VPAIDCreative.startAd == \"function\" &&\n" +
            "            VPAIDCreative.stopAd && typeof VPAIDCreative.stopAd == \"function\" &&\n" +
            "            VPAIDCreative.skipAd && typeof VPAIDCreative.skipAd == \"function\" &&\n" +
            "            VPAIDCreative.resizeAd && typeof VPAIDCreative.resizeAd == \"function\" &&\n" +
            "            VPAIDCreative.pauseAd && typeof VPAIDCreative.pauseAd == \"function\" &&\n" +
            "            VPAIDCreative.resumeAd && typeof VPAIDCreative.resumeAd == \"function\" &&\n" +
            "            VPAIDCreative.expandAd && typeof VPAIDCreative.expandAd == \"function\" &&\n" +
            "            VPAIDCreative.collapseAd && typeof VPAIDCreative.collapseAd == \"function\" &&\n" +
            "            VPAIDCreative.subscribe && typeof VPAIDCreative.subscribe == \"function\" &&\n" +
            "            VPAIDCreative.unsubscribe && typeof VPAIDCreative.unsubscribe == \"function\") {\n" +
            "            return true;\n" +
            "        }\n" +
            "        return false;\n" +
            "    };\n" +
            "\n" +
            "    function callNative(command) {\n" +
            "        var iframe = document.createElement(\"IFRAME\");\n" +
            "        iframe.setAttribute(\"src\", \"vpaid://\" + command);\n" +
            "        document.documentElement.appendChild(iframe);\n" +
            "        iframe.parentNode.removeChild(iframe);\n" +
            "        iframe = null;\n" +
            "    }\n" +
            "\n" +
            "    vpaid.AdStarted = function() {\n" +
            "        log.i(\"vpaid event: AdStarted\");\n" +
            "        callNative(\"AdStarted\");\n" +
            "    };\n" +
            "    vpaid.AdStopped = function() {\n" +
            "        log.i(\"vpaid event: AdStopped\");\n" +
            "        callNative(\"AdStopped\");\n" +
            "    };\n" +
            "    vpaid.AdSkipped = function() {\n" +
            "        log.i(\"vpaid event: AdSkipped\");\n" +
            "        callNative(\"AdSkipped\");\n" +
            "    };\n" +
            "    vpaid.AdLoaded = function() {\n" +
            "        log.i(\"vpaid event: AdLoaded\");\n" +
            "        callNative(\"AdLoaded\");\n" +
            "    };\n" +
            "    vpaid.AdLinearChange = function() {\n" +
            "        log.i(\"vpaid event: AdLinearChange\");\n" +
            "        callNative(\"AdLinearChange\");\n" +
            "    };\n" +
            "    vpaid.AdSizeChange = function() {\n" +
            "        log.i(\"vpaid event: AdSizeChange\");\n" +
            "        callNative(\"AdSizeChange\");\n" +
            "    };\n" +
            "    vpaid.AdExpandedChange = function() {\n" +
            "        log.i(\"vpaid event: AdExpandedChange\");\n" +
            "        callNative(\"AdExpandedChange\");\n" +
            "    };\n" +
            "    vpaid.AdSkippableStateChange = function() {\n" +
            "        var state = vpaid.VPAIDCreative.getAdSkippableState();\n" +
            "        log.i(\"vpaid event: AdSkippableStateChange (\" + state + \")\");\n" +
            "        callNative(\"AdSkippableStateChange?state=\" + state);\n" +
            "    };\n" +
            "    vpaid.AdDurationChange = function() {\n" +
            "        var state = vpaid.VPAIDCreative.getAdDuration();\n" +
            "        log.i(\"vpaid event: AdDurationChange (\" + state + \")\");\n" +
            "        callNative(\"AdDurationChange?state=\" + state);\n" +
            "    };\n" +
            "    vpaid.AdVolumeChange = function() {\n" +
            "        var state = vpaid.VPAIDCreative.getAdVolume();\n" +
            "        log.i(\"vpaid event: AdVolumeChange (\" + state + \")\");\n" +
            "        callNative(\"AdVolumeChange?state=\" + state);\n" +
            "    };\n" +
            "    vpaid.AdImpression = function() {\n" +
            "        log.i(\"vpaid event: AdImpression\");\n" +
            "        callNative(\"AdImpression\");\n" +
            "    };\n" +
            "    vpaid.AdClickThru = function(url, id, playerHandles) {\n" +
            "        log.i(\"vpaid event: AdClickThru (\" + url + \")\");\n" +
            "        callNative(\"AdClickThru?url=\" + encodeURIComponent(url));\n" +
            "    };\n" +
            "    vpaid.AdInteraction = function(id) {\n" +
            "        log.i(\"vpaid event: AdInteraction\");\n" +
            "        callNative(\"AdInteraction\");\n" +
            "    };\n" +
            "    vpaid.AdVideoStart = function() {\n" +
            "        log.i(\"vpaid event: AdVideoStart\");\n" +
            "        callNative(\"AdVideoStart\");\n" +
            "    };\n" +
            "    vpaid.AdVideoFirstQuartile = function() {\n" +
            "        log.i(\"vpaid event: AdVideoFirstQuartile\");\n" +
            "        callNative(\"AdVideoFirstQuartile\");\n" +
            "    };\n" +
            "    vpaid.AdVideoMidpoint = function() {\n" +
            "        log.i(\"vpaid event: AdVideoMidpoint\");\n" +
            "        callNative(\"AdVideoMidpoint\");\n" +
            "    };\n" +
            "    vpaid.AdVideoThirdQuartile = function() {\n" +
            "        log.i(\"vpaid event: AdVideoThirdQuartile\");\n" +
            "        callNative(\"AdVideoThirdQuartile\");\n" +
            "    };\n" +
            "    vpaid.AdVideoComplete = function() {\n" +
            "        log.i(\"vpaid event: AdVideoComplete\");\n" +
            "        callNative(\"AdVideoComplete\");\n" +
            "    };\n" +
            "    vpaid.AdUserAcceptInvitation = function() {\n" +
            "        log.i(\"vpaid event: AdUserAcceptInvitation\");\n" +
            "        callNative(\"AdUserAcceptInvitation\");\n" +
            "    };\n" +
            "    vpaid.AdUserMinimize = function() {\n" +
            "        log.i(\"vpaid event: AdUserMinimize\");\n" +
            "        callNative(\"AdUserMinimize\");\n" +
            "    };\n" +
            "    vpaid.AdUserClose = function() {\n" +
            "        log.i(\"vpaid event: AdUserClose\");\n" +
            "        callNative(\"AdUserClose\");\n" +
            "    };\n" +
            "    vpaid.AdPaused = function() {\n" +
            "        log.i(\"vpaid event: AdPaused\");\n" +
            "        callNative(\"AdPaused\");\n" +
            "    };\n" +
            "    vpaid.AdPlaying = function() {\n" +
            "        log.i(\"vpaid event: AdPlaying\");\n" +
            "        callNative(\"AdPlaying\");\n" +
            "    };\n" +
            "    vpaid.AdError = function(str) {\n" +
            "        log.i(\"vpaid event: AdError (\" + str + \")\");\n" +
            "        callNative(\"AdError?msg=\" + str);\n" +
            "    };\n" +
            "    vpaid.AdLog = function(str) {\n" +
            "        log.i(\"vpaid event: AdLog (\" + str + \")\");\n" +
            "        callNative(\"AdLog?msg=\" + str);\n" +
            "    };\n" +
            "    vpaid.vpaidCallbacks = {\n" +
            "        AdStarted : vpaid.AdStarted,\n" +
            "        AdStopped : vpaid.AdStopped,\n" +
            "        AdSkipped : vpaid.AdSkipped,\n" +
            "        AdLoaded : vpaid.AdLoaded,\n" +
            "        AdLinearChange : vpaid.AdLinearChange,\n" +
            "        AdSizeChange : vpaid.AdSizeChange,\n" +
            "        AdExpandedChange : vpaid.AdExpandedChange,\n" +
            "        AdSkippableStateChange : vpaid.AdSkippableStateChange,\n" +
            "        AdDurationChange : vpaid.AdDurationChange,\n" +
            "        AdVolumeChange : vpaid.AdVolumeChange,\n" +
            "        AdImpression : vpaid.AdImpression,\n" +
            "        AdClickThru : vpaid.AdClickThru,\n" +
            "        AdInteraction : vpaid.AdInteraction,\n" +
            "        AdVideoStart : vpaid.AdVideoStart,\n" +
            "        AdVideoFirstQuartile : vpaid.AdVideoFirstQuartile,\n" +
            "        AdVideoMidpoint : vpaid.AdVideoMidpoint,\n" +
            "        AdVideoThirdQuartile : vpaid.AdVideoThirdQuartile,\n" +
            "        AdVideoComplete : vpaid.AdVideoComplete,\n" +
            "        AdUserAcceptInvitation : vpaid.AdUserAcceptInvitation,\n" +
            "        AdUserMinimize : vpaid.AdUserMinimize,\n" +
            "        AdUserClose : vpaid.AdUserClose,\n" +
            "        AdPaused : vpaid.AdPaused,\n" +
            "        AdPlaying : vpaid.AdPlaying,\n" +
            "        AdError : vpaid.AdError,\n" +
            "        AdLog : vpaid.AdLog\n" +
            "    };\n" +
            "\n" +
            "    vpaid.fireStartAdEvent = function() {\n" +
            "        vpaid.VPAIDCreative.startAd();\n" +
            "    };\n" +
            "\n" +
            "    vpaid.getAdRemainingTime = function() {\n" +
            "        var time = vpaid.VPAIDCreative.getAdRemainingTime();\n" +
            "        log.i(\"vpaid event: AdRemainingTime (\" + time + \")\");\n" +
            "        callNative(\"AdRemainingTime?time=\" + time);\n" +
            "    };\n" +
            "\n" +
            "    vpaid.getAdDuration = function() {\n" +
            "        var state = vpaid.VPAIDCreative.getAdDuration();\n" +
            "        log.i(\"vpaid event: AdDurationChange (\" + state + \")\");\n" +
            "        callNative(\"AdDurationChange?state=\" + state);\n" +
            "    };\n" +
            "\n" +
            "    vpaid.fireAdPauseEvent = function() {\n" +
            "        vpaid.VPAIDCreative.pauseAd()\n" +
            "    };\n" +
            "\n" +
            "    vpaid.fireAdResumeEvent = function() {\n" +
            "        vpaid.VPAIDCreative.resumeAd()\n" +
            "    };\n" +
            "\n" +
            "    vpaid.creativeData = {};\n" +
            "\n" +
            "    vpaid.setCreativeData = function(data) {\n" +
            "        vpaid.creativeData = data;\n" +
            "    };\n" +
            "\n" +
            "// RUN\n" +
            "\n" +
            "    vpaid.loadAd = function() {\n" +
            "        var fn = window['getVPAIDAd'];\n" +
            "        if (fn && typeof fn == 'function') {\n" +
            "            vpaid.VPAIDCreative = fn();\n" +
            "            log.i(\"VPAIDCreative found\");\n" +
            "            if (vpaid.checkVPAIDInterface(vpaid.VPAIDCreative)) {\n" +
            "                log.i(\"VPAIDInterface checked\");\n" +
            "                if (vpaid.VPAIDCreative.handshakeVersion(VERSION) == VERSION) {\n" +
            "                    log.i(\"handshakeVersion done\");\n" +
            "\n" +
            "                    for (var eventName in vpaid.vpaidCallbacks) {\n" +
            "                        vpaid.VPAIDCreative.subscribe(vpaid.vpaidCallbacks[eventName], eventName, vpaid);\n" +
            "                    }\n" +
            "                    log.i(\"subscribe done\");\n" +
            "\n" +
            "                    environmentVars = {};\n" +
            "                    environmentVars.slot = document.getElementById('videoAdLayer');\n" +
            "                    environmentVars.videoSlot = document.getElementById('videoElement');\n" +
            "                    environmentVars.videoSlotCanAutoPlay = true;\n" +
            "                    environmentVars.https = 0;\n" +
            "                    environmentVars.autoplay = true;\n" +
            "\n" +
            "                    viewMode = \"fullscreen\";\n" +
            "                    desiredBitrate = 300;\n" +
            "\n" +
            "                    vpaid.VPAIDCreative.initAd(screen.width, screen.height, viewMode, desiredBitrate, JSON.stringify(vpaid.creativeData), environmentVars);\n" +
            "\n" +
            "                } else {\n" +
            "                    log.e(\"VPAID version not supported\");\n" +
            "                    callNative(\"AdError?msg=VPAID version not supported\");\n" +
            "                }\n" +
            "            } else {\n" +
            "                log.e(\"bad VPAIDInterface\");\n" +
            "                callNative(\"AdError?msg=bad VPAIDInterface\");\n" +
            "            }\n" +
            "        } else {\n" +
            "            log.e(\"getVPAIDAd is not a function\");\n" +
            "            callNative(\"AdError?msg=getVPAIDAd is not a function\");\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    vpaid.resizeAd = function() {\n" +
            "        vpaid.VPAIDCreative.resizeAd(window.innerWidth, window.innerHeight, \"fullscreen\");\n" +
            "    };\n" +
            "\n" +
            "    console.log(\"VPAID object loaded\");\n" +
            "})();";
}
