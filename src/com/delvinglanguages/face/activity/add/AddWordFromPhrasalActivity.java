package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordActivity;

public class AddWordFromPhrasalActivity extends AddWordActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		get_and_set_Name(getIntent().getExtras());
		((Button) findViewById(R.id.addmore)).setVisibility(View.GONE);

		for (int i = 0; i < types.length; i++) {
			types[i].setClickable(false);
		}
		types[PHRASAL].setSelected(true);
	}

}
