package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;

public class AddWordFromPhrasalActivity extends AddWordActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		get_and_set_Name(getIntent().getExtras());
		((Button) findViewById(R.id.addmore)).setVisibility(View.GONE);

	}

}
