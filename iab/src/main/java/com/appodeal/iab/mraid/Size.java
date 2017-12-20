package com.appodeal.iab.mraid;


class Size {
    private int width;
    private int height;

    Size() {
    }

    Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void update(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("Size (%s x %s)", width, height);
    }
}
