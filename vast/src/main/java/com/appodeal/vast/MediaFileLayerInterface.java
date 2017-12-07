package com.appodeal.vast;


import android.view.View;

public interface MediaFileLayerInterface {

    int getCurrentPosition();
    void start();
    void resume();
    void pause();
    void destroy();
    void setVolume(int volume);

    View getView();
}
