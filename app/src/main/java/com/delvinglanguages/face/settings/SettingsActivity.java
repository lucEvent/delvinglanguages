package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.data.BackUp;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.listers.AvailableLanguageLister;
import com.delvinglanguages.settings.Settings;

import java.io.File;

public class SettingsActivity extends Activity implements OnItemSelectedListener {

    private final static int REQUEST_COLOR = 0;
    private final static int REQUEST_GALLERY = 1;
    private final static int REQUEST_CAMERA = 2;

    private Spinner spinner;
    private Dialog dialog;

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
        dialog = new AlertDialog.Builder(this)
                .setView(getLayoutInflater().inflate(R.layout.d_select_background_options, null))
                .show();
    }

    public void onSelectBackgroundAction(View v) {
        dialog.dismiss();
        switch (v.getId()) {
            case R.id.option_color:
                Intent color = new Intent(this, SelectBackgroundColor.class);
                startActivityForResult(color, REQUEST_COLOR);
                break;
            case R.id.option_gallery:
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, REQUEST_GALLERY);
                break;
            case R.id.option_camera:
                imagePath = Settings.createDLFilePath();
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
                startActivityForResult(camera, REQUEST_CAMERA);
                break;
        }
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
                finalizedRecovering();
            }
        }).start();
    }

    private void finalizedRecovering() {
        setResult(AppCode.LANGUAGE_RECOVERED);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Settings.setNativeLanguage(pos, getResources().getStringArray(R.array.languages)[pos]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private String imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GALLERY:
                    Settings.setBackground(intent.getData().getPath());
                case REQUEST_CAMERA:
                    Settings.setBackgroundImagePath(imagePath);
                    break;
            }
            Settings.setBackgroundTo(findViewById(R.id.root));
            showMessage(R.string.backgroundchanged);
            setResult(AppCode.BACKGROUND_CHANGED);
        }
    }

    private void showMessage(int text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

}