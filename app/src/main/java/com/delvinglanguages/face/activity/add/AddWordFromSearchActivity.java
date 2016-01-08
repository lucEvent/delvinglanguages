package com.delvinglanguages.face.activity.add;

import android.os.Bundle;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.face.AppCode;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.set.Inflexions;

import java.util.ArrayList;

public class AddWordFromSearchActivity extends AddWordActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        get_and_set_Name(bundle);
        get_and_set_Translation(bundle);

        findViewById(R.id.addmore).setVisibility(View.GONE);
    }

    protected void get_and_set_Translation(Bundle bundle) {
        ArrayList<Inflexion> value = (ArrayList<Inflexion>) bundle.get(AppCode.TRANSLATION);
        setInflexionsList(new Inflexions(value));
    }
}
