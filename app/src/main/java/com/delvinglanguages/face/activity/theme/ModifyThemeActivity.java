package com.delvinglanguages.face.activity.theme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeKernelControl;
import com.delvinglanguages.listers.ThemePairInputLister;
import com.delvinglanguages.net.internal.Messages;

public class ModifyThemeActivity extends CreateThemeActivity implements
		Messages {

	private Theme theme;
	private ThemeKernelControl kernel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int th_id = getIntent().getExtras().getInt(THEME);

		kernel = new ThemeKernelControl();
		theme = kernel.getTheme(th_id);
		pairs = (ThemePairs) theme.getPairs().clone();

		adapter = new ThemePairInputLister(this, pairs, this);
		setListAdapter(adapter);

		((Button) findViewById(R.id.action)).setText(R.string.savechanges);
		in_name.setText(theme.getName());
		in_name.setSelection(theme.getName().length());
	}

	public void action(View v) {
		String name = in_name.getText().toString();
		if (name.isEmpty()) {
			showMessage(R.string.msgnamenotspecified);
			return;
		}
		theme.setName(name);
		theme.setPairs(pairs);
		kernel.modifyTheme(theme);
		showMessage(R.string.msgthememodified);
		setResult(Activity.RESULT_OK);
		finish();
	}

}
