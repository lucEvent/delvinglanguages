package com.delvinglanguages.debug;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.settings.Settings;

public class Debug extends ListActivity {

	private static final String DEBUG = "##DEBUG##";

	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_debug, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		LanguageKernelControl.deleteAllRemovedWords();

		index = 0;

		displayWord();
	}

	private void displayWord() {
	}

	public void next(View v) {
	}

	public void previous(View v) {
	}

	public void addandnext(View v) {
	}

	private void debug(String text) {
		Log.d(DEBUG, text);

	}
}
