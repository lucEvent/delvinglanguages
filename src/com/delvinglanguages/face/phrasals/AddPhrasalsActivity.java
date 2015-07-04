package com.delvinglanguages.face.phrasals;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordFromPhrasalActivity;
import com.delvinglanguages.kernel.phrasals.PVLink;
import com.delvinglanguages.kernel.phrasals.PhrasalVerbs;
import com.delvinglanguages.listers.PhrasalLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class AddPhrasalsActivity extends Activity implements TextWatcher, Messages {

	private Integer[] status_verbs, status_preps;

	private PhrasalLister prepadapter, verbadapter;

	private ArrayList<PVLink> verbslist, prepslist;

	private EditText inputverb, inputprep;

	private PhrasalVerbs phManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_add_phrasals, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		phManager = (PhrasalVerbs) getIntent().getExtras().getSerializable(SEND_PHRASAL_MANAGER);

		status_preps = new Integer[phManager.getPrepositions().size()];
		for (int i = 0; i < status_preps.length; i++) {
			status_preps[i] = PhrasalLister.STAT_NORMAL;
		}
		prepslist = new ArrayList<PVLink>(phManager.getPrepositions());
		prepadapter = new PhrasalLister(this, prepslist, status_preps);

		ListView preplist = (ListView) findViewById(R.id.prep_list);
		preplist.setAdapter(prepadapter);
		preplist.setOnItemClickListener(new PrepositionListListener());

		verbslist = new ArrayList<PVLink>(phManager.getVerbs());
		status_verbs = new Integer[verbslist.size()];
		for (int i = 0; i < status_verbs.length; i++) {
			status_verbs[i] = PhrasalLister.STAT_NORMAL;
		}
		verbadapter = new PhrasalLister(this, verbslist, status_verbs);

		ListView verblist = (ListView) findViewById(R.id.verb_list);
		verblist.setAdapter(verbadapter);
		verblist.setOnItemClickListener(new VerbListListener());

		inputverb = (EditText) findViewById(R.id.input_verb);
		inputverb.addTextChangedListener(this);

		inputprep = (EditText) findViewById(R.id.input_prep);
		inputprep.addTextChangedListener(this);
	}

	private class VerbListListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> ad, View view, int pos, long id) {
			String t = verbslist.get(pos).name;
			inputverb.setText(t);
			inputverb.setSelection(t.length());
			inputverb.requestFocus();

			unselect(status_preps);
			unselect(status_verbs, pos);

			for (PVLink link : verbslist.get(pos).links) {
				status_preps[prepslist.indexOf(link)] = PhrasalLister.STAT_MARKED;
			}

			prepadapter.notifyDataSetChanged();
			verbadapter.notifyDataSetChanged();
		}
	}

	private class PrepositionListListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> ad, View view, int pos, long id) {
			String t = prepslist.get(pos).name.toLowerCase();
			inputprep.setText(t);
			inputprep.setSelection(t.length());
			inputprep.requestFocus();

			unselect(status_verbs);
			unselect(status_preps, pos);

			for (PVLink link : prepslist.get(pos).links) {
				status_verbs[verbslist.indexOf(link)] = PhrasalLister.STAT_MARKED;
			}

			prepadapter.notifyDataSetChanged();
			verbadapter.notifyDataSetChanged();
		}
	}

	private void unselect(Integer[] vector) {
		int count = 0;
		for (int i = 0; i < vector.length; i++) {
			count += vector[i];
			vector[i] = PhrasalLister.STAT_NORMAL;
		}
		// return count;
	}

	private void unselect(Integer[] vector, int pressed) {
		unselect(vector);
		vector[pressed] = PhrasalLister.STAT_PRESSED;
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
		unselect(status_verbs);
		unselect(status_preps);
		verbadapter.notifyDataSetChanged();
		prepadapter.notifyDataSetChanged();
	}

	public void addPhrasal(View v) {
		String verb = inputverb.getText().toString();
		if (verb.length() == 0) {
			Toast.makeText(this, R.string.noverb, Toast.LENGTH_SHORT).show();
			return;
		}
		String prep = inputprep.getText().toString().toLowerCase();
		if (prep.length() == 0) {
			Toast.makeText(this, R.string.noprep, Toast.LENGTH_SHORT).show();
			return;
		}

		PVLink pvlVerb = phManager.getVerb(verb);
		if (pvlVerb != null) {
			PVLink pvlPrep = phManager.getPreposition(prep);
			if (pvlPrep != null) {
				if (pvlVerb.links.contains(pvlPrep)) {
					showMessage(verb + " " + prep);
					return;
				}
			}
		}
		Intent intent = new Intent(this, AddWordFromPhrasalActivity.class);
		intent.putExtra(SEND_NAME, verb + " " + prep);
		startActivity(intent);
		finish();
	}

	private void showMessage(String text) {
		String temp = text + " " + getString(R.string.alreadyindb);
		Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##AddPhrasalsActivity##", text);
	}

}
