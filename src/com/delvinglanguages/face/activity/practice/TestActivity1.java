package com.delvinglanguages.face.activity.practice;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.DReference;
import com.delvinglanguages.core.game.TestGame;
import com.delvinglanguages.listers.StringLister;
import com.delvinglanguages.settings.Configuraciones;

public class TestActivity1 extends ListActivity implements Runnable {

	private TestGame gamecontroller;
	private ArrayList<DReference> references;

	private ProgressBar progreso;

	private String searchString;
	private String[] values, dots = { "", " .", " . .", " . . .", " . . . ." };

	private Handler mHandler = new Handler();
	private StringLister adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_test_preview);

		searchString = getString(R.string.title_searching);

		View background = findViewById(R.id.tp_bg);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			background.setBackgroundDrawable(Configuraciones
					.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			background.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		int numero = getIntent().getExtras().getInt("number");
		int types = getIntent().getExtras().getInt("types");
		gamecontroller = new TestGame(ControlCore.getReferences());
		references = gamecontroller.getWords(numero, types);

		if (references.isEmpty()) {
			Toast.makeText(this, R.string.nowordmatched, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		progreso = (ProgressBar) findViewById(R.id.tp_progress);
		progreso.getProgressDrawable().setColorFilter(0xFF33CC00,
				PorterDuff.Mode.MULTIPLY);

		values = new String[references.size()];
		String com = getString(R.string.searching);
		for (int i = 0; i < values.length; i++) {
			values[i] = (i + 1) + com;
		}
		adapter = new StringLister(this, values);
		setListAdapter(adapter);
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(200);
			final int speed = 5;
			progreso.setMax(references.size() * speed);
			progreso.setProgress(0);
			for (int i = 1; i <= references.size() * speed; i++) {
				Thread.sleep(speed * 20);
				final int progressi = i;
				final int index = i / speed;
				if (i % speed == 0) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							progreso.setProgress(progressi);
							setTitle(searchString
									+ dots[progressi % dots.length]);
							values[index - 1] = index + ". "
									+ references.get(index - 1).name;
							adapter.notifyDataSetChanged();
						}
					});

				} else {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							progreso.setProgress(progressi);
							setTitle(searchString
									+ dots[progressi % dots.length]);
							values[index] = (index + 1)
									+ getString(R.string.searching)
									+ dots[progressi % dots.length];
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ControlCore.addTest(references);
		startActivity(new Intent(this, TestActivityLearn.class));
		finish();
	}
}
