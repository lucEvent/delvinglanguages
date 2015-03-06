package com.delvinglanguages.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class IOOperations {

	protected BufferedInputStream bufferIn;
	protected BufferedOutputStream bufferOut;

	protected void initInputBuffer(File file) throws FileNotFoundException {
		bufferIn = new BufferedInputStream(new FileInputStream(file));
	}

	protected void initOutputBuffer(File file) throws FileNotFoundException {
		bufferOut = new BufferedOutputStream(new FileOutputStream(file));
	}

	protected int readInteger() throws IOException {
		int result = bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		result = (result << 8) + bufferIn.read();
		return result;

	}

	protected void saveInteger(int i) throws IOException {
		byte[] buff = new byte[4];
		buff[3] = (byte) (i);
		buff[2] = (byte) (i >> 8);
		buff[1] = (byte) (i >> 16);
		buff[0] = (byte) (i >> 24);
		bufferOut.write(buff, 0, 4);
	}

	protected String readString() throws IOException {
		int lon = bufferIn.read();
		if (lon > 0) {
			byte[] b = new byte[lon];
			bufferIn.read(b, 0, lon);
			return new String(b, 0, lon);
		}
		return "";
	}

	protected void saveString(String n) throws IOException {
		byte[] aux = n.getBytes();
		bufferOut.write(aux.length);
		if (aux.length > 0) {
			bufferOut.write(aux);
		}
	}

	protected long readLong() throws IOException {
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

	protected void saveLong(long i) throws IOException {
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

}
