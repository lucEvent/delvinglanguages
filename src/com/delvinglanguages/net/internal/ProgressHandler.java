package com.delvinglanguages.net.internal;

import android.os.Handler;
import android.widget.ProgressBar;

public class ProgressHandler extends Handler implements Runnable {

	private int step;
	private int progress;
	private ProgressBar bar;

	public ProgressHandler(ProgressBar progressBar) {
		this.bar = progressBar;
	}

	public void start(final int max) {
		post(new Runnable() {

			@Override
			public void run() {
				bar.setProgress(0);
				bar.setMax(max);
				bar.setVisibility(ProgressBar.VISIBLE);
			}
		});
		step = (max / 100) + 1;
		progress = step - 1;
	}

	public void stepForward() {
		progress++;
		if (progress % step == 0) {
			post(this);
		}
	}

	public void finish() {
		post(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bar.setVisibility(ProgressBar.INVISIBLE);
			}
		});
	}

	@Override
	public void run() {
		bar.setProgress(progress + 1);
	}

}
