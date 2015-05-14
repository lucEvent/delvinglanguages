package com.delvinglanguages.face.fragment;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.net.internal.BackgroundTaskMessenger;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.settings.Settings;

public class LanguageFragment extends Fragment implements OnClickListener, BackgroundTaskMessenger {

	private static final String DEBUG = "##LanguageFragment##";

	private IDDelved idioma;

	private TextView labels[];
	private Button addword;
	private ImageButton toggle_dic;

	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		handler = new Handler();

		idioma = KernelControl.getCurrentLanguage();

		View view = inflater.inflate(R.layout.a_language, container, false);

		KernelControl.loadLanguage(idioma, new ProgressHandler((ProgressBar) view.findViewById(R.id.progressbar)), this);

		Settings.setBackgroundTo(view);

		labels = new TextView[Settings.NUM_TYPES];
		labels[Word.NOUN] = (TextView) view.findViewById(R.id.noun);
		labels[Word.VERB] = (TextView) view.findViewById(R.id.verb);
		labels[Word.ADJECTIVE] = (TextView) view.findViewById(R.id.adjective);
		labels[Word.ADVERB] = (TextView) view.findViewById(R.id.adverb);
		labels[Word.PHRASAL] = (TextView) view.findViewById(R.id.phrasal);
		labels[Word.EXPRESSION] = (TextView) view.findViewById(R.id.expression);
		labels[Word.OTHER] = (TextView) view.findViewById(R.id.other);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Activity view = getActivity();

		if (idioma.isLoaded()) {
			updateTypeCounters();
		} else {
			for (int i = 0; i < labels.length; ++i) {
				labels[i].setText("0");
			}
		}

		View phtitle = view.findViewById(R.id.phrasal_title);
		if (idioma.getSettings(IDDelved.MASK_PH)) {
			labels[Word.PHRASAL].setVisibility(View.VISIBLE);
			phtitle.setVisibility(View.VISIBLE);
		} else {
			Log.d(DEBUG, "Quitando la visibilidad");
			labels[Word.PHRASAL].setVisibility(View.GONE);
			phtitle.setVisibility(View.GONE);
		}

		addword = (Button) view.findViewById(R.id.newword);
		addword.setOnClickListener(this);
		toggle_dic = (ImageButton) view.findViewById(R.id.toggle_dic);
		toggle_dic.setOnClickListener(this);
		toggle_dic.bringToFront();

		TextView succes1 = (TextView) view.findViewById(R.id.as_ns1);
		TextView succes2 = (TextView) view.findViewById(R.id.as_ns2);
		TextView succes3 = (TextView) view.findViewById(R.id.as_ns3);
		TextView failures = (TextView) view.findViewById(R.id.as_nf);

		TextView psucces1 = (TextView) view.findViewById(R.id.as_ns1p);
		TextView psucces2 = (TextView) view.findViewById(R.id.as_ns2p);
		TextView psucces3 = (TextView) view.findViewById(R.id.as_ns3p);
		TextView pfailures = (TextView) view.findViewById(R.id.as_nfp);

		Estadisticas stats = idioma.getStatistics();
		TextView attempts = (TextView) view.findViewById(R.id.as_na);
		attempts.setText("" + stats.intentos);
		succes1.setText("" + stats.aciertos1);
		succes2.setText("" + stats.aciertos2);
		succes3.setText("" + stats.aciertos3);
		failures.setText("" + stats.fallos);

		DecimalFormat df = new DecimalFormat("0.0 %");

		psucces1.setText(df.format(stats.porcentageAcertadas1()));
		psucces2.setText(df.format(stats.porcentageAcertadas2()));
		psucces3.setText(df.format(stats.porcentageAcertadas3()));
		pfailures.setText(df.format(stats.porcentageFalladas()));
	}

	@Override
	public void onClick(View button) {

		if (button == addword) {
			Intent intent = new Intent(getActivity(), AddWordActivity.class);
			startActivity(intent);
		} else if (button == toggle_dic) {
			KernelControl.switchDictionary();
			idioma = KernelControl.getCurrentLanguage();
			String lang1, lang2;
			if (idioma.isNativeLanguage()) {
				lang1 = Settings.IdiomaNativo;
				lang2 = idioma.getName();
			} else {
				lang1 = idioma.getName();
				lang2 = Settings.IdiomaNativo;
			}
			Toast.makeText(getActivity(), lang1 + " to " + lang2, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onTaskStart() {
	}

	@Override
	public void onTaskDone(int codePetition, int codeResult, Object result) {
		if (codeResult == TASK_DONE) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					do {
						updateTypeCounters();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} while (!idioma.isDictionaryCreated());
					updateTypeCounters();
				}

			});
		}
	}

	private void updateTypeCounters() {
		int values[] = idioma.getTypeCounter();
		for (int i = 0; i < values.length; ++i) {
			labels[i].setText("" + values[i]);
		}
	}

}