package com.delvinglanguages.debug;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class Communicator implements Runnable {

	public interface Channel {

		public static final int SEND = -1;
		public static final int INFORMATION = -5;
		public static final int RECEIVED = -2;
		public static final int ERROR = -4;

		public void notificate(int code, String message);
	}

	public static final int APP = 23;
	public static final int PC = 25;
	private static final int PORT = 3333;
	protected static final String DEBUG = "##Communicator##";

	private Thread escucha;
	private ServerSocket server;
	private Socket canal;

	private DataOutputStream salida;
	private BufferedReader entrada;

	private Channel channel;

	private int counter;

	public Communicator(int who, Channel channel) {
		counter = 1;
		this.channel = channel;
		createSocket(who);
	}

	private void createSocket(int who) {
		switch (who) {
		case APP:
			channel.notificate(Channel.SEND, "Connecting");
			ConnectionProcess cp = new ConnectionProcess();
			cp.execute();
			break;
		case PC:
			connect = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						server = new ServerSocket(PORT);
						canal = server.accept();
						channel.notificate(Channel.RECEIVED,
								"Conexion aceptada");
						createConnexion();
						escucha = new Thread(this);
						escucha.start();
					} catch (IOException ex) {
						channel.notificate(
								Channel.ERROR,
								"IOException en createSocket(PC):"
										+ ex.toString());
					}
				}
			});
			connect.start();
		}
	}

	private Thread connect;

	private void createConnexion() {
		try {
			// debug
			if (canal == null) {
				channel.notificate(Channel.ERROR, "Error canal es null");
				return;
			}
			// enddebug
			entrada = new BufferedReader(new InputStreamReader(
					canal.getInputStream()));
			salida = new DataOutputStream(canal.getOutputStream());
		} catch (IOException ex) {
			channel.notificate(Channel.ERROR,
					"IOException en createConnexion(1):" + ex.toString());
		}
	}

	public int send(String text) {
		try {
			byte[] encod = (text + '\n').getBytes();
			if (salida == null) {
				channel.notificate(Channel.ERROR, "Salida es null");
			}
			salida.write(encod);
		} catch (NullPointerException ex) {
			channel.notificate(Channel.ERROR, "NullPointerException en send:"
					+ ex.toString());
		} catch (IOException ex) {
			channel.notificate(Channel.ERROR,
					"IOException en send:" + ex.toString());
		}
		return counter - 1;
	}

	public void desconnect() {
		try {
			server.close();
			if (connect != null) {
				if (connect.isAlive())
					connect.interrupt();
			} else
				canal.close();
		} catch (IOException ex) {
			channel.notificate(Channel.ERROR,
					"IOException en desconnect:" + ex.toString());
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				long t1 = System.nanoTime();
				String message = entrada.readLine();
				System.out.println("t:" + (System.currentTimeMillis() - t1));
				if (!message.isEmpty()) {
					channel.notificate(Channel.RECEIVED, message);
				}
			}
		} catch (IOException ex) {
			channel.notificate(Channel.ERROR,
					"IOException en run:" + ex.toString());
		}
	}

	private class ConnectionProcess extends AsyncTask<String, Void, String> {

		private Socket canal_i;
		
		@Override
		protected String doInBackground(String... params) {
			try {
				String ipa = "85.58.190.51";
				Log.d(DEBUG, "Step 1");
//				canal_i = new Socket("localhost", PORT);
				canal_i = new Socket(ipa, PORT);
				
			} catch (UnknownHostException ex) {
				Log.d(DEBUG, "UnknownHostException en createSocket(APP):"
								+ ex.toString());
			} catch (IOException ex) {
				Log.d(DEBUG, "IOException en createSocket(APP):" + ex.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d(DEBUG, "Step 1.2");
			canal = canal_i;
			Log.d(DEBUG, "Step 2");
			createConnexion();
			Log.d(DEBUG, "Step 3");
			channel.notificate(Channel.SEND, "Connected");
			Log.d(DEBUG, "Step 4");
			escucha = new Thread(Communicator.this);
			Log.d(DEBUG, "Step 5");
			escucha.start();
			Log.d(DEBUG, "Step last");			
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
