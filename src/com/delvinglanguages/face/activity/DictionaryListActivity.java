package com.delvinglanguages.face.activity;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Word;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Configuraciones;

public class DictionaryListActivity extends ListActivity implements Messages {

	private static final String DEBUG = "##DictionaryListActivity##";

	private static final int REQUEST_MODIFIED = 0;

	private Character capital;

	private ArrayList<DReference> values;

	private Button[] types;

	private boolean phMode;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.a_dictionary_list);

		View background = findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		phMode = ControlCore.getIdiomaActual(this)
				.getSettings(IDDelved.MASK_PH);

		types = new Button[Configuraciones.NUM_TYPES];
		int[] ids = { R.id.lab_nn, R.id.lab_vb, R.id.lab_adj, R.id.lab_adv,
				R.id.lab_phv, R.id.lab_exp, R.id.lab_oth };

		for (int i = 0; i < types.length; i++) {
			types[i] = (Button) findViewById(ids[i]);
			types[i].setSelected(true);
		}
		if (!ControlCore.getIdiomaActual(this).getSettings(IDDelved.MASK_PH)) {
			types[Word.PHRASAL].setVisibility(View.GONE);
		}

		capital = getIntent().getExtras().getChar(CHARACTER);

		setTitle(getString(R.string.wordsby) + " " + capital);

		setList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MODIFIED) {
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
		setListAdapter(new ReferenceLister(this, values, phMode));
		// Se podria actualizar en lugar de crear cada vez de nuevo
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
		intent.putExtra(DREFERENCE, values.get(pos).name);
		startActivityForResult(intent, REQUEST_MODIFIED);
	}

	public void changeState(View v) {
		v.setSelected(!v.isSelected());
	}

}
