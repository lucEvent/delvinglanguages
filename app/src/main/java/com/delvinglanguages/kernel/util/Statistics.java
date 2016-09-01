package com.delvinglanguages.kernel.util;

import android.support.annotation.NonNull;

public class Statistics implements Wrapper<Statistics> {     //// TODO: 08/04/2016  More complete statistics :)

    private static final String SEP = "%S";

    public final int id;

    public int attempts;
    public int hits_at_1st;
    public int hits_at_2nd;
    public int hits_at_3rd;
    public int misses;

    public Statistics(int id)
    {
        this(id, 0, 0, 0, 0, 0);
    }

    public Statistics(int id, int attempts, int hits_at_1st, int hits_at_2nd, int hits_at_3rd, int misses)
    {
        this.id = id;
        this.attempts = attempts;
        this.hits_at_1st = hits_at_1st;
        this.hits_at_2nd = hits_at_2nd;
        this.hits_at_3rd = hits_at_3rd;
        this.misses = misses;
    }

    public float percentageHitsAt1st()
    {
        return attempts == 0 ? 0 : (float) (hits_at_1st * 100) / (float) attempts;
    }

    public float percentageHitsAt2nd()
    {
        return attempts == 0 ? 0 : (float) (hits_at_2nd * 100) / (float) attempts;
    }

    public float percentageHitsAt3rd()
    {
        return attempts == 0 ? 0 : (float) (hits_at_3rd * 100) / (float) attempts;
    }

    public float percentageMisses()
    {
        return attempts == 0 ? 0 : (float) (misses * 100) / (float) attempts;
    }

    public void newAttempt(int result)
    {
        attempts++;
        switch (result) {
            case 1:
                hits_at_1st++;
                break;
            case 2:
                hits_at_2nd++;
                break;
            case 3:
                hits_at_3rd++;
                break;
            default:
                misses++;
        }
    }

    public void reset()
    {
        attempts = 0;
        hits_at_1st = 0;
        hits_at_2nd = 0;
        hits_at_3rd = 0;
        misses = 0;
    }

    @Override
    public String wrap()
    {
        return attempts
                + SEP + hits_at_1st
                + SEP + hits_at_2nd
                + SEP + hits_at_3rd
                + SEP + misses;
    }

    @Override
    public Statistics unWrap(@NonNull String wrapper)
    {
        String[] parts = wrapper.split("SEP");
        return new Statistics(-1,
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]));
    }

    @Override
    public int wrapType()
    {
        return TYPE_STATISTICS;
    }

}
