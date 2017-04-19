package com.ysn.exampleapplicationcompass.views.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ysn.exampleapplicationcompass.R;
import com.ysn.exampleapplicationcompass.views.submenu.location.MyLocationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainActivityView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "MainActivityTAG";
    private MainActivityPresenter mainActivityPresenter;
    private GoogleApiClient googleApiClient;

    @BindView(R.id.image_view_dial_activity_main)
    ImageView imageViewDialActivityMain;
    @BindView(R.id.image_view_image_hands_activity_main)
    ImageView imageViewHandsActivityMain;
    @BindView(R.id.text_view_location_now_activity_main)
    TextView textViewLocationNowActivityMain;
    @BindView(R.id.floating_action_button_menu_activity_main)
    FloatingActionButton floatingActionButtonMenuActivityMain;
    @BindView(R.id.floating_action_button_my_location_activity_main)
    FloatingActionButton floatingActionButtonMyLocationActivityMain;
    @BindView(R.id.floating_action_button_maps_activity_main)
    FloatingActionButton floatingActionButtonMapsActivityMain;

    private boolean menuShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPresenter();
        onAttach();
    }

    private void checkPermissionGps() {
        mainActivityPresenter.onCheckPermissionGps(this);
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
        mainActivityPresenter.stopCompass();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        checkPermissionGps();
    }

    @Override
    protected void onStop() {
        mainActivityPresenter.stopCompass();
        super.onStop();
    }

    @OnClick({
            R.id.floating_action_button_menu_activity_main,
            R.id.floating_action_button_my_location_activity_main,
            R.id.floating_action_button_maps_activity_main
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action_button_menu_activity_main:
                menuShown = (menuShown) ? false : true;
                if (menuShown) {
                    ObjectAnimator objectAnimatorRotation = ObjectAnimator.ofFloat(floatingActionButtonMenuActivityMain, "rotation", 0, 45);
                    objectAnimatorRotation.setDuration(100);
                    objectAnimatorRotation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            floatingActionButtonMyLocationActivityMain.show();
                            floatingActionButtonMapsActivityMain.show();
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            // nothing to do in here
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            // nothing to do in here
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                            // nothing to do in here
                        }
                    });
                    objectAnimatorRotation.start();
                } else {
                    ObjectAnimator objectAnimatorRotation = ObjectAnimator.ofFloat(floatingActionButtonMenuActivityMain, "rotation", 45, 0);
                    objectAnimatorRotation.setDuration(100);
                    objectAnimatorRotation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            floatingActionButtonMyLocationActivityMain.hide();
                            floatingActionButtonMapsActivityMain.hide();
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            // nothing to do in here
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            // nothing to do in here
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                            // nothing to do in here
                        }
                    });
                    objectAnimatorRotation.start();
                }
                break;
            case R.id.floating_action_button_my_location_activity_main:
                mainActivityPresenter.onGotoMyLocationActivity();
                break;
            case R.id.floating_action_button_maps_activity_main:
                mainActivityPresenter.onGotoMapsActivity();
                break;
        }
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
        Toast.makeText(this, "Location is updated", Toast.LENGTH_SHORT)
                .show();
        Log.d(TAG, "setLocationNameSuccess: " + formattedAddress);
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

    @Override
    public void showDialogEnabledGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        System.exit(1);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void gpsAlreadyEnabled() {
        Log.d(TAG, "gpsAlreadyEnabled");
        buildGoogleApiClient();
        textViewLocationNowActivityMain.setSelected(true);
    }

    @Override
    public void gotoMyLocationActivity() {
        Intent intentMyLocation = new Intent(this, MyLocationActivity.class);
        startActivity(intentMyLocation);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_right);
    }

    @Override
    public void gotoMapsActivity() {
        // go to MapsActivity
    }
}
