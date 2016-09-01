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
        String s = resources.getString(R.string.main_stats_content_description, data.statistics.attempts,
                data.statistics.hits_at_1st, data.statistics.percentageHitsAt1st(),
                data.statistics.hits_at_2nd, data.statistics.percentageHitsAt2nd(),
                data.statistics.hits_at_3rd, data.statistics.percentageHitsAt3rd(),
                data.statistics.misses, data.statistics.percentageMisses());
        holder.content.setText(s);
        holder.resetStatistics.setOnClickListener(data.onResetStatistics);
    }

}
