package com.appzone.halimah.activities.sign_up_activity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.sign_in_activity.SignInActivity;
import com.appzone.halimah.activities.sign_up_activity.fragments.Fragment_Client_SignUp;
import com.appzone.halimah.activities.sign_up_activity.fragments.Fragment_Nursery_SignUp;
import com.appzone.halimah.models.LocationModel;
import com.appzone.halimah.service.LocationUpdateService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback{

    private String type="";
    private ImageView image_back,image_close_sheet;
    private FloatingActionButton fab;
    private EditText edt_address;
    private Intent intentService=null;
    private Fragment_Client_SignUp fragment_client_signUp;
    private Fragment_Nursery_SignUp fragment_nursery_signUp;
    private final int gps_req = 102,loc_req=103;
    private final String fineLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private BottomSheetBehavior behavior;
    private View root;
    private Marker marker;
    private final float zoom =15.6f;
    private GoogleMap mMap;
    private double lat=0.0,lng=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        getDataFromIntent();
        CheckPermission();
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
    }

    private void initView()
    {

        image_back  = findViewById(R.id.image_back);
        image_close_sheet = findViewById(R.id.image_close_sheet);
        fab = findViewById(R.id.fab);
        edt_address = findViewById(R.id.edt_address);

        root = findViewById(R.id.root);
        behavior = BottomSheetBehavior.from(root);
        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_back.setRotation(180f);
            image_close_sheet.setRotation(180f);

        }
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        image_close_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState==BottomSheetBehavior.STATE_DRAGGING)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_address = edt_address.getText().toString();
                if (TextUtils.isEmpty(m_address))
                {
                    edt_address.setError(getString(R.string.address_req));
                }else
                    {
                        edt_address.setError(null);
                        if (fragment_nursery_signUp!=null&&fragment_nursery_signUp.isAdded())
                        {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            fragment_nursery_signUp.UpdateLocation_Data(m_address,lat,lng);
                        }
                    }
            }
        });



    }


    public void showBottomSheet()
    {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initMap()
    {
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }



    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            type = intent.getStringExtra("type");
            updateUI(type);
        }
    }

    private void updateUI(String type)
    {
        if (type.equals("member"))
        {
            fragment_client_signUp = Fragment_Client_SignUp.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_container, fragment_client_signUp).commit();
        }else if (type.equals("nursery"))
        {
            fragment_nursery_signUp = Fragment_Nursery_SignUp.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_sign_up_container, fragment_nursery_signUp).commit();

        }
    }

    private void CheckPermission()
    {
        if (ContextCompat.checkSelfPermission(this,fineLoc)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] perm = {fineLoc};
            ActivityCompat.requestPermissions(this,perm,loc_req);
        }else
        {
            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
            {
                CreateGpsDialog();
            }
        }
    }

    private boolean isGpsOpen()
    {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manager!=null&&manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }

        return false;
    }

    private void OpenGps()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,gps_req);
    }

    private void CreateGpsDialog()
    {
        final AlertDialog gps_dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(R.string.app_open_gps);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps_dialog.dismiss();
                OpenGps();
            }
        });

        gps_dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog;
        gps_dialog.setView(view);
        gps_dialog.setCanceledOnTouchOutside(false);
        gps_dialog.show();

    }
    private void StartLocationUpdate()
    {
        intentService = new Intent(this, LocationUpdateService.class);
        startService(intentService);
    }
    private void StopLocationUpdate()
    {
        if (intentService!=null)
        {
            stopService(intentService);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateLocation(LocationModel locationModel)
    {
        if (fragment_client_signUp!=null)
        {
            fragment_client_signUp.Location(locationModel.getLat(),locationModel.getLng());
        }else if (fragment_nursery_signUp!=null)
        {
            Log.e("ss","sss");
            //fragment_nursery_signUp.Location(locationModel.getLat(),locationModel.getLng());
            lat =locationModel.getLat();
            lng = locationModel.getLng();
            initMap();
        }
    }



    @Override
    public void onBackPressed() {
        if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else
            {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode==gps_req)
        {
            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
            {
                CreateGpsDialog();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode==loc_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (isGpsOpen())
                    {
                        StartLocationUpdate();
                    }else
                    {
                        CreateGpsDialog();
                    }
                }else
                {
                    Toast.makeText(this, R.string.acc_loc_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap!=null)
        {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    marker.setPosition(latLng);
                }
            });

            AddMarker(lat,lng);

        }
    }

    private void AddMarker(double lat, double lng) {
        Log.e("laaaaaaaaaatShet",lat+"_");
        if (marker==null)
        {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromBitmap(getBitmap())));
        }else
        {
            marker.setPosition(new LatLng(lat,lng));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),zoom));
    }

    private Bitmap getBitmap(){

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.user_map_icon);
        int newWidth = 75;
        int newHeight = 75;
        float scaleWidth = ((float)newWidth/bitmap1.getWidth());
        float scaleHeight = ((float)newHeight/bitmap1.getHeight());

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(bitmap1,0,0,bitmap1.getWidth(),bitmap1.getHeight(),matrix,true);


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        if (intentService!=null)
        {
            StopLocationUpdate();
        }
    }




}
