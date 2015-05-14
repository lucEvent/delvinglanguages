package com.delvinglanguages.face.activity;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordFromModifyActivity;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.listers.TranslationLister;
import com.delvinglanguages.listers.WordLister;
import com.delvinglanguages.net.internal.Messages;
import com.delvinglanguages.settings.Settings;

public class ReferenceActivity extends ListActivity implements Messages {

	private static final String DEBUG = "##ReferenceActivity##";

	private static final int REQUEST_MODIFIED = 0;

	private DReference reference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_word, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		String s = getIntent().getExtras().getString(DREFERENCE);

		IDDelved idioma = KernelControl.getCurrentLanguage();
		reference = idioma.getReference(s);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MODIFIED) {
			if (resultCode == Activity.RESULT_OK) {
				setResult(Activity.RESULT_OK, null);

				String ref = (String) data.getExtras().get(EDITED);
				reference = LanguageKernelControl.getReference(ref.split(",")[0]);
				if (reference == null) {
					finish();
				}

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		displayReference();
	}

	private void displayReference() {
		setTitle(reference.getName());

		((TextView) findViewById(R.id.word)).setText(reference.getName());
		((TextView) findViewById(R.id.pronuntiation)).setText("[ " + reference.getPronunciation() + " ]");
		setListAdapter(new TranslationLister(this, reference.getTranslations(), false, null));

		/*
		 * ReferenceTypeView contentMaker = new ReferenceTypeView(this); if
		 * (reference.isNoun()) {
		 * content.addView(contentMaker.create(Word.NOUN)); } if
		 * (reference.isVerb()) {
		 * content.addView(contentMaker.create(Word.VERB)); } if
		 * (reference.isAdjective()) {
		 * content.addView(contentMaker.create(Word.ADJECTIVE)); } if
		 * (reference.isAdverb()) {
		 * content.addView(contentMaker.create(Word.ADVERB)); } if
		 * (reference.isPhrasalVerb()) {
		 * content.addView(contentMaker.create(Word.PHRASAL)); } if
		 * (reference.isExpression()) {
		 * content.addView(contentMaker.create(Word.EXPRESSION)); } if
		 * (reference.isOther()) {
		 * content.addView(contentMaker.create(Word.OTHER)); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.palabra, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Words words = reference.getPureOwners();
		switch (item.getItemId()) {
		case R.id.m_edit:
			if (words.size() == 1) {
				editAction(0);
				return true;
			}
			chooseReferenceToEdit(words);
			break;
		case R.id.m_remove:
			if (words.size() == 1) {
				removeAction(0);
				return true;
			}
			chooseReferenceToRemove(words);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void chooseReferenceToEdit(Words words) {
		ListView view = new ListView(this);
		view.setAdapter(new WordLister(this, words));
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				editAction(pos);
			}

		});

		Builder builder = new AlertDialog.Builder(this);
		builder.setView(view).setTitle(getString(R.string.edit));
		builder.create().show();
	}

	private void chooseReferenceToRemove(Words words) {
		ListView view = new ListView(this);
		view.setAdapter(new WordLister(this, words));
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				removeAction(pos);
			}

		});

		Builder builder = new AlertDialog.Builder(this);
		builder.setView(view).setTitle(getString(R.string.remove));
		builder.create().show();
	}

	private void editAction(int pos) {
		Intent intent = new Intent(this, AddWordFromModifyActivity.class);
		intent.putExtra(SEND_WORD, reference.links.get(pos).owner.id);
		startActivityForResult(intent, REQUEST_MODIFIED);
	}

	private void removeAction(final int pos) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.title_removing));
		builder.setMessage(R.string.removewordquestion);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				remove(pos);
			}
		});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	private void remove(int pos) {
		KernelControl.deleteWordTemporarily(reference.links.get(pos).owner);
		setResult(Activity.RESULT_OK, null);
		finish();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		KernelControl.switchDictionary();
		Intent intent = new Intent(this, ReferenceActivity.class);
		intent.putExtra(DREFERENCE, reference.links.get(position).reference.getName());
		startActivity(intent);
	}

	private void addNewTense(int position) {
		Intent intent = new Intent(this, NewTenseActivity.class);
		intent.putExtra(DREFERENCE, reference.getName());
		intent.putExtra(NewTenseActivity.TENSE, position);
		startActivity(intent);
	}

}
