package com.appodeal.iab.mraid;

import android.graphics.Rect;

import com.appodeal.iab.views.ViewHelper;

class MraidScreenMetrics {
    private final Size screenSize;
    private final Size maxSize;
    private final Rect defaultSize;
    private final Rect currentSize;
    private int densityDpi;

    MraidScreenMetrics(int densityDpi) {
        this.densityDpi = densityDpi;
        screenSize = new Size();
        maxSize = new Size();
        defaultSize = new Rect();
        currentSize = new Rect();
    }

    void updateScreenSize(int width, int height) {
        screenSize.update(width, height);
    }

    void updateMaxSize(int width, int height) {
        maxSize.update(width, height);
    }

    void updateDefaultSize(int x, int y, int width, int height) {
        defaultSize.set(x, y, x + width, y + height);
    }

    void updateCurrentSize(int x, int y, int width, int height) {
        currentSize.set(x, y, x + width, y + height);
    }

    Size getMaxSize() {
        return maxSize;
    }

    Rect getMasSizeRect() {
        return new Rect(0, 0, maxSize.width(), maxSize.height());
    }

    Rect getDefaultSize() {
        return defaultSize;
    }

    void updateDensity(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    String getScreenSizeString() {
        return getSizeString(screenSize);
    }

    String getMaxSizeString() {
        return getSizeString(maxSize);
    }

    String getDefaultPositionString() {
        return getPositionString(defaultSize);
    }

    String setCurrentPositionString() {
        return getPositionString(currentSize);
    }

    private String getPositionString(Rect rect) {
        return String.format("%s, %s, %s, %s", ViewHelper.px2dip(rect.left, densityDpi),
                ViewHelper.px2dip(rect.top, densityDpi),
                ViewHelper.px2dip(rect.width(), densityDpi),
                ViewHelper.px2dip(rect.height(), densityDpi));
    }

    private String getSizeString(Size size) {
        return String.format("%s, %s", ViewHelper.px2dip(size.width(), densityDpi), ViewHelper.px2dip(size.height(), densityDpi));
    }


}
