package com.delvinglanguages.kernel.phrasals;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.DReferences;

import java.io.Serializable;
import java.util.TreeSet;

public class PhrasalVerbs implements Runnable, Serializable {

    private static final String[] englishPreps = {"About", "Across", "After", "Against", "Ahead", "Along", "Apart", "Around", "As", "Aside", "At",
            "Away", "Back", "By", "Down", "For", "Forth", "Forward", "From", "In", "Into", "It", "Of", "Off", "On", "Onto", "Out", "Over", "Round",
            "Through", "To", "Together", "Towards", "Under", "Up", "Upon", "With"};

    private static final String[] swedishPreps = {"I", "På", "Till", "Framför", "Åt", "Vid", "Ur", "Om", "Mot", "Av", "F�r", "F�re", "Genom",
            "Utan", "Efter", "Över", "Bakom", "Under", "Från", "Bredvid", "Mellan", "Igenom", "Med"};

    private DReferences words;
    private final int language;

    private TreeSet<PVLink> verbs;
    private TreeSet<PVLink> prepositions;


    public PhrasalVerbs(int language, DReferences words)
    {
        this.language = language;
        this.words = words;

        new Thread(this).start();
    }

    @Override
    public void run()
    {
        String[] sPrepositions;
        switch (language) {
            case Language.ENGLISH_UK:
            case Language.ENGLISH_US:
                sPrepositions = englishPreps;
                break;
            case Language.SWEDISH:
                sPrepositions = swedishPreps;
                break;
            default:
                sPrepositions = new String[]{};
        }
        prepositions = new TreeSet<>();
        for (String p : sPrepositions) {
            prepositions.add(new PVLink(p));
        }

        verbs = new TreeSet<>();
        for (DReference ref : words) {
            if (ref.isPhrasalVerb()) {
                addPhrasalVerb(ref.name);
            }
        }
    }

    public void addPhrasalVerb(String ph)
    {
        ph = ph.replaceAll("\\(.*?\\) ?", "").replaceAll("\\[.*?\\] ?", "").replaceAll("\\{.*?\\} ?", "");
        while (ph.endsWith(" "))
            ph = ph.substring(0, ph.length() - 1);

        int sep = ph.indexOf(" ");
        String verb = ph.substring(0, sep);
        String prep = Character.toUpperCase(ph.charAt(sep + 1)) + ph.substring(sep + 2, ph.length());

        PVLink pvlVerb = new PVLink(verb);
        if (!verbs.add(pvlVerb)) {
            pvlVerb = verbs.ceiling(pvlVerb);
        }
        PVLink pvlPrep = new PVLink(prep);
        if (!prepositions.add(pvlPrep)) {
            pvlPrep = prepositions.ceiling(pvlPrep);
        }
        pvlVerb.linkTo(pvlPrep);
        pvlPrep.linkTo(pvlVerb);
    }

    public TreeSet<PVLink> getPrepositions()
    {
        return prepositions;
    }

    public TreeSet<PVLink> getVerbs()
    {
        return verbs;
    }

    public PVLink getVerb(String verb)
    {
        return getPVLink(verbs, verb);
    }

    public PVLink getPreposition(String prep)
    {
        return getPVLink(prepositions, prep);
    }

    private PVLink getPVLink(TreeSet<PVLink> set, String name)
    {
        PVLink cand = set.ceiling(new PVLink(name));
        if (cand != null && cand.name.equalsIgnoreCase(name)) {
            return cand;
        }
        return null;
    }

}
