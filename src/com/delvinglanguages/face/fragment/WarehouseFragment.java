package com.delvinglanguages.face.fragment;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.core.Word;
import com.delvinglanguages.settings.Configuraciones;
import com.delvinglanguages.face.activity.add.AddWordFromWarehouseActivity;
import com.delvinglanguages.face.listeners.SpecialKeysBar;
import com.delvinglanguages.listers.StoreWordLister;

public class WarehouseFragment extends ListFragment implements OnClickListener {

	private ArrayList<Nota> lista;

	private ImageButton storeword;
	private EditText edit;
	private StoreWordLister adapter;

	private Word auxiliar;

	private SpecialKeysBar specialKeys;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_warehouse, container, false);

		RelativeLayout background = (RelativeLayout) view
				.findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		lista = ControlCore.getStore();
		adapter = new StoreWordLister(getActivity(), lista);
		setListAdapter(adapter);

		edit = (EditText) view.findViewById(R.id.enter);
		storeword = (ImageButton) view.findViewById(R.id.storeword);
		storeword.setOnClickListener(this);

		auxiliar = new Word(-1, "", "", "", 0, false, 0);

		specialKeys = new SpecialKeysBar(getActivity(), view, new SpecialListener());

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ControlCore.notaToModify = lista.get(position);
		Intent intent = new Intent(getActivity(), AddWordFromWarehouseActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (edit.getText().length() == 0) {
			showMessage(R.string.noword);
		} else {
			String stword = edit.getText().toString();
			auxiliar.setChanges(stword, "", "", 0);
			if (ControlCore.getIdiomaActual(getActivity()).contains(auxiliar)) {
				showMessage(R.string.wordalreadyindb);
				return;
			}
			ControlCore.addToStore(stword);
			adapter.notifyDataSetChanged();
			edit.setText("");
		}
	}

	private void showMessage(int text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	private class SpecialListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String t = edit.getText().toString();
			if (t.length() == 0) {
				t = t + v.getTag();
			} else {
				Button b = (Button) v;
				t = t + b.getText();
			}
			edit.setText(t);
			edit.setSelection(t.length());
		}

	}
}
