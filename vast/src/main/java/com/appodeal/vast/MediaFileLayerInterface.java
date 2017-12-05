package com.appodeal.vast;


import android.view.View;
import android.view.ViewGroup;

interface MediaFileLayerInterface {

    void setStartPosition(int position);
    int getCurrentPosition();
    void start();
    void resume();
    void pause();
    void destroy();
    void setVolume(int volume);

    View getView();
}
