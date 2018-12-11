package com.appzone.halimah.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.appzone.halimah.models.LocationModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

public class LocationUpdateService extends Service implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        initGoogleApiClient();
    }

    private synchronized void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        StartLocationUpdate();
            }

    private void StartLocationUpdate() {
        initLocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
        }
        Log.e("s","sffffffffffffff");
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5*60000);
        locationRequest.setFastestInterval(5*60000);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("service_lat",location.getLatitude()+"_");
        Log.e("service_lng",location.getLongitude()+"_");
        EventBus.getDefault().post(new LocationModel(location.getLatitude(),location.getLongitude()));

    }

    @Override
    public void onDestroy() {
        if (googleApiClient!=null&&locationCallback!=null&&googleApiClient.isConnected())
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
        }
        super.onDestroy();

    }
}
