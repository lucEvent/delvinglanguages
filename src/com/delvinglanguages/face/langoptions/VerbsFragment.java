package com.delvinglanguages.face.langoptions;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
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
import com.delvinglanguages.face.activities.AddWordActivity;
import com.delvinglanguages.face.activities.ReferenceActivity;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.settings.Configuraciones;

public class VerbsFragment extends ListFragment implements OnClickListener {

	private ArrayList<DReference> verbslist;
	private Button addVerb;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_verbs, container, false);

		RelativeLayout background = (RelativeLayout) view
				.findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		verbslist = ControlCore.getVerbs();
		setListAdapter(new ReferenceLister(getActivity(), verbslist));

		addVerb = (Button) view.findViewById(R.id.add_verb);
		addVerb.setOnClickListener(this);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		super.onListItemClick(l, v, pos, id);
		Intent intent = new Intent(getActivity(), ReferenceActivity.class);
		intent.putExtra(ControlCore.sendDReference, verbslist.get(pos).item);
		startActivity(intent);
	}

	@Override
	public void onClick(View button) {
		Intent intent = new Intent(getActivity(), AddWordActivity.class);
		intent.putExtra("from", AddWordActivity.FROM_VERB);
		startActivity(intent);
	}

}
