package com.appodeal.vast;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
                View iconView = icon.getView(context);
                iconView.setVisibility(GONE);
                iconView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onIconClicked(icon, null);
                    }
                });
                iconsMap.put(icon, iconView);
                addView(iconView);
            }
        }
    }

    void updateIcons(int playerPosition) {
        if (iconsMap != null && iconsMap.size() > 0) {
            Set<Icon> set = iconsMap.keySet();
            for (Icon icon : set) {
                if (icon.shouldShow(playerPosition) && iconsMap.get(icon) != null && iconsMap.get(icon).getVisibility() != VISIBLE) {
                    iconsMap.get(icon).setVisibility(VISIBLE);
                    listener.onIconShown(icon);
                } else if (!icon.shouldShow(playerPosition) && iconsMap.get(icon) != null && iconsMap.get(icon).getVisibility() == VISIBLE) {
                    iconsMap.get(icon).setVisibility(GONE);
                }
            }
        }
    }

    void destroy() {
        iconsMap = null;
        listener = null;
    }
}
