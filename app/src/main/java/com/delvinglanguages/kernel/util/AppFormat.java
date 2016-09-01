package com.delvinglanguages.kernel.util;

import com.delvinglanguages.AppSettings;

import java.util.ArrayList;

public class AppFormat {

    public static String formatReferenceName(String s)
    { // Funcion a revisar y rehacer de 0
        //// TODO: 06/01/2016  
        StringBuilder res = new StringBuilder(s);
        int index = res.indexOf(",");
        while (index != -1) {
            try {
                if (res.charAt(index + 1) != ' ') {
                    res.insert(index + 1, " ");
                } else {
                    while (res.charAt(index + 2) == ' ') {
                        res.delete(index + 2, index + 3);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                AppSettings.printerror("StringIndexOutOfBoundsException con:" + s, e);
                char car;
                int size = res.length();
                do {
                    res.delete(size, size + 1);
                    res.charAt(index);
                    car = res.charAt(size - 1);
                } while (car == ',' || car == ' ');
            }
            index = res.indexOf(",", index + 1);
        }
        return res.toString();
    }

    public static String[] formatTranslation(String string)
    {
        if (string.isEmpty()) return new String[]{};

        ArrayList<String> list = new ArrayList<>();

        int indi = 0, caps = 0;
        int indf;
        for (indf = 0; indf < string.length(); ++indf) {
            char car = string.charAt(indf);

            switch (car) {
                case ',':
                    if (caps > 0) {
                        continue;
                    }
                    list.add(string.substring(indi, indf));
                    indi = indf + 1;
                    break;
                case '(':
                case '[':
                case '{':
                    caps++;
                    break;
                case ')':
                case ']':
                case '}':
                    caps--;
                    break;
            }
        }
        if (caps == 0) {
            list.add(string.substring(indi, indf));
        }
        String[] res = new String[list.size()];
        for (int i = 0; i < res.length; i++) {
            String temp = list.get(i).trim();
            res[i] = Character.toUpperCase(temp.charAt(0)) + temp.substring(1);
        }
        return res;
    }

    public static String arrayToString(String[] array)
    {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (String s : array) {
            if (!first) {
                res.append(", ").append(s);
            } else {
                res.append(s);
                first = false;
            }
        }
        return res.toString();
    }

}
