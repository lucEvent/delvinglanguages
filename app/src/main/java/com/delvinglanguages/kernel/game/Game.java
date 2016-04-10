package com.delvinglanguages.kernel.game;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.DReferences;

import java.util.Random;

public abstract class Game extends Random {

    protected DReferences references;
    protected PriorityMap priorityMap;
    protected boolean running;

    public Game(DReferences references) {
        this.references = references;
        createPriorityMap();
    }

    private void createPriorityMap() {
        running = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
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
                running = false;
            }
        }).start();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public DReference nextReference() {
        while (running) ;

        Integer priority = priorityMap.getMaxKey();
        DReferences set = priorityMap.get(priority);

        DReference ref = set.remove(nextInt(set.size()));
        if (set.isEmpty()) {
            priorityMap.remove(priority);
            if (priorityMap.size() == 0)
                createPriorityMap();
        }
        System.out.println("Getting ref with prior:" + priority);
        return ref;
    }

    public char nextLetter(boolean upperCase) {
        if (upperCase) {
            return (char) (nextInt('Z' - 'A' + 1) + 'A');
        } else {
            return (char) (nextInt('z' - 'a' + 1) + 'a');
        }
    }

}
