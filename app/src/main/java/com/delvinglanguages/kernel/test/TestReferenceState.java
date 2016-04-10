package com.delvinglanguages.kernel.test;

import com.delvinglanguages.kernel.DReference;

public class TestReferenceState {

    public enum TestStage {
        DELVING, MATCH, COMPLETE, WRITE, LISTENING, END
    }

    public class Stats {
        public int attempts;
        public int errors;

        public Stats() {
            this.attempts = 0;
            this.errors = 0;
        }
    }

    public DReference reference;

    public TestStage stage;

    public Stats match, complete, write, listening;

    public TestReferenceState(DReference reference) {
        this.reference = reference;
        this.match = new Stats();
        this.complete = new Stats();
        this.write = new Stats();
        this.listening = new Stats();
    }

}
