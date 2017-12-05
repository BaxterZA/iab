package com.appodeal.vast;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class MediaFilePickerTest {

    @Test
    public void getBestMediaFile_emptyLists_shouldReturnNull() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);
        assertNull(mediaFilePicker.getBestMediaFile(new ArrayList<MediaFile>(), new ArrayList<MediaFile>()));
    }

    @Test
    public void getBestMediaFile_invalidVideo_shouldReturnNull() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);
        MediaFile.Builder mediaFileBuilder = new MediaFile.Builder("video_url");
        mediaFileBuilder.setWidth(480);
        mediaFileBuilder.setHeight(320);
        mediaFileBuilder.setDelivery("progressive");

        MediaFile mediaFile = mediaFileBuilder.build();

        assertNull(mediaFilePicker.getBestMediaFile(Collections.singletonList(mediaFile), new ArrayList<MediaFile>()));
    }

    @Test
    public void getBestMediaFile_validVideo_shouldReturnVideo() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);
        MediaFile.Builder mediaFileBuilder = new MediaFile.Builder("video_url");
        mediaFileBuilder.setWidth(480);
        mediaFileBuilder.setHeight(320);
        mediaFileBuilder.setDelivery("progressive");
        mediaFileBuilder.setType("video/mp4");
        MediaFile mediaFile = mediaFileBuilder.build();

        assertEquals(mediaFile, mediaFilePicker.getBestMediaFile(Collections.singletonList(mediaFile), new ArrayList<MediaFile>()));
    }

    @Test
    public void getBestMediaFile_chooseByOrientation_shouldReturnBestVideo() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);

        MediaFile.Builder mediaFileBuilder1 = new MediaFile.Builder("video_url");
        mediaFileBuilder1.setWidth(480);
        mediaFileBuilder1.setHeight(320);
        mediaFileBuilder1.setDelivery("progressive");
        mediaFileBuilder1.setType("video/mp4");
        MediaFile mediaFile1 = mediaFileBuilder1.build();

        MediaFile.Builder mediaFileBuilder2 = new MediaFile.Builder("video_url");
        mediaFileBuilder2.setWidth(320);
        mediaFileBuilder2.setHeight(480);
        mediaFileBuilder2.setDelivery("progressive");
        mediaFileBuilder2.setType("video/mp4");
        MediaFile mediaFile2 = mediaFileBuilder2.build();

        assertEquals(mediaFile1, mediaFilePicker.getBestMediaFile(Arrays.asList(mediaFile1, mediaFile2), new ArrayList<MediaFile>()));
    }

    @Test
    public void getBestMediaFile_chooseByFitness_shouldReturnBestVideo() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);

        MediaFile.Builder mediaFileBuilder1 = new MediaFile.Builder("video_url");
        mediaFileBuilder1.setWidth(480);
        mediaFileBuilder1.setHeight(320);
        mediaFileBuilder1.setDelivery("progressive");
        mediaFileBuilder1.setType("video/mp4");
        MediaFile mediaFile1 = mediaFileBuilder1.build();

        MediaFile.Builder mediaFileBuilder2 = new MediaFile.Builder("video_url");
        mediaFileBuilder2.setWidth(480);
        mediaFileBuilder2.setHeight(270);
        mediaFileBuilder2.setDelivery("progressive");
        mediaFileBuilder2.setType("video/mp4");
        MediaFile mediaFile2 = mediaFileBuilder2.build();

        assertEquals(mediaFile2, mediaFilePicker.getBestMediaFile(Arrays.asList(mediaFile1, mediaFile2), new ArrayList<MediaFile>()));
    }

    @Test
    public void getBestMediaFile_onlyInteractiveCreativeFile_shouldReturnInteractiveCreativeFile() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);

        MediaFile.Builder mediaFileBuilder1 = new MediaFile.Builder("video_url");
        mediaFileBuilder1.setType("application/javascript");
        mediaFileBuilder1.setApiFramework("VPAID");
        MediaFile interactiveFile = mediaFileBuilder1.build();

        assertEquals(interactiveFile, mediaFilePicker.getBestMediaFile(new ArrayList<MediaFile>(), Collections.singletonList(interactiveFile)));
    }

    @Test
    public void getBestMediaFile_videoAndInteractiveCreativeFile_shouldReturnVideoFile() throws Exception {
        MediaFilePicker mediaFilePicker = new MediaFilePicker((float) 16 / 9);

        MediaFile.Builder mediaFileBuilder1 = new MediaFile.Builder("video_url");
        mediaFileBuilder1.setWidth(480);
        mediaFileBuilder1.setHeight(320);
        mediaFileBuilder1.setDelivery("progressive");
        mediaFileBuilder1.setType("video/mp4");
        MediaFile mediaFile = mediaFileBuilder1.build();

        MediaFile.Builder mediaFileBuilder2 = new MediaFile.Builder("video_url");
        mediaFileBuilder2.setType("application/javascript");
        mediaFileBuilder2.setApiFramework("VPAID");
        MediaFile interactiveFile = mediaFileBuilder2.build();

        assertEquals(mediaFile, mediaFilePicker.getBestMediaFile(Collections.singletonList(mediaFile), Collections.singletonList(interactiveFile)));
    }
}