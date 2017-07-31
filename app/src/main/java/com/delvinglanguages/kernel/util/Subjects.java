package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.subject.Subject;

import java.util.ArrayList;

public class Subjects extends ArrayList<Subject> {

    public Subjects()
    {
        super();
    }

    public Subjects(int capacity)
    {
        super(capacity);
    }

    public Subject getSubjectById(int id)
    {
        for (Subject subject : this)
            if (subject.id == id)
                return subject;

        return null;
    }

}
