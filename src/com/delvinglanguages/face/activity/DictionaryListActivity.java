package com.delvinglanguages.face.activity;

import java.util.TreeSet;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class DictionaryListActivity extends ListActivity implements Messages {

	private static final String DEBUG = "##DictionaryListActivity##";

	private static final int REQUEST_MODIFIED = 0;

	private Character capital;

	private DReferences values;

	private Button[] types;

	private boolean phMode;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);
		View view = getLayoutInflater().inflate(R.layout.a_dictionary_list, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		phMode = LanguageKernelControl.getLanguageSettings(IDDelved.MASK_PH);

		types = new Button[Settings.NUM_TYPES];
		int[] ids = { R.id.lab_nn, R.id.lab_vb, R.id.lab_adj, R.id.lab_adv, R.id.lab_phv, R.id.lab_exp, R.id.lab_oth };

		for (int i = 0; i < types.length; i++) {
			types[i] = (Button) findViewById(ids[i]);
			types[i].setSelected(true);
		}
		if (!phMode) {
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

		TreeSet<DReference> sub = LanguageKernelControl.getSubdictionary(capital);
		if (type == 0) {
			values = new DReferences(sub);
		} else {
			values = new DReferences();
			for (DReference ref : sub) {
				if ((type & ref.getType()) != 0) {
					values.add(ref);
				}
			}
		}
		Log.d(DEBUG, "values length es:" + values.size());
		setListAdapter(new ReferenceLister(this, values, phMode));
		// Se podria actualizar en lugar de crear cada vez de nuevo
	}

	private int getType() {
		int type = 0;
		for (int i = 0; i < types.length; i++) {
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
		intent.putExtra(DREFERENCE, values.get(pos).getName());
		startActivityForResult(intent, REQUEST_MODIFIED);
	}

	public void changeState(View v) {
		v.setSelected(!v.isSelected());
	}

}
