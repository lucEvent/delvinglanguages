package com.delvinglanguages.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.Main;
import com.delvinglanguages.R;
import com.delvinglanguages.data.BackUpManager;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.view.lister.LanguageCheckBoxLister;
import com.delvinglanguages.view.utils.DelvingListListener;
import com.delvinglanguages.view.utils.MessageListener;

public class BackUpActivity extends Activity {

    public static final String ACTION_IMPORT = "com.delvinglanguages.backup.IMPORT";
    public static final String ACTION_EXPORT = "com.delvinglanguages.backup.EXPORT";

    private static final int REQUEST_PERMISSION_WRITE_IN_STORAGE = 222;

    private static final int IMPORT = 0;
    private static final int EXPORT = 1;

    private int action;
    private boolean done;

    private ScrollView scrollbox;
    private TextView console;
    private View buttons;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_backup);

        console = (TextView) findViewById(R.id.console);
        console.setMovementMethod(new ScrollingMovementMethod());
        scrollbox = (ScrollView) findViewById(R.id.scrollbox);
        buttons = findViewById(R.id.import_buttons);

        done = false;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE_IN_STORAGE);

        } else {
            actionStart();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_IN_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!.
                    actionStart();

                } else {

                    // permission denied, boo!
                    Toast.makeText(this, R.string.msg_permission_for_backup_denied, Toast.LENGTH_SHORT).show();
                    finish();

                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        setChanges();
        super.onBackPressed();
    }

    private void actionStart()
    {
        String action = getIntent().getAction();
        switch (action) {
            case ACTION_IMPORT:

                this.action = IMPORT;
                importAction();

                break;
            case ACTION_EXPORT:

                this.action = EXPORT;
                exportAction();

                break;
            default:
                finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMPORT) {
                final Uri uri = data.getData();
                if (uri == null) {
                    handler.obtainMessage(MessageListener.ERROR, getString(R.string.error)).sendToTarget();
                } else if (!uri.getPath().endsWith(".delv")) {
                    handler.obtainMessage(MessageListener.ERROR, getString(R.string.msg_not_dl_file)).sendToTarget();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            new BackUpManager(handler).restoreData(BackUpActivity.this, uri);
                        }
                    }).start();

                }
            }

        } else {
            handler.obtainMessage(MessageListener.ERROR, getString(R.string.error)).sendToTarget();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case MessageListener.ERROR:
                    if (msg.obj != null)
                        console.append(Html.fromHtml("<span style='color:red'>" + msg.obj + "</span><br><br>"));
                    console.append(Html.fromHtml("<span style='color:red'>" + getString(R.string.msg_error_backup) + "</span><br><br>"));
                    displayButtons();
                    break;
                case MessageListener.MESSAGE:
                    console.append(Html.fromHtml("<span style='color:#236990'>" + msg.obj + "</span><br><br>"));
                    break;
                case MessageListener.MESSAGE_INT:
                    console.append(Html.fromHtml("<span style='color:#009900'>" + getString((int) msg.obj) + "</span><br><br>"));
                    break;
                case AppCode.IMPORT_SUCCESSFUL:
                    console.append(Html.fromHtml("<span style='color:#009900'>" + getString(R.string.msg_import_finished) + "</span>"));
                    displayButtons();
                    done = true;
                    break;
                case AppCode.EXPORT_SUCCESSFUL:
                    console.append(Html.fromHtml("<span style='color:#009900'>" + getString(R.string.msg_export_finished) + "</span>"));
                    displayButtons();
                    done = true;
            }
            scrollbox.fullScroll(View.FOCUS_DOWN);
        }
    };

    public void more(View view)
    {
        if (action == IMPORT)
            importAction();
        else if (action == EXPORT)
            exportAction();


        console.setText("");
        buttons.setVisibility(View.INVISIBLE);
    }

    public void done(View view)
    {
        setChanges();
        finish();
    }

    private void setChanges()
    {
        if (done && action == IMPORT) {
            Main.handler.obtainMessage(DelvingListListener.LIST_RECOVERED).sendToTarget();
            setResult(AppCode.RESULT_IMPORT_DONE);
        } else
            setResult(AppCode.RESULT_IMPORT_CANCELED);
    }

    private void displayButtons()
    {
        buttons.setVisibility(View.VISIBLE);

        Button more = (Button) buttons.findViewById(R.id.more);
        if (action == IMPORT)
            more.setText(R.string.import_more);
        else if (action == EXPORT)
            more.setText(R.string.export_more);
    }

    private void importAction()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, IMPORT);
    }

    private void exportAction()
    {
        KernelManager dataManager = new KernelManager(this);

        ListView list = new ListView(this);
        export_adapter = new LanguageCheckBoxLister(this, dataManager.getDelvingLists());
        list.setAdapter(export_adapter);
        list.setOnItemClickListener(null);

        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_select_languages_backup)
                .setView(list)
                .setNegativeButton(R.string.cancel, onClickListenerDialog1)
                .setPositiveButton(R.string.proceed, onClickListenerDialog1)
                .setCancelable(false)
                .create()
                .show();
    }

    private DialogInterface.OnClickListener onClickListenerDialog1 = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if (which == Dialog.BUTTON_NEGATIVE) {
                finish();
            } else {

                DelvingLists allDelvingLists = new KernelManager(BackUpActivity.this).getDelvingLists();
                boolean[] checks = export_adapter.getChecks();
                export_delvingLists = new DelvingLists();
                for (int i = 0; i < checks.length; i++)
                    if (checks[i])
                        export_delvingLists.add(allDelvingLists.get(i));

                if (export_delvingLists.isEmpty()) {

                    handler.obtainMessage(MessageListener.ERROR, getString(R.string.msg_no_language_selected)).sendToTarget();

                } else {

                    View view = getLayoutInflater().inflate(R.layout.d_input, null);
                    export_input = (EditText) view.findViewById(R.id.input);
                    export_input.setHint(R.string.hint_enter_backup_name);

                    new AlertDialog.Builder(BackUpActivity.this)
                            .setView(view)
                            .setNegativeButton(R.string.cancel, onClickListenerDialog2)
                            .setPositiveButton(R.string.proceed, onClickListenerDialog2)
                            .setCancelable(false)
                            .create()
                            .show();
                }
            }
        }
    };

    private DelvingLists export_delvingLists;
    private LanguageCheckBoxLister export_adapter;
    private EditText export_input;

    private DialogInterface.OnClickListener onClickListenerDialog2 = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if (which == Dialog.BUTTON_NEGATIVE) {
                finish();
            } else {

                final String filename = export_input.getText().toString();
                if (filename.isEmpty()) {

                    handler.obtainMessage(MessageListener.ERROR, getString(R.string.msg_missing_filename)).sendToTarget();

                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            new BackUpManager(handler).backupData(BackUpActivity.this, filename, export_delvingLists);
                        }
                    }).start();

                }
            }
        }
    };

}
