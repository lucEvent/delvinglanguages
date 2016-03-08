package com.delvinglanguages.face.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.net.internal.TaskHandler;
import com.delvinglanguages.settings.Settings;

import java.text.DecimalFormat;

public class LanguageFragment extends Fragment implements OnClickListener, TaskHandler {

    private Language idioma;

    private TextView labels[];
    private Button addword;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler();

        idioma = KernelControl.getCurrentLanguage();

        View view = inflater.inflate(R.layout.a_language, container, false);

        KernelControl.loadLanguage(idioma, new ProgressHandler((ProgressBar) view.findViewById(R.id.progressbar)), this);

        labels = new TextView[7];
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
            /*for (TextView label : labels) {
                label.setText("0");
            }*/
        }

        if (idioma.getSettings(Language.MASK_PH)) {
            labels[Word.PHRASAL].setVisibility(View.VISIBLE);
        } else {
            labels[Word.PHRASAL].setVisibility(View.GONE);
        }

        addword = (Button) view.findViewById(R.id.newword);
        addword.setOnClickListener(this);

        TextView succes1 = (TextView) view.findViewById(R.id.as_ns1);
        TextView succes2 = (TextView) view.findViewById(R.id.as_ns2);
        TextView succes3 = (TextView) view.findViewById(R.id.as_ns3);
        TextView failures = (TextView) view.findViewById(R.id.as_nf);

        TextView psucces1 = (TextView) view.findViewById(R.id.as_ns1p);
        TextView psucces2 = (TextView) view.findViewById(R.id.as_ns2p);
        TextView psucces3 = (TextView) view.findViewById(R.id.as_ns3p);
        TextView pfailures = (TextView) view.findViewById(R.id.as_nfp);

        Estadisticas stats = idioma.statistics;
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

        view.setTitle(idioma.language_delved_name);
    }

    @Override
    public void onClick(View button) {
        if (button == addword) {
            Intent intent = new Intent(getActivity(), AddWordActivity.class);
            startActivity(intent);
        }
    }

    private void updateTypeCounters() {
        int values[] = idioma.getTypeCounter();

        labels[Word.NOUN].setText("NN\n" + values[Word.NOUN]);
        labels[Word.VERB].setText("VB\n" + values[Word.VERB]);
        labels[Word.ADJECTIVE].setText("ADJ\n" + values[Word.ADJECTIVE]);
        labels[Word.ADVERB].setText("ADV\n" + values[Word.ADVERB]);
        labels[Word.PHRASAL].setText("PHV\n" + values[Word.PHRASAL]);
        labels[Word.EXPRESSION].setText("EXP\n" + values[Word.EXPRESSION]);
        labels[Word.OTHER].setText("OTH\n" + values[Word.OTHER]);

    }

    @Override
    public void onTaskStart() {
    }

    @Override
    public void onTaskMessage(int message_id, Object message) {
    }

    @Override
    public void onTaskDone(int codePetition, TaskState state, Object result) {
        if (state == TaskState.TASK_DONE) {
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

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##LanguageFragment##", text);
    }

}