package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.phrasals.AddPhrasalsActivity;
import com.delvinglanguages.face.phrasals.ListPhrasalActivity;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.listers.OptionLister;
import com.delvinglanguages.settings.Settings;

public class PhrasalsFragment extends ListFragment {

	private Language idioma;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.a_simple_list, container, false);

		Settings.setBackgroundTo(view);

		idioma = KernelControl.getCurrentLanguage();
		idioma.analizePhrasals();

		setListAdapter(new OptionLister(getActivity(), getResources().getStringArray(R.array.phv_opt)));

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
