package com.delvinglanguages.kernel.util;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.subject.SubjectPair;

import java.util.ArrayList;

public class SubjectPairs extends ArrayList<SubjectPair> implements Wrapper {

    private static final String SEP = "%Th";

    public SubjectPairs()
    {
        super();
    }

    public SubjectPairs(int capacity)
    {
        super(capacity);
    }

    public static SubjectPairs fromWrapper(@NonNull String wrapper)
    {
        String[] parts = wrapper.split(SEP);

        int size = Integer.parseInt(parts[0]);
        int index = 1;
        SubjectPairs pair = new SubjectPairs();
        for (int i = 0; i < size; i++) {
            String delved = parts[index++];
            String _native = parts[index++];
            pair.add(new SubjectPair(delved, _native));
        }
        return pair;
    }

    @Override
    public String wrap()
    {
        StringBuilder res = new StringBuilder();
        res.append(size());
        for (SubjectPair pair : this)
            res.append(SEP).append(pair.inDelved)
                    .append(SEP).append(pair.inNative);

        return res.toString();
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_SUBJECT_PAIRS;
    }

}
