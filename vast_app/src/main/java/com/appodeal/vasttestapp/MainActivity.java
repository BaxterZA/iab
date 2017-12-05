package com.appodeal.vasttestapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TestsAdapter.ClickListener {
    private List<String> bannersFilesList;
    private List<String> interstitialFilesList;
    private TestsAdapter adapter;
    private String prefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        fillTestFiles();
        RecyclerView recyclerView = findViewById(R.id.tests_list);
        adapter = new TestsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.fill(bannersFilesList);
        prefix = "view";

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_view:
                        prefix = "view";
                        adapter.fill(bannersFilesList);
                        break;
                    case R.id.action_fullscreen:
                        prefix = "fullscreen";
                        adapter.fill(interstitialFilesList);
                        break;
                }
                return true;
            }
        });
    }

    private void fillTestFiles() {
        bannersFilesList = new ArrayList<>();
        interstitialFilesList = new ArrayList<>();
        String[] files = null;
        try {
            files = getResources().getAssets().list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (files != null) {
            for (String file : files) {
                if (file.startsWith("view")) {
                    bannersFilesList.add(processName("view", file));
                } else if (file.startsWith("fullscreen")) {
                    interstitialFilesList.add(processName("fullscreen", file));
                }
            }
        }
    }

    private String processName(String prefix, String name) {
        return name.replace(prefix + ".", "").replace(".xml", "");
    }

    private String getFileName(String prefix, String name) {
        return prefix + "." + name + ".xml";
    }

    @Override
    public void onItemClick(String name) {
        switch (prefix) {
            case "view":
                Intent intent = new Intent(this, ViewActivity.class);
                intent.putExtra("file", getFileName(prefix, name));
                startActivity(intent);
                break;
            case "fullscreen":
                intent = new Intent(this, FullScreenActivity.class);
                intent.putExtra("file", getFileName(prefix, name));
                startActivity(intent);
                break;
        }
    }
}
