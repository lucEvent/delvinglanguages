package com.delvinglanguages.face.activity.add;

import android.os.Bundle;

import com.delvinglanguages.face.activity.add.AddWordActivity;

public class AddWordFromVerbActivity extends AddWordActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		types[VERB].setSelected(true);
	}

}
