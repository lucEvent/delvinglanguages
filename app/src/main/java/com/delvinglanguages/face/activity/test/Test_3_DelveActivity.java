package com.delvinglanguages.face.activity.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.settings.Settings;

public class Test_3_DelveActivity extends Activity {

    private TestKernelControl kernel;
    private Test test;

    private Button next, previous;
    private TextView delv, delv_p, nativ, nativ_p;
    private TextView labels[];

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.a_test_delving, null);
        Settings.setBackgroundTo(view);
        setContentView(view);

        kernel = new TestKernelControl();
        test = TestKernelControl.runningTest;
        test.state = Test.PHASE_DELVING;

        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);

        labels = new TextView[7];
        labels[Word.NOUN] = (TextView) findViewById(R.id.noun);
        labels[Word.VERB] = (TextView) findViewById(R.id.verb);
        labels[Word.ADJECTIVE] = (TextView) findViewById(R.id.adjective);
        labels[Word.ADVERB] = (TextView) findViewById(R.id.adverb);
        labels[Word.PHRASAL] = (TextView) findViewById(R.id.phrasal);
        labels[Word.EXPRESSION] = (TextView) findViewById(R.id.expression);
        labels[Word.OTHER] = (TextView) findViewById(R.id.other);

        Language idioma = KernelControl.getCurrentLanguage();
        if (!idioma.getSettings(Language.MASK_PH)) {
            labels[Word.PHRASAL].setVisibility(View.GONE);
        }

        delv = (TextView) findViewById(R.id.delv_name);
        delv_p = (TextView) findViewById(R.id.delv_pron);
        nativ = (TextView) findViewById(R.id.native_name);
        nativ_p = (TextView) findViewById(R.id.nativ_pron);

        for (index = 0; test.references.get(index).passed; index++) ;
        actualiza();
    }

    public void nextWord(View v) {
        index++;
        if (index < test.references.size()) {
            actualiza();
        } else {
            test.nextStat();
            startActivity(new Intent(this, Test_4_MatchActivity.class));
            finish();
        }
    }

    public void previousWord(View v) {
        index--;
        actualiza();
    }

    private void actualiza() {
        DReference ref = test.references.get(index).reference;

        Settings.setBackgroundColorsforType(labels, ref.getType());
        delv.setText(ref.name);
        nativ.setText(ref.getTranslationsAsString());
        delv_p.setText("[ " + ref.pronunciation + " ]");

        if (index == 0) {
            previous.setEnabled(false);
        } else {
            previous.setEnabled(true);
        }
        if (index == test.references.size() - 1) {
            next.setText(getString(R.string.goon));
        } else {
            next.setText(getString(R.string.next));
        }
    }

    /**
     * ************** METODOS DE MENU ***************
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test, menu);
        if (!test.isSaved()) {
            menu.findItem(R.id.menu_test_remove).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_test_save:
                for (int i = 0; i < index; i++) {
                    test.references.get(i).passed = true;
                }

                if (test.isSaved()) {

                    kernel.saveTest(test);
                    showMessage(R.string.testsaved);

                } else {

                    new InputDialog(this).create().show();

                }
                return true;
            case R.id.menu_test_remove:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.title_removingtest);
                builder.setMessage(R.string.removetestquestion);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        kernel.removeTest(test);
                        showMessage(R.string.testremoved);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();

                return true;
        }
        return false;
    }

    private void showMessage(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
