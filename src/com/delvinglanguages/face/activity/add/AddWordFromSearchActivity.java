package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordActivity;

public class AddWordFromSearchActivity extends AddWordActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.a_add_word);

		Bundle bundle = getIntent().getExtras();
		get_and_set_Name(bundle);
		get_and_set_Translation(bundle);
		get_and_set_Type(bundle);

		((Button) findViewById(R.id.addmore)).setVisibility(View.GONE);
	}

}
