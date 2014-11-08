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
import com.delvinglanguages.core.Word;

public class RemovedWordLister extends ArrayAdapter<Word> implements
		OnClickListener {

	private Button[] buttons;
	private ArrayList<Word> trash;
	private LayoutInflater inflater;

	public RemovedWordLister(Context context) {
		super(context, R.layout.i_bin, ControlCore.getPapelera());
		this.trash = ControlCore.getPapelera();
		buttons = new Button[trash.size()];
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_bin, parent, false);
		}
		
		Word pal = trash.get(position);
		TextView word = (TextView) view.findViewById(R.id.word);
		TextView tranlation = (TextView) view.findViewById(R.id.translation);
		Button restore = (Button) view.findViewById(R.id.restore);

		buttons[position] = restore;
		restore.setOnClickListener(this);

		word.setText(pal.getName());
		tranlation.setText(pal.getTranslation());
		return view;
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
