package com.delvinglanguages.face.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.net.internal.NetWork;
import com.delvinglanguages.settings.Settings;

public class AddTranslationDialog extends Builder implements android.view.View.OnClickListener, OnFocusChangeListener {

	private static final String DEBUG = "##AddTranslationDialog##";

	private AlertDialog dialog;

	private EditText input;

	protected Button[] types;

	private NetWork net;

	public AddTranslationDialog(Context context, NetWork net) {
		super(context);
		this.net = net;

		View view = getView(context);

		setView(view);
		setPositiveButton(R.string.add, null);
		setNeutralButton("Add more", null);
		setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearFields();
			}
		});

		dialog = create();

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface di) {

				InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				}
				input.setSelection(input.getText().length());
				
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						onAddAction();
					}
				});
				dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						onAddMoreAction();
					}
				});
			}
		});

	}

	private View getView(Context context) {
		View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.d_add_translation, null);
		Settings.setBackgroundTo(view);
		
		input = (EditText) view.findViewById(R.id.input);
		input.setOnFocusChangeListener(this);
		String hint = context.getString(R.string.entertranslationin);
		if (LanguageKernelControl.isNativeLanguage()) {
			input.setHint(hint + " " + LanguageKernelControl.getLanguageName());
		} else {
			input.setHint(hint + " " + Settings.IdiomaNativo);
		}

		int[] i_types = new int[] { R.id.sel_noun, R.id.sel_verb, R.id.sel_adj, R.id.sel_adv, R.id.sel_phrasal, R.id.sel_expression, R.id.sel_other };
		types = new Button[Settings.NUM_TYPES];

		for (int i = 0; i < types.length; i++) {
			types[i] = (Button) view.findViewById(i_types[i]);
			types[i].setOnClickListener(this);
		}

		return view;
	}

	private void onAddAction() {
		if (checkAndAdd()) {
			dialog.dismiss();
		}
	}

	private void onAddMoreAction() {
		checkAndAdd();
	}

	private boolean checkAndAdd() {
		String translation = input.getText().toString();
		if (translation.isEmpty()) {
			net.datagram(NetWork.ERROR, "", R.string.notrans);
			return false;
		}
		int type = 0;
		for (int i = 0; i < types.length; i++) {
			if (types[i].isSelected()) {
				type += (1 << i);
			}
		}
		if (type == 0) {
			net.datagram(NetWork.ERROR, "", R.string.notype);
			return false;
		}
		net.datagram(NetWork.OK, "", new Translation(translation, type));

		clearFields();
		return true;
	}

	private void setType(int type) {
		for (int i = 0; i < Settings.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				types[i].setSelected(true);
			} else if (types[i].isSelected()) {
				types[i].setSelected(false);
			}
		}
	}

	private void clearFields() {
		input.setText("");
		setType(0);
	}

	@Override
	public AlertDialog show() {
		dialog.show();
		input.requestFocus();
		return dialog;
	}

	public void show(Translation translation) {
		input.setText(translation.name);
		setType(translation.type);
		this.show();
	}

	@Override
	public void onClick(View v) {
		for (Button btype : types) {
			btype.setSelected(false);
		}
		v.setSelected(true);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			EditText e = (EditText) v;
			e.setSelection(e.getText().length());
		}
	}

}
