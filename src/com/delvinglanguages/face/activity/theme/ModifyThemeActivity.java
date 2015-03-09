package com.delvinglanguages.face.activity.theme;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.set.ThemePairs;
import com.delvinglanguages.core.theme.Theme;
import com.delvinglanguages.listers.ThemePairInputLister;

public class ModifyThemeActivity extends CreateThemeActivity {

	private Theme theme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int th_id = getIntent().getExtras().getInt(ControlCore.sendTheme);

		theme = ControlCore.getTheme(th_id);
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
		ControlCore.modifyTheme(theme);
		showMessage(R.string.msgthememodified);
		setResult(Activity.RESULT_OK);
		finish();
	}

}
