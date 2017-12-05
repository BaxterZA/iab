package com.appodeal.vast;


import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

class TrackingTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {

        String url = params[0];
        HttpURLConnection connection = null;
        try {
            VastLog.d("connection to URL:" + url);
            URL httpUrl = new URL(url);

            HttpURLConnection.setFollowRedirects(true);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Connection", "close");
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            VastLog.d(String.format("Response code: %s, for URL: %s", code, url));
        } catch (Exception e) {
            VastLog.d(e.getMessage());
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
