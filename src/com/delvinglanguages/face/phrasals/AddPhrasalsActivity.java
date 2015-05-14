package com.delvinglanguages.face.phrasals;

import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordFromPhrasalActivity;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.listers.PhrasalLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class AddPhrasalsActivity extends Activity implements TextWatcher, OnFocusChangeListener, Messages {

	private String[] bases;
	private Integer[] m_bases, m_preps;

	private TreeMap<String, Boolean[]> phrasals;

	private PhrasalLister prepadapter, baseadapter;

	private EditText inputbase, inputprep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_add_phrasals, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		ListView preplist = (ListView) findViewById(R.id.prep_list);
		m_preps = new Integer[IDDelved.preps.length];
		for (int i = 0; i < m_preps.length; i++) {
			m_preps[i] = PhrasalLister.STAT_NORMAL;
		}
		prepadapter = new PhrasalLister(this, IDDelved.preps, m_preps);
		preplist.setAdapter(prepadapter);
		preplist.setOnItemClickListener(new PrepositionListListener());

		phrasals = LanguageKernelControl.getPhrasals();

		ListView verblist = (ListView) findViewById(R.id.verb_list);
		bases = phrasals.keySet().toArray(new String[0]);
		m_bases = new Integer[bases.length];
		for (int i = 0; i < m_bases.length; i++) {
			m_bases[i] = PhrasalLister.STAT_NORMAL;
		}
		baseadapter = new PhrasalLister(this, bases, m_bases);
		verblist.setAdapter(baseadapter);
		verblist.setOnItemClickListener(new VerbListListener());

		inputbase = (EditText) findViewById(R.id.input_verb);
		inputprep = (EditText) findViewById(R.id.input_prep);
		inputbase.addTextChangedListener(this);
		inputbase.setOnFocusChangeListener(this);
		inputprep.addTextChangedListener(this);
		inputprep.setOnFocusChangeListener(this);
	}

	private class VerbListListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> ad, View view, int position, long id) {
			// Lista de las bases
			if (m_bases[position] == PhrasalLister.STAT_MARKED) {
				String p = inputprep.getText().toString().toLowerCase();
				int pos = position(p, IDDelved.preps);
				if (pos != -1 && phrasals.get(bases[position])[pos]) {
					String pv = bases[position] + " " + p;
					showMessage(pv);
					return;
				}
			}
			String t = bases[position];
			inputbase.setText(t);
			inputbase.setSelection(t.length());
			inputbase.requestFocus();

			Boolean[] temp = phrasals.get(bases[position]);
			for (int i = 0; i < m_preps.length; i++) {
				if (temp[i]) {
					m_preps[i] = PhrasalLister.STAT_MARKED;
				} else {
					m_preps[i] = PhrasalLister.STAT_NORMAL;
				}
			}

			unselect(m_bases, position);
			baseadapter.notifyDataSetChanged();
			prepadapter.notifyDataSetChanged();
		}
	}

	private class PrepositionListListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> ad, View view, int pos, long id) {
			// Lista de las preposiciones
			if (m_preps[pos] == PhrasalLister.STAT_MARKED) {
				String p = inputbase.getText().toString();
				int position = position(p.toLowerCase(), bases);
				if (pos != -1 && phrasals.get(bases[position])[pos]) {
					String pv = p + " " + IDDelved.preps[pos].toLowerCase();
					showMessage(pv);
					return;
				}
			}

			String t = IDDelved.preps[pos].toLowerCase();
			inputprep.setText(t);
			inputprep.setSelection(t.length());
			inputprep.requestFocus();

			for (int i = 0; i < phrasals.size(); i++) {
				if (phrasals.get(bases[i])[pos]) {
					m_bases[i] = PhrasalLister.STAT_MARKED;
				} else {
					m_bases[i] = PhrasalLister.STAT_NORMAL;
				}
			}
			unselect(m_preps, pos);
			prepadapter.notifyDataSetChanged();
			baseadapter.notifyDataSetChanged();
		}
	}

	private int unselect(Integer[] vector) {
		int count = 0;
		for (int i = 0; i < vector.length; i++) {
			count += vector[i];
			vector[i] = PhrasalLister.STAT_NORMAL;
		}
		return count;
	}

	private void unselect(Integer[] vector, int maintain) {
		unselect(vector);
		vector[maintain] = PhrasalLister.STAT_PRESSED;
	}

	/** ******************* * ONTEXTCHANGED * ********************/
	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (focusINPUT) {
			if (unselect(m_bases) > 0) {
				baseadapter.notifyDataSetChanged();
			}
		} else {
			if (unselect(m_preps) > 0) {
				prepadapter.notifyDataSetChanged();
			}
		}
	}

	public void addPhrasal(View v) {
		String base = inputbase.getText().toString();
		if (base.length() == 0) {
			Toast.makeText(this, R.string.noverb, Toast.LENGTH_SHORT).show();
			return;
		}
		String prep = inputprep.getText().toString();
		if (prep.length() == 0) {
			Toast.makeText(this, R.string.noprep, Toast.LENGTH_SHORT).show();
			return;
		}

		String lowb = base.toLowerCase();
		for (int i = 0; i < bases.length; i++) {
			if (lowb.equals(bases[i].toLowerCase())) {
				String lowp = prep.toLowerCase();
				for (int j = 0; j < IDDelved.preps.length; j++) {
					if (lowp.equals(IDDelved.preps[j].toLowerCase())) {
						if (phrasals.get(bases[i])[j]) {
							showMessage(base + " " + lowp);
							return;
						}
						break;
					}
				}
				break;
			}
		}
		Intent intent = new Intent(this, AddWordFromPhrasalActivity.class);
		intent.putExtra(SEND_NAME, base + " " + prep);
		startActivity(intent);
		finish();
	}

	private boolean focusINPUT; // true -> base, false -> prep

	/** ******************* * ONFOCUSCHANGED * ********************/
	@Override
	public void onFocusChange(View view, boolean hasfocus) {
		if (view == inputbase) {
			if (hasfocus) {
				focusINPUT = true;
			}
		} else if (view == inputprep) {
			if (hasfocus) {
				focusINPUT = false;
			}
		}
	}

	private int position(String s, String[] vector) {
		for (int i = 0; i < vector.length; i++) {
			if (s.equals(vector[i].toLowerCase())) {
				return i;
			}
		}
		return -1;
	}

	private void showMessage(String text) {
		String temp = text + " " + getString(R.string.alreadyindb);
		Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
	}
}
