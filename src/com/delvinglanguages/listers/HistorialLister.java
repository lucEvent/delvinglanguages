package com.delvinglanguages.listers;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.HistorialItem;

public class HistorialLister extends ArrayAdapter<HistorialItem> {

	private static final String LANG_CAP = "L";
	private static final String STORE_CAP = "S";
	private static final String WORD_CAP = "W";
	private static final String TEST_CAP = "T";
	private static final String BIN_CAP = "B";
	private static final String SETT_CAP = "S";
	private static final String PRACTISED_CAP = "P";

	private ArrayList<HistorialItem> values;
	private Context context;

	public HistorialLister(Context context, ArrayList<HistorialItem> values) {
		super(context, R.layout.i_word, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		HistorialItem item = values.get(position);
	/*	View root = inflater.inflate(R.layout.i_historialitem, parent, false);

		Button cap = (Button) root.findViewById(R.id.cap);
		TextView description = (TextView) root.findViewById(R.id.description);
		TextView time = (TextView) root.findViewById(R.id.time);

		long t = (System.currentTimeMillis() - item.time) / 60000; // 60000ms =
																	// 60sec/min
																	// *
																	// 1000ms/s

		switch (item.type) {
		case HistorialItem.ITEM_CHANGE_LANGNAME:
		case HistorialItem.ITEM_LANG_CREATED:
		case HistorialItem.ITEM_LANG_ERASED:
		case HistorialItem.ITEM_LANG_INTEGRATED:
			cap.setText(LANG_CAP);
			break;
		case HistorialItem.ITEM_STORE_ADDED:
			cap.setText(STORE_CAP);
			break;
		case HistorialItem.ITEM_WORD_CREATED:
		case HistorialItem.ITEM_WORD_VIEWED:
		case HistorialItem.ITEM_WORD_MODIFIED:
		case HistorialItem.ITEM_WORD_REMOVED:
			cap.setText(WORD_CAP);
			break;
		case HistorialItem.ITEM_BIN_CLEARED:
			cap.setText(BIN_CAP);
			break;
		case HistorialItem.ITEM_TENSE_ADDED:
		case HistorialItem.ITEM_TENSE_MODIFIED:
		case HistorialItem.ITEM_TEST_CREATED:
		case HistorialItem.ITEM_TEST_DONE:
		case HistorialItem.ITEM_TEST_REMOVED:
			cap.setText(TEST_CAP);
			break;
		case HistorialItem.ITEM_PRACTISED_MATCH:
		case HistorialItem.ITEM_PRACTISED_COMPLETE:
		case HistorialItem.ITEM_PRACTISED_WRITE:
			cap.setText(PRACTISED_CAP);
			break;
		case HistorialItem.ITEM_SET_CHANGE_NATIVE_NAME:
		case HistorialItem.ITEM_SET_CHANGE_BACKGROUND:
		case HistorialItem.ITEM_SET_CHANGE_DOUBLEDIRECTION:
		case HistorialItem.ITEM_CHANGE_EN_PRHASAL:
		case HistorialItem.ITEM_CHANGE_EN_ADJ:
		case HistorialItem.ITEM_STATISTICS_CLEARED:
			cap.setText(SETT_CAP);
			break;

		}

		return root;
*/
	return null;	
	}

}
