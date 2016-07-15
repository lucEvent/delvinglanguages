package com.delvinglanguages.view.utils;

import android.app.Activity;
import android.content.Context;
import android.view.animation.TranslateAnimation;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;

public class AppAnimator {

    public static boolean[] getTypeStatusVector()
    {
        boolean[] status = new boolean[DReference.NUMBER_OF_TYPES];
        for (int i = 0; i < status.length; i++) status[i] = true;
        return status;
    }

    public static void typeAnimation(Context context, boolean[] currentStatus, int type)
    {
        TranslateAnimation showAnimation = new TranslateAnimation(0, 0, -26, 0);
        showAnimation.setDuration(400);
        showAnimation.setFillAfter(true);

        TranslateAnimation hideAnimation = new TranslateAnimation(0, 0, 0, -26);
        hideAnimation.setDuration(400);
        hideAnimation.setFillAfter(true);

        int[] button_ids = new int[]{R.id.noun, R.id.verb, R.id.adjective,
                R.id.adverb, R.id.phrasal_verb, R.id.expression, R.id.preposition,
                R.id.conjunction, R.id.other};

        for (int i = 0; i < DReference.NUMBER_OF_TYPES; i++) {
            if ((type & (1 << i)) != 0) {
                if (!currentStatus[i]) {
                    currentStatus[i] = true;
                    ((Activity) context).findViewById(button_ids[i]).startAnimation(showAnimation);
                }
            } else if (currentStatus[i]) {
                currentStatus[i] = false;
                ((Activity) context).findViewById(button_ids[i]).startAnimation(hideAnimation);
            }
        }
    }
}
