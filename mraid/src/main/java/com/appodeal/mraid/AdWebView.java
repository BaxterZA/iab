package com.appodeal.mraid;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class AdWebView extends WebView {
    private AdWebViewListener listener;

    public AdWebView(Context context) {
        super(new MutableContextWrapper(context));

        getSettings().setAllowFileAccess(false);
        getSettings().setAllowContentAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getSettings().setAllowFileAccessFromFileURLs(false);
            getSettings().setAllowUniversalAccessFromFileURLs(false);
        }
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);

        setScrollContainer(false);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setFocusableInTouchMode(false);

        setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (listener != null) {
                            listener.onTouch();
                        }
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setListener(AdWebViewListener listener) {
        this.listener = listener;
    }

    public void allowMediaPlayback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return false;
    }
}
