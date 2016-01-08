package com.delvinglanguages.face.activity.add;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.face.dialog.AddInflexionDialog;
import com.delvinglanguages.face.listeners.FoneticsKeyboard;
import com.delvinglanguages.face.view.SpecialKeysBar;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Inflexions;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.listers.InflexionLister;
import com.delvinglanguages.net.internal.NetWork;
import com.delvinglanguages.settings.Settings;

public class AddWordActivity extends ListActivity implements TextWatcher, OnClickListener, NetWork, OnFocusChangeListener {

    protected Language idioma;

    protected EditText word, pronuntiation;

    protected Button remove;

    protected FoneticsKeyboard fonetickb;

    protected Inflexions inflexions;
    protected InflexionLister adapter;

    protected AddInflexionDialog inflexionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_add_word, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        idioma = KernelControl.getCurrentLanguage();

        word = (EditText) findViewById(R.id.word);
        word.addTextChangedListener(this);
        word.setOnFocusChangeListener(this);
        pronuntiation = (EditText) findViewById(R.id.pronunciation);
        pronuntiation.setOnFocusChangeListener(this);

        String hint_word = getString(R.string.enterwordin);
        String hint_pron = getString(R.string.enterpronuntiationin);
        String delved_lang = idioma.language_delved_name;

        word.setHint(hint_word + " " + delved_lang);
        pronuntiation.setHint(hint_pron + " " + delved_lang);

        new SpecialKeysBar(findViewById(R.id.letrasespeciales), new EditText[]{word, pronuntiation});

        fonetickb = new FoneticsKeyboard(this, R.id.ap_keyboard, pronuntiation, idioma.CODE);

        setInflexionsList(new Inflexions());
        inflexionManager = new AddInflexionDialog(this, this);

        setTitle(R.string.addingnewword);
    }

    @Override
    public void onBackPressed() {
        if (fonetickb.isCustomKeyboardVisible())
            fonetickb.hideCustomKeyboard();
        else
            finish();
    }

    protected void get_and_set_Name(Bundle bundle) {
        String name = bundle.getString(AppCode.NAME);
        if (name != null) {
            word.setText(name);
            word.setSelection(name.length());
        }
    }

    protected void get_and_set_Pronuntiation(Bundle bundle) {
        String value = bundle.getString(AppCode.PRONUNCIATION);
        if (value != null) {
            pronuntiation.setText(value);
        }
    }

    protected void setInflexionsList(Inflexions list) {
        inflexions = list;
        adapter = new InflexionLister(this, list, true, this);
        setListAdapter(adapter);
    }

    public void addTranslation(View v) {
        editing = false;
        fonetickb.hideCustomKeyboard();
        inflexionManager.show();
    }

    public void cancel(View v) {
        finish();
    }

    public void secondOption(View v) {
        if (addWord()) {
            word.setText("");
            adapter.clear();
            pronuntiation.setText("");
            word.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public void addWord(View v) {
        if (addWord()) {
            finish();
        }
    }

    protected boolean addWord() {
        String nombre = word.getText().toString();
        if (nombre.isEmpty()) {
            showMessage(R.string.noword);
            return false;
        }
        if (inflexions.isEmpty()) {
            showMessage(R.string.notrans);
            return false;
        }
        String pron = pronuntiation.getText().toString();
        if (pron.isEmpty()) {
            showMessage(R.string.nopron);
            return false;
        }
        saveWord(nombre, inflexions, pron);
        return true;
    }

    protected void saveWord(String nombre, Inflexions inflexions, String pron) {
        if (autocomplete) {
            KernelControl.updateWord(modifiedWord, nombre, inflexions, pron);
            showMessage(R.string.msswordmodified);
            return;
        }
        KernelControl.addWord(nombre, inflexions, pron, Word.INITIAL_PRIORITY);
        showMessage(R.string.msswordadded);
    }

    @Override
    public void onFocusChange(View view, boolean hasfocus) {
        if (hasfocus) {
            EditText ed = (EditText) view;
            ed.setSelection(ed.getText().length());
        }
    }

    protected boolean autocomplete = false;
    protected Word modifiedWord = null;
    private Inflexions savedInflexions = null;
    private Editable savedPronunciation = null;

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (autocomplete) {
            if (remove != null) {
                remove.setEnabled(true);
            }
            autocomplete = false;
            pronuntiation.setText(savedPronunciation);
            inflexions = savedInflexions;
        }
        modifiedWord = null;
        if (s.length() > 0) {
            Words found = idioma.getWords(s.toString());
            modifiedWord = found == null ? null : found.get(0);
        }
        if (modifiedWord != null) {
            if (remove != null) {
                remove.setEnabled(false);
            }
            autocomplete = true;

            savedPronunciation = pronuntiation.getText();
            savedInflexions = inflexions;

            pronuntiation.setText(modifiedWord.getPronunciation());
            inflexions = modifiedWord.getInflexions();
        }
    }

    protected void showMessage(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private boolean editing = false;
    private Inflexion t_editing;

    @Override
    public void datagram(int code, String message, Object packet) {
        switch (code) {
            case NetWork.ERROR:
                showMessage((Integer) packet);
                inflexionManager.show();
                break;
            case NetWork.OK:
                Inflexion i = (Inflexion) packet;
                if (editing) {
                    editing = false;
                    t_editing.setInflexions(i.getInflexions());
                    t_editing.setTranslations(i.getTranslations());
                    t_editing.setType(i.getType());
                } else {
                    inflexions.add(i);
                }
                adapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(pronuntiation.getWindowToken(), 0);
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        Inflexion inflexion = inflexions.get((Integer) v.getTag());
        if (v.getId() == R.id.edit) {
            editing = true;
            t_editing = inflexion;
            fonetickb.hideCustomKeyboard();
            inflexionManager.show(inflexion);
        } else if (v.getId() == R.id.remove) {
            adapter.remove(inflexion);
        }
    }

}
