package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Settings;

public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_about, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        WebView wview = (WebView) view;
        wview.setPersistentDrawingCache(WebView.PERSISTENT_NO_CACHE);
        WebSettings webSettings = wview.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        wview.loadData(getString(R.string.about_content), "text/html", "utf-8");
    }

}
