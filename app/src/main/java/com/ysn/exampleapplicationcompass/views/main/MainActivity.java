package com.ysn.exampleapplicationcompass.views.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysn.exampleapplicationcompass.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    private final String TAG = "CompassActivityTAG";

    private MainActivityPresenter mainActivityPresenter;
    @BindView(R.id.image_view_dial_activity_main)
    ImageView imageViewDialActivityMain;
    @BindView(R.id.image_view_image_hands_activity_main)
    ImageView imageViewHandsActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPresenter();
        onAttach();
    }

    private void initPresenter() {
        mainActivityPresenter = new MainActivityPresenter();
        Log.d(TAG, "initPresenter");
    }

    @Override
    public void onAttach() {
        mainActivityPresenter.onAttach(this);
        mainActivityPresenter.initCompass(this);
    }

    @Override
    public void onDetach() {
        mainActivityPresenter.onDetach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*mainActivityPresenter.compassStart(this);*/
        // mainActivityPresenter.compassStart(this, "MainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // mainActivityPresenter.compassStop();
    }

    @Override
    protected void onDestroy() {
        onDetach();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        onAttach();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mainActivityPresenter.compassStop();
    }

    @Override
    public void adjustArrow(float currentAzimuth, float azimuth) {
        Animation animation = new RotateAnimation(currentAzimuth, azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(500);
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        imageViewHandsActivityMain.startAnimation(animation);
    }
}
