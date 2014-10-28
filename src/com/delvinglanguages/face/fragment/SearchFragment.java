package com.delvinglanguages.face.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.communications.NetWork;
import com.delvinglanguages.communications.WordReference;
import com.delvinglanguages.communications.WordReference.WRItem;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.face.activity.add.AddWordFromModifyActivity;
import com.delvinglanguages.face.activity.add.AddWordFromSearchActivity;
import com.delvinglanguages.settings.Configuraciones;

public class SearchFragment extends Fragment implements OnClickListener,
		NetWork, TextWatcher {

	private static final String DEBUG = "##SearchActivity##";

	private EditText input;
	private Button search, add;

	private LinearLayout list;

	private WordReference dictionary;

	private LayoutInflater inflater;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_search, container, false);

		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			view.setBackgroundDrawable(Configuraciones.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			view.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		this.inflater = inflater;
		input = (EditText) view.findViewById(R.id.input);
		search = (Button) view.findViewById(R.id.search);
		add = (Button) view.findViewById(R.id.add);
		list = (LinearLayout) view.findViewById(R.id.list);

		input.addTextChangedListener(this);
		search.setOnClickListener(this);
		add.setOnClickListener(this);
		add.setEnabled(false);

		dictionary = new WordReference(this);

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		}
		search.requestFocus();
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v == search) {
			searchedWord = input.getText().toString();
			dictionary.getContent(searchedWord);

			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} else if (v == add) {
			StringBuilder trans = new StringBuilder("");
			int type = 0;
			for (int i = 0; i < list.getChildCount(); i++) {
				View view = list.getChildAt(i);
				CheckedTextView ctw = (CheckedTextView) view
						.findViewById(R.id.text);
				if (ctw.isChecked()) {
					WRItem d = data.get(i);
					if (trans.length() != 0) {
						trans.append(", ");
					}
					trans.append(d.name);
					type |= d.type;
				}
			}
			Intent intent = new Intent(getActivity(),
					AddWordFromSearchActivity.class);
			intent.putExtra(AddWordActivity.SEND_NAME, searchedWord);
			intent.putExtra(AddWordActivity.SEND_TRANSLATION, trans.toString());
			intent.putExtra(AddWordActivity.SEND_TYPE, type);
			startActivity(intent);

		} else {
			((CheckedTextView) v).toggle();
		}
	}

	private ArrayList<WRItem> data;
	private String searchedWord;

	@Override
	public void datagram(int code, String message, Object packet) {
		if (code == NetWork.OK) {
			searchedWord = message;
			list.removeAllViews();
			data = (ArrayList<WRItem>) packet;
			for (WRItem item : data) {
				list.addView(getRow(item.type, item.name));
				Log.d(DEBUG, "Added ->" + item.name);
			}
			add.setEnabled(true);
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
		View view = inflater.inflate(R.layout.i_search_row, null);

		TextView btype = (TextView) view.findViewById(R.id.type);
		CheckedTextView ttext = (CheckedTextView) view.findViewById(R.id.text);
		ttext.setOnClickListener(this);

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

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		add.setEnabled(false);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

}
