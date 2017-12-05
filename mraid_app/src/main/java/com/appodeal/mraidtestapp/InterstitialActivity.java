package com.appodeal.mraidtestapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.widget.Toast;

import com.appodeal.mraid.MraidInterstitial;
import com.appodeal.mraid.MraidInterstitialListener;
import com.appodeal.mraid.MraidNativeFeature;
import com.appodeal.mraid.MraidNativeFeatureListener;
import com.appodeal.mraid.MraidWebViewDebugListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InterstitialActivity extends AppCompatActivity {
    private MraidInterstitial mraidInterstitial;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.interstitial_layout);
        mraidInterstitial = new MraidInterstitial(this);
        String file = getIntent().getStringExtra("file");
        String html = readHtml(file);
        mraidInterstitial.setHtml(html);
        if (file.toLowerCase().contains("jstag")) {
            mraidInterstitial.setJsTag(true);
        }
        mraidInterstitial.setMraidInterstitialListener(new MraidInterstitialListener() {
            @Override
            public void onMraidInterstitialLoaded(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialLoaded", Toast.LENGTH_SHORT).show();
                InterstitialActivity.this.findViewById(R.id.show).setEnabled(true);
            }

            @Override
            public void onMraidInterstitialFailedToLoad(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidInterstitialShown(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialShown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidInterstitialFailedToShow(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialFailedToShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidInterstitialUnloaded(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialUnloaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidInterstitialClicked(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMraidInterstitialClosed(MraidInterstitial mraidInterstitial) {
                Toast.makeText(InterstitialActivity.this, "onMraidInterstitialClosed", Toast.LENGTH_SHORT).show();
            }
        });

        List<MraidNativeFeature> nativeFeatureList = new ArrayList<>();
        nativeFeatureList.add(MraidNativeFeature.SMS);
        nativeFeatureList.add(MraidNativeFeature.TEL);
        nativeFeatureList.add(MraidNativeFeature.CALENDAR);
        nativeFeatureList.add(MraidNativeFeature.STORE_PICTURE);
        nativeFeatureList.add(MraidNativeFeature.INLINE_VIDEO);
        nativeFeatureList.add(MraidNativeFeature.LOCATION);
        nativeFeatureList.add(MraidNativeFeature.VPAID);
        mraidInterstitial.setSupportedFeatures(nativeFeatureList, new MraidNativeFeatureListener() {
            @Override
            public void mraidNativeFeatureSendSms(String url) {
                Toast.makeText(InterstitialActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureSendSms - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureCallTel(String url) {
                Toast.makeText(InterstitialActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureCallTel - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {
                Toast.makeText(InterstitialActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureCreateCalendarEvent - %s", eventJSON), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeaturePlayVideo(String url) {
                Toast.makeText(InterstitialActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeaturePlayVideo - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureStorePicture(String url) {
                Toast.makeText(InterstitialActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureStorePicture - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void mraidNativeFeatureOpenBrowser(String url) {
                Toast.makeText(InterstitialActivity.this, String.format("MraidNativeFeatureListener: mraidNativeFeatureOpenBrowser - %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public Location mraidNativeFeatureGetLocation() {
                Toast.makeText(InterstitialActivity.this, "MraidNativeFeatureListener: mraidNativeFeatureGetLocation", Toast.LENGTH_SHORT).show();
                Location stubLocation =  new Location("test");
                stubLocation.setLatitude(10.1);
                stubLocation.setLongitude(50.123);
                return stubLocation;
            }
        });

        mraidInterstitial.setMraidWebViewDebugListener(new MraidWebViewDebugListener() {
            @Override
            public boolean onJsAlert(@NonNull String message, @NonNull JsResult result) {
                new AlertDialog.Builder(InterstitialActivity.this).setMessage(message).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(true).create().show();
                result.cancel();
                return true;
            }

            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
//                Toast.makeText(InterstitialActivity.this, String.format("onConsoleMessage - %s", consoleMessage.message()), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        
        mraidInterstitial.load();
    }

    public void show(View view) {
        mraidInterstitial.show();
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
