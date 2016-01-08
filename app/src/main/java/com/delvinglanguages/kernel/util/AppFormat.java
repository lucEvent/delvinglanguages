package com.delvinglanguages.kernel.util;

import com.delvinglanguages.settings.Settings;

import java.util.ArrayList;

public class AppFormat {

    public static String formatWordName(String s) { // Funcion a revisar y rehacer de 0
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
                debug("StringIndexOutOfBoundsException con:" + s);
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

    public static String[] formatArray(String string) {
        // TODO: 06/01/2016 rehacer y optimizar.... con regex???
        ArrayList<String> list = new ArrayList<String>();
        int indi = 0, caps = 0;
        int indf;
        for (indf = 0; indf < string.length(); ++indf) {
            char car = string.charAt(indf);
            if (!Character.isLetter(car)) {
                if (car == ',') {
                    if (caps > 0)
                        continue;
                    while (string.charAt(indi) == ' ') {
                        indi++;
                    }
                    list.add(string.substring(indi, indf));
                    indi = indf + 1;
                } else if (car == '(' || car == '[' || car == '{') {
                    caps++;
                } else if (car == ')' || car == ']' || car == '}') {
                    caps--;
                }
            }
        }
        if (caps == 0) {
            try {
                while (string.charAt(indi) == ' ') {
                    indi++;
                }
            } catch (IndexOutOfBoundsException e) {
                debug("##Error por:" + string + "##");
            }
            list.add(string.substring(indi, indf));
        }
        String[] res = new String[list.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public static String arrayToString(String[] array) {
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

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##AppFormat##", text);
    }

}
