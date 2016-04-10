package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.theme.Theme;

import java.util.ArrayList;

public class Themes extends ArrayList<Theme> {

    public Themes() {
        super();
    }

    public Theme getThemeById(int id) {
        for (Theme theme : this)
            if (theme.id == id)
                return theme;

        return null;
    }

}
