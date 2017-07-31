package com.delvinglanguages.view.activity.practise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestManager;
import com.delvinglanguages.view.utils.TestListener;

public class TestEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner number_of_words;
    private EditText in_test_name;
    private Button type_filter[];

    private TestManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test_editor);

        dataManager = new TestManager(this);

        in_test_name = (EditText) findViewById(R.id.in_test_name);
        number_of_words = (Spinner) findViewById(R.id.number_of_words);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        number_of_words.setAdapter(adapter);

        int[] button_ids = new int[]{R.id.button_noun, R.id.button_verb, R.id.button_adjective,
                R.id.button_adverb, R.id.button_phrasal, R.id.button_expression,
                R.id.button_preposition, R.id.button_conjunction, R.id.button_other};

        type_filter = new Button[button_ids.length];
        for (int i = 0; i < type_filter.length; i++) {
            type_filter[i] = (Button) findViewById(button_ids[i]);
            type_filter[i].setOnClickListener(this);
            type_filter[i].setSelected(true);
        }

        if (!dataManager.getCurrentList().arePhrasalVerbsEnabled())
            type_filter[4].setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed()
    {
        actionCancel(null);
    }

    @Override
    public void onClick(View v)
    {
        v.setSelected(!v.isSelected());
    }

    public void actionSave(View v)
    {
        String test_name = in_test_name.getText().toString();
        if (test_name.isEmpty()) {
            showMessage(R.string.msg_missing_test_name);
            in_test_name.requestFocus();
            return;
        }

        int numberOfWords = (Integer) number_of_words.getSelectedItem();

        int type = 0;
        for (int i = 0; i < type_filter.length; i++)
            if (type_filter[i].isSelected())
                type += (1 << i);

        if (type == 0) {
            showMessage(R.string.msg_missing_type);
            return;
        }

        Test test = dataManager.createTest(test_name, numberOfWords, type);

        if (test == null) {
            showMessage(R.string.msg_cannot_start_test);
            return;
        }
        setResult(TestListener.TEST_CREATED);

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra(AppCode.TEST_ID, test.id);
        startActivity(intent);
        finish();
    }

    public void actionCancel(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_go_back_without_saving)
                .setNegativeButton(R.string.go_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        TestEditorActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.continue_, null)
                .show();
    }

    private void showMessage(int text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}






