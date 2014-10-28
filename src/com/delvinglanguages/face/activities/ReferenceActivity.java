package com.delvinglanguages.face.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Palabra;
import com.delvinglanguages.core.Tense;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.face.activity.add.AddWordFromModifyActivity;
import com.delvinglanguages.listers.TranslationLister;
import com.delvinglanguages.listers.WordLister;
import com.delvinglanguages.settings.Configuraciones;

public class ReferenceActivity extends Activity {

	private static final String DEBUG = "##PalabraActivity##";

	private static final String TRANSLATIONS = "Translations";
	private static final String TENSES = "Tense";
	private static final String ACTUALTENSE = "actualTense";

	private static final int REQUEST_EDIT = 0;

	private DReference reference;

	private TextView tword, tpron;
	private TextView ttypes[];

	private ListView transList, tenseList;
	private TableLayout tenseTable;

	private TabHost tabhost;

	private ArrayList<String> translations;
	private String[] tenses, subjects;

	private ArrayAdapter<String> adapter;

	private int actualTense = -1;
	private TabSpec tabspec;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_word);

		LinearLayout background = (LinearLayout) findViewById(R.id.background);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		String s = getIntent().getExtras().getString(ControlCore.sendDReference);
		reference = ControlCore.getIdiomaActual(this).getReference(s);

		tword = (TextView) findViewById(R.id.word);
		tpron = (TextView) findViewById(R.id.pronuntiation);

		ttypes = new TextView[Configuraciones.NUM_TYPES];
		ttypes[0] = (TextView) findViewById(R.id.lab_noun);
		ttypes[1] = (TextView) findViewById(R.id.lab_verb);
		ttypes[2] = (TextView) findViewById(R.id.lab_adj);
		ttypes[3] = (TextView) findViewById(R.id.lab_adv);
		ttypes[4] = (TextView) findViewById(R.id.lab_phrasal);
		ttypes[5] = (TextView) findViewById(R.id.lab_expression);
		ttypes[6] = (TextView) findViewById(R.id.lab_other);

		transList = (ListView) findViewById(R.id.transl_list);
		transList.setOnItemClickListener(new TranslationClickListener());

		tenseList = (ListView) findViewById(R.id.tenses_list);
		tenseTable = (TableLayout) findViewById(R.id.tense_table);

		tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();

		translations = reference.getTranslationArray(null);
		adapter = new TranslationLister(this, translations);
		adapter.setNotifyOnChange(true);
		transList.setAdapter(adapter);

		if (reference.isVerb()) {
			tabhost.addTab(tabhost.newTabSpec(TRANSLATIONS)
					.setIndicator("Translations").setContent(R.id.transl_list));
			tabhost.addTab(tabhost.newTabSpec(TENSES).setIndicator("Tenses")
					.setContent(R.id.tenses_list));

			IDDelved idioma = ControlCore.getIdiomaActual(this);
			int arrayid = idioma.getTensesArray();
			tenses = getResources().getStringArray(arrayid);
			arrayid = idioma.getSubjectArray();
			subjects = getResources().getStringArray(arrayid);

			tenseList.setAdapter(new TranslationLister(this, tenses));
			tenseList.setOnItemClickListener(new TensesClickListener());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_EDIT) {
			if (resultCode == Activity.RESULT_OK) {
				String ref = (String) data.getExtras().get(AddWordActivity.EDITED);
				reference = ControlCore.getIdiomaActual(this).getReference(ref);
				adapter.notifyDataSetChanged();
				
				setResult(Activity.RESULT_OK, new Intent());
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		setTitle(reference.item);
		tword.setText(reference.item);
		tpron.setText("[ " + reference.getPronunciation() + " ]");
		reference.getTranslationArray(translations);

		int type = reference.type;
		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			if ((type & (1 << i)) != 0) {
				ttypes[i].setBackgroundColor(Configuraciones.type_colors[i]);
			} else {
				ttypes[i].setBackgroundColor(0xFFCCCCCC);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.palabra, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Builder builder = new AlertDialog.Builder(this);
		ListView lw = new ListView(this);
		ArrayList<Palabra> v = reference.getPureOwners();
		lw.setAdapter(new WordLister(this, v));
		switch (item.getItemId()) {
		case R.id.m_edit:
			if (v.size() == 1) {
				editAction(0);
				return true;
			}
			lw.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos,
						long id) {
					editAction(pos);
				}

			});

			builder.setTitle(getString(R.string.edit));
			builder.setView(lw);
			builder.create().show();
			break;

		case R.id.m_remove:
			if (v.size() == 1) {
				removeAction(0);
				return true;
			}
			lw.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos,
						long id) {
					removeAction(pos);
				}

			});

			builder.setTitle(getString(R.string.remove));
			builder.setView(lw);
			builder.create().show();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void editAction(int pos) {
		Intent intent = new Intent(this, AddWordFromModifyActivity.class);
		intent.putExtra(AddWordActivity.SEND_WORD, reference.owners.get(pos).id);
		startActivityForResult(intent, REQUEST_EDIT);
	}

	private void removeAction(final int pos) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.title_removing));
		builder.setMessage(R.string.removewordquestion);
		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						remove(pos);
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	private void remove(int pos) {
		ControlCore.throwPalabra(reference.owners.get(pos));
		finish();
	}

	private class TranslationClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d(DEBUG, "Translation selected: " + translations.get(position));
			navigateTo(position);
		}

	}

	private void navigateTo(int pos) {
		ControlCore.switchDictionary();
		Intent intent = new Intent(this, ReferenceActivity.class);
		intent.putExtra(ControlCore.sendDReference,
				reference.links.get(pos).item);
		startActivity(intent);
	}

	private class TensesClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			Tense tense = ControlCore.getTense(reference.id, position);
			if (tense == null) {
				Builder builder = new AlertDialog.Builder(
						ReferenceActivity.this);
				builder.setTitle(reference.item);
				builder.setMessage(R.string.createconjugatuionquestion);
				builder.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								addNewTense(position);
							}

						});
				builder.setNegativeButton(R.string.cancel, null);
				builder.create().show();
				return;
			}
			if (actualTense == -1) {
				tabspec = tabhost.newTabSpec(ACTUALTENSE).setIndicator("")
						.setContent(R.id.tense_table);
				tabhost.addTab(tabspec);
			}
			if (actualTense != position) {
				actualTense = position;
				showActualTense(tense);
				TextView title = (TextView) tabhost.getTabWidget()
						.getChildAt(2).findViewById(android.R.id.title);
				title.setText(tenses[position]);
			}
			tabhost.setCurrentTab(2);
		}

	}

	private void showActualTense(Tense tense) {
		tenseTable.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < tense.getSize(); ++i) {
			TableRow tr = (TableRow) inflater.inflate(R.layout.i_tense_row,
					null, false);

			TextView per = (TextView) tr.findViewById(R.id.subject);
			per.setText(subjects[i]);
			TextView val = (TextView) tr.findViewById(R.id.form);
			val.setText(tense.forms[i]);
			TextView pro = (TextView) tr.findViewById(R.id.pronuntiation);
			pro.setText("[" + tense.pronuntiations[i] + "]");

			tenseTable.addView(tr);
		}
	}

	private void addNewTense(int position) {
		Intent intent = new Intent(this, NewTenseActivity.class);
		intent.putExtra(ControlCore.sendDReference, reference.item);
		intent.putExtra(NewTenseActivity.TENSE, position);
		startActivity(intent);
	}

}
