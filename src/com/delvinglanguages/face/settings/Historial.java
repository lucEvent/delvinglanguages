package com.delvinglanguages.face.settings;

import java.util.ArrayList;

import com.delvinglanguages.R;
import com.delvinglanguages.core.HistorialItem;
import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.listers.HistorialLister;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class Historial extends ListActivity {

	private ArrayList<HistorialItem> items;
	private ControlDisco disco;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_simple_list);
		disco = new ControlDisco();
		items = disco.getHistorial();

		setListAdapter(new HistorialLister(this,items));

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
		case HistorialItem.ITEM_LANG_CREATED:
		case HistorialItem.ITEM_LANG_ERASED:
		case HistorialItem.ITEM_LANG_INTEGRATED:
		case HistorialItem.ITEM_STORE_ADDED:
		case HistorialItem.ITEM_WORD_CREATED:
		case HistorialItem.ITEM_WORD_VIEWED:
		case HistorialItem.ITEM_WORD_MODIFIED:
		case HistorialItem.ITEM_WORD_REMOVED:
		case HistorialItem.ITEM_BIN_CLEARED:
		case HistorialItem.ITEM_TENSE_ADDED:
		case HistorialItem.ITEM_TENSE_MODIFIED:
		case HistorialItem.ITEM_TEST_CREATED:
		case HistorialItem.ITEM_TEST_DONE:
		case HistorialItem.ITEM_TEST_REMOVED:
		case HistorialItem.ITEM_PRACTISED_MATCH:
		case HistorialItem.ITEM_PRACTISED_COMPLETE:
		case HistorialItem.ITEM_PRACTISED_WRITE:
		case HistorialItem.ITEM_SET_CHANGE_NATIVE_NAME:
		case HistorialItem.ITEM_SET_CHANGE_BACKGROUND:
		case HistorialItem.ITEM_SET_CHANGE_DOUBLEDIRECTION:
		case HistorialItem.ITEM_CHANGE_LANGNAME:
		case HistorialItem.ITEM_CHANGE_EN_PRHASAL:
		case HistorialItem.ITEM_CHANGE_EN_ADJ:
		case HistorialItem.ITEM_STATISTICS_CLEARED:

			break;
		}

	}

}
