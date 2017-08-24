package com.delvinglanguages.kernel.phrasalverb;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;

import java.util.TreeSet;

public class PhrasalVerbsEngine {

    private static final String[] ENGLISH_PREPS = {"About", "Across", "After", "Against", "Ahead", "Along", "Apart", "Around", "As", "Aside", "At",
            "Away", "Back", "By", "Down", "For", "Forth", "Forward", "From", "In", "Into", "It", "Of", "Off", "On", "Onto", "Out", "Over", "Round",
            "Through", "To", "Together", "Towards", "Under", "Up", "Upon", "With"};

    private static final String[] SWEDISH_PREPS = {"I", "På", "Till", "Framför", "Åt", "Vid", "Ur", "Om", "Mot", "Av", "För", "Före", "Genom",
            "Utan", "Efter", "Över", "Bakom", "Under", "Från", "Bredvid", "Mellan", "Igenom", "Med"};

    private TreeSet<PhrasalVerb> phrasalVerbs;

    public PhrasalVerbsEngine(DReferences references)
    {
        phrasalVerbs = new TreeSet<>();
        for (DReference ref : references) {
            PhrasalVerb newPhV = new PhrasalVerb(ref.name.split(" ")[0]);

            if (!phrasalVerbs.add(newPhV)) {
                phrasalVerbs.ceiling(newPhV).addVariant(ref);
            } else
                newPhV.addVariant(ref);
        }
    }

    public TreeSet<PhrasalVerb> getPhrasalVerbs()
    {
        return phrasalVerbs;
    }

    public PhrasalVerb getPhrasalVerb(int id)
    {
        for (PhrasalVerb phv : phrasalVerbs) {
            if (phv.id == id)
                return phv;
        }
        return null;
    }

}
