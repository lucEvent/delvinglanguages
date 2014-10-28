package com.delvinglanguages.face.activities.practice;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.listers.TestLister;
import com.delvinglanguages.settings.Configuraciones;

public class SelectTestActivity extends ListActivity implements
		OnClickListener, OnLongClickListener {

	private int minimum = 2;
	private int maximum = 10;

	private RelativeLayout background;

	private int numero;

	private TextView number;
	private Button mas, menos, cancel, gotest;
	private Button types[];

	private ArrayList<Test> tests;
	private TestLister adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_select_test);

		background = (RelativeLayout) findViewById(R.id.ast_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		number = (TextView) findViewById(R.id.ast_number);
		numero = maximum / 2;
		number.setText("" + numero);

		mas = (Button) findViewById(R.id.ast_mas);
		menos = (Button) findViewById(R.id.ast_menos);
		mas.setOnClickListener(this);
		menos.setOnClickListener(this);
		mas.setOnLongClickListener(this);
		menos.setOnLongClickListener(this);

		cancel = (Button) findViewById(R.id.ast_cancel);
		gotest = (Button) findViewById(R.id.ast_go);
		cancel.setOnClickListener(this);
		gotest.setOnClickListener(this);

		types = new Button[Configuraciones.NUM_TYPES];
		types[0] = (Button) findViewById(R.id.ast_noun);
		types[1] = (Button) findViewById(R.id.ast_verb);
		types[2] = (Button) findViewById(R.id.ast_adj);
		types[3] = (Button) findViewById(R.id.ast_adv);
		types[4] = (Button) findViewById(R.id.ast_phrasal);
		types[5] = (Button) findViewById(R.id.ast_expression);
		types[6] = (Button) findViewById(R.id.ast_other);
		for (int i = 0; i < types.length; i++) {
			types[i].setOnClickListener(this);
			types[i].setSelected(true);
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
			intent = new Intent(this, TestActivity2.class);
			break;
		case Test.STAT_MATCH:
			intent = new Intent(this, TestActivity3.class);
			break;
		case Test.STAT_COMPLETE:
			intent = new Intent(this, TestActivity4.class);
			break;
		case Test.STAT_WRITE:
			intent = new Intent(this, TestActivity5.class);
			break;
		case Test.STAT_STATISTICS:
			intent = new Intent(this, TestActivity6.class);
			break;
		}
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
		} else if (v == gotest) {
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
		} else if (v == cancel) {
			finish();
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
