package com.delvinglanguages.server.util;

import com.delvinglanguages.server.db.Language;
import com.delvinglanguages.server.db.LanguageItem;

import java.util.ArrayList;
import java.util.List;

public class UpdateWrapper {

    public List<Language> languages_to_add, languages_to_update;
    public List<Integer> languages_to_remove;

    public List<LanguageItem> items_to_add, items_to_update, items_to_remove;

    public UpdateWrapper()
    {
        languages_to_add = new ArrayList<>();
        languages_to_update = new ArrayList<>();
        languages_to_remove = new ArrayList<>();
        items_to_add = new ArrayList<>();
        items_to_update = new ArrayList<>();
        items_to_remove = new ArrayList<>();
    }

}
