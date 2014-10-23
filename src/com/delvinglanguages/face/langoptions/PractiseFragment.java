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
import com.delvinglanguages.face.activities.practics.CompleteActivity;
import com.delvinglanguages.face.activities.practics.ListeningActivity;
import com.delvinglanguages.face.activities.practics.PreguntasActivity;
import com.delvinglanguages.face.activities.practics.SelectTestActivity;
import com.delvinglanguages.face.activities.practics.TranslateActivity;
import com.delvinglanguages.face.activities.practics.WriteWordsActivity;
import com.delvinglanguages.listers.OptionLister;
import com.delvinglanguages.settings.Configuraciones;

public class PractiseFragment extends ListFragment {

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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

		setListAdapter((new OptionLister(getActivity(), getResources()
				.getStringArray(R.array.prac_opt))));

		return view;
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Activity activity = getActivity();

		switch (position) {
		case 0:// Match
			startActivity(new Intent(activity, PreguntasActivity.class));
			break;
		case 1:// Complete
			startActivity(new Intent(activity, CompleteActivity.class));
			break;
		case 2:// Write
			startActivity(new Intent(activity, WriteWordsActivity.class));
			break;
		case 3:// Listening
			startActivity(new Intent(activity, ListeningActivity.class));
			break;
		case 4:// Test you
			startActivity(new Intent(activity, SelectTestActivity.class));
			break;
		case 5:// Translate
			startActivity(new Intent(activity, TranslateActivity.class));
			break;
		}
	}
}
