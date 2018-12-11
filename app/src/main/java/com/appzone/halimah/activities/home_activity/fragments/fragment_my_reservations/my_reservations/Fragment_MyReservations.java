package com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.my_reservations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Client_Current_Reservations;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Client_New_Reservations;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Nursery_Current_Reservations;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Nursery_New_Reservations;
import com.appzone.halimah.adapters.TabViewPagerAdapter;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.tags.Tags;

public class Fragment_MyReservations extends Fragment {
    private UserModel userModel;
    private double lat=0.0,lng=0.0;
    private HomeActivity homeActivity;
    private TabLayout tab;
    private ViewPager pager;
    private TabViewPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reservations,container,false);
        initView(view);
        return view;
    }

    public static Fragment_MyReservations getInstance()
    {
        return new Fragment_MyReservations();
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        UpdateUi(userModel);
    }

    private void UpdateUi(UserModel userModel) {
        adapter = new TabViewPagerAdapter(getChildFragmentManager());
        adapter.AddTitle(getString(R.string.new_reservation));
        adapter.AddTitle(getString(R.string.current_reservation));

        if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
        {
            adapter.AddFragment(Fragment_Client_New_Reservations.getInstance(userModel));
            adapter.AddFragment(Fragment_Client_Current_Reservations.getInstance(userModel));

        }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
        {
            adapter.AddFragment(Fragment_Nursery_New_Reservations.getInstance(userModel));
            adapter.AddFragment(Fragment_Nursery_Current_Reservations.getInstance(userModel));
        }
        pager.setAdapter(adapter);
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setLat_Lng(double lat,double lng)
    {
        this.lat=lat;
        this.lng = lng;
    }


}
