package com.delvinglanguages.kernel.test;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.TestReferenceStates;
import com.delvinglanguages.kernel.util.Wrapper;

public class Test implements Wrapper<Test> {

    private final static String SEP = "%Tt";
    private final static String SEP2 = "%T%";

    public final int id;
    public final int theme_id;

    public String name;

    private int runTimes;

    public TestReferenceStates references;

    public Test(int id, String name, int runTimes, @NonNull String wrappedContent, int theme_id)
    {
        this.id = id;
        this.theme_id = theme_id;
        this.name = name;
        this.runTimes = runTimes;
        this.references = unWrapContent(wrappedContent);
    }

    public Test(int id, String name, @NonNull DReferences refs, int theme_id)
    {
        this.id = id;
        this.theme_id = theme_id;
        this.name = name;
        this.runTimes = 0;
        this.references = new TestReferenceStates(refs.size());
        for (DReference ref : refs)
            this.references.add(new TestReferenceState(ref));
    }

    private TestReferenceStates unWrapContent(@NonNull String wrappedContent)
    {
        String[] items = wrappedContent.split(SEP);
        int index = 0;

        DReference refAux = DReference.createBait("");

        int nRefs = Integer.parseInt(items[index++]);
        TestReferenceStates res = new TestReferenceStates(nRefs);
        for (int i = 0; i < nRefs; i++) {
            TestReferenceState refState = new TestReferenceState(null);

            refState.reference = refAux.unWrap(items[index++]);
            refState.match.attempts = Integer.parseInt(items[index++]);
            refState.match.errors = Integer.parseInt(items[index++]);
            refState.complete.attempts = Integer.parseInt(items[index++]);
            refState.complete.errors = Integer.parseInt(items[index++]);
            refState.write.attempts = Integer.parseInt(items[index++]);
            refState.write.errors = Integer.parseInt(items[index++]);
            refState.listening.attempts = Integer.parseInt(items[index++]);
            refState.listening.errors = Integer.parseInt(items[index++]);

            res.add(refState);
        }
        return res;
    }

    public static String wrapContent(@NonNull Test test)
    {
        StringBuilder res = new StringBuilder();
        res.append(test.references.size());
        for (TestReferenceState refState : test.references) {

            res.append(SEP).append(refState.reference.wrap());
            res.append(SEP).append(refState.match.attempts).append(SEP).append(refState.match.errors);
            res.append(SEP).append(refState.complete.attempts).append(SEP).append(refState.complete.errors);
            res.append(SEP).append(refState.write.attempts).append(SEP).append(refState.write.errors);
            res.append(SEP).append(refState.listening.attempts).append(SEP).append(refState.listening.errors);

        }
        return res.toString();
    }

    public DReferences getReferences()
    {
        DReferences res = new DReferences();
        for (TestReferenceState refState : references)
            res.add(refState.reference);
        return res;
    }

    public boolean hasRun()
    {
        return runTimes != 0;
    }

    public void run_finished()
    {
        runTimes++;
    }

    public int getRunTimes()
    {
        return runTimes;
    }

    public double getAccuracy()
    {
        int attempts = 0;
        int errors = 0;
        for (TestReferenceState refState : references) {
            attempts += refState.match.attempts + refState.complete.attempts + refState.write.attempts + refState.listening.attempts;
            errors += refState.match.errors + refState.complete.errors + refState.write.errors + refState.listening.errors;
        }
        return getAccuracy(attempts, errors);
    }

    private double getAccuracy(int attempts, int errors)
    {
        if (attempts == 0) return 0;
        return ((double) (attempts - errors) / (double) attempts) * 100;
    }

    public String getBestReferenceName()
    {
        int bestAttempt = 0;
        double bestAccuracy = -1.0;
        String bestName = "";

        for (TestReferenceState refState : references) {
            int attempts = refState.match.attempts + refState.complete.attempts + refState.write.attempts + refState.listening.attempts;
            int errors = refState.match.errors + refState.complete.errors + refState.write.errors + refState.listening.errors;
            double accuracy = getAccuracy(attempts, errors);

            if (accuracy > bestAccuracy || ((accuracy == bestAccuracy) && (attempts < bestAttempt))) {
                bestName = refState.reference.name;
                bestAttempt = attempts;
                bestAccuracy = accuracy;
            } else if (accuracy == bestAccuracy && (attempts == bestAttempt)) {
                bestName = bestName + ", " + refState.reference.name;
            }
        }
        return bestName;
    }

    public String getWorstReferenceName()
    {
        int worstAttempt = 0;
        double worstAccuracy = 100000.0;
        String worstName = "";

        for (TestReferenceState refState : references) {
            int attempts = refState.match.attempts + refState.complete.attempts + refState.write.attempts + refState.listening.attempts;
            int errors = refState.match.errors + refState.complete.errors + refState.write.errors + refState.listening.errors;
            double accuracy = getAccuracy(attempts, errors);

            if (accuracy < worstAttempt || ((accuracy == worstAttempt) && (attempts > worstAttempt))) {
                worstName = refState.reference.name;
                worstAttempt = attempts;
                worstAccuracy = accuracy;
            } else if (accuracy == worstAccuracy && (attempts == worstAttempt)) {
                worstName = worstName + ", " + refState.reference.name;
            }
        }
        return worstName;
    }

    public boolean hasContent(CharSequence s)
    {
        return name.toLowerCase().contains(s);
    }

    @Override
    public String wrap()
    {
        return name + SEP2 +
                runTimes + SEP2 +
                Test.wrapContent(this) + SEP2 +
                theme_id;
    }

    @Override
    public Test unWrap(@NonNull String wrapper)
    {
        String[] parts = wrapper.split(SEP2);
        return new Test(-1, parts[0], Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_TEST;
    }

}
