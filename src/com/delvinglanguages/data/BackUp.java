package com.delvinglanguages.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.core.Word;

public class BackUp extends IOOperations {

	private static final String DEBUG = "##BackUp##";
	private static final String backupfile = "backup.delv";

	public BackUp() {
	}

	public void createBackUp(Context context) {
		int nlangs = 0;
		int nwords = 0;
		int nstor = 0;
		try {
			File file = getBackUpFile();
			initOutputBuffer(file);

			ArrayList<IDDelved> idiomas = ControlCore.getIdiomas();
			saveInteger(idiomas.size());
			// Por cada idioma
			for (int i = 0; i < idiomas.size(); i++, nlangs++) {
				IDDelved lang = idiomas.get(i);
				saveString(lang.getName());
				Log.d(DEBUG, lang.getName());
				ControlCore.setIdiomaActual(i);
				ArrayList<Word> palabras = ControlCore.getPalabras();
				saveInteger(palabras.size());
				// Por cada palabra del idioma
				for (int j = 0; j < palabras.size(); j++, nwords++) {
					Word p = palabras.get(j);
					Log.d(DEBUG, "---" + p.getName());
					saveString(p.getName());
					saveString(p.getTranslation());
					saveString(p.getPronunciation());
					saveInteger(p.getType());
				}
				ArrayList<Nota> notas = ControlCore.getStore();
				// Por cada entrada del store
				saveInteger(notas.size());
				for (int j = 0; j < notas.size(); j++, nstor++) {
					Nota n = notas.get(j);
					saveString(n.get());
				}
			}
			bufferOut.flush();
			bufferOut.close();
		} catch (IOException e) {
			Log.d(DEBUG, "IOException: " + e.toString());
		}
		String s1 = nlangs + " languages saved";
		Toast.makeText(context, s1, Toast.LENGTH_SHORT).show();
		String s2 = nwords + " words saved";
		Toast.makeText(context, s2, Toast.LENGTH_SHORT).show();
		String s3 = nstor + " stored words saved";
		Toast.makeText(context, s3, Toast.LENGTH_SHORT).show();
	}

	public void recoverBackUp(Context context) {
		try {
			File file = getBackUpFile();
			initInputBuffer(file);

			int nIdiomas = readInteger();
			// Por cada idioma
			for (int i = 0; i < nIdiomas; i++) {
				String idName = readString();
				ControlCore.setIdiomaActual(ControlCore.addIdioma(idName, 0));
				ControlCore.loadLanguage(true);
				ControlCore.getPalabras();
				while(ControlCore.getIdiomaActual(context).isbusy());
				Log.d	(DEBUG, (i + 1) + ". " + idName);
				// Por cada palabra del idioma
				int nPalabras = readInteger();
				for (int j = 0; j < nPalabras; j++) {
					String name = readString();
					String tran = readString();
					String pron = readString();
					int type = readInteger();

					ControlCore.addPalabra(name, tran, pron, type);
	//				Log.d(DEBUG, "(w) " + name);
				}
				// Por cada entrada del store
				int nNotas = readInteger();
				for (int j = 0; j < nNotas; j++) {
					String nota = readString();
					ControlCore.addToStore(nota);
				//	Log.d(DEBUG, "(sw) " + nota);
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

		File folder = new File(externalDir.getAbsolutePath() + File.separator
				+ "Delving");
		folder.mkdirs();
		return new File(folder, backupfile);
	}

}
