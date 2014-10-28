package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.listers.RemovedWordLister;
import com.delvinglanguages.settings.Configuraciones;

public class BinFragment extends ListFragment implements OnClickListener {

	private Button clear;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_bin, container, false);
		Activity activity = getActivity();

		RelativeLayout background = (RelativeLayout) view.findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		setListAdapter(new RemovedWordLister(activity));

		clear = (Button) view.findViewById(R.id.atow_clear);
		clear.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View button) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.emptyingbin);
		builder.setMessage(R.string.clearbinquestion);
		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						clear();
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	private void clear() {
		ControlCore.clearPapelera();
		Toast.makeText(getActivity(), R.string.trashemptied, Toast.LENGTH_SHORT)
				.show();
		getActivity().finish();
	}

}
