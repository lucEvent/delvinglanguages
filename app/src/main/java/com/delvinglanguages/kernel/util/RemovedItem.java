package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;

public class RemovedItem {

    public final int id;

    public final int list_id;

    public final int wrap_type;

    public String wrapper;

    public RemovedItem(int id, int list_id, int wrap_type, String wrapper)
    {
        this.id = id;
        this.list_id = list_id;
        this.wrap_type = wrap_type;
        this.wrapper = wrapper;
    }

    public DReference castToReference()
    {
        return DReference.fromWrapper(id, wrapper);
    }

    public Subject castToSubject()
    {
        return Subject.fromWrapper(id, wrapper);
    }

    public Test castToTest()
    {
        return Test.fromWrapper(id, wrapper);
    }

}
