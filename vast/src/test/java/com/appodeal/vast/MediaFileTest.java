package com.appodeal.vast;

import org.junit.Test;

import static org.junit.Assert.*;

public class MediaFileTest {

    @Test
    public void isValidVideoFileTest() throws Exception {
        MediaFile.Builder mediaFileBuilder = new MediaFile.Builder("url");
        assertFalse(mediaFileBuilder.build().isValidVideoFile());

        mediaFileBuilder.setWidth(480);
        assertFalse(mediaFileBuilder.build().isValidVideoFile());

        mediaFileBuilder.setHeight(320);
        assertFalse(mediaFileBuilder.build().isValidVideoFile());

        mediaFileBuilder.setDelivery("progressive");
        assertFalse(mediaFileBuilder.build().isValidVideoFile());

        mediaFileBuilder.setType("video/wmv");
        assertFalse(mediaFileBuilder.build().isValidVideoFile());

        mediaFileBuilder.setType("video/mp4");
        assertTrue(mediaFileBuilder.build().isValidVideoFile());
    }

    @Test
    public void isValidVPAIDMediaFile() throws Exception {
        MediaFile.Builder mediaFileBuilder = new MediaFile.Builder("url");
        assertFalse(mediaFileBuilder.build().isValidVPAIDMediaFile());

        mediaFileBuilder.setType("video/wmv");
        assertFalse(mediaFileBuilder.build().isValidVPAIDMediaFile());

        mediaFileBuilder.setType("video/mp4");
        assertFalse(mediaFileBuilder.build().isValidVPAIDMediaFile());

        mediaFileBuilder.setType("application/javascript");
        assertFalse(mediaFileBuilder.build().isValidVPAIDMediaFile());

        mediaFileBuilder.setApiFramework("VPAID");
        assertTrue(mediaFileBuilder.build().isValidVPAIDMediaFile());
    }

}