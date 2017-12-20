package com.appodeal.iab.vast;

import java.util.ArrayList;
import java.util.List;

class VideoClicks {

	private String clickThrough;
	private List<String> clickTracking = new ArrayList<>();
	private List<String> customClick = new ArrayList<>();

	String getClickThrough() {
		return clickThrough;
	}

	void setClickThrough(String clickThrough) {
		this.clickThrough = clickThrough;
	}


    void addClickTracking(String url) {
        clickTracking.add(url);
    }

	List<String> getClickTracking() {
		return clickTracking;
	}

	void addCustomClick(String url) {
        customClick.add(url);
    }

	List<String> getCustomClick() {
		return customClick;
	}
}
