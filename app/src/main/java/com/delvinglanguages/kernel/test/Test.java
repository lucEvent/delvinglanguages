package com.delvinglanguages.kernel.test;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.kernel.util.TestReferenceStates;
import com.delvinglanguages.kernel.util.Wrapper;

public class Test extends Item implements Wrapper {

    private final static String SEP = "%Tt";
    private final static String SEP2 = "%T%";

    public final int subject_id;

    public String name;

    private int runTimes;

    private TestReferenceStates totalStates;
    public TestReferenceStates states;

    public Test(int id, String name, int runTimes, @NonNull String wrappedContent, int subject_id)
    {
        super(id, Item.TEST);
        this.subject_id = subject_id;
        this.name = name;
        this.runTimes = runTimes;
        this.totalStates = unWrapContent(wrappedContent);
    }

    public Test(int id, String name, @NonNull DReferences refs, int subject_id)
    {
        super(id, Item.TEST);
        this.subject_id = subject_id;
        this.name = name;
        this.runTimes = 0;
        this.totalStates = new TestReferenceStates(refs.size());
        for (DReference ref : refs)
            this.totalStates.add(new TestReferenceState(ref));
    }

    public static Test fromWrapper(int id, @NonNull String wrapper)
    {
        String[] parts = wrapper.split(SEP2);
        return new Test(id, parts[0], Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));
    }

    @Override
    public String wrap()
    {
        return name + SEP2 +
                runTimes + SEP2 +
                Test.wrapContent(this) + SEP2 +
                subject_id;
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_TEST;
    }

    public void start()
    {
        states = new TestReferenceStates(totalStates.size());
        for (TestReferenceState state : totalStates)
            states.add(new TestReferenceState(state.reference));
    }

    public void finish()
    {
        runTimes++;
        for (int i = 0; i < totalStates.size(); i++) {
            TestReferenceState total = totalStates.get(i);
            TestReferenceState last = states.get(i);

            total.match.attempts = last.match.attempts;
            total.match.errors = last.match.errors;
            total.complete.attempts = last.complete.attempts;
            total.complete.errors = last.complete.errors;
            total.write.attempts = last.write.attempts;
            total.write.errors = last.write.errors;
            total.listening.attempts = last.listening.attempts;
            total.listening.errors = last.listening.errors;
        }
        states = null;
    }

    private TestReferenceStates unWrapContent(@NonNull String wrappedContent)
    {
        String[] items = wrappedContent.split(SEP);
        int index = 0;

        int nRefs = Integer.parseInt(items[index++]);
        TestReferenceStates res = new TestReferenceStates(nRefs);
        for (int i = 0; i < nRefs; i++) {
            TestReferenceState refState = new TestReferenceState(null);

            refState.reference = DReference.fromWrapper(-1, items[index++]);
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
        res.append(test.totalStates.size());
        for (TestReferenceState refState : test.totalStates) {

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
        for (TestReferenceState refState : totalStates)
            res.add(refState.reference);
        return res;
    }

    public boolean hasRun()
    {
        return runTimes != 0;
    }

    public int getRunTimes()
    {
        return runTimes;
    }

    public int size()
    {
        return totalStates.size();
    }

    public double getAccuracy()
    {
        int attempts = 0;
        int errors = 0;
        for (TestReferenceState refState : totalStates) {
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

        for (TestReferenceState refState : totalStates) {
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

        for (TestReferenceState refState : totalStates) {
            int attempts = refState.match.attempts + refState.complete.attempts + refState.write.attempts + refState.listening.attempts;
            int errors = refState.match.errors + refState.complete.errors + refState.write.errors + refState.listening.errors;
            double accuracy = getAccuracy(attempts, errors);

            if (accuracy < worstAccuracy || ((accuracy == worstAccuracy) && (attempts > worstAttempt))) {
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

}
