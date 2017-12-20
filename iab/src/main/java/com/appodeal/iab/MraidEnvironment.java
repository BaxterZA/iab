package com.appodeal.iab;


import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * MRAID_ENV object to facilitate the early passing of version and other relevant attributes at initialization time
 */
public class MraidEnvironment {
    private final static String TAG = "MraidEnvironment";

    public final static String version = "3.0";
    public final static String sdk = "Appodeal MRAID SDK";
    public final static String sdkVersion = "0.0.1";
    @Nullable private final String appId;
    @Nullable private final String ifa;
    @Nullable private final Boolean limitAdTracking;
    @Nullable private final Boolean coppa;

    private MraidEnvironment(Builder builder) {
        this.appId = builder.appId;
        this.ifa = builder.ifa;
        this.limitAdTracking = builder.limitAdTracking;
        this.coppa = builder.coppa;
    }

    public String toMraidString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", version);
            jsonObject.put("sdk", sdk);
            jsonObject.put("sdkVersion", sdkVersion);
            jsonObject.put("appId", appId);
            jsonObject.put("ifa", ifa);
            jsonObject.put("limitAdTracking", limitAdTracking);
            jsonObject.put("coppa", coppa);
        } catch (JSONException e) {
            Logger.e(TAG, e);
        }
        return jsonObject.toString();
    }

    /**
     * MraidEnvironment builder
     */
    public static class Builder {
        private String appId;
        private String ifa;
        private Boolean limitAdTracking;
        private Boolean coppa;

        public Builder() {
        }

        /**
         *
         * @param appId  The package name or application ID of the app running this ad. Usually referred to as the bundle id
         * @return it self
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         *
         * @param ifa The user identifier for advertising purposes. For Android, this must be the Google Advertising ID (AID)
         * @return it self
         */
        public Builder setIfa(String ifa) {
            this.ifa = ifa;
            return this;
        }

        /**
         *
         * @param limitAdTracking  {@code true} if limit ad tracking is enabled, {@code false} otherwise
         * @return it self
         */
        public Builder setLimitAdTracking(boolean limitAdTracking) {
            this.limitAdTracking = limitAdTracking;
            return this;
        }

        /**
         *
         * @param coppa {@code true} for child-directed, {@code false} otherwise
         * @return it self
         */
        public Builder setCoppa(boolean coppa) {
            this.coppa = coppa;
            return this;
        }

        /**
         * Create {@link MraidEnvironment}
         * @return new MraidEnvironment object
         */
        public MraidEnvironment build() {
            return new MraidEnvironment(this);
        }
    }
}
