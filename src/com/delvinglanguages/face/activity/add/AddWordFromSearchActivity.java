package com.delvinglanguages.face.activity.add;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.set.Translations;

public class AddWordFromSearchActivity extends AddWordActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		get_and_set_Name(bundle);
		get_and_set_Translation(bundle);

		((Button) findViewById(R.id.addmore)).setVisibility(View.GONE);
	}

	protected void get_and_set_Translation(Bundle bundle) {
		ArrayList<Translation> value = (ArrayList<Translation>) bundle.get(SEND_TRANSLATION);
		setTranslationsList(new Translations(value));
	}
}
