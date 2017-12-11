package com.appodeal.vasttestapp;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appodeal.vast.VastViewListener;
import com.appodeal.vast.VastView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ViewActivity extends AppCompatActivity {
    private String file;
    private VastView vastView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        file = getIntent().getStringExtra("file");
    }

    public void load(View view) {
        String xml = readXml(file);
        vastView = findViewById(R.id.vastView);
//        vastView = new VastView(this);
        vastView.setVastViewListener(new VastViewListener() {
            @Override
            public void onVastLoaded(VastView view) {
                Toast.makeText(ViewActivity.this, "onVastLoaded", Toast.LENGTH_SHORT).show();
//                addContentView(vastView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            @Override
            public void onVastFailedToLoad(VastView view) {
                Toast.makeText(ViewActivity.this, "onVastFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastFailedToShow(VastView view) {
                Toast.makeText(ViewActivity.this, "onVastFailedToShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastShown(VastView view) {
                Toast.makeText(ViewActivity.this, "onVastShown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastClicked(VastView view, String url) {
                Toast.makeText(ViewActivity.this, String.format("onVastClicked, url: %s", url), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVastFinished(VastView view) {
                Toast.makeText(ViewActivity.this, "onVastFinished", Toast.LENGTH_SHORT).show();
            }
        });
        vastView.loadXml(xml);
    }

    public void destroy(View view) {
        if (vastView != null) {
            vastView.destroy();
        }
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
