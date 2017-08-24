package com.delvinglanguages.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppSettings;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.kernel.game.MatchGame;
import com.delvinglanguages.kernel.phrasalverb.PhrasalVerb;
import com.delvinglanguages.kernel.phrasalverb.PhrasalVerbsEngine;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.view.activity.practise.PractiseCompleteActivity;
import com.delvinglanguages.view.activity.practise.PractiseListeningActivity;
import com.delvinglanguages.view.activity.practise.PractiseMatchActivity;
import com.delvinglanguages.view.activity.practise.PractiseWriteActivity;
import com.delvinglanguages.view.activity.practise.TestActivity;
import com.delvinglanguages.view.lister.PhrasalVerbLister;
import com.delvinglanguages.view.lister.UsageLister;
import com.delvinglanguages.view.lister.util.PhrasalVerbsSearchEngine;
import com.delvinglanguages.view.utils.AppAnimator;

import java.util.Calendar;

public class PhrasalVerbsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long TIME_NEXT_REMINDER = 7 * 1000;

    private DelvingListManager dataManager;
    private PhrasalVerbsEngine phEngine;

    private RecyclerView recyclerView;

    private PhrasalVerbsSearchEngine searchEngine;
    private DReference editingPhrasalVerb;

    // Reminder variables
    private View popUp, btnYes, btnNo;
    private FloatingActionButton fab;
    private boolean showReminder = true;
    private int today;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_phrasal_verbs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataManager = new DelvingListManager(this);
        phEngine = dataManager.getPhrasalVerbsEngine();

        PhrasalVerbLister adapter = new PhrasalVerbLister(this, phEngine.getPhrasalVerbs(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(reminderClickListener);

        reminderExecution.start();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        synchronized (reminderExecution) {
            showReminder = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AppCode.DREFERENCE_REMOVED:
                searchEngine.remove(editingPhrasalVerb);
                break;
            case AppCode.DREFERENCE_UPDATED:
                searchEngine.update(editingPhrasalVerb);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.m_dictionary, menu);

        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        searchEngine = new PhrasalVerbsSearchEngine(this, recyclerView, search, phEngine.getPhrasalVerbs());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        PhrasalVerb phv = phEngine.getPhrasalVerb(item.getGroupId());
        Class practise_selected = null;
        switch (item.getItemId()) {
            case R.id.practise_test:

                Test test = dataManager.createTest(this, phv);

                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra(AppCode.TEST_ID, test.id);
                startActivity(intent);

                return super.onContextItemSelected(item);

            case R.id.practise_match:
                practise_selected = PractiseMatchActivity.class;
                break;
            case R.id.practise_complete:
                practise_selected = PractiseCompleteActivity.class;
                break;
            case R.id.practise_write:
                practise_selected = PractiseWriteActivity.class;
                break;
            case R.id.practise_listening:
                practise_selected = PractiseListeningActivity.class;
                break;
        }

        Intent intent = new Intent(this, practise_selected);
        intent.putExtra(AppCode.DREFERENCE_NAME_NUM, phv.variants.size());

        for (int i = 0; i < phv.variants.size(); i++)
            intent.putExtra(AppCode.DREFERENCE_NAME + i, phv.variants.get(i).name);

        setResult(AppCode.DREFERENCE_SELECTED, intent);
        startActivity(intent);
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        editingPhrasalVerb = (DReference) v.getTag();

        Intent intent = new Intent(this, DReferenceActivity.class);
        intent.putExtra(AppCode.DREFERENCE_NAME, editingPhrasalVerb.name);
        startActivityForResult(intent, AppCode.ACTION_MODIFY);
    }

    public void onMatchAction(View view)
    {
    }

    public void onCompleteAction(View view)
    {
    }

    public void onWriteAction(View view)
    {
    }

    public void onListeningAction(View view)
    {
    }

    public void onTestAction(View view)
    {
    }

    private final Thread reminderExecution = new Thread(new Runnable() {
        @Override
        public void run()
        {
            int lastDay = AppSettings.getLastPhrasalVerbReminderDay(dataManager.getCurrentList().id);
            today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (today == lastDay) {
                showReminder = false;
                return;
            }

            fab.setTag(new MatchGame(dataManager.getCurrentList().getPhrasalVerbs()).nextReference());

            while (true) {
                try {
                    Thread.sleep(TIME_NEXT_REMINDER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean l_showReminder;
                synchronized (reminderExecution) {
                    l_showReminder = showReminder;
                }

                if (!l_showReminder)
                    return;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        fab.setVisibility(View.INVISIBLE);

                        AppAnimator.fadeIn(fab, 1000, 0).withStartAction(new Runnable() {
                            @Override
                            public void run()
                            {
                                fab.setVisibility(View.VISIBLE);
                            }
                        }).withEndAction(new Runnable() {
                            @Override
                            public void run()
                            {
                                AppAnimator.fadeOut(fab, 500, 2000).withEndAction(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        fab.setVisibility(View.GONE);
                                    }
                                }).start();
                            }
                        }).start();
                    }
                });
            }
        }
    });

    private View.OnClickListener reminderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.fab:
                    synchronized (reminderExecution) {
                        showReminder = false;
                    }

                    DReference phv = (DReference) v.getTag();

                    Context context = v.getContext();

                    popUp = ((ViewStub) findViewById(R.id.phv_pop_up)).inflate();
                    ((TextView) popUp.findViewById(R.id.name)).setText(phv.name);
                    ((TextView) popUp.findViewById(R.id.pronunciation)).setText("[" + phv.pronunciation + "]");

                    UsageLister adapter = new UsageLister(context, dataManager.getUsages(phv));

                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setAutoMeasureEnabled(true);

                    RecyclerView recyclerView = (RecyclerView) popUp.findViewById(R.id.list);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    btnYes = findViewById(R.id.btn_yes);
                    btnYes.setTag(phv);
                    btnNo = findViewById(R.id.btn_no);
                    btnNo.setTag(phv);

                    btnYes.setOnClickListener(reminderClickListener);
                    btnNo.setOnClickListener(reminderClickListener);

                    Animator anim = AppAnimator.viewRevealAnimation(popUp, fab, findViewById(R.id.topLayout));
                    anim.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animator)
                        {
                            getSupportActionBar().hide();
                            popUp.setVisibility(View.VISIBLE);
                            fab.setVisibility(View.GONE);
                        }

                    });
                    anim.start();
                    break;

                case R.id.btn_yes:

                    dataManager.reminded((DReference) v.getTag());

                case R.id.btn_no:

                    View viewClicked = v.getId() == R.id.btn_yes ? btnYes : btnNo;

                    anim = AppAnimator.viewConcealAnimation(popUp, viewClicked, findViewById(R.id.topLayout), getSupportActionBar().getHeight());
                    anim.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation)
                        {
                            getSupportActionBar().show();
                        }

                        @Override
                        public void onAnimationEnd(Animator animator)
                        {
                            popUp.setVisibility(View.GONE);
                        }

                    });
                    anim.start();

                    AppSettings.setLastPhrasalVerbReminderDay(dataManager.getCurrentList().id, today);
            }

        }
    };

}