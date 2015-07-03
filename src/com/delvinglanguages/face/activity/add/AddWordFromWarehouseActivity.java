package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.DrawerWord;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.net.internal.Messages;

public class AddWordFromWarehouseActivity extends AddWordActivity implements Messages {

	private DrawerWord nota;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		remove = ((Button) findViewById(R.id.addmore));
		remove.setText(R.string.remove);
		nota = LanguageKernelControl.getDrawerWords().get(getIntent().getExtras().getInt(STORE_NOTE));
		word.setText(nota.get());
	}

	@Override
	public void secondOption(View v) {
		KernelControl.removeFromStore(nota);
		finish();
	}

	@Override
	protected void saveWord(String nombre, Translations translations, String pron) {
		if (autocomplete) {
			KernelControl.removeFromStore(nota);
			KernelControl.updateWord(modifiedWord, nombre, translations, pron);
			showMessage(R.string.msswordmodified);
			return;
		}
		KernelControl.addWord(nota, nombre, translations, pron);
		showMessage(R.string.msswordadded);
	}

}
