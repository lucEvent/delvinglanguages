package com.delvinglanguages.face.activity.practice;

import java.util.ArrayList;

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
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.core.Word;
import com.delvinglanguages.listers.TestLister;
import com.delvinglanguages.settings.Configuraciones;

public class SelectTestActivity extends ListActivity implements
		OnClickListener, OnLongClickListener {

	private int minimum = 2;
	private int maximum = 10;

	private int numero;

	private TextView number;
	private Button mas, menos;
	private Button types[];

	private ArrayList<Test> tests;
	private TestLister adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_select_test);

		View background = findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		number = (TextView) findViewById(R.id.number);
		numero = maximum / 2;
		number.setText("" + numero);

		mas = (Button) findViewById(R.id.more);
		menos = (Button) findViewById(R.id.less);
		mas.setOnClickListener(this);
		menos.setOnClickListener(this);
		mas.setOnLongClickListener(this);
		menos.setOnLongClickListener(this);
	
		types = new Button[Configuraciones.NUM_TYPES];
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
		IDDelved idioma = ControlCore.getIdiomaActual(this);
		if (!idioma.getSettings(IDDelved.MASK_PH)) {
			types[Word.PHRASAL].setVisibility(View.GONE);
		}

		tests = ControlCore.getTests();

		adapter = new TestLister(this, tests);
		setListAdapter(adapter);

		String temp = getString(R.string.title_test);
		setTitle(temp + " " + ControlCore.getIdiomaActual(this).getName());
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
			int totalHeight = listItem.getMeasuredHeight() * tests.size()
					+ (listView.getDividerHeight() * (tests.size() - 1));
			LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight;
			listView.setLayoutParams(params);
		}
	
		adapter.notifyDataSetChanged();
	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ControlCore.testActual = tests.get(position);
		Intent intent = null;
		switch (ControlCore.testActual.state) {
		case Test.STAT_DELVING:
			intent = new Intent(this, TestActivityLearn.class);
			break;
		case Test.STAT_MATCH:
			intent = new Intent(this, TestActivityMatch.class);
			break;
		case Test.STAT_COMPLETE:
			intent = new Intent(this, TestActivityComplete.class);
			break;
		case Test.STAT_WRITE:
			intent = new Intent(this, TestActivityWrite.class);
			break;
		case Test.STAT_STATISTICS:
			intent = new Intent(this, TestActivityResult.class);
			break;
		}
		startActivity(intent);
	}

	
	public void cancel(View v) {
		finish();
	}
	
	public void createTest(View v) {
		int types = 0;
		for (int i = 0; i < Configuraciones.NUM_TYPES; i++) {
			if (this.types[i].isSelected()) {
				types += (1 << i);
			}
		}
		if (types == 0) {
			Toast.makeText(this, R.string.notypesselected,
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, TestActivity1.class);
		intent.putExtra("number", numero);
		intent.putExtra("types", types);
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
