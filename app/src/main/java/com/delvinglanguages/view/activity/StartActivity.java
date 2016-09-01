package com.delvinglanguages.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.net.CredentialsManager;
import com.delvinglanguages.net.SyncManager;

public class StartActivity extends Activity {

    private CredentialsManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_start);

        findViewById(R.id.option_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onCreateSelected();
            }
        });
        findViewById(R.id.option_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onImportSelected();
            }
        });
        findViewById(R.id.option_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onSyncSelected();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        setResult(AppCode.START_ABORTED);
        super.onBackPressed();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            switch (intent.getExtras().getInt(AppCode.ACTION_SYNC)) {
                case AppCode.SYNC_START:
                    dialog.setMessage(getString(R.string.msg_connecting_to_server));
                    break;
                case AppCode.SYNC_SAVING:
                    dialog.setMessage(getString(R.string.msg_synchronizing));
                    break;
                case AppCode.SYNC_OK:
                    dialog.setMessage(getString(R.string.done));
                    dialog.dismiss();

                    unregisterReceiver(broadcastReceiver);

                    KernelManager km = new KernelManager(StartActivity.this);
                    km.invalidateData();
                    if (!km.getLanguages().isEmpty()) {
                        StartActivity.this.setResult(AppCode.RESULT_SYNC_DONE);
                        finish();
                    } else {
                        Toast.makeText(StartActivity.this, R.string.msg_no_languages_found, Toast.LENGTH_LONG).show();
                    }

                    break;
                case AppCode.SYNC_NO_INTERNET:
                    dialog.dismiss();
                    unregisterReceiver(broadcastReceiver);

                    new AlertDialog.Builder(StartActivity.this)
                            .setMessage(R.string.msg_no_internet_connection)
                            .setPositiveButton(R.string.dismiss, null)
                            .show();
                    break;
            }
        }
    };

    private ProgressDialog dialog;

    public void onSyncSelected()
    {
        AppSettings.setLastSynchronization(0);
        if (syncManager == null)
            syncManager = new CredentialsManager(this);

        if (!syncManager.hasPermissions())
            syncManager.askPermissions(null);

        else if (!syncManager.hasCredentials())
            syncManager.askCredentials(null);

        else {
            registerReceiver(broadcastReceiver, new IntentFilter(AppCode.ACTION_SYNC));

            AppSettings.setOnlineBackUpState(true);
            SyncManager syncService = new SyncManager(this);
            syncService.synchronize();
            //handle result receiving
            dialog = ProgressDialog.show(this, "Full upload", "Starting service...", true, false, null);

        }
    }

    public void onImportSelected()
    {
        setResult(AppCode.ACTION_IMPORT);
        finish();
    }

    public void onCreateSelected()
    {
        setResult(AppCode.ACTION_CREATE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        syncManager.onActivityResult(requestCode, resultCode, data);

        onSyncSelected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        syncManager.onRequestPermissionsResult(requestCode, grantResults);

        if (syncManager.hasPermissions())
            onSyncSelected();
    }

}
