package com.delvinglanguages.face.activity.theme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeKernelControl;
import com.delvinglanguages.listers.ThemePairLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class ThemeActivity extends ListActivity implements Messages {

	private Theme theme;
	private ThemeKernelControl kernel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_theme, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		kernel = new ThemeKernelControl(this);
		int thpos = getIntent().getExtras().getInt(THEME);
		theme = kernel.getThemes().get(thpos);

		displayThemeName();
		setListAdapter(new ThemePairLister(this, theme.getPairs()));
	}

	private void displayThemeName() {
		String name = theme.getName();
		setTitle(name);
		((TextView) findViewById(R.id.title)).setText(name);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			displayThemeName();
			setListAdapter(new ThemePairLister(this, theme.getPairs()));
			setResult(Activity.RESULT_OK, null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.theme, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.m_edit) {
			Intent intent = new Intent(this, ModifyThemeActivity.class);
			intent.putExtra(THEME, theme.id);
			startActivityForResult(intent, 0);
			return true;
		}
		if (id == R.id.m_remove) {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.title_removing));
			builder.setMessage(R.string.removethemequestion);
			builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					remove();
				}
			});
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();
		}
		return super.onOptionsItemSelected(item);
	}

	private void remove() {
		kernel.removeTheme(theme);
		setResult(Activity.RESULT_OK, null);
		Toast.makeText(this, R.string.toast_themeremoved, Toast.LENGTH_SHORT).show();
		finish();
	}
}
