package com.gexton.cashinvesternew.activities;

import android.os.Bundle;

import com.gexton.cashinvesternew.R;
import com.gexton.cashinvesternew.activities.BaseActivity;

public class SupportTicketsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

    }
}