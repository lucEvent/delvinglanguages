package com.delvinglanguages.face.fragment;

import java.text.DecimalFormat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.core.Estadisticas;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.face.activity.add.AddWordActivity;
import com.delvinglanguages.settings.Configuraciones;

public class LanguageFragment extends Fragment implements OnClickListener {

	private static final String DEBUG = "##LanguageFragment##";

	private IDDelved idioma;

	private TextView labels[];

	private Button addword;
	private ImageButton toggle_dic;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		idioma = ControlCore.getIdiomaActual(getActivity());

		View view = inflater.inflate(R.layout.a_language, container, false);
		int type_bg = Configuraciones.backgroundType();
		if (type_bg == Configuraciones.BG_IMAGE_ON) {
			view.setBackgroundDrawable(Configuraciones.getBackgroundImage());
		} else if (type_bg == Configuraciones.BG_COLOR_ON) {
			view.setBackgroundColor(Configuraciones.getBackgroundColor());
		}

		int values[] = idioma.getNumTypes();
		labels = new TextView[Configuraciones.NUM_TYPES];
		labels[0] = (TextView) view.findViewById(R.id.ai_noun);
		labels[1] = (TextView) view.findViewById(R.id.ai_verb);
		labels[2] = (TextView) view.findViewById(R.id.ai_adj);
		labels[3] = (TextView) view.findViewById(R.id.ai_adv);
		labels[4] = (TextView) view.findViewById(R.id.ai_phrasal);
		labels[5] = (TextView) view.findViewById(R.id.ai_expression);
		labels[6] = (TextView) view.findViewById(R.id.ai_other);

		for (int i = 0; i < Configuraciones.NUM_TYPES; ++i) {
			labels[i].setText("" + values[i]);
		}

		addword = (Button) view.findViewById(R.id.newword);
		addword.setOnClickListener(this);
		toggle_dic = (ImageButton) view.findViewById(R.id.toggle_dic);
		toggle_dic.setOnClickListener(this);

		TextView succes1 = (TextView) view.findViewById(R.id.as_ns1);
		TextView succes2 = (TextView) view.findViewById(R.id.as_ns2);
		TextView succes3 = (TextView) view.findViewById(R.id.as_ns3);
		TextView failures = (TextView) view.findViewById(R.id.as_nf);

		TextView psucces1 = (TextView) view.findViewById(R.id.as_ns1p);
		TextView psucces2 = (TextView) view.findViewById(R.id.as_ns2p);
		TextView psucces3 = (TextView) view.findViewById(R.id.as_ns3p);
		TextView pfailures = (TextView) view.findViewById(R.id.as_nfp);

		Estadisticas stats = idioma.getEstadisticas();
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

		return view;
	}

	@Override
	public void onClick(View button) {
		if (button == addword) {
			Intent intent = new Intent(getActivity(), AddWordActivity.class);
			startActivity(intent);
		} else if (button == toggle_dic) {
			Log.d(DEBUG, "Cambiando el diccionario");
			ControlCore.switchDictionary();
			idioma = ControlCore.getIdiomaActual(getActivity());
		}
	}

}