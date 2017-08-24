package com.delvinglanguages.kernel.subject;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.kernel.util.Wrapper;

import java.util.Arrays;

public class Subject extends Item implements Wrapper {

    private static final String SEP = "%Th";

    private String name;
    private DReferences references;

    private int[] referencesIds;

    public Subject(int id, String name, String referencesIdsWrapper)
    {
        this(id, name, (DReferences) null);

        referencesIds = unwrapReferencesIds(referencesIdsWrapper);
    }

    public Subject(int id, String name, DReferences references)
    {
        super(id, Item.SUBJECT);
        this.name = name;
        this.references = references;
    }

    public static Subject fromWrapper(int id, @NonNull String wrapper)
    {
        String[] parts = wrapper.split(SEP);
        return new Subject(id, parts[0], parts[1]);
    }

    public String wrapReferencesIds()
    {
        if (referencesIds == null) {
            referencesIds = new int[references.size()];
            for (int i = 0; i < references.size(); i++)
                referencesIds[i] = references.get(i).id;
        }
        return Arrays.toString(referencesIds);
    }

    private int[] unwrapReferencesIds(String wrapper)
    {
        String[] refIds = wrapper.replace("[", "").replace("]", "").split(", ");
        int result[] = new int[refIds.length];
        for (int i = 0; i < result.length; i++)
            result[i] = Integer.parseInt(refIds[i]);

        return result;
    }

    @Override
    public String wrap()
    {
        return name + SEP + wrapReferencesIds();
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

    public DReferences getReferences()
    {
        return references;
    }

    public int[] getReferencesIds()
    {
        return referencesIds;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setReferences(DReferences references)
    {
        this.references = references;
        this.referencesIds = null;
    }

    public boolean hasContent(CharSequence s)
    {
        if (name.toLowerCase().contains(s)) return true;
        for (DReference ref : references)
            if (ref.hasContent(s))
                return true;
        return false;
    }

    public int size()
    {
        return references != null ? references.size() : referencesIds.length;
    }

}
