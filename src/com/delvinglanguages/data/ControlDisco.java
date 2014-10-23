package com.delvinglanguages.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.delvinglanguages.core.HistorialItem;

import android.os.Environment;
import android.util.Log;

public class ControlDisco {

	private static final String DEBUG = "##ControlDisco##";

	private static final String FOLDERNAME = "Delving";
	private static final String STATE = "state.dat";

	private static File folder;
	private static BufferedInputStream bufferIn;
	private static BufferedOutputStream bufferOut;

	public ControlDisco() {
		String estado = Environment.getExternalStorageState();
		if (!estado.equals(Environment.MEDIA_MOUNTED)) {
			Log.d(DEBUG, "No nay media montada\n");
			return;
		}
		File extDir = Environment.getExternalStorageDirectory();

		folder = new File(extDir.getAbsolutePath() + File.separator
				+ FOLDERNAME);
		folder.mkdirs();
	}

	public int getLastLanguage() {
		int result = 0;
		File file = new File(folder, STATE);
		// file.delete();
		try {
			bufferIn = new BufferedInputStream(new FileInputStream(file));

			result = readInteger();

			bufferIn.close();
		} catch (FileNotFoundException e) {
		                  System.out.println(DEBUG+ " FILENOTEXIST: " + e.toString());
		} catch (IOException e) {
			Log.d(DEBUG, "IOEXCEPTION: " + e.toString());
		}
		return result;
	}

	public void saveLastLaguage(int position) {
		try {
			File file = new File(folder, STATE);
			bufferOut = new BufferedOutputStream(new FileOutputStream(file));

			saveInteger(position);

			bufferOut.close();
		} catch (IOException e) {
			Log.d(DEBUG, "IOEXCEPTION->" + e.toString());
		}
	}

	public String[] readParams(String settings, int elems) {
		String[] params = new String[elems];
		File file = new File(folder, settings);
		try {
			bufferIn = new BufferedInputStream(new FileInputStream(file));

			for (int i = 0; i < elems; i++) {
				params[i] = readString();
			}

			bufferIn.close();
		} catch (FileNotFoundException e) {
			Log.d(DEBUG, "FILENOTEXIST: " + e.toString());
			params = null;
		} catch (IOException e) {
			Log.d(DEBUG, "IOEXCEPTION: " + e.toString());
			params = null;
		}
		return params;
	}

	public void saveParams(String settings, String[] params) {
		File file = new File(folder, settings);
		try {
			bufferOut = new BufferedOutputStream(new FileOutputStream(file));

			for (int i = 0; i < params.length; i++) {
				saveString(params[i]);
			}

			bufferOut.close();
		} catch (FileNotFoundException e) {
			Log.d(DEBUG, "FNFEXCEPTION: " + e.toString());
		} catch (IOException e) {
			Log.d(DEBUG, "IOEXCEPTION: " + e.toString());
		}

	}

	public ArrayList<HistorialItem> getHistorial() {
		ArrayList<HistorialItem> res = new ArrayList<HistorialItem>();
		File file = new File(folder, "historial.dat");
		try {
			bufferIn = new BufferedInputStream(new FileInputStream(file));

			int elems = readInteger();
			for (int i = 0; i < elems; i++) {
				int type = readInteger();
				int id = readInteger();
				String descrip = readString();
				long time = readLong();
				res.add(new HistorialItem(type, id, descrip, time));
			}

			bufferIn.close();
		} catch (FileNotFoundException e) {
			Log.d(DEBUG, "FILENOTEXIST: " + e.toString());
		} catch (IOException e) {
			Log.d(DEBUG, "IOEXCEPTION: " + e.toString());
		}
		return res;
	}

	public File getFile(String name) {
		File file = new File(folder, name);
		if (!file.exists()) {
			file = null;
		}
		return file;
	}

	public File saveFile(String name, StringBuilder data) {
		File file = new File(folder, name);
		try {
			bufferOut = new BufferedOutputStream(new FileOutputStream(
					file.getAbsolutePath()));

			byte[] buffer = data.toString().getBytes();
			bufferOut.write(buffer, 0, buffer.length);
			bufferOut.close();
		} catch (FileNotFoundException e) {
			Log.d(DEBUG, "FNF_EXCEPTION: " + e.toString());
		} catch (IOException e) {
			Log.d(DEBUG, "IO_EXCEPTION: " + e.toString());
		}
		return file;
	}

	public void copyFile(File forig, File fcopy) {
		try {
			bufferIn = new BufferedInputStream(new FileInputStream(forig));
			bufferOut = new BufferedOutputStream(new FileOutputStream(
					fcopy.getAbsolutePath()));
			int len;
			byte[] buffer = new byte[1024];
			while ((len = bufferIn.read(buffer, 0, buffer.length)) > 0) {
				bufferOut.write(buffer, 0, len);
			}
			bufferIn.close();
			bufferOut.close();
		} catch (FileNotFoundException e) {
			Log.d(DEBUG, "FNFEXCEPTION: " + e.toString());
		} catch (IOException e) {
			Log.d(DEBUG, "IOEXCEPTION: " + e.toString());
		}
	}

	public File createTempFile(String prefix, String sufix) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(prefix, sufix, folder);
		} catch (IOException e) {
			Log.d(DEBUG, "IOERROR en locationForImage");
		}
		return tempFile;
	}

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

	private static long readLong() throws IOException {
		long result = bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		return result;

	}

	private static void saveLong(long i) throws IOException {
		byte[] buff = new byte[8];
		buff[7] = (byte) (i);
		buff[6] = (byte) (i >> 8);
		buff[5] = (byte) (i >> 16);
		buff[4] = (byte) (i >> 24);
		buff[3] = (byte) (i >> 32);
		buff[2] = (byte) (i >> 40);
		buff[1] = (byte) (i >> 48);
		buff[0] = (byte) (i >> 56);
		bufferOut.write(buff, 0, 8);
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
