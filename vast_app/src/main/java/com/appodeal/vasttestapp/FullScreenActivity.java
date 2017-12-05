package com.appodeal.vasttestapp;


import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.appodeal.vast.VastInterstitial;
import com.appodeal.vast.VastInterstitialListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FullScreenActivity extends AppCompatActivity {
    private VastInterstitial vastInterstitial;
    private String file;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.fullscreen_layout);

        file = getIntent().getStringExtra("file");
        vastInterstitial = null;
        updateViews();
    }

    public void load(View view) {
        String xml = readXml(file);

        vastInterstitial = new VastInterstitial(this);
        vastInterstitial.setVastInterstitialListener(new VastInterstitialListener() {
            @Override
            public void onVastLoaded(VastInterstitial vastInterstitial) {
                Toast.makeText(FullScreenActivity.this, "onVastLoaded", Toast.LENGTH_SHORT).show();
                updateViews();
            }

            @Override
            public void onVastFailedToLoad(VastInterstitial vastInterstitial) {
                Toast.makeText(FullScreenActivity.this, "onVastFailedToLoad", Toast.LENGTH_SHORT).show();
                updateViews();

            }

            @Override
            public void onVastFailedToShow(VastInterstitial vastInterstitial) {
                Toast.makeText(FullScreenActivity.this, "onVastFailedToShow", Toast.LENGTH_SHORT).show();
                updateViews();
            }

            @Override
            public void onVastShown(VastInterstitial vastInterstitial) {
                Toast.makeText(FullScreenActivity.this, "onVastShown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastClicked(VastInterstitial vastInterstitial, String url) {
                Toast.makeText(FullScreenActivity.this, String.format("onVastClicked, url: %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastFinished(VastInterstitial vastInterstitial) {
                Toast.makeText(FullScreenActivity.this, "onVastFinished", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastClosed(VastInterstitial vastInterstitial) {
                Toast.makeText(FullScreenActivity.this, "onVastClosed", Toast.LENGTH_SHORT).show();
                updateViews();
            }
        });
        vastInterstitial.loadXml(xml);
        view.setEnabled(false);
    }

    public void show(View view) {
        vastInterstitial.show();
    }

    public void destroy(View view) {
        vastInterstitial.destroy();
        vastInterstitial = null;
        updateViews();
    }

    private void updateViews() {
        findViewById(R.id.load).setEnabled(vastInterstitial == null || !vastInterstitial.isLoaded());
        findViewById(R.id.show).setEnabled(vastInterstitial != null && vastInterstitial.isLoaded());
        findViewById(R.id.destroy).setEnabled(vastInterstitial != null && !vastInterstitial.isDestroyed());
    }

    private String readXml(String fileName) {
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
