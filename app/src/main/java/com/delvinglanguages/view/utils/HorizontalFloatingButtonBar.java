package com.delvinglanguages.view.utils;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.delvinglanguages.R;

import java.util.ArrayList;

public class HorizontalFloatingButtonBar extends CoordinatorLayout implements View.OnClickListener {

    private static final int ANIMATION_TIME = 300;

    private LayoutInflater inflater;
    private View trigger;
    public boolean closed;

    private ArrayList<View> buttons;

    public HorizontalFloatingButtonBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.v_horizontal_floating_button_bar, this, true);

        trigger = findViewById(R.id.button_trigger);
        trigger.setOnClickListener(this);

        closed = true;

        buttons = new ArrayList<>(3);
    }

    @Override
    public void onClick(View v)
    {
        if (closed)
            open();
        else
            close();
    }

    public void addButton(int iconResId, int descriptionResId, OnClickListener actionListener)
    {
        View button = inflater.inflate(R.layout.v_horizontal_floating_button, this, false);
        buttons.add(button);

        ((FloatingActionButton) button.findViewById(R.id.button)).setImageResource(iconResId);
        ((TextView) button.findViewById(R.id.description)).setText(descriptionResId);
        button.setOnClickListener(actionListener);
        button.setEnabled(false);

        addView(button);

    }

    public void close()
    {
        if (closed)
            return;

        trigger.animate()
                .rotationBy(-45)
                .scaleX(1.f)
                .scaleY(1.f)
                .setListener(buttonCollapsingAnimatorListener)
                .setDuration(ANIMATION_TIME)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        for (int i = 0; i < buttons.size(); i++) {
            View button = buttons.get(i);
            button.animate()
                    .translationYBy(1f * trigger.getHeight() * (i + 1))
                    .setDuration(ANIMATION_TIME)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            button.setEnabled(false);
        }
        closed = true;
    }

    public void open()
    {
        if (!closed)
            return;

        trigger.animate()
                .rotationBy(45)
                .scaleX(.75f)
                .scaleY(.75f)
                .setListener(buttonOpeningAnimatorListener)
                .setDuration(ANIMATION_TIME)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        for (int i = 0; i < buttons.size(); i++) {
            View button = buttons.get(i);
            button.animate()
                    .translationYBy(-1f * trigger.getHeight() * (i + 1))
                    .setDuration(ANIMATION_TIME)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
            button.setEnabled(true);
        }
        closed = false;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        trigger.setEnabled(enabled);
    }

    private Animator.AnimatorListener buttonOpeningAnimatorListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation)
        {
            trigger.setClickable(false);
            for (View fab : buttons)
                fab.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            trigger.setClickable(true);
            for (View fab : buttons)
                fab.setClickable(true);
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

    private Animator.AnimatorListener buttonCollapsingAnimatorListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation)
        {
            trigger.setClickable(false);
            for (View fab : buttons)
                fab.setClickable(false);
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            trigger.setClickable(true);
            for (View fab : buttons)
                fab.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

}
