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
			Button å = (Button) context.findViewById(R.id.sv_ao);
			if (å == null) {
				å = (Button) parent.findViewById(R.id.sv_ao);
			}
			å.setVisibility(Button.VISIBLE);

			Button ä = (Button) context.findViewById(R.id.sv_ae);
			if (ä == null) {
				ä = (Button) parent.findViewById(R.id.sv_ae);
			}
			ä.setVisibility(Button.VISIBLE);

			Button ö = (Button) context.findViewById(R.id.sv_oe);
			if (ö == null) {
				ö = (Button) parent.findViewById(R.id.sv_oe);
			}
			ö.setVisibility(Button.VISIBLE);
			break;
		case IDDelved.ES:
			Button ñ = (Button) context.findViewById(R.id.es_ene);
			if (ñ == null) {
				ñ = (Button) parent.findViewById(R.id.es_ene);
			}
			ñ.setVisibility(Button.VISIBLE);
			break;
		}
	}

}
