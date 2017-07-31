package com.delvinglanguages.kernel.subject;

import android.content.Context;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.SubjectPairs;
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

    public Subject addSubject(String subject_name, SubjectPairs subject_pairs)
    {
        DelvingList delvingList = getCurrentList();

        Subject subject = dbManager.insertSubject(delvingList.id, subject_name, subject_pairs);
        delvingList.addSubject(subject);

        RecordManager.subjectAdded(delvingList.id, delvingList.from_code, subject.id);
        synchronizeNewItem(delvingList.id, subject.id, subject);
        return subject;
    }

    public void updateSubject(Subject subject, String new_name, SubjectPairs new_pairs)
    {
        subject.setName(new_name);
        subject.setPairs(new_pairs);

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
        Test test = dbManager.readTestFromSubject(subject.id);

        if (test == null) {

            String test_name = subject.getName() + " " + context.getString(R.string.test);

            DReferences references = new DReferences(subject.getPairs().size());
            for (SubjectPair pair : subject.getPairs()) {
                Inflexions inflexions = new Inflexions(1);
                inflexions.add(new Inflexion(new String[]{}, new String[]{pair.inNative}, DReference.OTHER));
                references.add(new DReference(-1, pair.inDelved, "", inflexions, 0));
            }

            test = dbManager.insertTest(delvingList.id, test_name, references, subject.id);

            RecordManager.testAdded(delvingList.id, delvingList.from_code, test.id);
            synchronizeNewItem(delvingList.id, test.id, test);
        }
        Test replaceable = delvingList.tests.getTestById(test.id);
        if (replaceable != null) test = replaceable;
        else delvingList.tests.add(test);
        return test;
    }

}
