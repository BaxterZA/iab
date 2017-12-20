package com.appodeal.iab.vast;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CompanionPickerTest {

    @Test
    public void getBestCompanion_emptyList_shouldReturnNull() throws Exception {
        CompanionPicker companionPicker = new CompanionPicker((float) 16 / 9);
        assertNull(companionPicker.getBestCompanion(new ArrayList<Companion>()));
    }

    @Test
    public void getBestCompanion_disableByWidth_shouldReturnNull() throws Exception {
        CompanionPicker companionPicker = new CompanionPicker((float) 16 / 9);
        Companion.Builder companionBuilder = new Companion.Builder(160, 600);
        companionBuilder.setHtmlResource(new HtmlResource("url"));
        assertNull(companionPicker.getBestCompanion(Collections.singletonList(companionBuilder.build())));
    }

    @Test
    public void getBestCompanion_disableByHeight_shouldReturnNull() throws Exception {
        CompanionPicker companionPicker = new CompanionPicker((float) 16 / 9);
        Companion.Builder companionBuilder = new Companion.Builder(728, 90);
        companionBuilder.setHtmlResource(new HtmlResource("url"));
        assertNull(companionPicker.getBestCompanion(Collections.singletonList(companionBuilder.build())));
    }

    @Test
    public void getBestCompanion_onlyOne_shouldReturnCompanion() throws Exception {
        CompanionPicker companionPicker = new CompanionPicker((float) 16 / 9);
        Companion.Builder companionBuilder = new Companion.Builder(320, 480);
        companionBuilder.setHtmlResource(new HtmlResource("url"));
        Companion companion = companionBuilder.build();
        assertEquals(companion, companionPicker.getBestCompanion(Collections.singletonList(companion)));
    }

    @Test
    public void getBestCompanion_chooseByOrientation_shouldReturnBestCompanion() throws Exception {
        CompanionPicker companionPicker = new CompanionPicker((float) 16 / 9);
        Companion.Builder companionBuilder1 = new Companion.Builder(320, 480);
        companionBuilder1.setHtmlResource(new HtmlResource("url"));
        Companion companion1 = companionBuilder1.build();
        Companion.Builder companionBuilder2 = new Companion.Builder(480, 320);
        companionBuilder2.setHtmlResource(new HtmlResource("url"));
        Companion companion2 = companionBuilder2.build();
        assertEquals(companion2, companionPicker.getBestCompanion(Arrays.asList(companion1, companion2)));
    }

    @Test
    public void getBestCompanion_chooseByRatio_shouldReturnBestCompanion() throws Exception {
        CompanionPicker companionPicker = new CompanionPicker((float) 16 / 9);
        Companion.Builder companionBuilder1 = new Companion.Builder(480, 320);
        companionBuilder1.setHtmlResource(new HtmlResource("url"));
        Companion companion1 = companionBuilder1.build();
        Companion.Builder companionBuilder2 = new Companion.Builder(480, 270);
        companionBuilder2.setHtmlResource(new HtmlResource("url"));
        Companion companion2 = companionBuilder2.build();
        assertEquals(companion2, companionPicker.getBestCompanion(Arrays.asList(companion1, companion2)));
    }
}