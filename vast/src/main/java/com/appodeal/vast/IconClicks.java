package com.appodeal.vast;

import java.util.ArrayList;
import java.util.List;

class IconClicks {

	private String clickThrough;
	private List<String> clickTracking = new ArrayList<>();

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
}
