package com.delvinglanguages.face.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;

public class SpecialKeysBar implements OnClickListener {

	private EditText[] inputs;

	public SpecialKeysBar(View parent, EditText[] inputs) {
		this.inputs = inputs;
		IDDelved idioma = KernelControl.getCurrentLanguage();
		if (!idioma.getSettings(IDDelved.MASK_ESP_CHARS)) {
			return;
		}
		switch (idioma.CODE) {
		case IDDelved.SV:
		case IDDelved.FI:
			setUpButton(parent, R.id.sv_ao);
			setUpButton(parent, R.id.sv_ae);
			setUpButton(parent, R.id.sv_oe);
			break;
		case IDDelved.ES:
			setUpButton(parent, R.id.es_ene);
			break;
		}
	}

	private void setUpButton(View view, int res_id) {
		Button button = (Button) view.findViewById(res_id);
		button.setVisibility(Button.VISIBLE);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		EditText focused = null;
		for (EditText in : inputs) {
			if (in.isFocused()) {
				focused = in;
				break;
			}
		}
		String text = focused.getText().toString();
		if (text.isEmpty()) {
			text = (String) v.getTag();
		} else {
			text = text + ((Button) v).getText();
		}
		focused.setText(text);
		focused.setSelection(text.length());
	}

}
