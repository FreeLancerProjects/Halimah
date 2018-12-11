package com.appzone.halimah.activities.nursery_details.fragment.fragment_details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.halimah.R;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Location extends Fragment implements OnMapReadyCallback{
    private static final String TAG="DATA";
    private Slider_Nursery_Model.NurseryModel nurseryModel;
    private Marker marker;
    private GoogleMap mMap;
    private float zoom = 15.6f;
    private SupportMapFragment fragment;
    private double lat=0.0,lng=0.0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Location getInstance(Slider_Nursery_Model.NurseryModel nurseryModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,nurseryModel);
        Fragment_Location fragment_location = new Fragment_Location();
        fragment_location.setArguments(bundle);
        return fragment_location;

    }
    private void initView(View view) {

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            nurseryModel = (Slider_Nursery_Model.NurseryModel) bundle.getSerializable(TAG);
            UpdateUI(nurseryModel);
        }

    }

    private void initMap()
    {
        if (fragment==null)
        {
            fragment = SupportMapFragment.newInstance();
            fragment.getMapAsync(this);

        }
        getChildFragmentManager().beginTransaction().replace(R.id.map,fragment).commit();

    }

    private void UpdateUI(Slider_Nursery_Model.NurseryModel nurseryModel) {
        lat = Double.parseDouble(nurseryModel.getUser_google_lat());
        lng = Double.parseDouble(nurseryModel.getUser_google_long());

        initMap();
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
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.maps));
            AddMarker(lat,lng);
        }
    }

    public void AddMarker(double lat, double lng) {
        Log.e("add_marker",lat+"_");
        if (mMap!=null)
        {
            if (marker==null)
            {
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromBitmap(getBitmap())));
            }else
            {
                marker.setPosition(new LatLng(lat,lng));
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),zoom));

        }

    }

    private Bitmap getBitmap(){

        Bitmap bitmap1 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.user_map_icon);
        int newWidth = 75;
        int newHeight = 75;
        float scaleWidth = ((float)newWidth/bitmap1.getWidth());
        float scaleHeight = ((float)newHeight/bitmap1.getHeight());

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(bitmap1,0,0,bitmap1.getWidth(),bitmap1.getHeight(),matrix,true);


    }


}
