package com.ysn.exampleapplicationcompass.views.submenu.location;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ysn.exampleapplicationcompass.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyLocationActivity extends AppCompatActivity implements MyLocationActivityView, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private final String TAG = "MyLocationTAG";

    @BindView(R.id.coordinator_layout_container_activity_my_location)
    CoordinatorLayout coordinatorLayoutContainerActivityMyLocation;

    private MyLocationActivityPresenter myLocationActivityPresenter;
    private GoogleMap googleMap;
    private List<LatLng> latLngList = new ArrayList<>();
    private LatLng latLngLastLocation;
    private LatLng latLngDestination;
    private Snackbar snackbarDistanceMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        ButterKnife.bind(this);
        initPresenter();
        onAttach();
        initGoogleMaps();
        EventBus.getDefault().register(this);
    }

    private void initGoogleMaps() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_maps_activity_my_location);
        supportMapFragment.getMapAsync(this);   // onMapReady

    }

    private void initPresenter() {
        myLocationActivityPresenter = new MyLocationActivityPresenter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left);
    }

    @Override
    public void onAttach() {
        myLocationActivityPresenter.onAttach(this);
    }

    @Override
    public void onDetach() {
        myLocationActivityPresenter.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);

        // add a marker location
        if (latLngLastLocation == null) {
            // load data gps
            Log.d(TAG, "load data gps");
            myLocationActivityPresenter.onLoadDataGps(this);
        } else {
            // set marker origin
            Log.d(TAG, "set marker location");
            addMarkerOrigin(latLngLastLocation);
        }
    }

    @Subscribe(sticky = true)
    public void onMessageEvent(LatLng latLngLastLocation) {
        this.latLngLastLocation = latLngLastLocation;
    }

    @Override
    public void loadDataGps(LatLng latLngLastLocation) {
        this.latLngLastLocation = latLngLastLocation;
        addMarkerOrigin(latLngLastLocation);
    }

    @Override
    protected void onResume() {
        myLocationActivityPresenter.startGpsService(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        myLocationActivityPresenter.stopGpsService();
        super.onStop();
    }

    private void addMarkerOrigin(LatLng latLngLastLocation) {
        latLngList.add(latLngLastLocation);
        googleMap.addMarker(new MarkerOptions()
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(latLngLastLocation)
        );
        googleMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(latLngLastLocation)
                                .zoom(16)
                                .build())
        );
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.latLngDestination = latLng;
        Log.d(TAG, "latLngOrigin: " + latLngLastLocation);
        Log.d(TAG, "latLngDestination: " + latLngDestination);

        // clear marker
        googleMap.clear();
        latLngList.clear();
        latLngList.add(latLngLastLocation);
        latLngList.add(latLngDestination);

        // set marker origin
        googleMap.addMarker(new MarkerOptions()
                .position(latLngLastLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("My Location")
        );

        // set marker destination
        googleMap.addMarker(new MarkerOptions()
                .position(latLngDestination)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Destination")
        );

        String strLatlngOrigin = latLngLastLocation.latitude + ", " + latLngLastLocation.longitude;
        String strLatlngDestination = latLngDestination.latitude + ", " + latLng.longitude;
        myLocationActivityPresenter.drawRouteMaps(strLatlngOrigin, strLatlngDestination);
    }

    @Override
    public void drawPolyline(PolylineOptions polylineOptions) {
        googleMap.addPolyline(polylineOptions);
    }

    @Override
    public void drawPolylineFailed() {
        Toast.makeText(this, "Draw polyline failed", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void refreshLocation(Location locationRefresh) {
        Log.d(TAG, "latLngList.size: " + latLngList.size());
        latLngLastLocation = new LatLng(locationRefresh.getLatitude(), locationRefresh.getLongitude());
        String strLatlngOrigin = latLngLastLocation.latitude + ", " + latLngLastLocation.longitude;
        String strLatlngDestination = latLngLastLocation.latitude + ", " + latLngLastLocation.longitude;
        if (latLngList.size() < 2) {
            Toast.makeText(this, "Please select destination location", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            Toast.makeText(this, "Location is updated", Toast.LENGTH_SHORT)
                .show();
        }
        myLocationActivityPresenter.drawRouteMaps(strLatlngOrigin, strLatlngDestination);
    }

    @Override
    public void showEnabledGps() {
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
    public void refreshLocationFailed() {
        Toast.makeText(this, "Refresh location is not updated", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void getDataDistanceMatrixFailed() {
        Toast.makeText(this, "Distance not loaded successfully", Toast.LENGTH_SHORT)
                .show();
        snackbarDistanceMatrix.dismiss();
    }

    @Override
    public void getDataDistanceMatrixSuccess(String distance, String duration) {
        snackbarDistanceMatrix = Snackbar.make(coordinatorLayoutContainerActivityMyLocation, "Distance and Duration: " + distance + " " + duration, Snackbar.LENGTH_INDEFINITE);
        snackbarDistanceMatrix.show();
        Log.d(TAG, "getDataDistanceMatrixSuccess");
    }
}
