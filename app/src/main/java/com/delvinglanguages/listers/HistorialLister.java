package com.delvinglanguages.listers;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.Historial;
import com.delvinglanguages.kernel.HistorialItem;

public class HistorialLister extends ArrayAdapter<HistorialItem> {

    private static final int[] titles = {R.string.langcreated, R.string.langerased, R.string.langintegrated, R.string.wordaddedtostore,
            R.string.wordadded, R.string.wordconsulted, R.string.wordmodified, R.string.wordremoved, R.string.bincleared, R.string.tenseadded,
            R.string.tensemodified, R.string.testcreated, R.string.testdone, R.string.testremoved, R.string.practisedmatch,
            R.string.practisedcomplete, R.string.practisedwrite, R.string.nativenamechanged, R.string.backgroundchanged,
            R.string.changedoubledirection, R.string.langnamechanged, R.string.enablephrasalchanged, R.string.enableadjectivechange,
            R.string.statisticscleared};

    private LayoutInflater inflater;

    public HistorialLister(Context context, ArrayList<HistorialItem> values) {
        super(context, R.layout.i_word, values);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        HistorialItem item = getItem(position);

        String content = "";

        switch (item.type) {
            case Historial.ITEM_LANG_CREATED:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_LANG_ERASED:
                content = item.descriptions[0];
                // Añadir una funcion a posteriori para recuperar un idioma
                // eliminado
                // pero nunca provar con uno que no sea de testing
                // en descriptions[1] guardar el fichero .backup para recuperar
                break;
            case Historial.ITEM_LANG_INTEGRATED:
                content = item.descriptions[0] + " integrated in " + item.descriptions[1];
                break;
            case Historial.ITEM_STORE_ADDED:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_WORD_CREATED:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_WORD_VIEWED:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_WORD_MODIFIED:
                content = item.descriptions[0] + " changed by " + item.descriptions[1];
                break;
            case Historial.ITEM_WORD_REMOVED:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_BIN_CLEARED:
                content = item.descriptions[0] + " words cleared";
                break;
            case Historial.ITEM_TENSE_ADDED:
                content = item.descriptions[0] + " in " + item.descriptions[1];
                break;
            case Historial.ITEM_TENSE_MODIFIED:
                content = item.descriptions[0] + " in " + item.descriptions[1];
                break;
            case Historial.ITEM_TEST_CREATED:
                content = item.descriptions[0] + " in " + item.descriptions[1];
                break;
            case Historial.ITEM_TEST_DONE:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_TEST_REMOVED:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_PRACTISED_MATCH:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_PRACTISED_COMPLETE:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_PRACTISED_WRITE:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_SET_CHANGE_NATIVE_NAME:
                content = item.descriptions[0] + " renamed as " + item.descriptions[1];
                break;
            case Historial.ITEM_CHANGE_LANGNAME:
                content = item.descriptions[0] + " renamed as " + item.descriptions[1];
                break;
            case Historial.ITEM_SET_CHANGE_BACKGROUND:
                content = item.descriptions[0];
                break;
            case Historial.ITEM_SET_CHANGE_DOUBLEDIRECTION:
                content = item.descriptions[0] + " on " + item.descriptions[1];
                break;
            case Historial.ITEM_CHANGE_EN_PRHASAL:
                content = item.descriptions[0] + " on " + item.descriptions[1];
                break;
            case Historial.ITEM_CHANGE_EN_ADJ:
                content = item.descriptions[0] + " on " + item.descriptions[1];
                break;
            case Historial.ITEM_STATISTICS_CLEARED:
                content = item.descriptions[0];
                break;
        }

        if (view == null) {
            view = inflater.inflate(R.layout.i_historial_item, parent, false);
        }
        StringBuilder time = new StringBuilder();

        long t = (System.currentTimeMillis() - item.time) / 1000; // en seg

        long dias = t / 86400;
        if (dias > 0) {
            time.append(dias + " D. ");
        }
        t -= dias;
        long horas = t / 3600;
        if (horas > 0) {
            time.append(horas + " h. ");
        }
        t -= horas;
        long minutos = t / 30;
        if (minutos > 0) {
            time.append(minutos + " m. ");
        }
        long segundos = t - minutos;
        if (segundos > 0) {
            time.append(segundos + " s. ");
        }

        view.findViewById(R.id.type).setBackgroundColor(Color.RED);
        ((TextView) view.findViewById(R.id.title)).setText(titles[item.type]);
        ((TextView) view.findViewById(R.id.content)).setText(content);
        ((TextView) view.findViewById(R.id.date)).setText(time);

        return view;
    }

}
