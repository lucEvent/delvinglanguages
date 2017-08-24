package com.delvinglanguages.view.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;

public class AppAnimator {

    private static final long TYPE_LABELS_ANIMATION_DURATION = 400;
    private static final long BOTTOM_BAR_ANIMATION_DURATION = 400;
    private static final long REVEAL_CONCEAL_ANIMATION_DURATION = 2000;


    private static ViewPropertyAnimatorCompat animator;
    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();

    public static boolean[] getTypeStatusVector()
    {
        boolean[] status = new boolean[DReference.NUMBER_OF_TYPES];
        for (int i = 0; i < status.length; i++) status[i] = true;
        return status;
    }

    public static void typeAnimation(Context context, boolean[] currentStatus, int type)
    {
        TranslateAnimation showAnimation = new TranslateAnimation(0, 0, -26, 0);
        showAnimation.setDuration(TYPE_LABELS_ANIMATION_DURATION);
        showAnimation.setFillAfter(true);

        TranslateAnimation hideAnimation = new TranslateAnimation(0, 0, 0, -26);
        hideAnimation.setDuration(TYPE_LABELS_ANIMATION_DURATION);
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

    public static void bottomBarInAnimation(final View bar)
    {
        ensureOrCancelAnimator(bar, BOTTOM_BAR_ANIMATION_DURATION);
        animator.translationY(0).withStartAction(new Runnable() {
            @Override
            public void run()
            {
                bar.setVisibility(View.VISIBLE);
            }
        }).start();
    }

    public static void bottomBarOutAnimation(final View bar)
    {
        ensureOrCancelAnimator(bar, BOTTOM_BAR_ANIMATION_DURATION);
        animator.translationY(bar.getHeight()).withEndAction(new Runnable() {
            @Override
            public void run()
            {
                bar.setVisibility(View.GONE);
            }
        }).start();
    }

    public static void bottomBarFastOutAnimation(final View bar)
    {
        ensureOrCancelAnimator(bar, 0);
        animator.translationY(bar.getHeight()).withStartAction(new Runnable() {
            @Override
            public void run()
            {
                bar.setVisibility(View.GONE);
            }
        }).start();
    }

    private static void ensureOrCancelAnimator(View view, long duration)
    {
        if (animator != null) {
            animator.cancel();
        }
        animator = ViewCompat.animate(view);
        animator.setDuration(duration);
        animator.setInterpolator(INTERPOLATOR);
    }

    public static Animator viewRevealAnimation(View view, View from, View to)
    {
        int x = from.getLeft() + from.getWidth() / 2;
        int y = from.getTop() + from.getHeight() / 2;

        int startRadius = 0;
        int endRadius = (int) Math.hypot(to.getWidth(), to.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        anim.setDuration(REVEAL_CONCEAL_ANIMATION_DURATION);
        anim.setInterpolator(INTERPOLATOR);

        return anim;
    }

    public static Animator viewConcealAnimation(View view, final View from, View to, int topOffset)
    {
        int x = from.getLeft() + ((View) from.getParent()).getLeft() + from.getWidth() / 2;
        int y = -topOffset + from.getTop() + ((View) from.getParent()).getTop() + from.getHeight() / 2;

        int startRadius = Math.max(to.getWidth(), to.getHeight());
        int endRadius = 0;

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        anim.setDuration(REVEAL_CONCEAL_ANIMATION_DURATION);
        anim.setInterpolator(INTERPOLATOR);

        return anim;
    }

    public static ViewPropertyAnimatorCompat fadeIn(View view, int duration, int startDelay)
    {
        return ViewCompat.animate(view)
                .alpha(1)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(duration)
                .setStartDelay(startDelay);
    }

    public static ViewPropertyAnimatorCompat fadeOut(View view, int duration, int startDelay)
    {
        return ViewCompat.animate(view)
                .alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(duration)
                .setStartDelay(startDelay);
    }
}
