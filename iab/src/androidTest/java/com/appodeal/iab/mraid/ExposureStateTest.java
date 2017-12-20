package com.appodeal.iab.mraid;

import android.graphics.Rect;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExposureStateTest {

    @Test
    public void toMraidString() throws Exception {
        ExposureState exposureState = new ExposureState(0.0f, null, null);
        assertEquals(exposureState.toMraidString(), "0.0, null, null");

        exposureState = new ExposureState(0.0f, new Rect(1, 2, 5, 10), null);
        assertEquals(exposureState.toMraidString(), "0.0, {\"x\":1,\"y\":2,\"width\":4,\"height\":8}, null");

        List<Rect> rectList = new ArrayList<>();
        rectList.add(new Rect(1, 2, 3, 4));
        rectList.add(new Rect(5, 6, 8, 10));
        exposureState = new ExposureState(0.0f, new Rect(1, 2, 5, 10), rectList);
        assertEquals(exposureState.toMraidString(), "0.0, {\"x\":1,\"y\":2,\"width\":4,\"height\":8}, [{\"x\":1,\"y\":2,\"width\":2,\"height\":2},{\"x\":5,\"y\":6,\"width\":3,\"height\":4}]");
    }

}