package com.delvinglanguages.face.activities.practics;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.communications.NetWork;
import com.delvinglanguages.communications.WordReference;
import com.delvinglanguages.communications.WordReference.WRItem;
import com.delvinglanguages.settings.Configuraciones;

public class TranslateActivity extends Activity implements OnClickListener,
		NetWork {

	private static final String DEBUG = "##TranslateA##";
	private EditText input;
	private Button search;
	private LinearLayout list;

	private WordReference dictionary;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_translate);

		RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		input = (EditText) findViewById(R.id.tr_input);
		search = (Button) findViewById(R.id.tr_search);
		list = (LinearLayout) findViewById(R.id.list);

		search.setOnClickListener(this);

		dictionary = new WordReference(this);
	}

	@Override
	public void onClick(View v) {
		dictionary.getContent(input.getText().toString());

	}

	@Override
	public void datagram(int code, String message, Object packet) {
		if (code == NetWork.OK) {
			list.removeAllViews();
			ArrayList<WRItem> data = (ArrayList<WRItem>) packet;
			for (WRItem item : data) {
				list.addView(getRow(item.type, item.name));
				Log.d(DEBUG, "Added ->" + item.typeCode + " " + item.name);
			}
		}
	}

	private int[] bgtypes = { R.drawable.type_noun_backgrounds,
			R.drawable.type_verb_backgrounds,
			R.drawable.type_adjective_backgrounds,
			R.drawable.type_adverb_backgrounds,
			R.drawable.type_phrasal_backgrounds,
			R.drawable.type_expression_backgrounds,
			R.drawable.type_other_backgrounds };

	private int[] ttypes = { R.string.nn, R.string.vb, R.string.adj,
			R.string.adv, R.string.ph_v, R.string.exp, R.string.oth };

	private View getRow(int type, String content) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.i_search_row, null);

		TextView btype = (TextView) view.findViewById(R.id.type);
		TextView ttext = (TextView) view.findViewById(R.id.text);

		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				btype.setBackgroundResource(bgtypes[i]);
				btype.setText(ttypes[i]);
				break;
			}
		}
		btype.setSelected(true);

		ttext.setText(content);
		return view;
	}

}
