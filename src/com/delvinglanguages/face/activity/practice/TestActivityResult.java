package com.delvinglanguages.face.activity.practice;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.listers.TestStatsLister;
import com.delvinglanguages.settings.Configuraciones;

public class TestActivityResult extends ListActivity implements OnClickListener {

	private RelativeLayout background;

	private Test test;

	private Button save, redo, finish;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_test_statistics);

		background = (RelativeLayout) findViewById(R.id.ts_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		test = ControlCore.testActual;
		test.state = Test.STAT_STATISTICS;

		save = (Button) findViewById(R.id.ts_save);
		redo = (Button) findViewById(R.id.ts_redo);
		finish = (Button) findViewById(R.id.ts_exit);
		save.setOnClickListener(this);
		redo.setOnClickListener(this);
		finish.setOnClickListener(this);

		setListAdapter(new TestStatsLister(this));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onClick(View v) {
		if (v == save) {
			saveTest();
		} else if (v == redo) {
			test.nextStat();
			startActivity(new Intent(this, TestActivityLearn.class));
			finish();
		} else if (v == finish) {
			finish();
		}
	}

	private void saveTest() {
		if (test.isSaved()) {
			ControlCore.saveTestActual();
			showMessage(R.string.testsaved);
		} else {
			new InputDialog(this).create().show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		menu.findItem(R.id.menu_test_save).setVisible(false);
		if (!test.isSaved()) {
			menu.findItem(R.id.menu_test_remove).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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
