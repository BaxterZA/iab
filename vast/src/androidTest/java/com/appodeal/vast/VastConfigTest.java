package com.appodeal.vast;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;
import android.widget.RelativeLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class VastConfigTest {

    @Test
    public void onlyWrapper() throws Exception {
        String xmlString = "<VASTS>" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <Wrapper>\n" +
                "   <VASTAdTagURI>wrapper_ad_tag_uri</VASTAdTagURI>\n" +
                "   <Impression>wrapper_impression</Impression>\n" +
                "   <Error>wrapper_error</Error>\n" +
                "   <ViewableImpression>\n" +
                "    <Viewable>wrapper_viewableImpression_viewable</Viewable>\n" +
                "    <NotViewable>wrapper_viewableImpression_notViewable</NotViewable>\n" +
                "    <ViewUndetermined>wrapper_viewableImpression_viewUndetermined</ViewUndetermined>\n" +
                "   </ViewableImpression>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <VideoClicks>\n" +
                "       <ClickTracking>wrapper_clickTracking</ClickTracking>\n" +
                "       <CustomClick>wrapper_customClick</CustomClick>\n" +
                "      </VideoClicks>\n" +
                "      <Icons>\n" +
                "       <Icon height=\"100\" width=\"200\" pxratio=\"2\" xPosition=\"left\" yPosition=\"top\" duration=\"00:00:05\" offset=\"00:00:05\">\n" +
                "        <IFrameResource><![CDATA[warapper_iframe_url]]></IFrameResource>\n" +
                "        <IconViewTracking>wrapper_iconViewTracking</IconViewTracking>\n" +
                "        <IconClicks>\n" +
                "         <IconClickThrough>wrapper_iconClickThrough</IconClickThrough>\n" +
                "         <IconClickTracking>wrapper_iconClickTracking</IconClickTracking>\n" +
                "        </IconClicks>\n" +
                "       </Icon>\n" +
                "      </Icons>\n" +
                "      <TrackingEvents>\n" +
                "       <Tracking event=\"start\">\n" +
                "        <![CDATA[wrapper_start]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"firstQuartile\">\n" +
                "        <![CDATA[wrapper_firstQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"midpoint\">\n" +
                "        <![CDATA[wrapper_midpoint]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"thirdQuartile\">\n" +
                "        <![CDATA[wrapper_thirdQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"complete\">\n" +
                "        <![CDATA[wrapper_complete]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"mute\">\n" +
                "        <![CDATA[wrapper_mute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"unmute\">\n" +
                "        <![CDATA[wrapper_unmute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"pause\">\n" +
                "        <![CDATA[wrapper_pause]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"resume\">\n" +
                "        <![CDATA[wrapper_resume]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"progress\" offset=\"00:00:10\">\n" +
                "        <![CDATA[wrapper_resume]]>\n" +
                "       </Tracking>\n" +
                "      </TrackingEvents>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "    <Creative>\n" +
                "     <CompanionAds>\n" +
                "      <Companion width=\"300\" height=\"250\">\n" +
                "       <StaticResource creativeType=\"image/jpeg\">wrapper_companion_url</StaticResource>\n" +
                "       <TrackingEvents>\n" +
                "        <Tracking event=\"creativeView\">wrapper_companion_creativeView</Tracking>\n" +
                "       </TrackingEvents>\n" +
                "       <CompanionClickThrough>wrapper_companion_click</CompanionClickThrough>\n" +
                "      </Companion>\n" +
                "     </CompanionAds>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "   <Extensions>\n" +
                "    <Extension type=\"appodeal\">\n" +
                "     <ShowCta>false</ShowCta>\n" +
                "     <ShowCompanion>false</ShowCompanion>\n" +
                "    </Extension>\n" +
                "   </Extensions>\n" +
                "  </Wrapper>\n" +
                " </Ad>\n" +
                "</VAST>" +
                "</VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);

        assertNotNull(vastModel);

        VastConfig vastConfig = new VastConfig(vastModel, (float) 16/9, "dir");

        assertNotNull(vastConfig);

        assertEquals(1, vastConfig.getImpressionTracking().size());
        assertTrue(vastConfig.getImpressionTracking().contains("wrapper_impression"));

        assertEquals(1, vastConfig.getErrorTracking().size());
        assertTrue(vastConfig.getErrorTracking().contains("wrapper_error"));

        assertEquals(1, vastConfig.getViewableViewableImpressions().size());
        assertTrue(vastConfig.getViewableViewableImpressions().contains("wrapper_viewableImpression_viewable"));

        assertEquals(1, vastConfig.getNotViewableViewableImpression().size());
        assertTrue(vastConfig.getNotViewableViewableImpression().contains("wrapper_viewableImpression_notViewable"));

        assertEquals(1, vastConfig.getViewUndeterminedViewableImpression().size());
        assertTrue(vastConfig.getViewUndeterminedViewableImpression().contains("wrapper_viewableImpression_viewUndetermined"));

        VideoClicks videoClicks = vastConfig.getVideoClicks();
        assertNotNull(videoClicks);
        assertTrue(videoClicks.getClickTracking().contains("wrapper_clickTracking"));
        assertTrue(videoClicks.getCustomClick().contains("wrapper_customClick"));

        assertEquals(1, vastConfig.getIcons().size());
        Icon icon = vastConfig.getIcons().get(0);
        assertNotNull(icon);
        assertEquals(5_000, icon.getDuration());
        assertEquals(5_000, icon.getOffset());
        assertNotNull(icon.getIconClicks());
        assertNotNull(icon.getIFrameResource());

        assertEquals(9, vastConfig.getTrackingEventMap().size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.start).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.firstQuartile).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.midpoint).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.thirdQuartile).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.complete).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.mute).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.unmute).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.pause).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.resume).size());

        assertEquals(1, vastConfig.getProgressTrackingList().size());

        assertEquals(1, vastConfig.companions.size());
        Companion companion = vastConfig.companions.get(0);
        assertNotNull(companion);


        Extensions extensions = vastConfig.getExtensions();
        assertNotNull(extensions);
        assertFalse(extensions.canShowCta());
        assertFalse(extensions.canShowCompanion());
    }

    @Test
    public void onlyInLine() throws Exception {
        String xmlString = "<VASTS>" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                "   <Error>inline_error</Error>\n" +
                "   <ViewableImpression>\n" +
                "    <Viewable>inline_viewableImpression_viewable</Viewable>\n" +
                "    <NotViewable>inline_viewableImpression_notViewable</NotViewable>\n" +
                "    <ViewUndetermined>inline_viewableImpression_viewUndetermined</ViewUndetermined>\n" +
                "   </ViewableImpression>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <VideoClicks>\n" +
                "       <ClickThrough>inline_clickThrough</ClickThrough>\n" +
                "       <ClickTracking>inline_clickTracking</ClickTracking>\n" +
                "       <CustomClick>inline_customClick</CustomClick>\n" +
                "      </VideoClicks>\n" +
                "      <Icons>\n" +
                "       <Icon height=\"100\" width=\"200\" pxratio=\"2\" xPosition=\"left\" yPosition=\"top\" duration=\"00:00:05\" offset=\"00:00:05\">\n" +
                "        <IFrameResource><![CDATA[warapper_iframe_url]]></IFrameResource>\n" +
                "        <IconViewTracking>inline_iconViewTracking</IconViewTracking>\n" +
                "        <IconClicks>\n" +
                "         <IconClickThrough>inline_iconClickThrough</IconClickThrough>\n" +
                "         <IconClickTracking>inline_iconClickTracking</IconClickTracking>\n" +
                "        </IconClicks>\n" +
                "       </Icon>\n" +
                "      </Icons>\n" +
                "      <TrackingEvents>\n" +
                "       <Tracking event=\"start\">\n" +
                "        <![CDATA[inline_start]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"firstQuartile\">\n" +
                "        <![CDATA[inline_firstQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"midpoint\">\n" +
                "        <![CDATA[inline_midpoint]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"thirdQuartile\">\n" +
                "        <![CDATA[inline_thirdQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"complete\">\n" +
                "        <![CDATA[inline_complete]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"mute\">\n" +
                "        <![CDATA[inline_mute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"unmute\">\n" +
                "        <![CDATA[inline_unmute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"pause\">\n" +
                "        <![CDATA[inline_pause]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"resume\">\n" +
                "        <![CDATA[inline_resume]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"progress\" offset=\"00:00:10\">\n" +
                "        <![CDATA[inline_resume]]>\n" +
                "       </Tracking>\n" +
                "      </TrackingEvents>\n" +
                "      <MediaFiles>\n" +
                "       <MediaFile delivery=\"streaming\" type=\"video/mp4\" width=\"1920\" height=\"1080\">inline_mediafile_url</MediaFile>\n" +
                "      </MediaFiles>\n" +
                "      <InteractiveCreativeFile type=\"application/javascript\" apiFramework=\"VPAID\">inline_interactiveCreativeFile_url</InteractiveCreativeFile>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "    <Creative>\n" +
                "     <CompanionAds>\n" +
                "      <Companion width=\"300\" height=\"250\">\n" +
                "       <StaticResource creativeType=\"image/jpeg\">inline_companion_url</StaticResource>\n" +
                "       <TrackingEvents>\n" +
                "        <Tracking event=\"creativeView\">inline_companion_creativeView</Tracking>\n" +
                "       </TrackingEvents>\n" +
                "       <CompanionClickThrough>inline_companion_click</CompanionClickThrough>\n" +
                "      </Companion>\n" +
                "     </CompanionAds>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "   <Extensions>\n" +
                "    <Extension type=\"appodeal\">\n" +
                "     <ShowCta>false</ShowCta>\n" +
                "     <ShowCompanion>false</ShowCompanion>\n" +
                "    </Extension>\n" +
                "   </Extensions>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>" +
                "</VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);

        assertNotNull(vastModel);

        VastConfig vastConfig = new VastConfig(vastModel, (float) 16/9, "dir");

        assertNotNull(vastConfig);

        assertEquals(1, vastConfig.getImpressionTracking().size());
        assertTrue(vastConfig.getImpressionTracking().contains("inline_impression"));

        assertEquals(1, vastConfig.getErrorTracking().size());
        assertTrue(vastConfig.getErrorTracking().contains("inline_error"));

        assertEquals(1, vastConfig.getViewableViewableImpressions().size());
        assertTrue(vastConfig.getViewableViewableImpressions().contains("inline_viewableImpression_viewable"));

        assertEquals(1, vastConfig.getNotViewableViewableImpression().size());
        assertTrue(vastConfig.getNotViewableViewableImpression().contains("inline_viewableImpression_notViewable"));

        assertEquals(1, vastConfig.getViewUndeterminedViewableImpression().size());
        assertTrue(vastConfig.getViewUndeterminedViewableImpression().contains("inline_viewableImpression_viewUndetermined"));

        VideoClicks videoClicks = vastConfig.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals("inline_clickThrough", videoClicks.getClickThrough());
        assertTrue(videoClicks.getClickTracking().contains("inline_clickTracking"));
        assertTrue(videoClicks.getCustomClick().contains("inline_customClick"));

        assertEquals(1, vastConfig.getIcons().size());
        Icon icon = vastConfig.getIcons().get(0);
        assertNotNull(icon);
        assertEquals(5_000, icon.getDuration());
        assertEquals(5_000, icon.getOffset());
        assertNotNull(icon.getIconClicks());
        assertNotNull(icon.getIFrameResource());

        assertEquals(9, vastConfig.getTrackingEventMap().size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.start).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.firstQuartile).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.midpoint).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.thirdQuartile).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.complete).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.mute).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.unmute).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.pause).size());
        assertEquals(1, vastConfig.getTrackingEventMap().get(TrackingEventsType.resume).size());

        assertEquals(1, vastConfig.getProgressTrackingList().size());

        assertEquals(1, vastConfig.companions.size());
        Companion companion = vastConfig.companions.get(0);
        assertNotNull(companion);

        assertEquals(1, vastConfig.mediaFiles.size());
        MediaFile mediaFile = vastConfig.mediaFiles.get(0);
        assertNotNull(mediaFile);

        Extensions extensions = vastConfig.getExtensions();
        assertNotNull(extensions);
        assertFalse(extensions.canShowCta());
        assertFalse(extensions.canShowCompanion());
    }

    @Test
    public void bothWrapperAndInLine() throws Exception {
        String xmlString = "<VASTS>" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <Wrapper>\n" +
                "   <VASTAdTagURI>wrapper_ad_tag_uri</VASTAdTagURI>\n" +
                "   <Impression>wrapper_impression</Impression>\n" +
                "   <Error>wrapper_error</Error>\n" +
                "   <ViewableImpression>\n" +
                "    <Viewable>wrapper_viewableImpression_viewable</Viewable>\n" +
                "    <NotViewable>wrapper_viewableImpression_notViewable</NotViewable>\n" +
                "    <ViewUndetermined>wrapper_viewableImpression_viewUndetermined</ViewUndetermined>\n" +
                "   </ViewableImpression>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <VideoClicks>\n" +
                "       <ClickTracking>wrapper_clickTracking</ClickTracking>\n" +
                "       <ClickTracking>wrapper_clickTracking</ClickTracking>\n" +
                "       <CustomClick>wrapper_customClick</CustomClick>\n" +
                "      </VideoClicks>\n" +
                "      <Icons>\n" +
                "       <Icon height=\"100\" width=\"200\" pxratio=\"2\" xPosition=\"left\" yPosition=\"top\" duration=\"00:00:05\" offset=\"00:00:05\">\n" +
                "        <IFrameResource><![CDATA[warapper_iframe_url]]></IFrameResource>\n" +
                "        <IconViewTracking>wrapper_iconViewTracking</IconViewTracking>\n" +
                "        <IconClicks>\n" +
                "         <IconClickThrough>wrapper_iconClickThrough</IconClickThrough>\n" +
                "         <IconClickTracking>wrapper_iconClickTracking</IconClickTracking>\n" +
                "        </IconClicks>\n" +
                "       </Icon>\n" +
                "      </Icons>\n" +
                "      <TrackingEvents>\n" +
                "       <Tracking event=\"start\">\n" +
                "        <![CDATA[wrapper_start]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"firstQuartile\">\n" +
                "        <![CDATA[wrapper_firstQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"midpoint\">\n" +
                "        <![CDATA[wrapper_midpoint]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"thirdQuartile\">\n" +
                "        <![CDATA[wrapper_thirdQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"complete\">\n" +
                "        <![CDATA[wrapper_complete]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"mute\">\n" +
                "        <![CDATA[wrapper_mute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"unmute\">\n" +
                "        <![CDATA[wrapper_unmute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"pause\">\n" +
                "        <![CDATA[wrapper_pause]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"resume\">\n" +
                "        <![CDATA[wrapper_resume]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"progress\" offset=\"00:00:10\">\n" +
                "        <![CDATA[wrapper_resume]]>\n" +
                "       </Tracking>\n" +
                "      </TrackingEvents>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "    <Creative>\n" +
                "     <CompanionAds>\n" +
                "      <Companion width=\"300\" height=\"250\">\n" +
                "       <StaticResource creativeType=\"image/jpeg\">wrapper_companion_url</StaticResource>\n" +
                "       <TrackingEvents>\n" +
                "        <Tracking event=\"creativeView\">wrapper_companion_creativeView</Tracking>\n" +
                "       </TrackingEvents>\n" +
                "       <CompanionClickThrough>wrapper_companion_click</CompanionClickThrough>\n" +
                "      </Companion>\n" +
                "     </CompanionAds>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "   <Extensions>\n" +
                "    <Extension type=\"appodeal\">\n" +
                "     <ShowCta>false</ShowCta>\n" +
                "     <ShowCompanion>false</ShowCompanion>\n" +
                "    </Extension>\n" +
                "   </Extensions>\n" +
                "  </Wrapper>\n" +
                " </Ad>\n" +
                "</VAST>\n" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Impression>inline_impression</Impression>\n" +
                "   <Error>inline_error</Error>\n" +
                "   <ViewableImpression>\n" +
                "    <Viewable>inline_viewableImpression_viewable</Viewable>\n" +
                "    <NotViewable>inline_viewableImpression_notViewable</NotViewable>\n" +
                "    <ViewUndetermined>inline_viewableImpression_viewUndetermined</ViewUndetermined>\n" +
                "   </ViewableImpression>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <VideoClicks>\n" +
                "       <ClickThrough>inline_clickThrough</ClickThrough>\n" +
                "       <ClickTracking>inline_clickTracking</ClickTracking>\n" +
                "       <CustomClick>inline_customClick</CustomClick>\n" +
                "      </VideoClicks>\n" +
                "      <Icons>\n" +
                "       <Icon height=\"100\" width=\"200\" pxratio=\"2\" xPosition=\"left\" yPosition=\"top\" duration=\"00:00:05\" offset=\"00:00:05\">\n" +
                "        <IFrameResource><![CDATA[warapper_iframe_url]]></IFrameResource>\n" +
                "        <IconViewTracking>inline_iconViewTracking</IconViewTracking>\n" +
                "        <IconClicks>\n" +
                "         <IconClickThrough>inline_iconClickThrough</IconClickThrough>\n" +
                "         <IconClickTracking>inline_iconClickTracking</IconClickTracking>\n" +
                "        </IconClicks>\n" +
                "       </Icon>\n" +
                "      </Icons>\n" +
                "      <TrackingEvents>\n" +
                "       <Tracking event=\"start\">\n" +
                "        <![CDATA[inline_start]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"firstQuartile\">\n" +
                "        <![CDATA[inline_firstQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"midpoint\">\n" +
                "        <![CDATA[inline_midpoint]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"thirdQuartile\">\n" +
                "        <![CDATA[inline_thirdQuartile]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"complete\">\n" +
                "        <![CDATA[inline_complete]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"mute\">\n" +
                "        <![CDATA[inline_mute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"unmute\">\n" +
                "        <![CDATA[inline_unmute]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"pause\">\n" +
                "        <![CDATA[inline_pause]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"resume\">\n" +
                "        <![CDATA[inline_resume]]>\n" +
                "       </Tracking>\n" +
                "       <Tracking event=\"progress\" offset=\"00:00:10\">\n" +
                "        <![CDATA[inline_resume]]>\n" +
                "       </Tracking>\n" +
                "      </TrackingEvents>\n" +
                "      <MediaFiles>\n" +
                "       <MediaFile delivery=\"streaming\" type=\"video/mp4\" width=\"1920\" height=\"1080\">inline_mediafile_url</MediaFile>\n" +
                "      </MediaFiles>\n" +
                "      <InteractiveCreativeFile type=\"application/javascript\" apiFramework=\"VPAID\">inline_interactiveCreativeFile_url</InteractiveCreativeFile>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "    <Creative>\n" +
                "     <CompanionAds>\n" +
                "      <Companion width=\"300\" height=\"250\">\n" +
                "       <StaticResource creativeType=\"image/jpeg\">inline_companion_url</StaticResource>\n" +
                "       <TrackingEvents>\n" +
                "        <Tracking event=\"creativeView\">inline_companion_creativeView</Tracking>\n" +
                "       </TrackingEvents>\n" +
                "       <CompanionClickThrough>inline_companion_click</CompanionClickThrough>\n" +
                "      </Companion>\n" +
                "     </CompanionAds>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "   <Extensions>\n" +
                "    <Extension type=\"appodeal\">\n" +
                "     <ShowCta>false</ShowCta>\n" +
                "     <ShowCompanion>false</ShowCompanion>\n" +
                "    </Extension>\n" +
                "   </Extensions>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>" +
                "</VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);

        assertNotNull(vastModel);

        VastConfig vastConfig = new VastConfig(vastModel, (float) 16/9, "dir");

        assertNotNull(vastConfig);

        assertEquals(2, vastConfig.getImpressionTracking().size());
        assertTrue(vastConfig.getImpressionTracking().contains("inline_impression"));
        assertTrue(vastConfig.getImpressionTracking().contains("wrapper_impression"));

        assertEquals(2, vastConfig.getErrorTracking().size());
        assertTrue(vastConfig.getErrorTracking().contains("inline_error"));
        assertTrue(vastConfig.getErrorTracking().contains("wrapper_error"));

        assertEquals(2, vastConfig.getViewableViewableImpressions().size());
        assertTrue(vastConfig.getViewableViewableImpressions().contains("inline_viewableImpression_viewable"));
        assertTrue(vastConfig.getViewableViewableImpressions().contains("wrapper_viewableImpression_viewable"));

        assertEquals(2, vastConfig.getNotViewableViewableImpression().size());
        assertTrue(vastConfig.getNotViewableViewableImpression().contains("inline_viewableImpression_notViewable"));
        assertTrue(vastConfig.getNotViewableViewableImpression().contains("wrapper_viewableImpression_notViewable"));

        assertEquals(2, vastConfig.getViewUndeterminedViewableImpression().size());
        assertTrue(vastConfig.getViewUndeterminedViewableImpression().contains("inline_viewableImpression_viewUndetermined"));
        assertTrue(vastConfig.getViewUndeterminedViewableImpression().contains("wrapper_viewableImpression_viewUndetermined"));

        VideoClicks videoClicks = vastConfig.getVideoClicks();
        assertNotNull(videoClicks);
        assertEquals("inline_clickThrough", videoClicks.getClickThrough());
        assertTrue(videoClicks.getClickTracking().contains("inline_clickTracking"));
        assertTrue(videoClicks.getClickTracking().contains("wrapper_clickTracking"));
        assertTrue(videoClicks.getCustomClick().contains("inline_customClick"));
        assertTrue(videoClicks.getCustomClick().contains("wrapper_customClick"));

        assertEquals(2, vastConfig.getIcons().size());

        assertEquals(9, vastConfig.getTrackingEventMap().size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.start).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.firstQuartile).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.midpoint).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.thirdQuartile).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.complete).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.mute).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.unmute).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.pause).size());
        assertEquals(2, vastConfig.getTrackingEventMap().get(TrackingEventsType.resume).size());

        assertEquals(2, vastConfig.getProgressTrackingList().size());

        assertEquals(2, vastConfig.companions.size());
        Companion companion = vastConfig.companions.get(0);
        assertNotNull(companion);

        assertEquals(1, vastConfig.mediaFiles.size());
        MediaFile mediaFile = vastConfig.mediaFiles.get(0);
        assertNotNull(mediaFile);

        Extensions extensions = vastConfig.getExtensions();
        assertNotNull(extensions);
        assertFalse(extensions.canShowCta());
        assertFalse(extensions.canShowCompanion());
    }

    @Test
    public void withoutExtension_shouldReturnDefaultValues() throws Exception {
        String xmlString = "<VASTS>" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <MediaFiles>\n" +
                "       <MediaFile delivery=\"streaming\" type=\"video/mp4\" width=\"1920\" height=\"1080\">inline_mediafile_url</MediaFile>\n" +
                "      </MediaFiles>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>" +
                "</VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);

        VastConfig vastConfig = new VastConfig(vastModel, (float) 16/9, "dir");

        assertNotNull(vastConfig);

        assertTrue(vastConfig.canShowCompanion());
        assertTrue(vastConfig.canShowCta());
        assertTrue(vastConfig.canShowMute());
        assertTrue(vastConfig.canShowProgress());

        assertEquals(0, vastConfig.getCompanionCloseTime());

        assertEquals(VastTools.defaultCtaText, vastConfig.getCtaText());

        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getCloseButtonPosition());
        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getMuteButtonPosition());
        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM), vastConfig.getCtaButtonPosition());

        assertEquals(VastTools.assetsColor, vastConfig.getAssetsColor());
        assertEquals(VastTools.backgroundColor, vastConfig.getAssetsBackgroundColor());
    }


    @Test
    public void withEmptyExtension_shouldReturnDefaultValues() throws Exception {
        String xmlString = "<VASTS>" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <MediaFiles>\n" +
                "       <MediaFile delivery=\"streaming\" type=\"video/mp4\" width=\"1920\" height=\"1080\">inline_mediafile_url</MediaFile>\n" +
                "      </MediaFiles>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "   <Extensions>\n" +
                "    <Extension type=\"appodeal\">\n" +
                "    </Extension>\n" +
                "   </Extensions>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>" +
                "</VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);

        VastConfig vastConfig = new VastConfig(vastModel, (float) 16/9, "dir");

        assertNotNull(vastConfig);

        assertTrue(vastConfig.canShowCompanion());
        assertTrue(vastConfig.canShowCta());
        assertTrue(vastConfig.canShowMute());
        assertTrue(vastConfig.canShowProgress());

        assertEquals(0, vastConfig.getCompanionCloseTime());

        assertEquals(VastTools.defaultCtaText, vastConfig.getCtaText());

        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getCloseButtonPosition());
        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getMuteButtonPosition());
        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM), vastConfig.getCtaButtonPosition());

        assertEquals(VastTools.assetsColor, vastConfig.getAssetsColor());
        assertEquals(VastTools.backgroundColor, vastConfig.getAssetsBackgroundColor());
    }

    @Test
    public void withExtension_shouldReturnExtansionsValues() throws Exception {
        String xmlString = "<VASTS>" +
                "<VAST version=\"4.0\">\n" +
                " <Ad>\n" +
                "  <InLine>\n" +
                "   <Creatives>\n" +
                "    <Creative>\n" +
                "     <Linear>\n" +
                "      <MediaFiles>\n" +
                "       <MediaFile delivery=\"streaming\" type=\"video/mp4\" width=\"1920\" height=\"1080\">inline_mediafile_url</MediaFile>\n" +
                "      </MediaFiles>\n" +
                "     </Linear>\n" +
                "    </Creative>\n" +
                "   </Creatives>\n" +
                "   <Extensions>\n" +
                "    <Extension type=\"appodeal\">\n" +
                "     <CtaText>Download</CtaText>\n" +
                "     <ShowCta>0</ShowCta>\n" +
                "     <ShowMute>0</ShowMute>\n" +
                "     <ShowCompanion>0</ShowCompanion>\n" +
                "     <ShowProgress>0</ShowProgress>\n" +
                "     <CompanionCloseTime>00:02</CompanionCloseTime>\n" +
                "     <AssetsColor>#4286f4</AssetsColor>\n" +
                "     <AssetsBackgroundColor>#13b521</AssetsBackgroundColor>\n" +
                "     <CtaXPosition>center</CtaXPosition>\n" +
                "     <CtaYPosition>center</CtaYPosition>\n" +
                "     <MuteXPosition>right</MuteXPosition>\n" +
                "     <MuteYPosition>center</MuteYPosition>\n" +
                "     <CloseXPosition>left</CloseXPosition>\n" +
                "     <CloseYPosition>top</CloseYPosition>\n" +
                "    </Extension>\n" +
                "   </Extensions>\n" +
                "  </InLine>\n" +
                " </Ad>\n" +
                "</VAST>" +
                "</VASTS>";

        Document document = VastTools.getDocumentFromString(xmlString);
        VastModel vastModel = new VastModel(document);

        VastConfig vastConfig = new VastConfig(vastModel, (float) 16/9, "dir");

        assertNotNull(vastConfig);

        assertFalse(vastConfig.canShowCompanion());
        assertFalse(vastConfig.canShowCta());
        assertFalse(vastConfig.canShowMute());
        assertFalse(vastConfig.canShowProgress());

        assertEquals(2000, vastConfig.getCompanionCloseTime());

        assertEquals("Download", vastConfig.getCtaText());

        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP), vastConfig.getCloseButtonPosition());
        assertEquals(new Pair<>(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL), vastConfig.getMuteButtonPosition());
        assertEquals(new Pair<>(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.CENTER_VERTICAL), vastConfig.getCtaButtonPosition());

        assertEquals(Color.parseColor("#4286f4"), vastConfig.getAssetsColor());
        assertEquals(Color.parseColor("#13b521"), vastConfig.getAssetsBackgroundColor());
    }

}