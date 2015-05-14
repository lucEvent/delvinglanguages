package com.delvinglanguages.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Nota;
import com.delvinglanguages.kernel.Tense;
import com.delvinglanguages.kernel.TenseKernelControl;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Notas;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeKernelControl;
import com.delvinglanguages.kernel.theme.ThemePair;

public class BackUp extends IOOperations {

	private static final String DEBUG = "##BackUp##";
	private static final String backupfile = "backup.delv";

	public BackUp() {
	}

	public void createBackUp(Context context) {
		int nlangs = 0;
		try {
			File file = getBackUpFile();
			initOutputBuffer(file);

			Languages idiomas = KernelControl.getLanguages();
			writeInteger(idiomas.size());
			// Por cada idioma
			for (int i = 0; i < idiomas.size(); i++, nlangs++) {
				IDDelved lang = idiomas.get(i);
				writeString(lang.getName());
				Log.d(DEBUG, lang.getName());
				KernelControl.setCurrentLanguage(i);
				Words palabras = LanguageKernelControl.getWords();
				writeInteger(palabras.size());
				// Por cada palabra del idioma
				for (Word p : palabras) {
					Log.d(DEBUG, "---" + p.getName());
					writeString(p.getName());
					writeString(p.getPronunciation());
					// Por cada traduccion de P
					Translations Ts = p.getTranslations();
					writeInteger(Ts.size());
					for (Translation T : Ts) {
						writeString(T.name);
						writeInteger(T.type);
					}
				}
				// Por cada entrada del store
				Notas notas = LanguageKernelControl.getDrawerWords();
				writeInteger(notas.size());
				for (Nota n : notas) {
					writeString(n.get());
				}
				// Por cada theme
				Themes themes = new ThemeKernelControl(context).getThemes();
				writeInteger(themes.size());
				for (Theme theme : themes) {
					writeString(theme.getName());
					// Por cada themepair del theme
					writeInteger(theme.getPairs().size());
					for (ThemePair pair : theme.getPairs()) {
						writeString(pair.inDelved);
						writeString(pair.inNative);
					}
				}
				// Por cada Tense
				ArrayList<Pair<Integer, Tense>> tenses = new TenseKernelControl(context).getTenses();
				writeInteger(tenses.size());
				for (Pair<Integer, Tense> tense : tenses) {
					writeInteger(tense.first); // verbId
					writeInteger(tense.second.tense);
					writeString(tense.second.getFormsString());
					writeString(tense.second.getPronuntiationsString());
				}
				// Por cada Test
				Tests tests = new TestKernelControl(context).getTests();
				writeInteger(tests.size());
				for (Test test : tests) {
					writeString(test.name);
					writeString(test.encapsulate());
				}
			}
			bufferOut.flush();
			bufferOut.close();
		} catch (IOException e) {
			Log.d(DEBUG, "IOException: " + e.toString());
		}
		Toast.makeText(context, nlangs + " languages saved", Toast.LENGTH_SHORT).show();
	}

	public void recoverBackUp(Context context) {
		try {
			File file = getBackUpFile();
			initInputBuffer(file);

			int nIdiomas = readInteger();
			// Por cada idioma
			for (int i = 0; i < nIdiomas; i++) {
				String idName = readString();
				KernelControl.setCurrentLanguage(KernelControl.addLanguage(idName, 0));
				LanguageKernelControl.getWords();
				while (!LanguageKernelControl.isDictionaryCreated());
				Log.d(DEBUG, (i + 1) + ". " + idName);
				// Por cada palabra del idioma
				int nPalabras = readInteger();
				for (int j = 0; j < nPalabras; j++) {
					String name = readString();
					Log.d(DEBUG, "---" + name);
					String pron = readString();
					int nTrans = readInteger();
					Translations trans = new Translations();
					for (int t = 0; t < nTrans; t++) {
						String ntrans = readString();
						int ntype = readInteger();
						trans.add(new Translation(ntrans, ntype));
					}
					KernelControl.addWord(name, trans, pron);
				}
				// Por cada entrada del store
				int nNotas = readInteger();
				for (int j = 0; j < nNotas; j++) {
					String nota = readString();
					KernelControl.addToStore(nota);
				}
				// Por cada theme
				ThemeKernelControl thKernel = new ThemeKernelControl(context);
				int nThemes = readInteger();
				for (int j = 0; j < nThemes; j++) {
					String thName = readString();
					ThemePairs thPairs = new ThemePairs();
					int nthPairs = readInteger();
					for (int k = 0; k < nthPairs; k++) {
						String thpDelv = readString();
						String thpNatv = readString();
						thPairs.add(new ThemePair(thpDelv, thpNatv));
					}
					thKernel.addTheme(thName, thPairs);
				}
				// Por cada Tense
				TenseKernelControl tenKernel = new TenseKernelControl(context);
				int nTenses = readInteger();
				for (int j = 0; j < nTenses; j++) {
					int vId = readInteger();
					int teId = readInteger();
					String forms = readString();
					String prons = readString();
					tenKernel.addTense(vId, "", teId, forms, prons);
				}
				// Por cada Test
				TestKernelControl tesKernel = new TestKernelControl(context);
				int nTests = readInteger();
				for (int j = 0; j < nTests; j++) {
					String tName = readString();
					String tContent = readString();
					tesKernel.addTest(tName, tContent);
				}
			}
			bufferIn.close();
		} catch (FileNotFoundException e) {
			Log.d(DEBUG, "FileNotFoundException: " + e.toString());
		} catch (IOException e) {
			Log.d(DEBUG, "IOException: " + e.toString());
		}
	}

	private File getBackUpFile() {
		String estado = Environment.getExternalStorageState();
		if (!estado.equals(Environment.MEDIA_MOUNTED)) {
			Log.d(DEBUG, "No nay media montada\n");
			return null;
		}
		File externalDir = Environment.getExternalStorageDirectory();

		File folder = new File(externalDir.getAbsolutePath() + File.separator + "Delving");
		folder.mkdirs();
		return new File(folder, backupfile);
	}

}
