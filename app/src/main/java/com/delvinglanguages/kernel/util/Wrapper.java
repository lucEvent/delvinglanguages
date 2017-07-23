package com.delvinglanguages.kernel.util;

public interface Wrapper {

    int TYPE_LANGUAGE = 1;
    int TYPE_REFERENCE = 2;
    int TYPE_DRAWER_REFERENCE = 3;
    int TYPE_THEME = 4;
    int TYPE_TEST = 5;
    int TYPE_STATISTICS = 6;
    int TYPE_THEME_PAIRS = 7;
    int TYPE_INFLEXIONS = 8;

    String wrap();

    int wrapType();

}
