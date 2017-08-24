package com.delvinglanguages.view.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.delvinglanguages.AppCode;
import com.delvinglanguages.AppData;
import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DelvingListManager;
import com.delvinglanguages.view.fragment.DelvingListSettingsFragment;
import com.delvinglanguages.view.fragment.DrawerFragment;
import com.delvinglanguages.view.fragment.PractiseFragment;
import com.delvinglanguages.view.fragment.PronunciationFragment;
import com.delvinglanguages.view.fragment.RecycleBinFragment;
import com.delvinglanguages.view.fragment.SubjectsFragment;
import com.delvinglanguages.view.utils.DelvingListListener;

public class DelvingListActivity extends AppCompatActivity {

    public enum Option {
        PRACTISE, VERBS, DRAWER, RECYCLE_BIN, SUBJECTS, PRONUNCIATION, SETTINGS
    }

    private Option currentOption;

    private DelvingListManager dataManager;

    private DelvingList delvingList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_delving_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataManager = new DelvingListManager(this);
        delvingList = dataManager.getCurrentList();

        currentOption = (Option) getIntent().getExtras().get(AppCode.FRAGMENT);
        setFragment(currentOption);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.clear_recycler_bin:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.msg_confirm_to_clear_all_items)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                clearRecycleBin();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create().show();
                break;
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case DelvingListListener.LIST_MERGED_AND_REMOVED:
                setResult(resultCode, data);
                finish();
                break;
        }
    }

    private void setFragment(Option option)
    {
        Fragment fragment = null;
        String title = "Fragment not implemented";
        switch (option) {
            case PRACTISE:
                fragment = new PractiseFragment();
                title = getString(R.string.title_practise_fragment, delvingList.name);
                break;
            case DRAWER:
                fragment = new DrawerFragment();
                title = getString(R.string.title_drawer_fragment, delvingList.name);
                break;
            case SUBJECTS:
                fragment = new SubjectsFragment();
                title = getString(R.string.title_subjects_fragment, delvingList.name);
                break;
            case VERBS:
                //fragment = new VerbsFragment();
                //title = currentList.name + "'s Verbs";
                break;
            case PRONUNCIATION:
                fragment = new PronunciationFragment();
                title = getString(R.string.title_pronunciation_fragment, AppData.getLanguageName(delvingList.from_code));
                break;
            case RECYCLE_BIN:
                fragment = new RecycleBinFragment();
                title = getString(R.string.title_recyclebin_fragment, delvingList.name);
                break;
            case SETTINGS:
                fragment = new DelvingListSettingsFragment();
                title = getString(R.string.title_language_settings_fragment, delvingList.name);
                break;
        }

        currentOption = option;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
        setTitle(title);
    }

    private void clearRecycleBin()
    {
        dataManager.deleteAllRemovedItems();
        finish();
    }

}
