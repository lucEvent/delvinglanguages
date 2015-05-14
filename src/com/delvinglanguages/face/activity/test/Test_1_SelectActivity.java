package com.delvinglanguages.face.activity.test;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.listers.TestLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class Test_1_SelectActivity extends ListActivity implements OnClickListener, OnLongClickListener, Messages {

	private int minimum = 2;
	private int maximum = 10;

	private int numero;

	private TextView number;
	private Button mas, menos;
	private Button types[];

	private Tests tests;
	private TestLister adapter;
	private TestKernelControl kernel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = getLayoutInflater().inflate(R.layout.a_select_test, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		kernel = new TestKernelControl(this);

		number = (TextView) findViewById(R.id.number);
		numero = maximum / 2;
		number.setText("" + numero);

		mas = (Button) findViewById(R.id.more);
		menos = (Button) findViewById(R.id.less);
		mas.setOnClickListener(this);
		menos.setOnClickListener(this);
		mas.setOnLongClickListener(this);
		menos.setOnLongClickListener(this);

		types = new Button[Settings.NUM_TYPES];
		types[0] = (Button) findViewById(R.id.noun);
		types[1] = (Button) findViewById(R.id.verb);
		types[2] = (Button) findViewById(R.id.adjective);
		types[3] = (Button) findViewById(R.id.adverb);
		types[4] = (Button) findViewById(R.id.phrasal);
		types[5] = (Button) findViewById(R.id.expression);
		types[6] = (Button) findViewById(R.id.other);
		for (int i = 0; i < types.length; i++) {
			types[i].setOnClickListener(this);
			types[i].setSelected(true);
		}
		IDDelved idioma = KernelControl.getCurrentLanguage();
		if (!idioma.getSettings(IDDelved.MASK_PH)) {
			types[Word.PHRASAL].setVisibility(View.GONE);
		}

		tests = kernel.getTests();
		for (int i = 0; i < tests.size(); i++) {
			Test test = tests.get(i);
			test.check();
			if (test.isEmpty()) {
				kernel.removeTest(test);
				i--;
			}
		}

		adapter = new TestLister(this, tests);
		setListAdapter(adapter);

		String temp = getString(R.string.title_test);
		setTitle(temp + " " + LanguageKernelControl.getLanguageName());
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Ajustando la medida del listview para que no de problemas con el
		// scrollview
		if (tests.size() > 0) {
			ListView listView = getListView();
			View listItem = adapter.getView(0, null, listView);
			listItem.measure(0, 0);
			int totalHeight = listItem.getMeasuredHeight() * tests.size() + (listView.getDividerHeight() * (tests.size() - 1));
			LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight;
			listView.setLayoutParams(params);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		TestKernelControl.runningTest = tests.get(position);
		Intent intent = null;
		switch (tests.get(position).state) {
		case Test.PHASE_DELVING:
			intent = new Intent(this, Test_3_DelveActivity.class);
			break;
		case Test.PHASE_MATCH:
			intent = new Intent(this, Test_4_MatchActivity.class);
			break;
		case Test.PHASE_COMPLETE:
			intent = new Intent(this, Test_5_CompleteActivity.class);
			break;
		case Test.PHASE_WRITE:
			intent = new Intent(this, Test_6_WriteActivity.class);
			break;
		case Test.PHASE_STATISTICS:
			intent = new Intent(this, Test_7_ResultActivity.class);
			break;
		}
		startActivity(intent);
	}

	public void cancel(View v) {
		finish();
	}

	public void createTest(View v) {
		int types = 0;
		for (int i = 0; i < Settings.NUM_TYPES; i++) {
			if (this.types[i].isSelected()) {
				types += (1 << i);
			}
		}
		if (types == 0) {
			Toast.makeText(this, R.string.notypesselected, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, Test_2_CreateActivity.class);
		intent.putExtra(NUMBER, numero);
		intent.putExtra(TYPES, types);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == mas) {
			if (numero < maximum) {
				numero++;
				number.setText("" + numero);
			}
			return;
		} else if (v == menos) {
			if (numero > minimum) {
				numero--;
				number.setText("" + numero);
			}
			return;
		} else {
			v.setSelected(!v.isSelected());
		}
	}

	@Override
	public boolean onLongClick(View view) {
		if (view == mas) {
			numero = maximum;
		} else if (view == menos) {
			numero = minimum;
		}
		number.setText("" + numero);
		return false;
	}

}
