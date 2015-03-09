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

	private ArrayList<Word> values;
	private LayoutInflater inflater;

	public RemovedWordLister(Context context) {
		super(context, R.layout.i_bin, ControlCore.getPapelera());
		this.values = ControlCore.getPapelera();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.i_bin, parent, false);
		}

		Word pal = values.get(position);
		TextView word = (TextView) view.findViewById(R.id.word);
		TextView tranlation = (TextView) view.findViewById(R.id.translation);
		Button restore = (Button) view.findViewById(R.id.restore);
		restore.setTag(position);
		restore.setOnClickListener(this);

		word.setText(pal.getName());
		tranlation.setText(pal.getTranslation());
		return view;
	}

	private Button clicked;

	@Override
	public void onClick(View button) {
		if (button == clicked) {
			restoreWord((Integer) button.getTag());
			clicked.setBackgroundResource(android.R.drawable.ic_menu_revert);
			clicked = null;
		} else {
			if (clicked != null) {
				clicked.setBackgroundResource(android.R.drawable.ic_menu_revert);
			}
			clicked = (Button) button;
			clicked.setBackgroundResource(R.drawable.ic_revert_pressed);
		}
	}

	private void restoreWord(int position) {
		ControlCore.restorePalabra(position);
		notifyDataSetChanged();
	}

}
