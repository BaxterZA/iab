package com.appodeal.vast;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

class VastModel {
    private transient Document vastsDocument;

    // Tracking xpath expressions
    private static final String inlineLinearTrackingXPATH = "/VASTS/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String inlineNonLinearTrackingXPATH = "/VASTS/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String wrapperLinearTrackingXPATH = "/VASTS/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String wrapperNonLinearTrackingXPATH = "/VASTS/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String inlineCompanionsXPATH = "/VASTS/VAST/Ad/InLine/Creatives/Creative/CompanionAds/Companion";
    private static final String wrapperCompanionsXPATH = "/VASTS/VAST/Ad/Wrapper/Creatives/Creative/CompanionAds/Companion";
    private static final String combinedCompanionsXPATH = inlineCompanionsXPATH
            + "|"
            + wrapperCompanionsXPATH;

    private static final String wrapperExtensionPATH = "/VASTS/VAST/Ad/Wrapper/Extensions/Extension";
    private static final String extensionPATH = "/VASTS/VAST/Ad/InLine/Extensions/Extension";
    private static final String combinedExtensionPATH = wrapperExtensionPATH
            + "|"
            + extensionPATH;

    private static final String combinedTrackingXPATH = inlineLinearTrackingXPATH
            + "|"
            + inlineNonLinearTrackingXPATH
            + "|"
            + wrapperLinearTrackingXPATH + "|" + wrapperNonLinearTrackingXPATH;

    // Mediafile xpath expression
    private static final String mediaFileXPATH = "//MediaFile";

    // Mediafile xpath expression
    private static final String interactiveCreativeFileXPATH = "//InteractiveCreativeFile";

    // Duration xpath expression
    private static final String durationXPATH = "//Duration";

    // Duration xpath expression
    private static final String linearXPATH = "//Linear";

    // Videoclicks xpath expression
    private static final String videoClicksInlineXPATH = "/VASTS/VAST/Ad/InLine/Creatives/Creative/Linear/VideoClicks";
    private static final String videoClicksWrapperXPATH = "/VASTS/VAST/Ad/Wrapper/Creatives/Creative/Linear/VideoClicks";

    // Videoclicks xpath expression
    private static final String impressionXPATH = "//Impression";

    // Error url  xpath expression
    private static final String errorUrlXPATH = "//Error";

    // Duration xpath expression
    private static final String adParametersXPATH = "/VASTS/VAST/Ad/InLine/Creatives/Creative/Linear/AdParameters";

    private static final String inlineViewableImpressionViewableXPATH = "/VASTS/VAST/Ad/InLine/ViewableImpression/Viewable";
    private static final String wrapperViewableImpressionViewableXPATH = "/VASTS/VAST/Ad/Wrapper/ViewableImpression/Viewable";
    private static final String combinedViewableImpressionViewableXPATH = inlineViewableImpressionViewableXPATH + "|" + wrapperViewableImpressionViewableXPATH;

    private static final String inlineViewableImpressionNotViewableXPATH = "/VASTS/VAST/Ad/InLine/ViewableImpression/NotViewable";
    private static final String wrapperViewableImpressionNotViewableXPATH = "/VASTS/VAST/Ad/Wrapper/ViewableImpression/NotViewable";
    private static final String combinedViewableImpressionNotViewableXPATH = inlineViewableImpressionNotViewableXPATH + "|" + wrapperViewableImpressionNotViewableXPATH;

    private static final String inlineViewableImpressionViewUndeterminedXPATH = "/VASTS/VAST/Ad/InLine/ViewableImpression/ViewUndetermined";
    private static final String wrapperViewableImpressionViewUndeterminedXPATH = "/VASTS/VAST/Ad/Wrapper/ViewableImpression/ViewUndetermined";
    private static final String combinedViewableImpressionViewUndeterminedXPATH = inlineViewableImpressionViewUndeterminedXPATH + "|" + wrapperViewableImpressionViewUndeterminedXPATH;

    private static final String iconsXPATH = "//Icons/Icon";

    private static final String EXTENSION_NAME_CTA_TEXT = "CtaText";
    private static final String EXTENSION_NAME_SHOW_CTA_WIDGET = "ShowCta";
    private static final String EXTENSION_NAME_SHOW_MUTE_WIDGET = "ShowMute";
    private static final String EXTENSION_NAME_SHOW_COMPANION_AFTER_VIDEO = "ShowCompanion";
    private static final String EXTENSION_NAME_COMPANION_CLOSE_TIME = "CompanionCloseTime";
    private static final String EXTENSION_NAME_VIDEO_CLICKABLE = "VideoClickable";
    private static final String EXTENSION_NAME_CTA_XPOSITION = "CtaXPosition";
    private static final String EXTENSION_NAME_CTA_YPOSITION = "CtaYPosition";
    private static final String EXTENSION_NAME_CLOSE_XPOSITION = "CloseXPosition";
    private static final String EXTENSION_NAME_CLOSE_YPOSITION = "CloseYPosition";
    private static final String EXTENSION_NAME_MUTE_XPOSITION = "MuteXPosition";
    private static final String EXTENSION_NAME_MUTE_YPOSITION = "MuteYPosition";
    private static final String EXTENSION_NAME_ASSETS_COLOR = "AssetsColor";
    private static final String EXTENSION_NAME_ASSETS_BACKGROUND_COLOR = "AssetsBackgroundColor";
    private static final String EXTENSION_NAME_COMPANION = "Companion";
    private static final String EXTENSION_NAME_SHOW_PROGRESSBAR = "ShowProgress";

    VastModel(Document vasts) {
        this.vastsDocument = vasts;
    }

    HashMap<TrackingEventsType, List<String>> getTrackingUrls() {
        VastLog.d("getTrackingUrls");
        List<String> tracking;
        HashMap<TrackingEventsType, List<String>> trackingMap = new HashMap<>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedTrackingXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;
            String trackingURL;
            String eventName;
            TrackingEventsType key;

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    NamedNodeMap attributes = node.getAttributes();

                    eventName = (attributes.getNamedItem("event")).getNodeValue();
                    try {
                        key = TrackingEventsType.valueOf(eventName);
                    } catch (IllegalArgumentException e) {
                        VastLog.d(String.format("Event: %s is not valid. Skipping it.", eventName));
                        continue;
                    }

                    trackingURL = VastTools.getElementValue(node);

                    if (!TextUtils.isEmpty(trackingURL)) {
                        if (trackingMap.containsKey(key)) {
                            tracking = trackingMap.get(key);
                            tracking.add(trackingURL);
                        } else {
                            tracking = new ArrayList<>();
                            tracking.add(trackingURL);
                            trackingMap.put(key, tracking);
                        }
                    }

                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return trackingMap;
    }

    List<ProgressEvent> getProgressTracking() {
        VastLog.d("getTrackingUrls");
        List<ProgressEvent> trackingList = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedTrackingXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;
            String trackingURL;
            String eventName;

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    NamedNodeMap attributes = node.getAttributes();

                    eventName = (attributes.getNamedItem("event")).getNodeValue();
                    if (eventName.equals("progress")) {
                        trackingURL = VastTools.getElementValue(node);
                        Node offsetNode = node.getAttributes().getNamedItem("offset");
                        if (offsetNode != null) {
                            String offset = offsetNode.getNodeValue();
                            if (offset != null) {
                                if (offset.endsWith("%")) {
                                    if (getDuration() > 0) {
                                        int offsetTime = Math.round(Float.parseFloat(offset.replace("%", "")) / 100f * getDuration());
                                        trackingList.add(new ProgressEvent(offsetTime, trackingURL));
                                    }
                                } else {
                                    int offsetTime = VastTools.getMillsFromTimeString(offset);
                                    trackingList.add(new ProgressEvent(offsetTime, trackingURL));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return trackingList;
    }

    List<MediaFile> getMediaFiles() {
        VastLog.d("getMediaFiles");
        ArrayList<MediaFile> mediaFiles = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(mediaFileXPATH, vastsDocument, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    NamedNodeMap attributes = node.getAttributes();

                    String mediaURL = VastTools.getElementValue(node);
                    if (!TextUtils.isEmpty(mediaURL)) {
                        MediaFile.Builder mediaFileBuilder = new MediaFile.Builder(mediaURL);

                        Node attributeNode = attributes.getNamedItem("apiFramework");
                        if (attributeNode != null) {
                            mediaFileBuilder.setApiFramework(attributeNode.getNodeValue());
                        }

                        attributeNode = attributes.getNamedItem("bitrate");
                        if (attributeNode != null) {
                            mediaFileBuilder.setBitrate(Integer.valueOf(attributeNode.getNodeValue()));
                        }

                        attributeNode = attributes.getNamedItem("delivery");
                        if (attributeNode != null) {
                            mediaFileBuilder.setDelivery(attributeNode.getNodeValue());
                        }

                        attributeNode = attributes.getNamedItem("height");
                        if (attributeNode != null) {
                            mediaFileBuilder.setHeight(Integer.valueOf(attributeNode.getNodeValue()));
                        }

                        attributeNode = attributes.getNamedItem("id");
                        if (attributeNode != null) {
                            mediaFileBuilder.setId(attributeNode.getNodeValue());
                        }

                        attributeNode = attributes.getNamedItem("maintainAspectRatio");
                        if (attributeNode != null) {
                            mediaFileBuilder.setMaintainAspectRatio(Boolean.valueOf(attributeNode.getNodeValue()));
                        }

                        attributeNode = attributes.getNamedItem("scalable");
                        if (attributeNode != null) {
                            mediaFileBuilder.setScalable(Boolean.valueOf(attributeNode.getNodeValue()));
                        }

                        attributeNode = attributes.getNamedItem("type");
                        if (attributeNode != null) {
                            mediaFileBuilder.setType(attributeNode.getNodeValue());
                        }

                        attributeNode = attributes.getNamedItem("width");
                        if (attributeNode != null) {
                            mediaFileBuilder.setWidth(Integer.valueOf(attributeNode.getNodeValue()));
                        }

                        mediaFiles.add(mediaFileBuilder.build());
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return mediaFiles;
    }

    int getDuration() {
        VastLog.d("getDuration");

        int duration = 0;

        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(durationXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    duration = VastTools.getMillsFromTimeString(VastTools.getElementValue(node));
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return duration;
    }

    List<Companion> getCompanions() {
        VastLog.d("getCompanions");
        List<Companion> companionList = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedCompanionsXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;
            if (nodes != null) {
                if (nodes.getLength() == 0) {
                    try {
                        sendError(Error.ERROR_CODE_COMPANION_NODE_NOT_FOUND);
                    } catch (Exception ex) {
                        VastLog.e(ex.getMessage());
                    }
                } else {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        node = nodes.item(i);
                        NamedNodeMap attributes = node.getAttributes();
                        Node heightNode = attributes.getNamedItem("height");
                        Node widthNode = attributes.getNamedItem("width");
                        if (heightNode != null && widthNode != null) {
                            Companion companion = createCompanionFromNode(node);
                            if (companion != null && companion.isValid()) {
                                companionList.add(companion);
                            }
                        }
                    }
                }
            } else {
                try {
                    sendError(Error.ERROR_CODE_COMPANION_NODE_NOT_FOUND);
                } catch (Exception ex) {
                    VastLog.e(ex.getMessage());
                }
            }

        } catch (Exception e) {
            VastLog.e(e.getMessage());
            try {
                sendError(Error.ERROR_CODE_COMPANION_NODE_NOT_FOUND);
            } catch (Exception ex) {
                VastLog.e(ex.getMessage());
            }
        }
        return companionList;
    }

    int getSkipOffset() {
        VastLog.d("getSkipOffset");
        int skipTime = 0;
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(linearXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    Node skipOffsetNode = node.getAttributes().getNamedItem("skipOffset");
                    if (skipOffsetNode != null) {
                        String skipOffset = skipOffsetNode.getNodeValue();
                        if (skipOffset != null) {
                            skipTime = VastTools.getMillsFromTimeString(skipOffset);
                        }
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
            return 0;
        }

        return skipTime;
    }

    VideoClicks getVideoClicks() {
        VastLog.d("getVideoClicks");
        VideoClicks videoClicks = new VideoClicks();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(videoClicksInlineXPATH, vastsDocument, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    NodeList childNodes = node.getChildNodes();
                    Node child;
                    String value;
                    for (int childIndex = 0; childIndex < childNodes .getLength(); childIndex++) {
                        child = childNodes.item(childIndex);
                        String nodeName = child.getNodeName();

                        if (nodeName.equalsIgnoreCase("ClickTracking")) {
                            value = VastTools.getElementValue(child);
                            if (!TextUtils.isEmpty(value)) {
                                videoClicks.addClickTracking(value);
                            }

                        } else if (nodeName.equalsIgnoreCase("ClickThrough")) {
                            value = VastTools.getElementValue(child);
                            if (!TextUtils.isEmpty(value)) {
                                videoClicks.setClickThrough(value);
                            }

                        } else if (nodeName.equalsIgnoreCase("CustomClick")) {
                            value = VastTools.getElementValue(child);
                            if (!TextUtils.isEmpty(value)) {
                                videoClicks.addCustomClick(value);
                            }
                        }
                    }
                }
            }

            nodes = (NodeList) xpath.evaluate(videoClicksWrapperXPATH, vastsDocument, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    NodeList childNodes = node.getChildNodes();
                    Node child;
                    String value;
                    for (int childIndex = 0; childIndex < childNodes .getLength(); childIndex++) {
                        child = childNodes.item(childIndex);
                        String nodeName = child.getNodeName();

                        if (nodeName.equalsIgnoreCase("ClickTracking")) {
                            value = VastTools.getElementValue(child);
                            if (!TextUtils.isEmpty(value)) {
                                videoClicks.addClickTracking(value);
                            }

                        } else if (nodeName.equalsIgnoreCase("CustomClick")) {
                            value = VastTools.getElementValue(child);
                            if (!TextUtils.isEmpty(value)) {
                                videoClicks.addCustomClick(value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return videoClicks;
    }

    List<String> getImpressions() {
        VastLog.d("getImpressions");
        return getListFromXPath(impressionXPATH);
    }

    List<String> getErrorUrls() {
        VastLog.d("getErrorUrl");
        return getListFromXPath(errorUrlXPATH);
    }

    String getAdParameters() {
        VastLog.d("getAdParameters");
        String adParameters = "";
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(adParametersXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    adParameters = VastTools.getElementValue(node);
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return adParameters;
    }

    List<MediaFile> getInteractiveCreativeFiles() {
        VastLog.d("getInteractiveCreativeFile");
        List<MediaFile> list = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(interactiveCreativeFileXPATH, vastsDocument, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node child = nodes.item(i);
                    if (child != null) {
                        String url = VastTools.getElementValue(child);
                        if (!TextUtils.isEmpty(url)) {
                            MediaFile.Builder mediaFileBuilder = new MediaFile.Builder(url);
                            NamedNodeMap attributes = child.getAttributes();
                            if (attributes != null) {
                                Node typeNode = attributes.getNamedItem("type");
                                Node apiFramework = attributes.getNamedItem("apiFramework");
                                if (typeNode != null) {
                                    mediaFileBuilder.setType(typeNode.getNodeValue());
                                }
                                if (apiFramework != null) {
                                    mediaFileBuilder.setApiFramework(apiFramework.getNodeValue());
                                }
                            }
                            list.add(mediaFileBuilder.build());
                        }
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return list;
    }

    @Nullable
    Extensions getExtensions() {
        VastLog.d("getExtensions");
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedExtensionPATH, vastsDocument, XPathConstants.NODESET);
            if (nodes != null && nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); ++i) {
                    Node child = nodes.item(i);
                    if (child != null) {
                        NamedNodeMap attributes = child.getAttributes();
                        if (attributes != null) {
                            Node typeNode = attributes.getNamedItem("type");
                            if (typeNode != null && typeNode.getNodeValue().equals("appodeal")) {
                                Extensions.Builder extensionsBuilder = new Extensions.Builder();
                                NodeList extensions = child.getChildNodes();
                                if (extensions != null) {
                                    for (int j = 0; j < extensions.getLength(); ++j) {
                                        Node extensionNode = extensions.item(j);
                                        String name = extensionNode.getNodeName();
                                        if (name.equals(EXTENSION_NAME_COMPANION)) {
                                            Companion companion = createCompanionFromNode(extensionNode);
                                            if (companion != null && companion.isValid()) {
                                                extensionsBuilder.setCompanion(companion);
                                            }
                                        } else {
                                            String value = VastTools.getElementValue(extensionNode);
                                            switch (name) {
                                                case EXTENSION_NAME_CTA_TEXT:
                                                    extensionsBuilder.setCtaText(value);
                                                    break;
                                                case EXTENSION_NAME_SHOW_CTA_WIDGET:
                                                    extensionsBuilder.setShowCta(VastTools.parseBoolean(value));
                                                    break;
                                                case EXTENSION_NAME_SHOW_MUTE_WIDGET:
                                                    extensionsBuilder.setShowMute(VastTools.parseBoolean(value));
                                                    break;
                                                case EXTENSION_NAME_SHOW_COMPANION_AFTER_VIDEO:
                                                    extensionsBuilder.setShowCompanion(VastTools.parseBoolean(value));
                                                    break;
                                                case EXTENSION_NAME_COMPANION_CLOSE_TIME:
                                                    extensionsBuilder.setCompanionCloseTime(VastTools.getMillsFromTimeString(value));
                                                    break;
                                                case EXTENSION_NAME_VIDEO_CLICKABLE:
                                                    extensionsBuilder.setVideoClickable(VastTools.parseBoolean(value));
                                                    break;
                                                case EXTENSION_NAME_SHOW_PROGRESSBAR:
                                                    extensionsBuilder.setShowProgress(VastTools.parseBoolean(value));
                                                    break;
                                                case EXTENSION_NAME_ASSETS_COLOR:
                                                    try {
                                                        extensionsBuilder.setAssetsColor(Color.parseColor(value));
                                                    } catch (Exception e) {
                                                        VastLog.e(e.getMessage());
                                                    }
                                                    break;
                                                case EXTENSION_NAME_ASSETS_BACKGROUND_COLOR:
                                                    try {
                                                        extensionsBuilder.setAssetsBackgroundColor(Color.parseColor(value));
                                                    } catch (Exception e) {
                                                        VastLog.e(e.getMessage());
                                                    }
                                                    break;
                                                case EXTENSION_NAME_CTA_XPOSITION:
                                                    switch (value.toLowerCase()) {
                                                        case "left":
                                                            extensionsBuilder.setCtaXPosition(RelativeLayout.ALIGN_PARENT_LEFT);
                                                            break;
                                                        case "right":
                                                            extensionsBuilder.setCtaXPosition(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                            break;
                                                        case "center":
                                                            extensionsBuilder.setCtaXPosition(RelativeLayout.CENTER_HORIZONTAL);
                                                            break;
                                                    }
                                                    break;
                                                case EXTENSION_NAME_CTA_YPOSITION:
                                                    switch (value.toLowerCase()) {
                                                        case "top":
                                                            extensionsBuilder.setCtaYPosition(RelativeLayout.ALIGN_PARENT_TOP);
                                                            break;
                                                        case "bottom":
                                                            extensionsBuilder.setCtaYPosition(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                                            break;
                                                        case "center":
                                                            extensionsBuilder.setCtaYPosition(RelativeLayout.CENTER_VERTICAL);
                                                            break;
                                                    }
                                                    break;
                                                case EXTENSION_NAME_CLOSE_XPOSITION:
                                                    switch (value.toLowerCase()) {
                                                        case "left":
                                                            extensionsBuilder.setCloseXPosition(RelativeLayout.ALIGN_PARENT_LEFT);
                                                            break;
                                                        case "right":
                                                            extensionsBuilder.setCloseXPosition(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                            break;
                                                        case "center":
                                                            extensionsBuilder.setCloseXPosition(RelativeLayout.CENTER_HORIZONTAL);
                                                            break;
                                                    }
                                                    break;
                                                case EXTENSION_NAME_CLOSE_YPOSITION:
                                                    switch (value.toLowerCase()) {
                                                        case "top":
                                                            extensionsBuilder.setCloseYPosition(RelativeLayout.ALIGN_PARENT_TOP);
                                                            break;
                                                        case "bottom":
                                                            extensionsBuilder.setCloseYPosition(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                                            break;
                                                        case "center":
                                                            extensionsBuilder.setCloseYPosition(RelativeLayout.CENTER_VERTICAL);
                                                            break;
                                                    }
                                                    break;
                                                case EXTENSION_NAME_MUTE_XPOSITION:
                                                    switch (value.toLowerCase()) {
                                                        case "left":
                                                            extensionsBuilder.setMuteXPosition(RelativeLayout.ALIGN_PARENT_LEFT);
                                                            break;
                                                        case "right":
                                                            extensionsBuilder.setMuteXPosition(RelativeLayout.ALIGN_PARENT_RIGHT);
                                                            break;
                                                        case "center":
                                                            extensionsBuilder.setMuteXPosition(RelativeLayout.CENTER_HORIZONTAL);
                                                            break;
                                                    }
                                                    break;
                                                case EXTENSION_NAME_MUTE_YPOSITION:
                                                    switch (value.toLowerCase()) {
                                                        case "top":
                                                            extensionsBuilder.setMuteYPosition(RelativeLayout.ALIGN_PARENT_TOP);
                                                            break;
                                                        case "bottom":
                                                            extensionsBuilder.setMuteYPosition(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                                            break;
                                                        case "center":
                                                            extensionsBuilder.setMuteYPosition(RelativeLayout.CENTER_VERTICAL);
                                                            break;
                                                    }
                                                    break;
                                                default:
                                                    VastLog.d(String.format("Extension %s is not supported", name));
                                            }
                                        }
                                    }
                                    return extensionsBuilder.build();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return null;
    }

    void sendError(Error error) {
        List<String> errorUrls = getErrorUrls();
        if (errorUrls != null) {
            for (String url : errorUrls) {
                url = VastTools.replaceMacros(url, null, 0, error);
                VastLog.d("Fire error url:" + url);
                VastTools.fireUrl(url);
            }
        } else {
            VastLog.d("Error url list is null");
        }
    }

    List<String> getViewableViewableImpression() {
        VastLog.d("getViewableViewableImpression");
        List<String> viewableViewableImpressionList = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedViewableImpressionViewableXPATH,
                    vastsDocument, XPathConstants.NODESET);
            Node node;

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    String url = VastTools.getElementValue(node);
                    if (!TextUtils.isEmpty(url)) {
                        viewableViewableImpressionList.add(url);
                    }
                }
            }

        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return viewableViewableImpressionList;
    }

    List<String> getNotViewableViewableImpression() {
        VastLog.d("getNotViewableViewableImpression");
        List<String> notViewableViewableImpressionList = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedViewableImpressionNotViewableXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    String url = VastTools.getElementValue(node);
                    if (!TextUtils.isEmpty(url)) {
                        notViewableViewableImpressionList.add(url);
                    }
                }
            }

        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return notViewableViewableImpressionList;
    }

    List<String> getViewUndeterminedViewableImpression() {
        VastLog.d("getViewUndeterminedViewableImpression");
        List<String> viewUndeterminedViewableImpressionList = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodes = (NodeList) xpath.evaluate(combinedViewableImpressionViewUndeterminedXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    String url = VastTools.getElementValue(node);
                    if (!TextUtils.isEmpty(url)) {
                        viewUndeterminedViewableImpressionList.add(url);
                    }
                }
            }

        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return viewUndeterminedViewableImpressionList;
    }

    List<Icon> getIcons() {
        VastLog.d("getIcons");

        List<Icon> icons = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodes = (NodeList) xpath.evaluate(iconsXPATH, vastsDocument, XPathConstants.NODESET);
            Node node;
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    node = nodes.item(i);
                    NamedNodeMap iconAttributes = node.getAttributes();

                    Icon.Builder iconBuilder = new Icon.Builder();
                    List<String> iconViewTracking = new ArrayList<>();
                    IconClicks iconClicks = new IconClicks();
                    if (iconAttributes.getNamedItem("pxratio") != null) {
                        iconBuilder.setPxratio(Integer.valueOf(iconAttributes.getNamedItem("pxratio").getNodeValue()));
                    }
                    if (iconAttributes.getNamedItem("height") != null && iconAttributes.getNamedItem("width") != null) {
                        iconBuilder.setHeight(Integer.valueOf(iconAttributes.getNamedItem("height").getNodeValue()));
                        iconBuilder.setWidth(Integer.valueOf(iconAttributes.getNamedItem("width").getNodeValue()));
                    }
                    if (iconAttributes.getNamedItem("xPosition") != null && iconAttributes.getNamedItem("yPosition") != null) {
                        String xPositionString = iconAttributes.getNamedItem("xPosition").getNodeValue();
                        switch (xPositionString) {
                            case "left":
                                iconBuilder.setXPosition(0);
                                break;
                            case "right":
                                iconBuilder.setXPosition(Integer.MAX_VALUE);
                                break;
                            default:
                                iconBuilder.setXPosition(Integer.valueOf(xPositionString));
                                break;
                        }
                        String yPositionString = iconAttributes.getNamedItem("yPosition").getNodeValue();
                        switch (yPositionString) {
                            case "top":
                                iconBuilder.setYPosition(0);
                                break;
                            case "bottom":
                                iconBuilder.setYPosition(Integer.MAX_VALUE);
                                break;
                            default:
                                iconBuilder.setYPosition(Integer.valueOf(yPositionString));
                                break;
                        }
                    }
                    if (iconAttributes.getNamedItem("duration") != null) {
                        iconBuilder.setDuration(VastTools.getMillsFromTimeString(iconAttributes.getNamedItem("duration").getNodeValue()));
                    }
                    if (iconAttributes.getNamedItem("offset") != null) {
                        iconBuilder.setOffset(VastTools.getMillsFromTimeString(iconAttributes.getNamedItem("offset").getNodeValue()));
                    }

                    NodeList children = node.getChildNodes();
                    Node child;
                    if (children != null) {
                        for (int j = 0; j < children.getLength(); j++) {
                            child = children.item(j);
                            String nodeName = child.getNodeName();

                            if (nodeName.equalsIgnoreCase("StaticResource")) {
                                String creativeType = child.getAttributes().getNamedItem("creativeType").getNodeValue();
                                String url = VastTools.getElementValue(child);
                                if (VastTools.isStaticResourceTypeSupported(creativeType)) {
                                    iconBuilder.setStaticResource(new StaticResource(creativeType, url));
                                }
                            } else if (nodeName.equalsIgnoreCase("IFrameResource")) {
                                iconBuilder.setIFrameResource(new IFrameResource(VastTools.getElementValue(child)));
                            } else if (nodeName.equalsIgnoreCase("HTMLResource")) {
                                iconBuilder.setHtmlResource(new HtmlResource(VastTools.getElementValue(child)));
                            } else if (nodeName.equalsIgnoreCase("IconViewTracking")) {
                                iconViewTracking.add(VastTools.getElementValue(child));
                            } else if (nodeName.equalsIgnoreCase("IconClicks")) {
                                NodeList clickNodes = child.getChildNodes();
                                Node clickNode;
                                if (clickNodes != null) {
                                    for (int k = 0; k < clickNodes.getLength(); k++) {
                                        clickNode = clickNodes.item(k);
                                        String clickNodeName = clickNode.getNodeName();
                                        if (clickNodeName.equalsIgnoreCase("IconClickThrough")) {
                                            iconClicks.setClickThrough(VastTools.getElementValue(clickNode));
                                        } else if (clickNodeName.equalsIgnoreCase("IconClickTracking")) {
                                            iconClicks.addClickTracking(VastTools.getElementValue(clickNode));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    iconBuilder.setIconViewTracking(iconViewTracking);
                    iconBuilder.setIconClicks(iconClicks);
                    icons.add(iconBuilder.build());
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return icons;
    }

    @Nullable
    private Companion createCompanionFromNode(Node node) {
        try {
            NamedNodeMap companionAttributes = node.getAttributes();
            int height = Integer.valueOf(companionAttributes.getNamedItem("height").getNodeValue());
            int width = Integer.valueOf(companionAttributes.getNamedItem("width").getNodeValue());
            Companion.Builder companionBuilder = new Companion.Builder(width, height);

            List<String> clickTracking = new ArrayList<>();
            HashMap<TrackingEventsType, List<String>> trackingMap = new HashMap<>();

            NodeList childNodes = node.getChildNodes();
            Node child;
            if (childNodes != null) {
                for (int j = 0; j < childNodes.getLength(); j++) {
                    child = childNodes.item(j);
                    String nodeName = child.getNodeName();

                    if (nodeName.equalsIgnoreCase("StaticResource")) {
                        String creativeType = child.getAttributes().getNamedItem("creativeType").getNodeValue();
                        String url = VastTools.getElementValue(child);
                        if (VastTools.isStaticResourceTypeSupported(creativeType)) {
                            companionBuilder.setStaticResource(new StaticResource(creativeType, url));
                        }
                    } else if (nodeName.equalsIgnoreCase("IFrameResource")) {
                        companionBuilder.setIFrameResource(new IFrameResource(VastTools.getElementValue(child)));
                    } else if (nodeName.equalsIgnoreCase("HTMLResource")) {
                        companionBuilder.setHtmlResource(new HtmlResource(VastTools.getElementValue(child)));
                    } else if (nodeName.equalsIgnoreCase("CompanionClickThrough")) {
                        companionBuilder.setClickThrough(VastTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("CompanionClickTracking")) {
                        clickTracking.add(VastTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("AdParameters")) {
                        companionBuilder.setAdParameters(VastTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("TrackingEvents")) {
                        NodeList nodes = child.getChildNodes();
                        Node trackingNode;
                        TrackingEventsType key;
                        if (nodes != null) {
                            for (int i = 0; i < nodes.getLength(); i++) {
                                trackingNode = nodes.item(i);
                                if (trackingNode.getNodeName().equalsIgnoreCase("Tracking")) {
                                    NamedNodeMap attributes = trackingNode.getAttributes();

                                    String eventName = (attributes.getNamedItem("event")).getNodeValue();
                                    try {
                                        key = TrackingEventsType.valueOf(eventName);
                                    } catch (IllegalArgumentException e) {
                                        VastLog.d(String.format("Event: %s is not valid. Skipping it.", eventName));
                                        continue;
                                    }

                                    String trackingURL = VastTools.getElementValue(trackingNode);

                                    if (trackingMap.containsKey(key)) {
                                        List<String> tracking = trackingMap.get(key);
                                        tracking.add(trackingURL);
                                    } else {
                                        List<String> tracking = new ArrayList<>();
                                        tracking.add(trackingURL);
                                        trackingMap.put(key, tracking);
                                    }
                                }
                            }
                        }
                    }
                }
                companionBuilder.setClickTracking(clickTracking);
                companionBuilder.setTracking(trackingMap);
                return companionBuilder.build();
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }
        return null;
    }

    private List<String> getListFromXPath(String xPath) {
        VastLog.d("getListFromXPath");
        ArrayList<String> list = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            NodeList nodes = (NodeList) xpath.evaluate(xPath, vastsDocument, XPathConstants.NODESET);

            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String value = VastTools.getElementValue(node);
                    if (!TextUtils.isEmpty(value)) {
                        list.add(value);
                    }
                }
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        }

        return list;
    }
}
