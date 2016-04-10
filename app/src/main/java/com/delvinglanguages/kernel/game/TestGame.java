package com.delvinglanguages.kernel.game;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.test.TestReferenceState;
import com.delvinglanguages.kernel.util.DReferences;

public class TestGame extends Game {

    public TestGame(DReferences references) {
        super(references);
    }

    public DReferences getRandomReferences(int number, int type) {
        DReferences res = new DReferences(number);
        if (number > references.size())
            number = references.size();

        int checked = 0;
        while (number > 0 && checked < references.size()) {
            checked++;
            DReference candidate = nextReference();

            if ((candidate.type & type) != 0) {
                res.add(candidate);
                number--;
            }

        }
        return res;
    }

    public TestReferenceState nextTestReference(Test test) {
        while (true) {
            int position = nextInt(test.references.size());

            TestReferenceState refState = test.references.get(position);
            if (refState.stage != TestReferenceState.TestStage.END)
                return refState;
        }
    }

}
