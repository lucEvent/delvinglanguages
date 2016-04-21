package com.delvinglanguages.view.lister.viewholder;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.util.Statistics;

public class MainStatsViewHolder extends RecyclerView.ViewHolder {

    public static class Data {

        public Statistics statistics;
        public View.OnClickListener onResetStatistics;

    }

    private TextView content;
    private Button resetStatistics;

    public MainStatsViewHolder(View v)
    {
        super(v);
        content = (TextView) v.findViewById(R.id.main_stats_content);
        resetStatistics = (Button) v.findViewById(R.id.button_clear_statistics);
    }

    public static void populateViewHolder(MainStatsViewHolder holder, Data data, Resources resources)
    {
        String s = resources.getString(R.string.main_stats_content_description, data.statistics.intentos,
                data.statistics.aciertos1, data.statistics.porcentageAcertadas1(),
                data.statistics.aciertos2, data.statistics.porcentageAcertadas2(),
                data.statistics.aciertos3, data.statistics.porcentageAcertadas3(),
                data.statistics.fallos, data.statistics.porcentageFalladas());
        holder.content.setText(s);
        holder.resetStatistics.setOnClickListener(data.onResetStatistics);
    }

}
