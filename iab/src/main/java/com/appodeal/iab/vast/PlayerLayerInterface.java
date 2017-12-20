package com.appodeal.iab.vast;


import android.view.View;

public interface PlayerLayerInterface {

    int getCurrentPosition();
    void load();
    void start();
    void resume();
    void pause();
    void destroy();
    void setVolume(int volume);

    View getView();
}
