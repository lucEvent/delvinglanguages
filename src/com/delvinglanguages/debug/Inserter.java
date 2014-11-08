package com.delvinglanguages.debug;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;

public class Inserter extends Activity implements Runnable {

	/*
	 * Clase que inserta una tabla de Palabra - Traduccion
	 * 
	 * Normalmente las creadas en drive.excel
	 */
	private String[] values = {};
	
			
	private TextView index, word, trans;

	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_inserter);

		index = (TextView) findViewById(R.id.textup);
		word = (TextView) findViewById(R.id.textword);
		trans = (TextView) findViewById(R.id.texttrans);

		
	}

	@Override
	protected void onResume() {
		super.onResume();

		// new Thread(this).start();

	}

	private Handler handler = new Handler();

	@Override
	public void run() {
		for (int i = 0; i < values.length; i += 2) {
			final String d = ((i >> 1) + 1) + " / " + (values.length >> 1);
			final String t = values[i];
			final String p = values[i + 1];

			handler.post(new Runnable() {
				@Override
				public void run() {
					index.setText(d);
					word.setText(p);
					trans.setText(t);
				}
			});

			ControlCore.addPalabra(p, t, "-", type);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

}
