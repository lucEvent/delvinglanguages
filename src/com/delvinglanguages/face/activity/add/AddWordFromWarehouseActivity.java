package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.net.internal.Messages;

public class AddWordFromWarehouseActivity extends AddWordActivity implements
		Messages {

	private Nota nota;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		remove = ((Button) findViewById(R.id.addmore));
		remove.setText(R.string.remove);
		nota = ControlCore.getStore().get(
				getIntent().getExtras().getInt(STORE_NOTE));
		word.setText(nota.get());
		tranlation.requestFocus();
	}

	@Override
	public void secondOption(View v) {
		ControlCore.removeFromStore(nota);
		finish();
	}

	@Override
	protected void saveWord(String nombre, String trans, String pron, int type) {
		if (autocomplete) {
			ControlCore.removeFromStore(nota);
			ControlCore.updatePalabra(modifiedWord, nombre, trans, pron, type);
			showMessage(R.string.msswordmodified);
			return;
		}
		ControlCore.addWordFromStore(nota, nombre, trans, pron, type);
		showMessage(R.string.msswordadded);
	}

}
