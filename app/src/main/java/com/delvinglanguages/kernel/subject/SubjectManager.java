package com.delvinglanguages.kernel.subject;

import android.content.Context;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Subjects;
import com.delvinglanguages.kernel.util.Wrapper;

public class SubjectManager extends KernelManager {

    public SubjectManager(Context context)
    {
        super(context);
    }

    public Subjects getSubjects()
    {
        DelvingList delvingList = getCurrentList();
        if (delvingList.subjects == null)
            delvingList.setSubjects(dbManager.readSubjects(delvingList.id));

        return delvingList.subjects;
    }

    public DReference getReference(String name)
    {
        return getCurrentList().getReference(name);
    }

    public DReferences getReferences(Subject subject)
    {
        if (subject.getReferences() == null)
            subject.setReferences(dbManager.readReferences(getCurrentList().id, subject.getReferencesIds()));

        return subject.getReferences();
    }

    public Subject addSubject(String name, DReferences references)
    {
        DelvingList delvingList = getCurrentList();

        Subject subject = dbManager.insertSubject(delvingList.id, name, references);
        delvingList.addSubject(subject);

        RecordManager.subjectAdded(delvingList.id, delvingList.from_code, subject.id);
        synchronizeNewItem(delvingList.id, subject.id, subject);
        return subject;
    }

    public void updateSubject(Subject subject, String new_name, DReferences new_references)
    {
        subject.setName(new_name);
        subject.setReferences(new_references);

        DelvingList delvingList = getCurrentList();
        dbManager.updateSubject(subject, delvingList.id);

        RecordManager.subjectModified(delvingList.id, delvingList.from_code, subject.id);
        synchronizeUpdateItem(delvingList.id, subject.id, subject);
    }

    public void removeSubject(Subject subject)
    {
        DelvingList delvingList = getCurrentList();
        delvingList.removeSubject(subject);
        dbManager.removeSubject(delvingList.id, subject);

        RecordManager.subjectRemoved(delvingList.id, delvingList.from_code, subject.id);
        synchronizeDeleteItem(delvingList.id, subject.id, Wrapper.TYPE_SUBJECT);
    }

    public Test toTest(Context context, Subject subject)
    {
        DelvingList delvingList = getCurrentList();

        Test test = delvingList.tests.getTestFrom(subject.id);
        if (test != null)
            return test;

        test = dbManager.readTestFrom(subject.id);
        if (test != null)
            return test;

        String test_name = subject.getName() + " " + context.getString(R.string.test);
        DReferences references = getReferences(subject);

        test = dbManager.insertTest(delvingList.id, test_name, references, subject.id);

        delvingList.tests.add(test);
        RecordManager.testAdded(delvingList.id, delvingList.from_code, test.id);
        synchronizeNewItem(delvingList.id, test.id, test);

        return test;
    }

}
