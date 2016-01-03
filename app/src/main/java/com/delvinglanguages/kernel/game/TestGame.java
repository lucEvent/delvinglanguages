package com.delvinglanguages.kernel.game;

import android.util.Log;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.set.DReferences;

public class TestGame extends Game {

    public TestGame(DReferences references) {
        super(references);
    }

    public DReferences getWords(int numero, int types) {
        DReferences res = new DReferences(numero);
        while (running)
            ;
        if (numero > references.size())
            numero = references.size();
        while (numero > 0) {
            Integer priority = priorityMap.getMaxKey();
            DReferences set = priorityMap.get(priority);

            DReference ref = set.remove(nextInt(set.size()));
            Log.v(DEBUG, "Getting ref with prior:" + priority);
            if ((ref.getType() & types) != 0) {
                res.add(ref);
                numero--;
            }

            if (set.isEmpty()) {
                priorityMap.remove(priority);
                if (priorityMap.size() == 0) {
                    break;
                }
            }
        }
        return res;
    }

}
