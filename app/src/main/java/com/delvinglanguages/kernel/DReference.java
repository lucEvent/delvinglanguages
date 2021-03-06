package com.delvinglanguages.kernel;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.kernel.util.Wrapper;

public class DReference extends Item implements Wrapper, Comparable<DReference> {

    private final static String SEP = "%Dr";

    public static final int INITIAL_PRIORITY = 100;

    public static final int NUMBER_OF_TYPES = 9;

    public static final int NOUN = 0x1;
    public static final int VERB = 0x2;
    public static final int ADJECTIVE = 0x4;
    public static final int ADVERB = 0x08;
    public static final int PHRASAL_VERB = 0x10;
    public static final int EXPRESSION = 0x20;
    public static final int PREPOSITION = 0x40;
    public static final int CONJUNCTION = 0x80;
    public static final int OTHER = 0x100;

    /**
     * DReference parameters
     **/
    public String name;
    private Inflexions inflexions;
    public String pronunciation;
    public int priority;
    public int type;

    private String lower_case_name;

    public static DReference createBait(String name)
    {
        return new DReference(-1, name, null, new Inflexions(), -1);
    }

    public DReference(int id, String name, String pronunciation, String inflexionWrapper, int priority)
    {
        this(id, name, pronunciation, Inflexions.fromWrapper(inflexionWrapper), priority);
    }

    public DReference(int id, String name, String pronunciation, Inflexions inflexions, int priority)
    {
        super(id, Item.DREFERENCE);
        this.name = name;
        this.lower_case_name = name.toLowerCase();
        this.inflexions = inflexions;
        this.pronunciation = pronunciation;
        this.priority = priority;

        for (Inflexion i : inflexions)
            this.type = this.type | i.getType();
    }

    public static DReference fromWrapper(int id, @NonNull String wrapper)
    {
        String[] items = wrapper.split(SEP);

        String name = items[0];
        Inflexions inflexions = Inflexions.fromWrapper(items[1]);
        String pronunciation = items[2];
        int priority = Integer.parseInt(items[3]);

        return new DReference(id, name, pronunciation, inflexions, priority);
    }

    @Override
    public String wrap()
    {
        return name +
                SEP + inflexions.wrap() +
                SEP + pronunciation +
                SEP + priority;
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_REFERENCE;
    }

    /**
     * ***************** Getters ******************
     **/
    public String[] getTranslations()
    {
        return inflexions.getTranslations();
    }

    public String getTranslationsAsString()
    {
        return inflexions.getTranslationsAsString();
    }

    public Inflexions getInflexions()
    {
        return inflexions;
    }

    public String getInflexionsAsString()
    {
        return inflexions.getInflexionsAsString();
    }

    /**
     * ***************** Askers ******************
     **/
    public boolean isNoun()
    {
        return (type & NOUN) != 0;
    }

    public boolean isVerb()
    {
        return (type & VERB) != 0;
    }

    public boolean isAdjective()
    {
        return (type & ADJECTIVE) != 0;
    }

    public boolean isAdverb()
    {
        return (type & ADVERB) != 0;
    }

    public boolean isPhrasalVerb()
    {
        return (type & PHRASAL_VERB) != 0;
    }

    public boolean isExpression()
    {
        return (type & EXPRESSION) != 0;
    }

    public boolean isPreposition()
    {
        return (type & PREPOSITION) != 0;
    }

    public boolean isConjunction()
    {
        return (type & CONJUNCTION) != 0;
    }

    public boolean isOther()
    {
        return (type & OTHER) != 0;
    }

    public boolean hasContent(CharSequence s)
    {
        return lower_case_name.contains(s) || inflexions.hasContent(s);
    }

    /**
     * ***************** Modifiers ******************
     **/
    public void addInflexion(Inflexion inflexion)
    {
        this.type = this.type | inflexion.getType();
        this.inflexions.add(inflexion);
    }

    public void removeInflexion(Inflexion inflexion)
    {
        for (Inflexion inf : this.inflexions) {
            if (inf.getInflexions() == inflexion.getInflexions()) {
                this.type = this.type & ~inf.getType();
                this.inflexions.remove(inf);
                break;
            }
        }
    }

    public void update(String name, String pronunciation, Inflexions inflexions)
    {
        this.name = name;
        this.lower_case_name = name.toLowerCase();
        this.inflexions = inflexions;
        this.pronunciation = pronunciation;

        this.type = 0;
        for (Inflexion i : inflexions)
            this.type = this.type | i.getType();
    }

    @Override
    public int compareTo(@NonNull DReference o)
    {
        return this.name.compareTo(o.name);
    }

}
