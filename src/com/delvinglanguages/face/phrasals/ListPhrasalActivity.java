package com.delvinglanguages.face.phrasals;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.ReferenceActivity;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.listers.ReferenceLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class ListPhrasalActivity extends ListActivity implements Runnable, Messages {

	private DReferences values;
	private ReferenceLister adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_simple_list, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		values = new DReferences();
		adapter = new ReferenceLister(this, values, true);
		setListAdapter(adapter);
		new Thread(this).start();

	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		Intent intent = new Intent(this, ReferenceActivity.class);
		intent.putExtra(DREFERENCE, values.get(pos).getName());
		startActivity(intent);
	}

	@Override
	public void run() {
		DReferences lista = LanguageKernelControl.getPhrasalVerbs();
		for (DReference ref : lista) {
			values.add(ref);
		}
		adapter.notifyDataSetChanged();
	}
}
