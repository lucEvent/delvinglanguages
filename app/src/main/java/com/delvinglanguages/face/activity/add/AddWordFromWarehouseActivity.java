package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.kernel.DrawerWord;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.set.Inflexions;

public class AddWordFromWarehouseActivity extends AddWordActivity {

    private DrawerWord nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remove = ((Button) findViewById(R.id.addmore));
        remove.setText(R.string.remove);
        nota = LanguageKernelControl.getDrawerWords().get(getIntent().getExtras().getInt(AppCode.NOTE));
        word.setText(nota.name);
    }

    @Override
    public void secondOption(View v) {
        KernelControl.removeFromStore(nota);
        finish();
    }

    @Override
    protected void saveWord(String nombre, Inflexions inflexions, String pron) {
        if (autocomplete) {
            KernelControl.removeFromStore(nota);
            KernelControl.updateWord(modifiedWord, nombre, inflexions, pron);
            showMessage(R.string.msswordmodified);
            return;
        }
        KernelControl.addWord(nota, nombre, inflexions, pron);
        showMessage(R.string.msswordadded);
    }

}
