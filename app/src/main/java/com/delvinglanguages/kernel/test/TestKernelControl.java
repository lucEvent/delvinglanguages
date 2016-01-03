package com.delvinglanguages.kernel.test;

import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Tests;

public class TestKernelControl extends KernelControl {

    public static Test runningTest;

    public TestKernelControl() {
        super(KernelControl.context);
    }

    public Tests getTests() {
        Tests result = currentLanguage.tests;
        if (result == null) {
            result = dbManager.readTests(currentLanguage.id);
            currentLanguage.setTests(result);
        }
        return result;
    }

    public void runTest(DReferences words) {
        runningTest = new Test(words);
    }

    public void saveTest(Test test) {
        dbManager.updateTest(test);
    }

    public void saveRunningTest(String name) {
        runningTest.id = addTest(name, runningTest.encapsulate());
        runningTest.name = name;
        currentLanguage.addTest(runningTest);
    }

    public int addTest(String name, String content) {
        return dbManager.insertTest(name, content, currentLanguage.id);
    }

    public void removeTest(Test test) {
        currentLanguage.tests.remove(test);
        dbManager.deleteTest(test.id);
    }

}
