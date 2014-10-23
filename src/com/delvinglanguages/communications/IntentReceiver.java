package com.delvinglanguages.communications;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class IntentReceiver extends Activity {

	private TextView viewer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewer = new TextView(this);
		viewer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		setContentView(viewer);

		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			}
		} else {
			viewer.setText("Another Action: " + action + " type: " + type);
		}

	}

	void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {

			viewer.setText(sharedText);
		}
	}

}
