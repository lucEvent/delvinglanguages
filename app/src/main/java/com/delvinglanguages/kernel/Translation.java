package com.delvinglanguages.kernel;

import com.delvinglanguages.settings.Settings;

import java.util.ArrayList;

public class Translation {

    protected static final String SEP = "%t";

    public String name;
    public int type;

    public Translation(String translation, int type) {
        this.name = translation;
        this.type = type;
    }

    public Translation(String wrapper) {
        String[] elems = wrapper.split(SEP);
        this.name = elems[0];
        this.type = Integer.parseInt(elems[1]);
    }

    public ArrayList<String> getItems() {
        return formatArray(name);
    }

    public static ArrayList<String> formatArray(String string) {
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
        return list;
    }

    public String toSavingString() {
        return name + SEP + type;
    }

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##Translation##", text);
    }
}
