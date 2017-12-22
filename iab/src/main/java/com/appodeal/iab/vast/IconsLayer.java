package com.appodeal.iab.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.appodeal.iab.MraidView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class IconsLayer extends RelativeLayout {

    interface IconsLayerListener {
        void onIconShown(Icon icon);
        void onIconClicked(Icon icon, String url);
    }

    private HashMap<Icon, View> iconsMap;
    private IconsLayerListener listener;
    
    public IconsLayer(Context context, @NonNull VastConfig vastConfig, @NonNull final IconsLayerListener listener) {
        super(context);

        this.listener = listener;

        List<Icon> icons = vastConfig.getIcons();
        if (icons != null && icons.size() > 0) {
            iconsMap = new HashMap<>(icons.size());
            for (final Icon icon : icons) {
                View iconView = icon.getView(context, new ResourceViewHelper.ResourceListener() {
                    @Override
                    public void onClick(String url) {
                        listener.onIconClicked(icon, url);
                    }

                    @Override
                    public void onClose() {
                    }
                });
                iconView.setVisibility(GONE);
                iconsMap.put(icon, iconView);
                addView(iconView);
            }
        }
    }

    void updateIcons(int playerPosition) {
        if (iconsMap != null && iconsMap.size() > 0) {
            for (Map.Entry<Icon, View> mapEntry : iconsMap.entrySet()) {
                Icon icon = mapEntry.getKey();
                View view = mapEntry.getValue();
                if (icon.shouldShow(playerPosition) && icon.viewIsLoaded() && view != null && view.getVisibility() != VISIBLE) {
                    view.setVisibility(VISIBLE);
                    listener.onIconShown(icon);
                } else if (!icon.shouldShow(playerPosition) && view != null && view.getVisibility() == VISIBLE) {
                    view.setVisibility(GONE);
                }
            }
        }
    }

    void destroy() {
        if (iconsMap != null) {
            for (View view : iconsMap.values()) {
                if (view instanceof MraidView) {
                    ((MraidView) view).destroy();
                }
            }
            iconsMap = null;
        }
        listener = null;
        setVisibility(View.GONE);
        removeAllViews();
    }
}
