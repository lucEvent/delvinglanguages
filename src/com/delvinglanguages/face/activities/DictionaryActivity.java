package com.delvinglanguages.face.activities;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;

import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.settings.Configuraciones;

public class DictionaryActivity extends ListActivity implements OnClickListener {

	private static final String DEBUG = "##DictionaryAct##";

	private static final int REQUEST_EDIT = 0;

	private Character capital;

	private int type;

	private ArrayList<DReference> values;

	private Button[] types;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.a_dictionary_list);

		LinearLayout background = (LinearLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		types = new Button[Configuraciones.NUM_TYPES];
		int[] ids = { R.id.lab_nn, R.id.lab_vb, R.id.lab_adj, R.id.lab_adv,
				R.id.lab_phv, R.id.lab_exp, R.id.lab_oth };

		for (int i = 0; i < types.length; i++) {
			types[i] = (Button) findViewById(ids[i]);
			types[i].setOnClickListener(this);
			types[i].setSelected(true);
		}

		capital = getIntent().getExtras().getChar(ControlCore.sendCharacter);

		setTitle(getString(R.string.wordsby) + " " + capital);

		setList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_EDIT) {
			if (resultCode == Activity.RESULT_OK) {
				setList();
			}
		}
	}

	private void setList() {
		int type = getType();

		TreeSet<DReference> sub = ControlCore.getSubdiccionario(capital);
		if (type == 0) {
			values = new ArrayList<DReference>(sub);
		} else {
			values = new ArrayList<DReference>();
			for (DReference ref : sub) {
				if ((type & ref.type) != 0) {
					values.add(ref);
				}
			}
		}
		setListAdapter(new ReferenceLister(this, values));
		//Se podria actualizar en lugar de crear cada vez de nuevo
	}

	private int getType() {
		int type = 0;
		for (int i = 0; i < Configuraciones.NUM_TYPES; i++) {
			if (types[i].isSelected()) {
				type += (1 << i);
			}
		}
		return type;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dictionary, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			setList();
			return true;
		}
		return false;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		Intent intent = new Intent(this, ReferenceActivity.class);
		intent.putExtra(ControlCore.sendDReference, values.get(pos).item);
		startActivityForResult(intent, REQUEST_EDIT);
	}

	@Override
	public void onClick(View v) {
		v.setSelected(!v.isSelected());
	}

}
