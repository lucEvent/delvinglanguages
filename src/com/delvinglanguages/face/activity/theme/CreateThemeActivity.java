package com.delvinglanguages.face.activity.theme;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.view.SpecialKeysBar;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.theme.ThemeKernelControl;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.listers.ThemePairInputLister;
import com.delvinglanguages.settings.Settings;

public class CreateThemeActivity extends ListActivity implements OnClickListener {

	protected ThemePairInputLister adapter;

	protected ThemePairs pairs;

	protected EditText in_name, in_delv, in_nativ;

	private boolean editing;
	private int editing_pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_theme_create, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		pairs = new ThemePairs();
		adapter = new ThemePairInputLister(this, pairs, this);
		setListAdapter(adapter);

		in_name = (EditText) findViewById(R.id.input);
		in_delv = (EditText) findViewById(R.id.in_delv);
		in_nativ = (EditText) findViewById(R.id.in_nativ);

		Language language = KernelControl.getCurrentLanguage();
		if (language.isNativeLanguage()) {
			in_delv.setHint("in " + Settings.NativeLanguage);
			in_nativ.setHint("in " + language.getName());
		} else {
			in_delv.setHint("in " + language.getName());
			in_nativ.setHint("in " + Settings.NativeLanguage);
		}

		new SpecialKeysBar(findViewById(R.id.letrasespeciales), new EditText[] { in_name, in_delv, in_nativ });
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

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##CreateThemeActivity##", text);
	}

}
