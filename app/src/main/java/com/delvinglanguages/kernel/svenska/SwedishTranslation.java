package com.delvinglanguages.kernel.svenska;

import com.delvinglanguages.kernel.Translation;

public class SwedishTranslation extends Translation {

    public String[] forms;

    public SwedishTranslation(String translation, int type, String... forms) {
        super(translation, type);
        this.forms = forms;
    }

    public SwedishTranslation(Translation T, String... forms) {
        super(T.name, T.type);
        this.forms = forms;
    }

    public SwedishTranslation(String wrapper) {
        super("", -1);
        String[] elems = wrapper.split(SEP);
        this.name = elems[0];
        this.type = Integer.parseInt(elems[1]);
        this.forms = new String[elems.length - 2];
        for (int i = 0; i < forms.length; ++i) {
            forms[i] = elems[i + 2];
        }
    }

    public boolean contains(String string) {
        for (String form : forms) {
            if (form.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(" [ ");
        for (String form : forms) {
            res.append(form).append(", ");
        }
        return res.append("]").toString();
    }

    public String toSavingString() {
        StringBuilder res = new StringBuilder();
        for (String form : forms) {
            res.append(SEP).append(form);
        }
        return super.toSavingString() + res.toString();
    }

}
