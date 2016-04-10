package com.delvinglanguages.kernel.verb;

import android.util.Log;

import com.delvinglanguages.kernel.Tense;

public class EnglishTense extends Tense {

    private final static String DEBUG = "##EnglishTense##";

    private int type;

    public EnglishTense(int tenseID, String verb) {
        super(0, tenseID, verb);

        type = getType();
    }

    private int getType() {
        return 0;
    }

    String[] get() {
        return null;
    }

    @Override
    public String[] getConjugations() {
        Log.d(DEBUG, "Taking conjugations");
        if (forms == null) {
            switch (tense) {
                case EN_PRESENT:
                    forms = getPresent();
                    break;
                case EN_PAST_SIMPLE:
                    forms = getPastSimple();
                    break;
                case EN_FUTURE:
                    forms = getFuture();
                    break;
                case EN_PRESENT_CONTINUOUS:
                    forms = getPresentContinuous();
                    break;
                case EN_PRESENT_PERFECT:
                    forms = getPresentPerfect();
                    break;
                case EN_PRESENT_PERFECT_CONTINUOUS:
                    forms = getPresentPerfectContinuous();
                    break;
                case EN_PAST_CONTINUOUS:
                    forms = getPastContinuous();
                    break;
                case EN_PAST_PERFECT_CONTINUOUS:
                    forms = getPastPerfectContinuous();
                    break;
                case EN_FUTURE_CONTINUOUS:
                    forms = getFutureContinuous();
                    break;
                case EN_FUTURE_PERFECT:
                    forms = getFuturePerfect();
                    break;
                case EN_FUTURE_GOING_TO:
                    forms = getFutureGoingTo();
                    break;
                case EN_CONDITIONAL:
                    forms = getConditional();
                    break;
                case EN_IMPERATIVE:
                    forms = getImperative();
            }
        }
        return forms;
    }

    private String[] getImperative() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getConditional() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getFutureGoingTo() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getFuturePerfect() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getFutureContinuous() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getPastPerfectContinuous() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getPastContinuous() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getPresentPerfectContinuous() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getPresentPerfect() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getPresentContinuous() {
        // TODO Auto-generated method stub
        return null;
    }

    private String[] getPastSimple() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getPronunciations() {
        return getConjugations();
    }

    private String[] getPresent() {
        String[] res = new String[6];
        return res;
    }

    private String[] getFuture() {
        String[] res = new String[6];
        return res;
    }

}
