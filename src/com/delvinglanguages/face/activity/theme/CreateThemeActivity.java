package com.delvinglanguages.face.activity.theme;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.core.theme.ThemeKernelControl;
import com.delvinglanguages.core.theme.ThemePair;
import com.delvinglanguages.face.listeners.SpecialKeysBar;
import com.delvinglanguages.listers.ThemePairInputLister;
import com.delvinglanguages.settings.Configuraciones;

public class CreateThemeActivity extends ListActivity implements
		OnClickListener {

	private static final String DEBUG = "##CreateThemeActivity##";

	protected ThemePairInputLister adapter;

	protected ThemePairs pairs;

	protected EditText in_name, in_delv, in_nativ;

	private boolean editing;
	private int editing_pos;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_theme_create);

		View background = findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		pairs = new ThemePairs();
		adapter = new ThemePairInputLister(this, pairs, this);
		setListAdapter(adapter);

		in_name = (EditText) findViewById(R.id.input);
		in_delv = (EditText) findViewById(R.id.in_delv);
		in_nativ = (EditText) findViewById(R.id.in_nativ);

		IDDelved language = ControlCore.getIdiomaActual(this);
		if (language.isIdiomaNativo()) {
			in_delv.setHint("in " + Configuraciones.IdiomaNativo);
			in_nativ.setHint("in " + language.getName());
		} else {
			in_delv.setHint("in " + language.getName());
			in_nativ.setHint("in " + Configuraciones.IdiomaNativo);
		}

		new SpecialKeysBar(this, null);
	}

	public void addPair(View v) {
		String pdelv = in_delv.getText().toString();
		String pnativ = in_nativ.getText().toString();
		if (pdelv.isEmpty() || pnativ.isEmpty()) {
			showMessage(R.string.msgbothsidesnotfilled);
			return;
		}
		if (editing) {
			pairs.get(editing_pos).inDelved = pdelv;
			pairs.get(editing_pos).inNative = pnativ;
			adapter.notifyDataSetInvalidated();
			editing = false;
		} else {
			pairs.add(new ThemePair(pdelv, pnativ));
		}
		in_delv.setText("");
		in_nativ.setText("");
		in_delv.requestFocus();
	}

	public void action(View v) {
		String name = in_name.getText().toString();
		if (name.isEmpty()) {
			showMessage(R.string.msgnamenotspecified);
			return;
		}
		if (pairs.isEmpty()) {
			showMessage(R.string.msgnamenotspecified);
			return;
		}
		new ThemeKernelControl(this).addTheme(name, pairs);
		showMessage(R.string.msgthemecreated);
		setResult(Activity.RESULT_OK);
		finish();
	}

	public void cancel(View v) {
		finish();
	}

	public void specialKeyAction(View v) {
		EditText focused = null;
		if (in_delv.isFocused()) {
			focused = in_delv;
		} else if (in_nativ.isFocused()) {
			focused = in_nativ;
		} else if (in_name.isFocused()) {
			focused = in_name;
		}
		String in = focused.getText().toString();
		if (in.isEmpty()) {
			in = (String) v.getTag();
		} else {
			in = in + ((Button) v).getText();
		}
		focused.setText(in);
		focused.setSelection(in.length());
	}

	protected void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		editing_pos = (Integer) v.getTag();
		editing = true;
		in_delv.setText(pairs.get(editing_pos).inDelved);
		in_nativ.setText(pairs.get(editing_pos).inNative);
	}

}
