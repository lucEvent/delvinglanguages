package com.delvinglanguages.view.lister.util;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.util.Item;

public final class ReferenceSelector extends Item {

    public boolean selected;
    public DReference reference;

    public ReferenceSelector(DReference reference)
    {
        super(reference.id, Item.DREFERENCE);
        this.reference = reference;
        selected = false;
    }

}