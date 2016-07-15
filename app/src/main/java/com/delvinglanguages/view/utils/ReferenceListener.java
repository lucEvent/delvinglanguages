package com.delvinglanguages.view.utils;

import com.delvinglanguages.kernel.Inflexion;

public interface ReferenceListener extends MessageListener {

    int INFLEXION_ADDED = 300;

    void onInflexionAdded(Inflexion inflexion);

}
