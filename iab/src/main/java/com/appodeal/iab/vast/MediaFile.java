package com.appodeal.iab.vast;

import java.io.Serializable;

class MediaFile implements Serializable {
	private static final String SUPPORTED_VIDEO_TYPE_REGEX = "video/.*(?i)(mp4|3gpp)";

	private String url;
	private String id;
	private String delivery;
	private String type;
	private Integer bitrate;
	private Integer width;
	private Integer height;
	private Boolean scalable;
	private Boolean maintainAspectRatio;
	private String apiFramework;

	private MediaFile(Builder builder) {
		this.url = builder.url;
		this.id = builder.id;
		this.delivery = builder.delivery;
		this.type = builder.type;
		this.bitrate = builder.bitrate;
		this.width = builder.width;
		this.height = builder.height;
		this.scalable = builder.scalable;
		this.maintainAspectRatio = builder.maintainAspectRatio;
		this.apiFramework = builder.apiFramework;
	}

	String getUrl() {
		return url;
	}

	String getId() {
		return id;
	}

	String getDelivery() {
		return delivery;
	}

	String getType() {
		return type;
	}

	Integer getBitrate() {
		return bitrate;
	}

	Integer getWidth() {
		return width;
	}

	Integer getHeight() {
		return height;
	}

	Boolean getScalable() {
		return scalable;
	}

	Boolean getMaintainAspectRatio() {
		return maintainAspectRatio;
	}

	String getApiFramework() {
		return apiFramework;
	}

	boolean isValidVideoFile() {
		return (width != null && width > 0) && (height != null && height > 0) && delivery != null && type != null && isMediaFileCompatible();
	}

	private boolean isMediaFileCompatible() {
		return type.matches(SUPPORTED_VIDEO_TYPE_REGEX) || isValidVPAIDMediaFile();
	}

	boolean isValidVPAIDMediaFile() {
		return type != null && isJavascript(type) && apiFramework != null && apiFramework.equals("VPAID");
	}

	private boolean isJavascript(String type) {
		return ("application/javascript".equalsIgnoreCase(type)) ||
				("application/x-javascript".equalsIgnoreCase(type)) ||
				("text/javascript".equalsIgnoreCase(type));
	}


	static class Builder {
		private final String url;
		private String id;
		private String delivery;
		private String type;
		private Integer bitrate;
		private Integer width;
		private Integer height;
		private Boolean scalable;
		private Boolean maintainAspectRatio;
		private String apiFramework;

		Builder(String url) {
			this.url = url;
		}

		void setId(String id) {
			this.id = id;
		}

		void setDelivery(String delivery) {
			this.delivery = delivery;
		}

		void setType(String type) {
			this.type = type;
		}

		void setBitrate(Integer bitrate) {
			this.bitrate = bitrate;
		}

		void setWidth(Integer width) {
			this.width = width;
		}

		void setHeight(Integer height) {
			this.height = height;
		}

		void setScalable(Boolean scalable) {
			this.scalable = scalable;
		}

		void setMaintainAspectRatio(Boolean maintainAspectRatio) {
			this.maintainAspectRatio = maintainAspectRatio;
		}

		void setApiFramework(String apiFramework) {
			this.apiFramework = apiFramework;
		}
		
		MediaFile build() {
			return new MediaFile(this);
		}
	}
}