package com.delvinglanguages.debug;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.core.Palabra;

public class Recover {

	private static String DEBUG = "##Recover##";
	
	private static BufferedInputStream bufferIn;
	private static BufferedOutputStream bufferOut;

	private ArrayList<IDDelved> idiomas;
	
	private String fileName = "backup.delv";
	
	public Recover() {
		idiomas = ControlCore.getIdiomas();
	}
	
	public void makeCopy() {
		try {
			String estado = Environment.getExternalStorageState();
			if (!estado.equals(Environment.MEDIA_MOUNTED)) {
				Log.d("##Configuraciones##", "No nay media montada\n");
				return;
			}
			File externalDir = Environment.getExternalStorageDirectory();
			File folder = new File(externalDir.getAbsolutePath() + File.separator
					+ "Delving");
			folder.mkdirs();
			File file = new File(folder, fileName);
			bufferOut = new BufferedOutputStream(new FileOutputStream(file));

			//Por cada idioma
			saveInteger(idiomas.size());
			for (int i = 0; i < idiomas.size(); i++) {
				IDDelved idm = idiomas.get(i);
				saveString(idm.getName());
				Log.d(DEBUG, idm.getName());
				ControlCore.setIdiomaActual(i);
				ArrayList<Palabra> palabras = ControlCore.getPalabras();
				//Por cada palabra del idioma
				saveInteger(palabras.size());
				for (int j = 0; j < palabras.size(); j++) {
					Palabra p = palabras.get(j);
					Log.d(DEBUG, "_____"+p.getName());
					saveString(p.getName());
					saveString(p.getTranslation());
					saveString(p.getPronunciation());
					saveInteger(p.getType());
				}
				ArrayList<Nota> notas = ControlCore.getStore();
				//Por cada entrada del store
				saveInteger(notas.size());
				for (int j = 0; j < notas.size(); j++) {
					Nota n = notas.get(j);
					saveString(n.get());
				}
			}

			bufferOut.close();
		} catch (IOException e) {
			Log.d("##CONFS##", "IOEXCEPTION->" + e.toString());
		}
	}
	
	public void recoverDatafromCopy() {
		String estado = Environment.getExternalStorageState();
		if (!estado.equals(Environment.MEDIA_MOUNTED)) {
			Log.d("##Configuraciones##", "No nay media montada\n");
			return;
		}
		File externalDir = Environment.getExternalStorageDirectory();

		File folder = new File(externalDir.getAbsolutePath() + File.separator
				+ "Delving");
		folder.mkdirs();
		File file = new File(folder, "backup.delv");
		try {
			bufferIn = new BufferedInputStream(new FileInputStream(file));

			//Por cada idioma
			int nIdiomas = readInteger();
			for (int i = 0; i < nIdiomas; i++) {
				String idName = readString();
				ControlCore.addIdioma(idName);
				ControlCore.setIdiomaActual(i);
				ControlCore.loadLanguage(true);
				ControlCore.getPalabras();
Log.d(DEBUG, (i+1)+". "+idName);
				//Por cada palabra del idioma
				int nPalabras = readInteger();
				for (int j = 0; j < nPalabras; j++) {
					String name = readString();
					String tran = readString();
					String pron = readString();
					int type = readInteger();
					
					ControlCore.addPalabra(name, tran, pron, type);
Log.d(DEBUG,"-->"+name);
//Log.d(ERRORM,"			"+tran);
//Log.d(ERRORM,"			"+pron);
//Log.d(ERRORM," 			"+type);
				}
				//Por cada entrada del store
				int nNotas = readInteger();
				for (int j = 0; j < nNotas; j++) {
					String nota = readString();
					ControlCore.addToStore(nota);
Log.d(DEBUG,"		-"+nota);
				}
			}
			
			bufferIn.close();
		} catch (FileNotFoundException e) {
			Log.d("##CONFS##", "FILENOTEXIST: " + e.toString());
		} catch (IOException e) {
			Log.d("##CONFS##", "IOEXCEPTION: " + e.toString());
		}
	}

	/** ********************* PRIVATES **************************** **/

	private static int readInteger() throws IOException {
		int result = bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		return result;

	}

	private static void saveInteger(int i) throws IOException {
		byte[] buff = new byte[4];
		buff[3] = (byte) (i);
		buff[2] = (byte) (i >> 8);
		buff[1] = (byte) (i >> 16);
		buff[0] = (byte) (i >> 24);
		bufferOut.write(buff, 0, 4);
	}

	private static String readString() throws IOException {
		int lon = bufferIn.read();
		if (lon > 0) {
			byte[] b = new byte[lon];
			bufferIn.read(b, 0, lon);
			return new String(b, 0, lon);
		}
		return "";
	}

	private static void saveString(String n) throws IOException {
		byte[] aux = n.getBytes();
		bufferOut.write(aux.length);
		if (aux.length > 0) {
			bufferOut.write(aux);
		}
	}	

	
}
