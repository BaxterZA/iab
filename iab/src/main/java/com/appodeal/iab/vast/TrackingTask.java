package com.appodeal.iab.vast;


import android.os.AsyncTask;

import com.appodeal.iab.Logger;

import java.net.HttpURLConnection;
import java.net.URL;

class TrackingTask extends AsyncTask<String, Void, Void> {
    private final static String TAG = "TrackingTask";
    @Override
    protected Void doInBackground(String... params) {

        String url = params[0];
        HttpURLConnection connection = null;
        try {
            Logger.d(TAG, "connection to URL:" + url);
            URL httpUrl = new URL(url);

            HttpURLConnection.setFollowRedirects(true);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Connection", "close");
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            Logger.d(TAG, String.format("Response code: %s, for URL: %s", code, url));
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ignore) {
                }
            }
        }
        return null;
    }
}
