package com.delvinglanguages.face.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class IntegrateRepeatedActivity extends Activity implements OnClickListener {

	private Words words;

	private TextView name, pronOrig, pronNew;
	private Button[] translateOrig, translateNew, types, typesNew;
	private LinearLayout layOrig, layNew;

	private Button integrate, ignore;

	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_integrate_repeated, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		words = KernelControl.integrateWords;
		index = 0;

		integrate = (Button) findViewById(R.id.air_integrate);
		integrate.setOnClickListener(this);
		ignore = (Button) findViewById(R.id.air_ignore);
		ignore.setOnClickListener(this);

		// Nombre
		name = (TextView) findViewById(R.id.air_name);
		// Tranducciones original
		layOrig = (LinearLayout) findViewById(R.id.air_laytrans);
		// Pronunciacion original
		pronOrig = (TextView) findViewById(R.id.air_pron);
		// Types original
		types = new Button[7];
		types[0] = (Button) findViewById(R.id.air_nn);
		types[1] = (Button) findViewById(R.id.air_vb);
		types[2] = (Button) findViewById(R.id.air_adj);
		types[3] = (Button) findViewById(R.id.air_adv);
		types[4] = (Button) findViewById(R.id.air_phrasal);
		types[5] = (Button) findViewById(R.id.air_expression);
		types[6] = (Button) findViewById(R.id.air_other);
		for (int i = 0; i < types.length; i++) {
			types[i].setOnClickListener(this);
		}

		// Tranducciones new
		layNew = (LinearLayout) findViewById(R.id.air_laynewtrans);
		// Pronunciacion new
		pronNew = (TextView) findViewById(R.id.air_newpron);
		// Types new
		typesNew = new Button[7];
		typesNew[0] = (Button) findViewById(R.id.air_new_nn);
		typesNew[1] = (Button) findViewById(R.id.air_new_vb);
		typesNew[2] = (Button) findViewById(R.id.air_new_adj);
		typesNew[3] = (Button) findViewById(R.id.air_new_adv);
		typesNew[4] = (Button) findViewById(R.id.air_new_phrasal);
		typesNew[5] = (Button) findViewById(R.id.air_new_expression);
		typesNew[6] = (Button) findViewById(R.id.air_new_other);
		for (int i = 0; i < typesNew.length; i++) {
			typesNew[i].setClickable(false);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		siguiente();
	}

	private void siguiente() {
		Word orig = words.get(index);

		// Nombre
		name.setText(orig.getName());
		// Tranducciones original
		layOrig.removeAllViews();
		ArrayList<String> trans = orig.getTranslationArray();
		translateOrig = new Button[trans.size()];
		for (int i = 0; i < trans.size(); ++i) {
			Button tmp = getLabelView();
			tmp.setText(trans.get(i));
			tmp.setSelected(true);
			tmp.setOnClickListener(this);
			translateOrig[i] = tmp;
			layOrig.addView(tmp);
		}
		// Pronunciacion original
		pronOrig.setText(orig.getPronunciation());
		// Types original
		setType(types, orig.getType());

		Word pnew = words.get(index + 1);

		// Tranducciones new
		layNew.removeAllViews();
		trans.clear();
		trans = pnew.getTranslationArray();
		translateNew = new Button[trans.size()];
		for (int i = 0; i < trans.size(); ++i) {
			Button tmp = getLabelView();
			tmp.setText(trans.get(i));
			tmp.setSelected(false);
			tmp.setOnClickListener(this);
			translateNew[i] = tmp;
			layNew.addView(tmp);
		}
		// Pronunciacion new
		pronNew.setText(pnew.getPronunciation());
		// Types new
		setType(typesNew, pnew.getType());

		String intregrating = getResources().getString(R.string.integratelang);
		setTitle(intregrating + " " + ((index >> 1) + 1) + "/" + (words.size() >> 1));
	}

	private int getType() {
		int type = 0;
		for (int i = 0; i < types.length; i++) {
			if (types[i].isSelected()) {
				type += (1 << i);
			}
		}
		return type;
	}

	private void setType(Button[] set, int type) {
		for (int i = 0; i < set.length; ++i) {
			if ((type & (1 << i)) != 0) {
				set[i].setSelected(true);
			} else if (set[i].isSelected()) {
				set[i].setSelected(false);
			}
		}
	}

	private Button getLabelView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return (Button) inflater.inflate(R.layout.i_selectable_label, null);
	}

	@Override
	public void onClick(View v) {
		if (v == integrate) {
			int type = getType();
			if (type == 0) {
				showMessage(R.string.notype);
				return;
			}
			StringBuilder trads = new StringBuilder();
			collectTranslations(trads, translateOrig);
			collectTranslations(trads, translateNew);

			if (trads.length() == 0) {
				showMessage(R.string.notrans);
				return;
			}

			Word p = words.get(index);
			Language tmp = KernelControl.getCurrentLanguage();
			KernelControl.setCurrentLanguage(KernelControl.integrateLanguage);
			// KernelControl.updateWord(p, p.getName(), trads.toString(),
			// p.getPronunciation(), type);
			KernelControl.setCurrentLanguage(tmp);
		} else if (v == ignore) {
		} else {
			v.setSelected(!v.isSelected());
			return;
		}

		index += 2;
		if (words.size() == index) {

			showMessage(R.string.languageintegrated);
			KernelControl.deleteLanguage();
			finish();

		} else {
			siguiente();
		}
	}

	private void collectTranslations(StringBuilder trads, Button[] buttset) {
		for (int i = 0; i < buttset.length; i++) {
			if (buttset[i].isSelected()) {
				if (trads.length() != 0) {
					trads.append(", ");
				}
				trads.append(buttset[i].getText());
			}
		}
	}

	private void showMessage(int text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
