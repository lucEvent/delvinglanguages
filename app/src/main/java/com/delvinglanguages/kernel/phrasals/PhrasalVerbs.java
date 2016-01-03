package com.delvinglanguages.kernel.phrasals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeSet;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.net.internal.TaskHandler;
import com.delvinglanguages.settings.Settings;

public class PhrasalVerbs implements Runnable, Serializable {

    private static final long serialVersionUID = -3561298495761959147L;

    private static final String[] englishPreps = {"About", "Across", "After", "Against", "Ahead", "Along", "Apart", "Around", "As", "Aside", "At",
            "Away", "Back", "By", "Down", "For", "Forth", "Forward", "From", "In", "Into", "It", "Of", "Off", "On", "Onto", "Out", "Over", "Round",
            "Through", "To", "Together", "Towards", "Under", "Up", "Upon", "With"};

    private static final String[] swedishPreps = {"I", "På", "Till", "Framför", "Åt", "Vid", "Ur", "Om", "Mot", "Av", "F�r", "F�re", "Genom",
            "Utan", "Efter", "Över", "Bakom", "Under", "Från", "Bredvid", "Mellan", "Igenom", "Med"};

    private DReferences words;
    private final int language;

    private TreeSet<PVLink> verbs;
    private TreeSet<PVLink> prepositions;

    private TaskHandler handler;

    public PhrasalVerbs(int language, DReferences words, TaskHandler handler) {
        this.language = language;
        this.words = words;
        this.handler = handler;

        new Thread(this).start();
    }

    @Override
    public void run() {
        handler.onTaskStart();
        String[] sPrepositions = null;
        switch (language) {
            case Language.UK:
            case Language.US:
                sPrepositions = englishPreps;
                break;
            case Language.SV:
                sPrepositions = swedishPreps;
                break;
            default:
                sPrepositions = new String[]{};
        }
        prepositions = new TreeSet<PVLink>();
        for (String p : sPrepositions) {
            prepositions.add(new PVLink(p));
        }

        verbs = new TreeSet<PVLink>();
        for (DReference ref : words) {
            if (ref.isPhrasalVerb()) {
                addPhrasalVerb(ref.name);
            }
        }
        handler.onTaskDone(-1, TaskHandler.TaskState.TASK_DONE, this);
    }

    public void addPhrasalVerb(String ph) {
        ph = ph.replaceAll("\\(.*?\\) ?", "").replaceAll("\\[.*?\\] ?", "").replaceAll("\\{.*?\\} ?", "");
        while (ph.endsWith(" "))
            ph = ph.substring(0, ph.length() - 1);

        int sep = ph.indexOf(" ");
        String verb = ph.substring(0, sep);
        String prep = Character.toUpperCase(ph.charAt(sep + 1)) + ph.substring(sep + 2, ph.length());

        debug("V:[" + verb + "], P:[" + prep + "]");

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

    public TreeSet<PVLink> getPrepositions() {
        return prepositions;
    }

    public TreeSet<PVLink> getVerbs() {
        return verbs;
    }

    public PVLink getVerb(String verb) {
        return getPVLink(verbs, verb);
    }

    public PVLink getPreposition(String prep) {
        return getPVLink(prepositions, prep);
    }

    private PVLink getPVLink(TreeSet<PVLink> set, String name) {
        PVLink cand = set.ceiling(new PVLink(name));
        if (cand != null && cand.name.equalsIgnoreCase(name)) {
            return cand;
        }
        return null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(verbs);
        out.writeObject(prepositions);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        verbs = (TreeSet<PVLink>) in.readObject();
        prepositions = (TreeSet<PVLink>) in.readObject();
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##PhrasalVerbs##", text);
    }

}
