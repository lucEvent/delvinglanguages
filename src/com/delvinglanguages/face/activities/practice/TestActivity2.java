package com.delvinglanguages.face.activities.practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.settings.Configuraciones;

public class TestActivity2 extends Activity implements OnClickListener {

	// Delving Activity
	private Test test;

	// Elementos graficos
	private Button next, previous;
	private TextView delv, delv_p, nativ, nativ_p;
	private TextView labels[];

	private int index;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_test_delving);

		RelativeLayout background = (RelativeLayout) findViewById(R.id.ta2_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		test = ControlCore.testActual;
		test.state = Test.STAT_DELVING;

		next = (Button) findViewById(R.id.ta2_next);
		previous = (Button) findViewById(R.id.ta2_previous);

		next.setOnClickListener(this);
		previous.setOnClickListener(this);

		labels = new TextView[Configuraciones.NUM_TYPES];
		labels[0] = (TextView) findViewById(R.id.ta2_noun);
		labels[1] = (TextView) findViewById(R.id.ta2_verb);
		labels[2] = (TextView) findViewById(R.id.ta2_adj);
		labels[3] = (TextView) findViewById(R.id.ta2_adv);
		labels[4] = (TextView) findViewById(R.id.ta2_phrasal);
		labels[5] = (TextView) findViewById(R.id.ta2_expression);
		labels[6] = (TextView) findViewById(R.id.ta2_other);

		delv = (TextView) findViewById(R.id.ta2_delv);
		delv_p = (TextView) findViewById(R.id.ta2_delv_p);
		nativ = (TextView) findViewById(R.id.ta2_nativ);
		nativ_p = (TextView) findViewById(R.id.ta2_nativ_p);

		for (index = 0; test.passed[index]; index++)
			;
		actualiza();
	}

	@Override
	public void onClick(View view) {
		if (view == next) {
			index++;
			if (index < test.references.size()) {
				actualiza();
			} else {
				test.nextStat();
				startActivity(new Intent(this, TestActivity3.class));
				finish();
			}
		} else if (view == previous) {
			index--;
			actualiza();

		}
	}

	private void actualiza() {
		DReference ref = test.references.get(index);

		int type = ref.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				labels[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				labels[i].setBackgroundColor(0xFFCCCCCC);
			}
		}

		delv.setText(ref.item);
		nativ.setText(ref.getTranslation());
		if (ControlCore.getIdiomaActual(this).isIdiomaNativo()) {
			nativ_p.setText("[ " + ref.getPronunciation() + " ]");
		} else {
			delv_p.setText("[ " + ref.getPronunciation() + " ]");
		}

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

	/** *************** METODOS DE MENÚ *************** **/
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
				test.passed[i] = true;
			}

			if (test.isSaved()) {

				ControlCore.saveTestActual();
				showMessage(R.string.testsaved);

			} else {

				new InputDialog(this).create().show();

			}
			return true;
		case R.id.menu_test_remove:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.title_removingtest);
			builder.setMessage(R.string.removetestquestion);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							ControlCore.removeTestActual();
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
