package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.adapters.IntegrateManager;
import com.delvinglanguages.settings.Configuraciones;

public class LanguageSettingsFragment extends Fragment implements
		OnClickListener {

	private Button languagename, clearstats, remove, integrate;
	private CheckedTextView phrasalsEn, adjectivesEn, specialcharsEn;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_language_settings, container,
				false);

		ScrollView background = (ScrollView) view.findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		languagename = (Button) view.findViewById(R.id.set_languagename);
		phrasalsEn = (CheckedTextView) view
				.findViewById(R.id.set_phrasalsenabled);
		adjectivesEn = (CheckedTextView) view
				.findViewById(R.id.set_adjectsenabled);
		specialcharsEn = (CheckedTextView) view
				.findViewById(R.id.set_special_chars_enabled);
		clearstats = (Button) view.findViewById(R.id.set_clearstats);
		remove = (Button) view.findViewById(R.id.set_remove);
		integrate = (Button) view.findViewById(R.id.set_integrate);

		languagename.setOnClickListener(this);
		phrasalsEn.setOnClickListener(this);
		adjectivesEn.setOnClickListener(this);
		specialcharsEn.setOnClickListener(this);
		clearstats.setOnClickListener(this);
		remove.setOnClickListener(this);
		integrate.setOnClickListener(this);

		IDDelved idioma = ControlCore.getIdiomaActual(getActivity());
		phrasalsEn.setChecked(idioma.getSettings(IDDelved.MASK_PH));
		adjectivesEn.setChecked(idioma.getSettings(IDDelved.MASK_ADJ));
		specialcharsEn.setChecked(idioma.getSettings(IDDelved.MASK_ESP_CHARS));

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		Activity activity = getActivity();
		if (v == languagename) {
			String name = ControlCore.getIdiomaActual(getActivity()).getName();

			View view = LayoutInflater.from(activity).inflate(R.layout.i_input,
					null);
			final EditText input = (EditText) view
					.findViewById(R.id.input_dialog);

			input.setText(name);
			input.setSelection(name.length());

			new AlertDialog.Builder(activity)
					.setTitle(R.string.renaminglanguage)
					.setView(view)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface d, int id) {
									String s = input.getText().toString();
									if (s.length() == 0) {
										showMessage(R.string.nonamelang);
									} else {
										ControlCore.renameLanguage(s);
										showMessage(R.string.renamedsuccessfully);
									}
								}
							}).setNegativeButton(R.string.cancel, null)
					.create().show();

			((InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.showSoftInput(input, InputMethodManager.SHOW_FORCED);

		} else if (v == phrasalsEn) {
			phrasalsEn.toggle();
			ControlCore.setLangSettings(phrasalsEn.isChecked(),
					IDDelved.MASK_PH);
		} else if (v == adjectivesEn) {
			adjectivesEn.toggle();
			ControlCore.setLangSettings(adjectivesEn.isChecked(),
					IDDelved.MASK_ADJ);
		} else if (v == specialcharsEn) {
			specialcharsEn.toggle();
			ControlCore.setLangSettings(specialcharsEn.isChecked(),
					IDDelved.MASK_ESP_CHARS);
		} else if (v == clearstats) {
			ControlCore.clearStatistics();
			showMessage(R.string.mssclearstats);
		} else if (v == integrate) {
			IntegrateManager im = new IntegrateManager(activity);
			im.start();
		} else if (v == remove) {
			String temp = getString(R.string.title_removing) + " "
					+ ControlCore.getIdiomaActual(getActivity()).getName();

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle(temp);
			builder.setMessage(R.string.removeidiomquestion);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							removeLanguage();
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();

		}
	}

	private void removeLanguage() {
		ControlCore.removeLanguage();
		getActivity().finish();
	}

	private void showMessage(int text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

}
