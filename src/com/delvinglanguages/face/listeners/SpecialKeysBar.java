package com.delvinglanguages.face.listeners;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;

public class SpecialKeysBar {

	public SpecialKeysBar(Activity context, View parent) {
		IDDelved idioma = ControlCore.getIdiomaActual(context);
		if (!idioma.getSettings(IDDelved.MASK_ESP_CHARS)) {
			return;
		}
		switch (idioma.CODE) {
		case IDDelved.SV:
		case IDDelved.FI:
			Button � = (Button) context.findViewById(R.id.sv_ao);
			if (� == null) {
				� = (Button) parent.findViewById(R.id.sv_ao);
			}
			�.setVisibility(Button.VISIBLE);

			Button � = (Button) context.findViewById(R.id.sv_ae);
			if (� == null) {
				� = (Button) parent.findViewById(R.id.sv_ae);
			}
			�.setVisibility(Button.VISIBLE);

			Button � = (Button) context.findViewById(R.id.sv_oe);
			if (� == null) {
				� = (Button) parent.findViewById(R.id.sv_oe);
			}
			�.setVisibility(Button.VISIBLE);
			break;
		case IDDelved.ES:
			Button � = (Button) context.findViewById(R.id.es_ene);
			if (� == null) {
				� = (Button) parent.findViewById(R.id.es_ene);
			}
			�.setVisibility(Button.VISIBLE);
			break;
		}
	}

}
