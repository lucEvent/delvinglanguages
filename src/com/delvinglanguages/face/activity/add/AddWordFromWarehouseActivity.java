package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.face.activity.add.AddWordActivity;

public class AddWordFromWarehouseActivity extends AddWordActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		remove = ((Button) findViewById(R.id.addmore));
		remove.setText(R.string.remove);
		Nota nota = ControlCore.notaToModify;
		word.setText(nota.get());
		tranlation.requestFocus();
	}

	@Override
	public void secondOption(View v) {
		ControlCore.removeFromStore();
		finish();
	}

	@Override
	protected void saveWord(String nombre, String trans, String pron, int type) {
		if (autocomplete) {
			ControlCore.removeFromStore();
			ControlCore.updatePalabra(modifiedWord, nombre, trans, pron, type);
			showMessage(R.string.msswordmodified);
			return;
		}
		ControlCore.addWordFromStore(nombre, trans, pron, type);
		showMessage(R.string.msswordadded);
	}

}
