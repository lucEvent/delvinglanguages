package com.delvinglanguages.net.external;

import com.delvinglanguages.kernel.DReference;

import java.util.TreeSet;

public class Search {

    private static int idCounter = 0;

    public final int code;

    public final String searchTerm;

    public TreeSet<String>[] translations;

    public Search(String searchTerm)
    {
        code = idCounter++;
        this.searchTerm = searchTerm;

        translations = new TreeSet[DReference.NUMBER_OF_TYPES];
        for (int i = 0; i < translations.length; i++) {
            translations[i] = new TreeSet<>();
        }
    }

}
