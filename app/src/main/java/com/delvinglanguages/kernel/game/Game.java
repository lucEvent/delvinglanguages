package com.delvinglanguages.kernel.game;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;

import java.util.Random;

public abstract class Game extends Random {

    protected DReferences references;
    private PriorityMap priorityMap;
    private Thread pmCreator;

    public Game(DReferences references)
    {
        this.references = references;
        this.pmCreator = new Thread(priorityMapCreation);
        this.pmCreator.start();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public DReference nextReference()
    {
        while (pmCreator.isAlive()) ;

        Integer priority = priorityMap.getMaxKey();
        DReferences set = priorityMap.get(priority);

        DReference ref = set.remove(nextInt(set.size()));
        if (set.isEmpty()) {
            priorityMap.remove(priority);
            if (priorityMap.size() == 0) {
                pmCreator = new Thread(priorityMapCreation);
                pmCreator.start();
            }
        }
        return ref;
    }

    public char nextLetter(boolean upperCase)
    {
        if (upperCase)
            return (char) (nextInt('Z' - 'A' + 1) + 'A');
        else
            return (char) (nextInt('z' - 'a' + 1) + 'a');
    }

    private Runnable priorityMapCreation = new Runnable() {
        @Override
        public void run()
        {
            priorityMap = new PriorityMap();

            for (int i = 0; i < references.size(); i++) {
                DReference ref = references.get(i);
                DReferences set = priorityMap.get(ref.priority);
                if (set == null) {
                    set = new DReferences();
                    priorityMap.put(ref.priority, set);
                }
                set.add(ref);
            }
        }
    };
}
