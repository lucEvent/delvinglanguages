package com.delvinglanguages.face.phrasals;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.activity.ReferenceActivity;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Configuraciones;

public class ListPhrasalActivity extends ListActivity implements Runnable,
		Messages {

	private ArrayList<DReference> values;
	private ReferenceLister adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_simple_list);

		values = new ArrayList<DReference>();
		adapter = new ReferenceLister(this, values, true);
		setListAdapter(adapter);
		new Thread(this).start();

		View background = findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		Intent intent = new Intent(this, ReferenceActivity.class);
		intent.putExtra(DREFERENCE, values.get(pos).name);
		startActivity(intent);
	}

	@Override
	public void run() {
		ArrayList<DReference> lista = ControlCore.getIdiomaActual(this)
				.getPhrasalVerbs();
		for (DReference ref : lista) {
			values.add(ref);
		}
		adapter.notifyDataSetChanged();
	}
}
