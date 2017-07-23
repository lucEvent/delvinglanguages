package com.delvinglanguages.view.utils;

import com.delvinglanguages.kernel.util.Item;

public class MainSearch extends Item {

    public final String term;
    public final boolean withMessage;

    public MainSearch(String term, boolean withMessage)
    {
        super(-524, Item.WEB_SEARCH);

        this.term = term;
        this.withMessage = withMessage;
    }

}
