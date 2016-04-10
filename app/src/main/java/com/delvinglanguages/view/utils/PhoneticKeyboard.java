package com.delvinglanguages.view.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.delvinglanguages.R;
import com.delvinglanguages.Settings;
import com.delvinglanguages.kernel.Language;

public class PhoneticKeyboard implements KeyboardView.OnKeyboardActionListener,
        View.OnFocusChangeListener, View.OnClickListener, View.OnTouchListener {

    private Context context;

    private Vibrator vibrator = null;

    private EditText edittext;

    private KeyboardView keyboardView;

    private Keyboard keyboard_symbols, keyboard_punctuation, keyboard_empty;
    private boolean defaultkb = false; // true->vowels, false->consonants

    public PhoneticKeyboard(Context context, EditText edittext, int langCode) {
        this.context = context;

        if (Settings.getPreferencePhonKBVibration()) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        this.edittext = edittext;
        this.edittext.setOnFocusChangeListener(this);
        this.edittext.setOnClickListener(this);
        this.edittext.setOnTouchListener(this);

        keyboard_punctuation = new Keyboard(context, R.xml.keyboard_punctuation);
        keyboard_empty = new Keyboard(context, R.xml.keyboard_empty);
        switch (langCode) {
            case Language.SV:
                keyboard_symbols = new Keyboard(context, R.xml.keyboard_phonetic_swedish);
                break;
            default:
                keyboard_symbols = new Keyboard(context, R.xml.keyboard_phonetic_english);
        }

        keyboardView = (KeyboardView) ((Activity) context).findViewById(R.id.phonetic_keyboard);

        keyboardView.setKeyboard(keyboard_empty);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void hide() {
        keyboardView.setKeyboard(keyboard_empty);
    }

    private Handler mHandler = new Handler();

    public void show(View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        if (keyboardView.getKeyboard() != keyboard_empty) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        keyboardView.setKeyboard(keyboard_symbols);
                        defaultkb = false;
                    }
                });
            }
        }).start();
    }

    public boolean isVisible() {
        return keyboardView.getKeyboard() != keyboard_empty;
    }

    /**
     * **************** KEYBOARD LISTENER METHODS **************** *
     **/
    public final static int KEYCODE_CANCEL = -3; // Keyboard.KEYCODE_CANCEL

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (vibrator != null) {
            vibrator.vibrate(15);
        }

        Editable editable = edittext.getText();
        int start = edittext.getSelectionStart();
        // Handle key
        if (primaryCode < 0) {
            if (primaryCode == Keyboard.KEYCODE_DELETE) {
                if (editable != null && start > 0)
                    editable.delete(start - 1, start);
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                keyboardView.setKeyboard(defaultkb ? keyboard_symbols : keyboard_punctuation);
                defaultkb = !defaultkb;
            } else if (primaryCode == Keyboard.KEYCODE_DONE) {
                View focusNew = edittext.focusSearch(View.FOCUS_FORWARD);
                if (focusNew != null)
                    focusNew.requestFocus();
            } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                // hideCustomKeyboard();
            }
        } else {// Insert character
            editable.insert(start, Character.toString((char) primaryCode));
            if (keyCodes[1] != -1) {
                char c = (char) keyCodes[1];
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

    /**
     * ********************************************************** *
     **/

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            show(v);
            edittext.setSelection(edittext.getText().length());
        }
        else
            hide();
    }

    @Override
    public void onClick(View v) {
        show(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (keyboardView.getKeyboard() == keyboard_empty) {
            edittext.requestFocus();
        }
        return false;
    }

}
