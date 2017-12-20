package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
public abstract class CompanionLayer extends RelativeLayout {
    protected VastConfig vastConfig;
    protected CompanionListener listener;
    protected Companion companion;

    interface CompanionListener {
        void onCompanionShown(@Nullable Companion companion);
        void onCompanionClicked(@Nullable Companion companion, String url);
        void onCompanionClosed(@Nullable Companion companion);
        void onRepeatButtonClicked();
    }

    public CompanionLayer(Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionListener listener) {
        super(context);
        this.vastConfig = vastConfig;
        this.listener = listener;
        this.companion = vastConfig.getCompanion();
    }

    abstract boolean canClose();

    abstract void showCompanion();

    boolean hasCompanion() {
        return companion != null;
    }

    @CallSuper
    void destroy() {
        listener = null;
        removeAllViews();
    }
}
