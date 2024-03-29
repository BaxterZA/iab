package com.appodeal.mraid;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;


public class TestActivity extends Activity {
    MraidView mraidView;
    public static int width = 320;
    public static int height = 50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mraidView = new MraidView(this);
        mraidView.setBackgroundColor(Color.WHITE);
    }

    public void setVisibility(boolean visibility) {
        mraidView.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    public void removeView() {
        ((ViewGroup) mraidView.getParent()).removeView(mraidView);
    }

    public void drawView() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        setContentView(mraidView, new ViewGroup.LayoutParams((int) (width * displayMetrics.density), (int) (height * displayMetrics.density)));
    }
}
