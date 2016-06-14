package com.delvinglanguages.kernel.verb;

public class SvenskTense extends Tense {

    private int type;

    public SvenskTense(int tenseID, String verb)
    {
        super(0, tenseID, verb);

        type = getType();
    }

    private int getType()
    {
        return 0;
    }

    @Override
    public String[] getConjugations()
    {
        System.out.println("Taking conjugations");
        if (forms == null) {
            switch (tense) {
                case SV_PRESENT:
                    forms = getPresent();
                    break;
                case SV_PRETERITUM:
                    forms = getPreteritum();
                    break;
                case SV_SUPINUM:
                    forms = getSupinum();
                    break;
                case SV_FUTURE:
                    forms = getFuture();
                    break;
                case SV_IMPERATIV:
                    forms = getImperativ();
            }
        }
        return forms;
    }

    @Override
    public String[] getPronunciations()
    {
        return getConjugations();
    }

    private String[] getPresent()
    {
        String[] res = new String[6];
        return res;
    }

    private String[] getPreteritum()
    {
        String[] res = new String[6];
        return res;
    }

    private String[] getSupinum()
    {
        String[] res = new String[6];
        return res;
    }

    private String[] getFuture()
    {
        String[] res = new String[6];
        return res;
    }

    private String[] getImperativ()
    {
        String[] res = new String[6];
        return res;
    }

}
