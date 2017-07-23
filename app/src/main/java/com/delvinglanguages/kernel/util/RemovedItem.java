package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;

public class RemovedItem {

    public final int id;

    public final int language_id;

    public final int wrap_type;

    public String wrapper;

    public RemovedItem(int id, int lang_id, int wrap_type, String wrapper)
    {
        this.id = id;
        this.language_id = lang_id;
        this.wrap_type = wrap_type;
        this.wrapper = wrapper;
    }

    public DReference castToReference()
    {
        return DReference.fromWrapper(id, wrapper);
    }

    public Theme castToTheme()
    {
        return Theme.fromWrapper(id, wrapper);
    }

    public Test castToTest()
    {
        return Test.fromWrapper(id, wrapper);
    }

}
