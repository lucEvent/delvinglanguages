package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.theme.ThemeKernelControl;
import com.delvinglanguages.face.activity.theme.CreateThemeActivity;
import com.delvinglanguages.face.activity.theme.ThemeActivity;
import com.delvinglanguages.listers.ThemeLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Configuraciones;

public class ThemesFragment extends ListFragment implements OnClickListener,
		Messages {

	private static final String DEBUG = "##ThemesFragment##";
	private ThemeLister adapter;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.a_list_with_button, container,
				false);

		View background = view.findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		adapter = new ThemeLister(getActivity(), new ThemeKernelControl(
				getActivity()).getThemes());
		setListAdapter(adapter);

		Button action = ((Button) view.findViewById(R.id.action));
		action.setText(getString(R.string.addtheme));
		action.setOnClickListener(this);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), ThemeActivity.class);
		intent.putExtra(THEME, position);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		startActivityForResult(new Intent(getActivity(),
				CreateThemeActivity.class), 0);
	}

}
