package com.delvinglanguages.face.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.activity.add.AddWordFromVerbActivity;
import com.delvinglanguages.face.activity.ReferenceActivity;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.settings.Configuraciones;

public class VerbsFragment extends ListFragment implements OnClickListener {

	private static final int REQUEST_MODIFIED = 0;

	private ArrayList<DReference> verbslist;

	private Button addVerb;
	
	private boolean phMode;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_verbs, container, false);

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

		addVerb = (Button) view.findViewById(R.id.add_verb);
		addVerb.setOnClickListener(this);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
		Intent intent = new Intent(getActivity(), ReferenceActivity.class);
		intent.putExtra(ControlCore.sendDReference, verbslist.get(pos).name);
		startActivityForResult(intent, REQUEST_MODIFIED);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MODIFIED) {
			if (resultCode == Activity.RESULT_OK) {
				verbslist = ControlCore.getVerbs();
				setListAdapter(new ReferenceLister(getActivity(), verbslist, phMode));
			}
		}
	}

	@Override
	public void onClick(View button) {
		startActivity(new Intent(getActivity(), AddWordFromVerbActivity.class));
	}

}
