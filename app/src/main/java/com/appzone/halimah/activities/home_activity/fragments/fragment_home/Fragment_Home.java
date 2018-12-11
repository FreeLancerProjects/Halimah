package com.appzone.halimah.activities.home_activity.fragments.fragment_home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.activities.nursery_details.activity.NurseryDetailsActivity;
import com.appzone.halimah.adapters.NurseryAdapter;
import com.appzone.halimah.adapters.TabSliderAdapter;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.singletone.UserSingleTone;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {
    private TabLayout tab;
    private ViewPager pager;
    private TabSliderAdapter tabSliderAdapter;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar;
    private TextView tv_no_nursery;
    public TimerTask timerTask;
    public Timer timer;
    private double lat=0.0,lng=0.0;
    private  List<Slider_Nursery_Model.SliderModel> sliderModelList;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private HomeActivity homeActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Home getInstance()
    {
        return new Fragment_Home();
    }
    private void initView(View view)
    {
        homeActivity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        tv_no_nursery = view.findViewById(R.id.tv_no_nursery);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setAdapter(adapter);

        if (userModel==null)
        {
            getSlider_Nurseries(lat,lng,"all");

        }else
            {
                getSlider_Nurseries(lat,lng,userModel.getUser_id());

            }
    }
    public void setLatLong(double lat,double lng)
    {

        if (userModel==null)
        {
            getSlider_Nurseries(lat,lng,"all");

        }else
        {
            getSlider_Nurseries(lat,lng,userModel.getUser_id());

        }
    }
    private void getSlider_Nurseries(double lat, double lng, String user_id)
    {
        Api.getService()
                .getSlider_Nursery(user_id,lat,lng)
                .enqueue(new Callback<Slider_Nursery_Model>() {
                    @Override
                    public void onResponse(Call<Slider_Nursery_Model> call, Response<Slider_Nursery_Model> response) {
                        if (response.isSuccessful())
                        {

                            progBar.setVisibility(View.GONE);
                            UpdateUI(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Slider_Nursery_Model> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdateUI(Slider_Nursery_Model slider_nursery_model) {
        sliderModelList = slider_nursery_model.getSlider();
        UpdateSliderUI(sliderModelList);
        UpdateNurseryAdapter(slider_nursery_model.getList());

    }



    private void UpdateSliderUI(List<Slider_Nursery_Model.SliderModel> sliderModelList) {


        if (sliderModelList.size()>0)
        {
            tabSliderAdapter = new TabSliderAdapter(getChildFragmentManager());

            if (sliderModelList.size()>1)
            {
                for (Slider_Nursery_Model.SliderModel sliderModel:sliderModelList)
                {
                    tabSliderAdapter.AddFragment(Fragment_Slider.getInstance(sliderModel.getShow_iamge()));
                }
                pager.setAdapter(tabSliderAdapter);
                timer = new Timer();
                timerTask  = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,4000,4000);
            }else {

                Log.e("sssssssss","ssssssssssss");
                for (Slider_Nursery_Model.SliderModel sliderModel:sliderModelList)
                {
                    tabSliderAdapter.AddFragment(Fragment_Slider.getInstance(sliderModel.getShow_iamge()));
                }
                pager.setAdapter(tabSliderAdapter);


            }
        }
    }

    private void UpdateNurseryAdapter(List<Slider_Nursery_Model.NurseryModel> nurseryModelList) {

        if (nurseryModelList.size()>0)
        {
            tv_no_nursery.setVisibility(View.GONE);
            adapter = new NurseryAdapter(getActivity(),nurseryModelList,this);
            recView.setAdapter(adapter);
        }else
            {
                tv_no_nursery.setVisibility(View.VISIBLE);

            }


    }

    public void setItem(Slider_Nursery_Model.NurseryModel nurseryModel) {
        Intent intent = new Intent(getActivity(), NurseryDetailsActivity.class);
        intent.putExtra("data",nurseryModel);
        startActivity(intent);
    }

    private class MyTimerTask extends TimerTask
    {

        @Override
        public void run() {
           homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager.getCurrentItem()<pager.getChildCount()-1)
                    {
                        pager.setCurrentItem(pager.getCurrentItem()+1);
                    }else
                        {
                            pager.setCurrentItem(0);
                        }



                }
            });
        }
    }

    @Override
    public void onDestroy() {
        if (timer!=null)
        {

            timer.cancel();
            timer.purge();
        }

        if (timerTask!=null)
        {
            timerTask.cancel();

        }

        super.onDestroy();

    }
}
