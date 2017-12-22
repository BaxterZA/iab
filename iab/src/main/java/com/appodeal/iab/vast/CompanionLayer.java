package com.appodeal.iab.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
public abstract class CompanionLayer extends RelativeLayout {
    protected final VastConfig vastConfig;
    protected CompanionListener listener;
    protected final Companion companion;

    interface CompanionListener {
        void onCompanionShown(@Nullable Companion companion);
        void onCompanionClicked(@Nullable Companion companion, String url);
        void onCompanionClosed(@Nullable Companion companion);
        void onRepeatButtonClicked();
    }

    CompanionLayer(Context context, @NonNull final VastConfig vastConfig, @NonNull final CompanionListener listener) {
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
