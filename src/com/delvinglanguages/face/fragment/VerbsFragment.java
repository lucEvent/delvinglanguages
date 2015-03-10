package com.delvinglanguages.face.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.activity.ReferenceActivity;
import com.delvinglanguages.face.activity.add.AddWordFromVerbActivity;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Configuraciones;

public class VerbsFragment extends ListFragment implements OnClickListener,
		Messages {

	private static final int REQUEST_MODIFIED = 0;

	private ArrayList<DReference> verbslist;

	private boolean phMode;

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

		verbslist = ControlCore.getVerbs();
		phMode = ControlCore.getIdiomaActual(getActivity()).getSettings(
				IDDelved.MASK_PH);
		setListAdapter(new ReferenceLister(getActivity(), verbslist, phMode));

		Button action = ((Button) view.findViewById(R.id.action));
		action.setText(getString(R.string.addverb));
		action.setOnClickListener(this);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
		Intent intent = new Intent(getActivity(), ReferenceActivity.class);
		intent.putExtra(DREFERENCE, verbslist.get(pos).name);
		startActivityForResult(intent, REQUEST_MODIFIED);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MODIFIED) {
			if (resultCode == Activity.RESULT_OK) {
				verbslist = ControlCore.getVerbs();
				setListAdapter(new ReferenceLister(getActivity(), verbslist,
						phMode));
			}
		}
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(getActivity(), AddWordFromVerbActivity.class));
	}

}
