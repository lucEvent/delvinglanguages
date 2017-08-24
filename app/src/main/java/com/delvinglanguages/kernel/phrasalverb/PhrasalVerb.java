package com.delvinglanguages.kernel.phrasalverb;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;

public class PhrasalVerb implements Comparable<PhrasalVerb> {

    public final int id;

    public final String verb;

    public final DReferences variants;

    public PhrasalVerb(String verb)
    {
        this.id = verb.hashCode();
        this.verb = verb;
        this.variants = new DReferences();
    }

    public void addVariant(DReference reference)
    {
        variants.add(reference);
    }

    public void removeVariant(DReference reference)
    {
        variants.remove(reference);
    }

    @Override
    public int compareTo(@NonNull PhrasalVerb o)
    {
        return this.verb.compareTo(o.verb);
    }

    public boolean hasContent(String query)
    {
        boolean res = verb.contains(query);
        if (!res)
            for (DReference ref : variants)
                if (ref.hasContent(query))
                    return true;
        return res;
    }

}
