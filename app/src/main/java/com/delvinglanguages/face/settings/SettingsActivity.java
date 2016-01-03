package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;

import com.delvinglanguages.R;
import com.delvinglanguages.data.BackUp;
import com.delvinglanguages.listers.AvailableLanguageLister;
import com.delvinglanguages.settings.Settings;

public class SettingsActivity extends Activity implements OnItemSelectedListener {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_app_settings);

        spinner = (Spinner) findViewById(R.id.selector);
        spinner.setAdapter(new AvailableLanguageLister(this, getResources().getStringArray(R.array.languages)));
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(Settings.NativeLanguageCode);
    }

    @Override
    public void onResume() {
        super.onResume();

        Settings.setBackgroundTo(findViewById(android.R.id.content));

        CheckedTextView vkb = (CheckedTextView) findViewById(R.id.vibratekb_state);
        vkb.setChecked(Settings.vibration());
    }

    public void vibratekb_state(View v) {
        ((CheckedTextView) v).toggle();
        Settings.toggleVibration();

    }

    public void fonetics(View v) {
        startActivity(new Intent(this, FoneticsActivity.class));
    }

    public void background(View v) {
        startActivity(new Intent(this, SelectBackground.class));
    }

    public void createBackup(View v) {
        new BackUp().createBackUp(this);
    }

    public void recoverBackup(View v) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                new BackUp().recoverBackUp(SettingsActivity.this);
            }
        }).start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Settings.setNativeLanguage(pos, getResources().getStringArray(R.array.languages)[pos]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

}