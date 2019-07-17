package com.gd70.android.expcal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity mainActivity=MainActivity.getInstance();
        mainActivity.getGraphRecyclerView().smoothScrollToPosition(mainActivity.getCalenderInstance().getIntMonth());

    }
}
