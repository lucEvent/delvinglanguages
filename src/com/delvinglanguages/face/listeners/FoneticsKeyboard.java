package com.delvinglanguages.face.listeners;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.settings.Configuraciones;

public class FoneticsKeyboard implements OnKeyboardActionListener,
		OnFocusChangeListener, OnClickListener, OnLongClickListener,
		OnTouchListener {

	private Activity context;

	private Keyboard puntuation;
	private Keyboard symbols;
	private Keyboard empty;
	private boolean defaultkb = false; // true->vowels, false->consonants

	private KeyboardView view;

	private EditText input;

	private Vibrator vibrator;

	public FoneticsKeyboard(Activity context, int kbViewID, EditText edittext,
			int langCode) {
		this.context = context;

		// Create the Keyboard
		puntuation = new Keyboard(context, R.xml.puntuation_kb);
		empty = new Keyboard(context, R.xml.empty_kb);
		switch (langCode) {
		case IDDelved.SV:
			symbols = new Keyboard(context, R.xml.kb_phon_sv);
			break;
		case IDDelved.EN:
		default:
			symbols = new Keyboard(context, R.xml.kb_phon_en);
		}

		view = (KeyboardView) context.findViewById(kbViewID);

		view.setKeyboard(empty);
		view.setPreviewEnabled(false);
		view.setOnKeyboardActionListener(this);

		input = edittext;

		input.setOnFocusChangeListener(this);
		input.setOnClickListener(this);
		input.setOnLongClickListener(this);
		input.setOnTouchListener(this);

		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public void hideCustomKeyboard() {
		view.setKeyboard(empty);
	}

	private Handler mHandler = new Handler();

	public void showCustomKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

		if (view.getKeyboard() != empty) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						view.setKeyboard(symbols);
						defaultkb = false;
					}
				});
			}
		}).start();
	}

	public boolean isCustomKeyboardVisible() {
		return view.getKeyboard() != empty;
	}

	/** * **************** KEYBOARD LISTENER METHODS **************** * **/
	public final static int KEYCODE_CANCEL = -3; // Keyboard.KEYCODE_CANCEL

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		if (Configuraciones.vibration()) {
			vibrator.vibrate(15);
		}
		// Get the EditText and its Editable
		View focusCurrent = context.getWindow().getCurrentFocus();
		if (focusCurrent == null || focusCurrent.getClass() != EditText.class) {
			return;
		}
		EditText input = (EditText) focusCurrent;
		Editable editable = input.getText();
		int start = input.getSelectionStart();
		// Handle key
		if (primaryCode < 0) {
			if (primaryCode == Keyboard.KEYCODE_DELETE) {
				if (editable != null && start > 0)
					editable.delete(start - 1, start);
			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
				view.setKeyboard(defaultkb ? symbols : puntuation);
				defaultkb = !defaultkb;
			} else if (primaryCode == Keyboard.KEYCODE_DONE) {
				View focusNew = input.focusSearch(View.FOCUS_FORWARD);
				if (focusNew != null)
					focusNew.requestFocus();
			} else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
				// hideCustomKeyboard();
			}
		} else {// Insert character
			editable.insert(start, Character.toString((char) primaryCode));
			if (keyCodes[1] != -1) {
				Character c = Character.valueOf(((char) keyCodes[1]));
				editable.insert(start + 1, Character.toString(c));
			}
		}

	}

	@Override
	public void onPress(int primaryCode) {

	}

	@Override
	public void onRelease(int primaryCode) {
	}

	@Override
	public void onText(CharSequence text) {
	}

	@Override
	public void swipeDown() {
	}

	@Override
	public void swipeLeft() {
	}

	@Override
	public void swipeRight() {
	}

	@Override
	public void swipeUp() {
	}

	/** * ********************************************************** * **/

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (hasFocus) {
			showCustomKeyboard(view);
			EditText ed = (EditText) view;
			ed.setSelection(ed.getText().length());
		} else {
			hideCustomKeyboard();
		}
	}

	@Override
	public void onClick(View v) {
		showCustomKeyboard(v);
	}

	@Override
	public boolean onLongClick(View v) {
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (view.getKeyboard() == empty) {
			input.requestFocus();
		}
		return false;
	}

}
