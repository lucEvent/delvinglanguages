package com.delvinglanguages.face.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Tense;
import com.delvinglanguages.kernel.TenseKernelControl;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class NewTenseActivity extends Activity implements OnCheckedChangeListener, Messages {

	public final static String TENSE = "tense";

	private int tposition;
	private boolean repiteAll;

	private TextView tverb, ttense;
	private EditText[] forms;
	private EditText[] prons;
	private CheckBox repite;

	private int tenseId;

	private DReference reference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_new_tense, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		Bundle bundle = getIntent().getExtras();

		String s = bundle.getString(DREFERENCE);
		reference = LanguageKernelControl.getReference(s);

		tverb = (TextView) findViewById(R.id.tverb);

		tverb.setText(reference.getName());

		ttense = (TextView) findViewById(R.id.ttense);
		int arrayid = LanguageKernelControl.getTensesArrayResId();
		String[] tenses = getResources().getStringArray(arrayid);
		tposition = bundle.getInt(NewTenseActivity.TENSE);
		ttense.setText(tenses[tposition]);

		int[] subjectsIds = { R.id.tjag, R.id.tdu, R.id.thanhon, R.id.tvi, R.id.tni, R.id.tde };
		int[] formsIds = { R.id.edit_njag, R.id.edit_ndu, R.id.edit_nhanhon, R.id.edit_nvi, R.id.edit_nni, R.id.edit_nde };
		int[] pronsIds = { R.id.edit_pjag, R.id.edit_pdu, R.id.edit_phanhon, R.id.edit_pvi, R.id.edit_pni, R.id.edit_pde };

		arrayid = LanguageKernelControl.getSubjectArrayResId();
		String[] subcont = getResources().getStringArray(arrayid);

		TextView[] subjects = new TextView[6];
		forms = new EditText[6];
		prons = new EditText[6];
		for (int i = 0; i < 6; i++) {
			subjects[i] = (TextView) findViewById(subjectsIds[i]);
			subjects[i].setText(subcont[i]);
			forms[i] = (EditText) findViewById(formsIds[i]);
			prons[i] = (EditText) findViewById(pronsIds[i]);
		}
		forms[0].addTextChangedListener(new FormsWatcher());
		prons[0].addTextChangedListener(new PronsWatcher());

		repite = (CheckBox) findViewById(R.id.repiteall);
		repite.setOnCheckedChangeListener(this);

		repiteAll = false;
	}

	public void addTense(View v) {
		String[] form = new String[6];
		String[] pron = new String[6];

		for (int i = 0; i < 6; i++) {
			form[i] = forms[i].getText().toString();
			pron[i] = prons[i].getText().toString();
		}
		new TenseKernelControl(this).addTense(reference, tenseId, Tense.queueString(form), Tense.queueString(pron));
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean repiteAll) {
		this.repiteAll = repiteAll;
		for (int i = 1; i < 6; i++) {
			prons[i].setEnabled(!repiteAll);
			forms[i].setEnabled(!repiteAll);
		}
	}

	private class FormsWatcher implements TextWatcher {
		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (repiteAll) {
				completeRepiteAll(forms);
			}
		}
	}

	private class PronsWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (repiteAll) {
				completeRepiteAll(prons);
			}
		}
	}

	private void completeRepiteAll(TextView[] views) {
		CharSequence text = views[0].getText();
		for (int i = 1; i < views.length; i++) {
			views[i].setText(text);
		}
	}

}