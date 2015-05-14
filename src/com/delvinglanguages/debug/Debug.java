package com.delvinglanguages.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.data.IOOperations;
import com.delvinglanguages.kernel.Datos;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Nota;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Notas;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class Debug extends ListActivity {

	private static final String DEBUG = "##DEBUG##";

	private Disc disc;
	private Words english;

	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.a_debug, null);
		Settings.setBackgroundTo(view);
		setContentView(view);

		LanguageKernelControl.deleteAllRemovedWords();
		disc = new Disc();
		english = disc.readData().get(0).getWords();

		index = 0;

		displayWord();
	}

	private void displayWord() {
		Word cWord = english.get(index);

		((TextView) findViewById(R.id.word)).setText(cWord.getName());
		((TextView) findViewById(R.id.pron)).setText(cWord.getPronunciation() + " [" + (index + 1) + "/" + english.size() + "]");

		Translations T = new Translations();
		for (Translation trans : cWord.getTranslations()) {
			for (String S : trans.getItems()) {
				T.add(new Translation(S, trans.type));
			}
		}
		cWord.setTranslations(T);

		{// debug start
			debug("####### - " + cWord.getName() + " - #######");
			debug("Mostrando:");
			for (Translation tt : T) {
				debug(" -> " + tt.name + " [" + Integer.toBinaryString(tt.type) + "]");
			}
			debug("");
		}// debug end

		setListAdapter(new Lister(cWord.getTranslations()));

	}

	public void next(View v) {
		if (index + 1 < english.size()) {
			index++;
			displayWord();
		}
	}

	public void previous(View v) {
		if (index - 1 >= 0) {
			index--;
			displayWord();
		}
	}

	public void addandnext(View v) {
		Word W = english.get(index);
		Translations T = W.getTranslations();

		Translations[] R = new Translations[Settings.NUM_TYPES];
		for (Translation tr : T) {
			for (int i = 0; i < Settings.NUM_TYPES; ++i) {
				int type = (1 << i);
				if ((tr.type & type) != 0) {
					if (R[i] == null) {
						R[i] = new Translations();
					}
					R[i].add(new Translation(tr.name, type));
				}
			}
		}
		Translations result = new Translations();
		for (int i = 0; i < R.length; i++) {
			if (R[i] != null) {
				Translation trtmp = R[i].get(0);
				for (int j = 1; j < R[i].size(); j++) {
					trtmp.name += ", " + R[i].get(j).name;
				}
				result.add(trtmp);
			}
		}
		W.setTranslations(result);

		{// debug start
			debug("Iniciales:");
			for (Translation tt : T) {
				debug(" -> " + tt.name + " [" + Integer.toBinaryString(tt.type) + "]");
			}
			debug("Finales:");
			for (Translation tt : result) {
				debug(" -> " + tt.name + " [" + Integer.toBinaryString(tt.type) + "]");
			}
			debug("");
		}// debug end

		KernelControl.addWord(W.getName(), result, W.getPronunciation());

		next(null);
	}

	private void debug(String text) {
		Log.d(DEBUG, text);

	}

	/** ************************************************** **/
	class Lister extends ArrayAdapter<Translation> implements OnClickListener {

		class Join {

			int type;
			int position;

			Join(int t, int p) {
				type = t;
				position = p;
			}
		}

		private Translations values;
		private LayoutInflater inflater;

		public Lister(Translations values) {
			super(Debug.this, R.layout.i_word, values);
			this.values = values;
			this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = inflater.inflate(R.layout.i_debug, parent, false);
			}

			Translation translation = values.get(position);

			((TextView) view.findViewById(R.id.translation)).setText(translation.name);

			int[] btypes = new int[] { R.id.noun, R.id.verb, R.id.adj, R.id.adverb, R.id.phrasal, R.id.expression, R.id.other };

			for (int i = 0; i < Settings.NUM_TYPES; ++i) {
				Button bt = (Button) view.findViewById(btypes[i]);
				int type = (1 << i);
				if ((translation.type & type) == 0) {
					bt.setEnabled(false);
				}
				bt.setTag(new Join(type, position));
				bt.setOnClickListener(this);
			}

			translation.type = 0;
			return view;
		}

		@Override
		public void onClick(View v) {
			v.setSelected(!v.isSelected());
			Join j = (Join) v.getTag();
			if (v.isSelected()) {
				values.get(j.position).type |= j.type;

			} else {
				values.get(j.position).type &= (~j.type);
			}
		}

	}

	class Disc extends IOOperations {

		private String path;
		private String filename = "english";

		public Disc() {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Delving";
		}

		public Languages readData() {
			Languages data = new Languages();
			try {
				initInputBuffer(new File(path, filename + ".delv"));

				int nIdiomas = readInteger();
				// Por cada idioma
				for (int i = 0; i < nIdiomas; i++) {
					String idName = readString();
					IDDelved lang = new IDDelved(new Datos(-1, idName, "Español", "0"));
					Log.d(DEBUG, "Leyendo: " + idName);
					Words words = new Words();
					// Por cada palabra del idioma
					int nPalabras = readInteger();
					for (int j = 0; j < nPalabras; j++) {
						String name = readString();
						String pron = readString();
						int nTrans = readInteger();
						Translations trans = new Translations();
						for (int t = 0; t < nTrans; ++t) {
							String ntrans = readString();
							int ntype = readInteger();
							trans.add(new Translation(ntrans, ntype));
						}
						words.add(new Word(-1, name, trans, pron, 100));
					}
					lang.setWords(words);
					// Por cada entrada del store
					Notas store = new Notas();
					int nNotas = readInteger();
					for (int j = 0; j < nNotas; j++) {
						String nota = readString();
						store.add(new Nota(-1, nota, 0));
					}
					lang.setDrawerWords(store);
					// Por cada theme

					data.add(lang);
				}
				bufferIn.close();
			} catch (FileNotFoundException e) {
				Log.d(DEBUG, "FileNotFoundException: " + e.toString());
			} catch (IOException e) {
				Log.d(DEBUG, "IOException: " + e.toString());
			}
			return data;
		}
	}

}
