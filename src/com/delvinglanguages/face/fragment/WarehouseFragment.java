package com.delvinglanguages.face.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordFromWarehouseActivity;
import com.delvinglanguages.face.view.SpecialKeysBar;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.listers.StoreWordLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class WarehouseFragment extends ListFragment implements OnClickListener, Messages {

	private ImageButton storeword;
	private EditText edit;
	private StoreWordLister adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.a_warehouse, container, false);

		Settings.setBackgroundTo(view);

		adapter = new StoreWordLister(getActivity(), LanguageKernelControl.getDrawerWords());
		setListAdapter(adapter);

		edit = (EditText) view.findViewById(R.id.enter);
		storeword = (ImageButton) view.findViewById(R.id.storeword);
		storeword.setOnClickListener(this);

		IDDelved idioma = KernelControl.getCurrentLanguage();
		String hint_word = getString(R.string.enterwordin);
		if (idioma.isNativeLanguage()) {
			edit.setHint(hint_word + " " + Settings.IdiomaNativo);
		} else {
			edit.setHint(hint_word + " " + idioma.getName());
		}

		new SpecialKeysBar(view, new EditText[] { edit });

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), AddWordFromWarehouseActivity.class);
		intent.putExtra(STORE_NOTE, position);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (edit.getText().length() == 0) {
			showMessage(R.string.noword);
		} else {
			String stword = edit.getText().toString();
			if (LanguageKernelControl.getCurrentLanguage().getPalabra(stword) != null) {
				showMessage(R.string.wordalreadyindb);
				return;
			}
			KernelControl.addToStore(stword);
			adapter.notifyDataSetChanged();
			edit.setText("");
		}
	}

	private void showMessage(int text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

}
