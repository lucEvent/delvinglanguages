package com.delvinglanguages.face.activity.test;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.dialog.InputDialog;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.listers.TestStatsLister;
import com.delvinglanguages.settings.Settings;

public class Test_7_ResultActivity extends ListActivity {

	private Test test;
	private TestKernelControl kernel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_test_statistics, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		kernel = new TestKernelControl();
		test = TestKernelControl.runningTest;
		test.state = Test.PHASE_STATISTICS;

		setListAdapter(new TestStatsLister(this, test.references));
	}

	public void save(View v) {
		if (test.isSaved()) {
			kernel.saveTest(test);
			showMessage(R.string.testsaved);
		} else {
			new InputDialog(this).create().show();
		}
	}

	public void doItAgain(View v) {
		test.nextStat();
		startActivity(new Intent(this, Test_3_DelveActivity.class));
		finish();
	}

	public void exit(View v) {
		finish();
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
