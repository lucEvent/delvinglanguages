package com.delvinglanguages.face.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.dialog.IntegrateManager;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.listers.AvailableLanguageLister;
import com.delvinglanguages.settings.Settings;

public class LanguageSettingsActivity extends Activity implements OnItemSelectedListener {

    private Spinner spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_language_settings, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        Language idioma = KernelControl.getCurrentLanguage();
        ((CheckedTextView) findViewById(R.id.phrasal_state)).setChecked(idioma.getSettings(Language.MASK_PH));
        ((CheckedTextView) findViewById(R.id.adjective_state)).setChecked(idioma.getSettings(Language.MASK_ADJ));
        ((CheckedTextView) findViewById(R.id.special_chars_state)).setChecked(idioma.getSettings(Language.MASK_ESP_CHARS));

        spinner = (Spinner) findViewById(R.id.selector);
        spinner.setAdapter(new AvailableLanguageLister(this, getResources().getStringArray(R.array.languages)));
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(idioma.CODE);
    }

    public void changeLanguageName(View v) {
        String name = LanguageKernelControl.getLanguageName();

        View view = LayoutInflater.from(this).inflate(R.layout.i_input, null);
        final EditText input = (EditText) view.findViewById(R.id.input_dialog);

        input.setText(name);
        input.setSelection(name.length());

        new AlertDialog.Builder(this).setTitle(R.string.renaminglanguage).setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int id) {
                        String s = input.getText().toString();
                        if (s.length() == 0) {
                            showMessage(R.string.nonamelang);
                        } else {
                            KernelControl.updateLanguage(KernelControl.getCurrentLanguage().CODE, s);
                            showMessage(R.string.renamedsuccessfully);
                        }
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(input, InputMethodManager.SHOW_FORCED);
    }

    public void togglePhrasalState(View v) {
        CheckedTextView ctv = (CheckedTextView) v;
        ctv.toggle();
        KernelControl.updateLanguageSettings(ctv.isChecked(), Language.MASK_PH);
    }

    public void toggleAdjectiveState(View v) {
        CheckedTextView ctv = (CheckedTextView) v;
        ctv.toggle();
        KernelControl.updateLanguageSettings(ctv.isChecked(), Language.MASK_ADJ);
    }

    public void toggleSpecialCharacterState(View v) {
        CheckedTextView ctv = (CheckedTextView) v;
        ctv.toggle();
        KernelControl.updateLanguageSettings(ctv.isChecked(), Language.MASK_ESP_CHARS);
    }

    public void integrate(View v) {
        IntegrateManager im = new IntegrateManager(this);
        im.start();
    }

    public void remove(View v) {
        String temp = getString(R.string.title_removing) + " " + LanguageKernelControl.getLanguageName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(temp);
        builder.setMessage(R.string.removeidiomquestion);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                removeLanguage();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public void clearStatistics(View v) {
        KernelControl.clearStatistics();
        showMessage(R.string.mssclearstats);
    }

    private void removeLanguage() {
        String mssg = LanguageKernelControl.getLanguageName() + " " + getResources().getString(R.string._removed);
        KernelControl.deleteLanguage();
        setResult(Activity.RESULT_OK, null);
        showMessage(mssg);
        finish();
    }

    private void showMessage(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String[] languages = getResources().getStringArray(R.array.languages);
        Language language = KernelControl.getCurrentLanguage();

        if (language.language_delved_name.equals(languages[language.CODE])) {
            KernelControl.updateLanguage(pos, languages[pos]);
        } else {
            KernelControl.updateLanguage(pos, language.language_delved_name);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> av) {
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##LanguageSettingsAct##", text);
    }

}
