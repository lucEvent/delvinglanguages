package com.delvinglanguages.face.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.face.activity.DictionaryListActivity;
import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.settings.Settings;

public class DictionaryFragment extends Fragment implements OnClickListener {

    private final int NUM_LETRAS = 31;

    private Language idioma;
    private Button[] letras;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        idioma = KernelControl.getCurrentLanguage();

        View view;
        switch (idioma.CODE) {
            case Language.SV:
                view = inflater.inflate(R.layout.a_dictionary_sv, container, false);
                break;
            case Language.ES:
                view = inflater.inflate(R.layout.a_dictionary_es, container, false);
                break;
            default:
                view = inflater.inflate(R.layout.a_dictionary, container, false);
        }

        letras = new Button[NUM_LETRAS];
        letras[0] = (Button) view.findViewById(R.id.dic_a);
        letras[1] = (Button) view.findViewById(R.id.dic_b);
        letras[2] = (Button) view.findViewById(R.id.dic_c);
        letras[3] = (Button) view.findViewById(R.id.dic_d);
        letras[4] = (Button) view.findViewById(R.id.dic_e);
        letras[5] = (Button) view.findViewById(R.id.dic_f);
        letras[6] = (Button) view.findViewById(R.id.dic_g);
        letras[7] = (Button) view.findViewById(R.id.dic_h);
        letras[8] = (Button) view.findViewById(R.id.dic_i);
        letras[9] = (Button) view.findViewById(R.id.dic_j);
        letras[10] = (Button) view.findViewById(R.id.dic_k);
        letras[11] = (Button) view.findViewById(R.id.dic_l);
        letras[12] = (Button) view.findViewById(R.id.dic_m);
        letras[13] = (Button) view.findViewById(R.id.dic_n);
        letras[14] = (Button) view.findViewById(R.id.dic_o);
        letras[15] = (Button) view.findViewById(R.id.dic_p);
        letras[16] = (Button) view.findViewById(R.id.dic_q);
        letras[17] = (Button) view.findViewById(R.id.dic_r);
        letras[18] = (Button) view.findViewById(R.id.dic_s);
        letras[19] = (Button) view.findViewById(R.id.dic_t);
        letras[20] = (Button) view.findViewById(R.id.dic_u);
        letras[21] = (Button) view.findViewById(R.id.dic_v);
        letras[22] = (Button) view.findViewById(R.id.dic_w);
        letras[23] = (Button) view.findViewById(R.id.dic_x);
        letras[24] = (Button) view.findViewById(R.id.dic_y);
        letras[25] = (Button) view.findViewById(R.id.dic_z);
        switch (idioma.CODE) {
            case Language.SV:
                letras[26] = (Button) view.findViewById(R.id.dic_sv_ao);
                letras[27] = (Button) view.findViewById(R.id.dic_sv_ae);
                letras[28] = (Button) view.findViewById(R.id.dic_sv_oe);
                break;
            case Language.ES:
                letras[29] = (Button) view.findViewById(R.id.dic_es_n);
                break;
        }
        letras[30] = (Button) view.findViewById(R.id.dic_others);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        for (Button letra : letras) {
            try {
                letra.setEnabled(false);
            } catch (NullPointerException e) {
            }
        }

        Character[] keys = idioma.getDictionaryIndexes();
        for (Character cap : keys) {
            int index = cap - 'A';
            if (index > 26 || index < 0) {
                switch (cap) {
                    case 'Å':
                        index = 26;
                        break;
                    case 'Ä':
                        index = 27;
                        break;
                    case 'Ö':
                        index = 28;
                        break;
                    case 'Ñ':
                        index = 29;
                        break;
                    default:
                        index = NUM_LETRAS - 1;
                }
            }
            try {
                if (!idioma.getDiccionaryAt(cap).isEmpty()) {
                    letras[index].setEnabled(true);
                    letras[index].setOnClickListener(this);
                    letras[index].setTag(cap);
                }
            } catch (NullPointerException e) {
                debug("ERROR index->" + index + ", cap->" + cap);
            }
        }
    }

    @Override
    public void onClick(View button) {
        button.setOnClickListener(null);
        Intent intent = new Intent(getActivity(), DictionaryListActivity.class);
        intent.putExtra(AppCode.CHARACTER, (Character) button.getTag());
        startActivity(intent);
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##DictionaryFragment##", text);
    }

}