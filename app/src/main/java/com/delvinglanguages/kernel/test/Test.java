package com.delvinglanguages.kernel.test;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.LanguageKernelControl;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.TestReferenceStates;

public class Test {

    public final static int PHASE_DELVING = 1;
    public final static int PHASE_MATCH = 2;
    public final static int PHASE_COMPLETE = 3;
    public final static int PHASE_WRITE = 4;
    public final static int PHASE_STATISTICS = 5;

    private final static String p = "-_-";

    public int id;
    public String name;
    public int state;

    public TestReferenceStates references;

    public Test(DReferences references) {
        id = -1;
        this.references = new TestReferenceStates();
        for (DReference ref : references) {
            this.references.add(new TestReferenceState(ref));
        }
    }

    public Test(int id, String name, String encapsulation) {
        this.id = id;
        this.name = name;
        this.references = new TestReferenceStates();

        String[] elems = encapsulation.split(p);
        int index = 0;
        // Estado
        state = Integer.parseInt(elems[index++]);
        // Numero de palabras
        int size = Integer.parseInt(elems[index++]);
        // Por cada palabra (con su estado y passed)
        for (int i = 0; i < size; i++) {
            DReference ref = LanguageKernelControl.getReference(elems[index++]);
            boolean passed = Boolean.parseBoolean(elems[index++]);
            int fallos_match = Integer.parseInt(elems[index++]);
            int fallos_complete = Integer.parseInt(elems[index++]);
            int fallos_write = Integer.parseInt(elems[index++]);

            if (ref != null) {
                TestReferenceState refstate = new TestReferenceState(ref);
                refstate.passed = passed;
                refstate.fallos_match = fallos_match;
                refstate.fallos_complete = fallos_complete;
                refstate.fallos_write = fallos_write;
                references.add(refstate);
            }
        }
    }

    public String encapsulate() {
        StringBuilder res = new StringBuilder();
        // Estado
        res.append(state).append(p);
        // Numero de palabras
        res.append(references.size()).append(p);
        // Por cada palabra (con su estado y passed)
        for (TestReferenceState refstate : references) {
            res.append(refstate.reference.name).append(p);
            res.append(refstate.passed).append(p);
            res.append(refstate.fallos_match).append(p);
            res.append(refstate.fallos_complete).append(p);
            res.append(refstate.fallos_write).append(p);
        }
        return res.toString();
    }

    public void clear() {
        for (TestReferenceState refstate : references) {
            refstate.passed = false;
            refstate.fallos_match = 0;
            refstate.fallos_complete = 0;
            refstate.fallos_write = 0;
        }
    }

    public void nextStat() {
        for (TestReferenceState refstate : references) {
            refstate.passed = false;
        }
    }

    public boolean isSaved() {
        return id != -1;
    }

    public DReferences getReferences() {
        DReferences res = new DReferences();
        for (TestReferenceState refstate : references) {
            res.add(refstate.reference);
        }
        return res;
    }

    public int indexOf(DReference reference) {
        for (int i = 0; i < references.size(); i++) {
            if (reference == references.get(i).reference) {
                return i;
            }
        }
        return -1;
    }

    public void check() {
        for (int i = 0; i < references.size(); i++) {
            String name = references.get(i).reference.name;
            if (LanguageKernelControl.getReference(name) == null) {
                references.remove(i);
                i--;
            }
        }
    }

    public boolean isEmpty() {
        return references.isEmpty();
    }
}
