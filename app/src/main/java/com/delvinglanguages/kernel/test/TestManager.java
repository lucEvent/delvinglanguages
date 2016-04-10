package com.delvinglanguages.kernel.test;

import android.content.Context;

import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.game.TestGame;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Tests;

public class TestManager extends KernelManager {

    public TestManager(Context context) {
        super(context);
    }

    public Tests getTests() {
        Language language = getCurrentLanguage();
        if (language.tests == null)
            language.setTests(dbManager.readTests(language.id));

        return language.tests;
    }

    public Test createTest(String test_name, int numberOfWords, int type) {
        Language language = getCurrentLanguage();

        DReferences references = new TestGame(language.getReferences()).getRandomReferences(numberOfWords, type);
        if (references.isEmpty())
            return null;

        Test test = dbManager.insertTest(test_name, references, language.id);
        language.tests.add(test);

        return test;
    }

    public void updateTest(Test test) {
        dbManager.updateTest(test);
    }

    public void deleteTest(Test test) {
        getCurrentLanguage().tests.remove(test);
        dbManager.deleteTest(test.id);
    }

}
