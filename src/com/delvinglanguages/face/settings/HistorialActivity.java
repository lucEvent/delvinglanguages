package com.delvinglanguages.face.settings;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.kernel.Historial;
import com.delvinglanguages.kernel.HistorialItem;
import com.delvinglanguages.listers.HistorialLister;
import com.delvinglanguages.settings.Settings;

public class HistorialActivity extends ListActivity {

	private ArrayList<HistorialItem> items;
	private ControlDisco disco;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_simple_list, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		disco = new ControlDisco();
		items = disco.getHistorial();

		setListAdapter(new HistorialLister(this, items));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.historial, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_clear) {
			// Borrar todo el historial
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		HistorialItem item = items.get(position);
		switch (item.type) {
		case Historial.ITEM_LANG_CREATED:
		case Historial.ITEM_LANG_ERASED:
		case Historial.ITEM_LANG_INTEGRATED:
		case Historial.ITEM_STORE_ADDED:
		case Historial.ITEM_WORD_CREATED:
		case Historial.ITEM_WORD_VIEWED:
		case Historial.ITEM_WORD_MODIFIED:
		case Historial.ITEM_WORD_REMOVED:
		case Historial.ITEM_BIN_CLEARED:
		case Historial.ITEM_TENSE_ADDED:
		case Historial.ITEM_TENSE_MODIFIED:
		case Historial.ITEM_TEST_CREATED:
		case Historial.ITEM_TEST_DONE:
		case Historial.ITEM_TEST_REMOVED:
		case Historial.ITEM_PRACTISED_MATCH:
		case Historial.ITEM_PRACTISED_COMPLETE:
		case Historial.ITEM_PRACTISED_WRITE:
		case Historial.ITEM_SET_CHANGE_NATIVE_NAME:
		case Historial.ITEM_SET_CHANGE_BACKGROUND:
		case Historial.ITEM_SET_CHANGE_DOUBLEDIRECTION:
		case Historial.ITEM_CHANGE_LANGNAME:
		case Historial.ITEM_CHANGE_EN_PRHASAL:
		case Historial.ITEM_CHANGE_EN_ADJ:
		case Historial.ITEM_STATISTICS_CLEARED:

			break;
		}

	}

}
