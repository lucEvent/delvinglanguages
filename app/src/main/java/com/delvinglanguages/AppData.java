package com.delvinglanguages;

import android.content.Context;
import android.util.SparseArray;

public class AppData {

    private static SparseArray<String> languagesMap;

    public static void initialize(Context c)
    {
        String[] codes = c.getResources().getStringArray(R.array.language_codes);
        String[] names = c.getResources().getStringArray(R.array.languages);

        languagesMap = new SparseArray<>(names.length);
        for (int i = 0; i < names.length; i++)
            languagesMap.put(Integer.parseInt(codes[i]), names[i]);

    }

    public static String getLanguageName(int lang_code)
    {
        return languagesMap.get(lang_code);
    }

}
