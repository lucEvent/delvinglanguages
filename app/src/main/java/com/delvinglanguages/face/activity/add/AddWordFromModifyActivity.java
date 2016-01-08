package com.delvinglanguages.face.activity.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Inflexions;

public class AddWordFromModifyActivity extends AddWordActivity {

    private Word palabra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        word.removeTextChangedListener(this);
        findViewById(R.id.addmore).setVisibility(View.GONE);
        ((Button) findViewById(R.id.add)).setText(R.string.savechanges);

        int id = getIntent().getExtras().getInt(AppCode.WORD);
        palabra = idioma.getWordById(id);

        String name = palabra.getName();
        word.setText(name);
        word.setSelection(name.length());

        inflexions.addAll(palabra.getInflexions());
        pronuntiation.setText(palabra.getPronunciation());
        setTitle(R.string.modifyingword);

    }

    @Override
    protected void saveWord(String nombre, Inflexions inflexions, String pron) {
        KernelControl.updateWord(palabra, nombre, inflexions, pron);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppCode.EDITED, nombre);
        setResult(Activity.RESULT_OK, resultIntent);
        showMessage(R.string.msswordadded);
    }

}
