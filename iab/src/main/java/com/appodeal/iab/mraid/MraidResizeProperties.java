package com.appodeal.iab.mraid;


import android.content.Context;
import android.graphics.Rect;

public class MraidResizeProperties {
    public int width;
    public int height;
    public int offsetX;
    public int offsetY;
    public ClosePosition customClosePosition;
    public boolean allowOffscreen;

    MraidResizeProperties() {
        this(0, 0, 0, 0, ClosePosition.TOP_RIGHT, true);
    }

    MraidResizeProperties(int width, int height, int offsetX, int offsetY, ClosePosition customClosePosition, boolean allowOffscreen) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.customClosePosition = customClosePosition;
        this.allowOffscreen = allowOffscreen;
    }

    Rect getResizeRect(Context context, int currentLeft, int currentTop) {
        float density = context.getResources().getDisplayMetrics().density;
        int widthPx = (int) (width * density);
        int heightPx = (int) (height * density);
        int offsetXPx = (int) (offsetX * density);
        int offsetYPx = (int) (offsetY * density);
        int leftPx = currentLeft + offsetXPx;
        int topPx = currentTop + offsetYPx;
        return new Rect(leftPx, topPx, leftPx + widthPx, topPx + heightPx);
    }
}
