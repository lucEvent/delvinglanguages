package com.delvinglanguages.face.langoptions;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.settings.Configuraciones;
import com.delvinglanguages.face.phrasals.*;
import com.delvinglanguages.listers.OptionLister;

public class PhrasalsFragment extends ListFragment {

	private IDDelved idioma;
	private String[] options;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Activity activity = getActivity();

		View view = inflater.inflate(R.layout.a_simple_list, container, false);

		FrameLayout background = (FrameLayout) view
				.findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		options = getResources().getStringArray(R.array.phv_opt);

		idioma = ControlCore.getIdiomaActual(activity);
		idioma.analizePhrasals();

		setListAdapter(new OptionLister(activity, options));

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Activity activity = getActivity();

		switch (position) {
		case 0:
			// startActivity(new Intent(activity, Activity.class));
			break;
		case 1:
			startActivity(new Intent(activity, AddPhrasalsActivity.class));
			break;
		case 2:
			startActivity(new Intent(activity, ListPhrasalActivity.class));
		}
	}

}
