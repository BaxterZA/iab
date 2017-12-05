package com.appodeal.mraid;

import android.graphics.Rect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

class ExposureState {
    private final float percent;
    private final Rect visibleRectangle;
    private final List<Rect> occlusionRectangles;

    ExposureState(float percent, Rect visibleRectangle, List<Rect> occlusionRectangles) {
        this.percent = percent;
        this.visibleRectangle = visibleRectangle;
        this.occlusionRectangles = occlusionRectangles;
    }

    String toMraidString() {
        return String.valueOf(percent) + ", " + rectToJson(visibleRectangle) + ", " + rectListToJsonArray(occlusionRectangles);
    }

    private JSONObject rectToJson(Rect rect) {
        if (rect == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("x", rect.left);
            jsonObject.put("y", rect.top);
            jsonObject.put("width", rect.width());
            jsonObject.put("height", rect.height());
        } catch (JSONException e) {
            MraidLog.e(e.getMessage());
        }
        return jsonObject;
    }

    private JSONArray rectListToJsonArray(List<Rect> list) {
        if (list == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (Rect rect : list) {
            jsonArray.put(rectToJson(rect));
        }
        return jsonArray;
    }

    boolean isVisible() {
        return percent > 0;
    }

    static ExposureState empty() {
        return  new ExposureState(0, new Rect(0, 0, 0, 0), null);
    }
}
