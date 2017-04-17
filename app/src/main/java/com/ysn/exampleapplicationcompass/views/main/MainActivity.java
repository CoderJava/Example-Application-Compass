package com.ysn.exampleapplicationcompass.views.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ysn.exampleapplicationcompass.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "MainActivityTAG";

    private MainActivityPresenter mainActivityPresenter;
    @BindView(R.id.image_view_dial_activity_main)
    ImageView imageViewDialActivityMain;
    @BindView(R.id.image_view_image_hands_activity_main)
    ImageView imageViewHandsActivityMain;
    @BindView(R.id.text_view_location_now_activity_main)
    TextView textViewLocationNowActivityMain;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPresenter();
        onAttach();
        buildGoogleApiClient();
        Log.d(TAG, "onCreate");
        textViewLocationNowActivityMain.setSelected(true);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainActivityPresenter.stopCompass();
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
        mainActivityPresenter.stopCompass();
        super.onStop();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        mainActivityPresenter.onConnected(this, googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // nothing to do in here
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // nothing to do in here
    }

    @Override
    public void setLocationNameSuccess(String formattedAddress) {
        textViewLocationNowActivityMain.setText(formattedAddress);
    }

    @Override
    public void setLocationNameFail() {
        Toast.makeText(this, "get location name failed", Toast.LENGTH_LONG)
                .show();
    }

    public synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        Log.d(TAG, "buildGoogleApiClient");
    }
}
