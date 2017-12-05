package com.appodeal.vast;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CompanionTest {

    @Test
    public void isValid_withIFrameResource_shouldReturnTrue() throws Exception {
        Companion.Builder companionBuilder = new Companion.Builder(320, 240);
        companionBuilder.setIFrameResource(new IFrameResource("uri"));
        Companion companion = companionBuilder.build();
        assertTrue(companion.isValid());
    }

    @Test
    public void isValid_withHtmlResource_shouldReturnTrue() throws Exception {
        Companion.Builder companionBuilder = new Companion.Builder(320, 240);
        companionBuilder.setHtmlResource(new HtmlResource("html"));
        Companion companion = companionBuilder.build();
        assertTrue(companion.isValid());
    }

    @Test
    public void isValid_withBadStaticResource_shouldReturnFalse() throws Exception {
        Companion.Builder companionBuilder = new Companion.Builder(320, 240);
        companionBuilder.setStaticResource(new StaticResource("video/mp4", "uri"));
        Companion companion = companionBuilder.build();
        assertFalse(companion.isValid());
    }

    @Test
    public void isValid_withStaticResource_shouldReturnTrue() throws Exception {
        Companion.Builder companionBuilder = new Companion.Builder(320, 240);
        companionBuilder.setStaticResource(new StaticResource("image/png", "uri"));
        Companion companion = companionBuilder.build();
        assertTrue(companion.isValid());
    }

    @Test
    public void isValid_withZeroWidth_shouldReturnFalse() throws Exception {
        Companion.Builder companionBuilder = new Companion.Builder(0, 240);
        companionBuilder.setStaticResource(new StaticResource("image/png", "uri"));
        Companion companion = companionBuilder.build();
        assertFalse(companion.isValid());
    }

    @Test
    public void isValid_withZeroHeight_shouldReturnFalse() throws Exception {
        Companion.Builder companionBuilder = new Companion.Builder(320, 0);
        companionBuilder.setStaticResource(new StaticResource("image/png", "uri"));
        Companion companion = companionBuilder.build();
        assertFalse(companion.isValid());
    }

}