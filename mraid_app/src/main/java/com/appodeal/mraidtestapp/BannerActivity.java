package com.appodeal.mraidtestapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.Toast;

import com.appodeal.mraid.MraidNativeFeature;
import com.appodeal.mraid.MraidNativeFeatureListener;
import com.appodeal.mraid.MraidView;
import com.appodeal.mraid.MraidViewListener;
import com.appodeal.mraid.MraidWebViewDebugListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_layout);

        String file = getIntent().getStringExtra("file");
        MraidView mraidView = findViewById(R.id.mraidView);
        String html = readHtml(file);
        mraidView.setHtml(html);
        if (file.toLowerCase().contains("jstag")) {
            mraidView.setJsTag(true);
        }

        mraidView.setMraidViewListener(new MraidViewListener() {
            @Override
            public void onMraidViewLoaded(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidViewFailedToLoad(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidViewUnloaded(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewUnloaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidViewClicked(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidViewExpanded(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewExpanded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidViewResized(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewResized", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidViewClosed(MraidView mraidView) {
                Toast.makeText(BannerActivity.this, "onMraidViewClosed", Toast.LENGTH_SHORT).show();
            }
        });

        List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
        nativeFeatureList.add(MraidNativeFeature.SMS);
        nativeFeatureList.add(MraidNativeFeature.TEL);
        nativeFeatureList.add(MraidNativeFeature.CALENDAR);
        nativeFeatureList.add(MraidNativeFeature.STORE_PICTURE);
        nativeFeatureList.add(MraidNativeFeature.INLINE_VIDEO);
        nativeFeatureList.add(MraidNativeFeature.VPAID);
        nativeFeatureList.add(MraidNativeFeature.LOCATION);
        mraidView.setSupportedFeatures(nativeFeatureList, new MraidNativeFeatureListener() {
            @Override
            public void mraidNativeFeatureSendSms(String url) {
                Toast.makeText(BannerActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureSendSms - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureCallTel(String url) {
                Toast.makeText(BannerActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureCallTel - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {
                Toast.makeText(BannerActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureCreateCalendarEvent - %s", eventJSON), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeaturePlayVideo(String url) {
                Toast.makeText(BannerActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeaturePlayVideo - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureStorePicture(String url) {
                Toast.makeText(BannerActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureStorePicture - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureOpenBrowser(String url) {
                Toast.makeText(BannerActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureOpenBrowser - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public Location mraidNativeFeatureGetLocation() {
                Toast.makeText(BannerActivity.this, "MraidNativeFeatureListener: mraidNativeFeatureGetLocation", Toast.LENGTH_SHORT).show();
                Location stubLocation = new Location("test");
                stubLocation.setLatitude(10.1);
                stubLocation.setLongitude(50.123);
                return stubLocation;
            }
        });

        mraidView.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
            @Override
            public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                new AlertDialog.Builder(BannerActivity.this).setMessage(message).setNegativeButton("Cnacel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(true).create().show();
                result.cancel();
                return true;
            }

            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
//                Toast.makeText(BannerActivity.this, String.format("onConsoleMessage - %s", consoleMessage.message()), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mraidView.load();
    }

    private String readHtml(String fileName) {
        BufferedReader reader = null;
        StringBuilder returnString = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                returnString.append(mLine);
                returnString.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnString.toString();
    }
}
