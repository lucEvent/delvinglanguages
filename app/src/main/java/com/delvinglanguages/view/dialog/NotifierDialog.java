package com.delvinglanguages.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.delvinglanguages.R;

public class NotifierDialog {

    private Context context;

    public NotifierDialog(Context context) {
        this.context = context;
    }

    public void notify_languageTTSnotAvailable(String language) {

        View view = LayoutInflater.from(context).inflate(R.layout.d_notifier_tts, null);

        new AlertDialog.Builder(context)
                .setView(view)
                .setMessage(context.getString(R.string.msg_notifier_tts, language))
                .setPositiveButton(R.string.open_google_play, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=tts")));

                    }
                })
                .setNegativeButton(R.string.open_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setAction("com.android.settings.TTS_SETTINGS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                })
                .setNeutralButton(R.string.dismiss, null)
                .create()
                .show();
    }
}
