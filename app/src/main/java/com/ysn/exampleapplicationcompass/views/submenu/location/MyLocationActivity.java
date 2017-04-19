package com.ysn.exampleapplicationcompass.views.submenu.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ysn.exampleapplicationcompass.R;

public class MyLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
    }
}
