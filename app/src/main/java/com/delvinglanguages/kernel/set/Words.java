package com.delvinglanguages.kernel.set;

import com.delvinglanguages.kernel.Word;

import java.util.ArrayList;
import java.util.Collection;

public class Words extends ArrayList<Word> {

    public Words() {
        super();
    }

    public Words(int size) {
        super(size);
    }

    public Words(Collection<? extends Word> collection) {
        super(collection);
    }

}