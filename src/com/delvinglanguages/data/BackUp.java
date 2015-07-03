package com.delvinglanguages.data;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.delvinglanguages.kernel.DrawerWord;
import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.svenska.SwedishTranslation;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestKernelControl;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemeKernelControl;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.settings.Settings;

public class BackUp extends IOOperations {

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
				Language lang = idiomas.get(i);
				debug(lang.getName());

				writeInteger(lang.CODE);
				writeString(lang.getName());
				writeInteger(lang.getSettings());

				KernelControl.setCurrentLanguage(i);
				// Statistics
				Estadisticas e = lang.getStatistics();
				writeInteger(e.intentos);
				writeInteger(e.aciertos1);
				writeInteger(e.aciertos2);
				writeInteger(e.aciertos3);
				writeInteger(e.fallos);

				Words palabras = LanguageKernelControl.getWords();
				writeInteger(palabras.size());
				// Por cada palabra del idioma
				if (lang.CODE == Language.SV) {
					for (Word p : palabras) {
						writeString(p.getName());
						writeString(p.getPronunciation());
						writeInteger(p.getPriority());
						// Por cada traduccion de P
						Translations Ts = p.getTranslations();
						writeInteger(Ts.size());
						for (Translation T : Ts) {
							SwedishTranslation SVT = KernelControl.getSwedishForm(T);
							writeString(SVT.name);
							writeInteger(SVT.type);

							for (int j = 0; j < 6; j++) {
								writeString(SVT.forms[j]);
							}
						}
					}
				} else {
					for (Word p : palabras) {
						writeString(p.getName());
						writeString(p.getPronunciation());
						writeInteger(p.getPriority());
						// Por cada traduccion de P
						Translations Ts = p.getTranslations();
						writeInteger(Ts.size());
						for (Translation T : Ts) {
							writeString(T.name);
							writeInteger(T.type);
						}
					}
				}

				// Por cada entrada del store
				DrawerWords notas = LanguageKernelControl.getDrawerWords();
				writeInteger(notas.size());
				for (DrawerWord n : notas) {
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
		} catch (Exception e) {
			debug("Exception: " + e.toString());
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
				int Lcode = readInteger();
				String Lname = readString();
				int Lsettings = readInteger();
				KernelControl.setCurrentLanguage(KernelControl.addLanguage(Lcode, Lname, Lsettings));
				LanguageKernelControl.getWords();
				debug((i + 1) + ". " + Lname);
				while (!LanguageKernelControl.isDictionaryCreated())
					;

				// Statistics
				Estadisticas e = KernelControl.getCurrentLanguage().getStatistics();
				e.intentos = readInteger();
				e.aciertos1 = readInteger();
				e.aciertos2 = readInteger();
				e.aciertos3 = readInteger();
				e.fallos = readInteger();
				KernelControl.saveStatistics();

				// Por cada palabra del idioma
				int nPalabras = readInteger();
				if (Lcode == Language.SV) {
					debug("En sueco!!");
					for (int j = 0; j < nPalabras; j++) {
						String name = readString();
						String pron = readString();
						int priority = readInteger();

						debug(" ·" + name);
						int nTrans = readInteger();
						Translations trans = new Translations();
						for (int t = 0; t < nTrans; t++) {
							String ntrans = readString();
							int ntype = readInteger();

							String[] forms = new String[6];
							for (int k = 0; k < forms.length; k++) {
								forms[k] = readString();
							}
							trans.add(new SwedishTranslation(-1, ntrans, ntype, forms));
						}
						KernelControl.addWord(name, trans, pron, priority);
					}
				} else {
					for (int j = 0; j < nPalabras; j++) {
						String name = readString();
						String pron = readString();
						int priority = readInteger();

						debug(" ·" + name);
						int nTrans = readInteger();
						Translations trans = new Translations();
						for (int t = 0; t < nTrans; t++) {
							String ntrans = readString();
							int ntype = readInteger();
							trans.add(new Translation(-1, ntrans, ntype));
						}
						KernelControl.addWord(name, trans, pron, priority);
					}
				}

				// Por cada entrada del store
				int nDrawerWords = readInteger();
				for (int j = 0; j < nDrawerWords; j++) {
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
		} catch (Exception e) {
			debug("Exception: " + e.toString() + "\n\n");
			e.printStackTrace();
		}
	}

	private File getBackUpFile() {
		String estado = Environment.getExternalStorageState();
		if (!estado.equals(Environment.MEDIA_MOUNTED)) {
			debug("No nay media montada\n");
			return null;
		}
		File externalDir = Environment.getExternalStorageDirectory();

		File folder = new File(externalDir.getAbsolutePath() + File.separator + "Delving");
		folder.mkdirs();
		return new File(folder, backupfile);
	}

	private static void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##BackUp##", text);
	}

}
