package com.delvinglanguages.face.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.face.activity.add.AddWordFromModifyActivity;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.listers.InflexionLister;
import com.delvinglanguages.listers.WordLister;
import com.delvinglanguages.settings.Settings;

public class ReferenceActivity extends ListActivity implements OnInitListener, OnClickListener {

    private static final int REQUEST_MODIFIED = 0;

    private DReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_word, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        Language idioma = KernelControl.getCurrentLanguage();
        reference = idioma.getReference(getIntent().getExtras().getString(AppCode.DREFERENCE));

        displayReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFIED) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, null);

                String ref = (String) data.getExtras().get(AppCode.EDITED);
                reference = LanguageKernelControl.getReference(ref.split(",")[0]);
                if (reference == null) {
                    finish();
                }
                displayReference();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        speechEngine = new TextToSpeech(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        speechEngine.stop();
        speechEngine.shutdown();
    }

    private void displayReference() {
        setTitle(reference.name);

        ((TextView) findViewById(R.id.word)).setText(reference.name);
        ((TextView) findViewById(R.id.pronuntiation)).setText("[ " + reference.pronunciation + " ]");

        setListAdapter(new InflexionLister(this, reference.getInflexions(), false, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reference, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_edit:
                if (reference.appearances.size() == 1) {
                    editAction(reference.appearances.first());
                    return true;
                }
                chooseWordToEdit();
                break;
            case R.id.m_remove:
                if (reference.appearances.size() == 1) {
                    removeAction(reference.appearances.first());
                    return true;
                }
                chooseWordToRemove();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void chooseWordToEdit() {
        ListView view = new ListView(this);
        view.setAdapter(new WordLister(this, new Words(reference.appearances)));
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                editAction((Word) v.getTag());
            }

        });

        Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(getString(R.string.edit));
        builder.create().show();
    }

    private void chooseWordToRemove() {
        ListView view = new ListView(this);
        view.setAdapter(new WordLister(this, new Words(reference.appearances)));
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                removeAction((Word) v.getTag());
            }

        });

        Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(getString(R.string.remove));
        builder.create().show();
    }

    private void editAction(Word wordtoedit) {
        Intent intent = new Intent(this, AddWordFromModifyActivity.class);
        intent.putExtra(AppCode.WORD, wordtoedit.id);
        startActivityForResult(intent, REQUEST_MODIFIED);
    }

    private void removeAction(final Word wordtoremove) {
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_removing));
        builder.setMessage(R.string.removewordquestion);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                remove(wordtoremove);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void remove(Word wordtoremove) {
        KernelControl.deleteWordTemporarily(wordtoremove);
        setResult(Activity.RESULT_OK, null);
        finish();
    }

    @Override
    public void onClick(View v) {
        String name = ((TextView) v.findViewById(R.id.text)).getText().toString();
        if (true) return;//de momento
// TODO: 31/12/2015  
        //  KernelControl.switchDictionary();
        Intent intent = new Intent(this, ReferenceActivity.class);
        intent.putExtra(AppCode.DREFERENCE, name);
        startActivity(intent);

    }

    private TextToSpeech speechEngine;

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            speechEngine.setLanguage(KernelControl.getCurrentLanguage().getLocale());
        }
    }

    public void onSpeech(View view) {
        speechEngine.speak(reference.name, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##ReferenceActivity##", text);
    }

}
