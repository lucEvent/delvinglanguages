package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Palabra;

public class RemovedWordLister extends ArrayAdapter<Palabra> implements
		OnClickListener {

	private Button[] buttons;
	private ArrayList<Palabra> trash;
	private Context context;

	public RemovedWordLister(Context context) {
		super(context, R.layout.i_bin, ControlCore.getPapelera());
		this.trash = ControlCore.getPapelera();
		this.context = context;
		buttons = new Button[trash.size()];
	//mirar set tag
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View viewres = inflater.inflate(R.layout.i_bin, parent, false);

		Palabra pal = trash.get(position);
		TextView word = (TextView) viewres.findViewById(R.id.tp_word);
		TextView tranlation = (TextView) viewres
				.findViewById(R.id.tp_translation);
		Button restore = (Button) viewres.findViewById(R.id.tp_restore);

		buttons[position] = restore;
		restore.setOnClickListener(this);

		word.setText(pal.getName());
		tranlation.setText(pal.getTranslation());
		return viewres;
	}

	private Button clicked;
	private int positionClicked;

	@Override
	public void onClick(View button) {
		if (button == clicked) {
			restoreWord(positionClicked);
		} else {
			if (clicked != null) {
				clicked.setBackgroundResource(android.R.drawable.ic_menu_revert);
			}
			for (positionClicked = 0; positionClicked < buttons.length; ++positionClicked) {
				if (buttons[positionClicked] == button) {
					clicked = (Button) button;
					clicked.setBackgroundResource(R.drawable.ic_revert_pressed);
					return;
				}
			}
		}
	}

	private void restoreWord(int position) {
		ControlCore.restorePalabra(position);
		buttons = new Button[trash.size()];
		notifyDataSetChanged();
	}

}
