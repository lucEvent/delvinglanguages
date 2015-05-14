package com.delvinglanguages.face.settings;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.delvinglanguages.R;
import com.delvinglanguages.settings.Settings;

public class About extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_simple_list, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.about_content)));
	}

}
