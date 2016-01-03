package com.delvinglanguages.face.activity.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Translations;

public class AddWordFromModifyActivity extends AddWordActivity {

    private Word palabra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        word.removeTextChangedListener(this);
        findViewById(R.id.addmore).setVisibility(View.GONE);
        ((Button) findViewById(R.id.add)).setText(R.string.savechanges);

        int id = getIntent().getExtras().getInt(SEND_WORD);
        palabra = idioma.getWordById(id);

        String name = palabra.getName();
        word.setText(name);
        word.setSelection(name.length());

        translations.addAll(palabra.getTranslations());
        pronuntiation.setText(palabra.getPronunciation());
        setTitle(R.string.modifyingword);

    }

    @Override
    protected void saveWord(String nombre, Translations translations, String pron) {
        KernelControl.updateWord(palabra, nombre, translations, pron);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EDITED, nombre);
        setResult(Activity.RESULT_OK, resultIntent);
        showMessage(R.string.msswordadded);
    }

}
