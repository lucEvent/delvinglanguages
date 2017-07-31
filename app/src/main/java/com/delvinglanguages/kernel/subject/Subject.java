package com.delvinglanguages.kernel.subject;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.kernel.util.SubjectPairs;
import com.delvinglanguages.kernel.util.Wrapper;

public class Subject extends Item implements Wrapper {

    private static final String SEP = "%Th";

    private String name;
    private SubjectPairs pairs;

    public Subject(int id, String name, String pairsWrapper)
    {
        this(id, name, SubjectPairs.fromWrapper(pairsWrapper));
    }

    public Subject(int id, String name, SubjectPairs pairs)
    {
        super(id, Item.SUBJECT);
        this.name = name;
        this.pairs = pairs;
    }

    public static Subject fromWrapper(int id, @NonNull String wrapper)
    {
        int index = 2;
        SubjectPairs pairs;

        String[] parts = wrapper.split(SEP);

        String name = parts[0];
        int size = Integer.parseInt(parts[1]);
        pairs = new SubjectPairs(size);
        for (int i = 0; i < size; i++) {
            String delved = parts[index++];
            String _native = parts[index++];
            pairs.add(new SubjectPair(delved, _native));
        }
        return new Subject(id, name, pairs);
    }

    @Override
    public String wrap()
    {
        StringBuilder res = new StringBuilder();
        res.append(name).append(SEP)
                .append(pairs.size());

        for (SubjectPair thp : pairs)
            res.append(SEP).append(thp.inDelved)
                    .append(SEP).append(thp.inNative);

        return res.toString();
    }

    @Override
    public int wrapType()
    {
        return TYPE_SUBJECT;
    }

    public String getName()
    {
        return name;
    }

    public SubjectPairs getPairs()
    {
        return pairs;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPairs(SubjectPairs pairs)
    {
        this.pairs = pairs;
    }

    public boolean hasContent(CharSequence s)
    {
        if (name.toLowerCase().contains(s)) return true;
        for (SubjectPair pair : pairs)
            if (pair.inDelved.toLowerCase().contains(s) || pair.inNative.toLowerCase().contains(s))
                return true;
        return false;
    }

}
