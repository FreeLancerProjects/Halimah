package com.appzone.halimah.activities.nursery_details.fragment.fragment_details.fragment_details;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.fragments.fragment_home.Fragment_Slider;
import com.appzone.halimah.activities.nursery_details.fragment.fragment_details.Fragment_Location;
import com.appzone.halimah.activities.nursery_details.fragment.fragment_details.Fragment_Reserve;
import com.appzone.halimah.activities.nursery_details.fragment.fragment_details.Fragment_Service;
import com.appzone.halimah.adapters.TabSliderAdapter;
import com.appzone.halimah.adapters.TabViewPagerAdapter;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_Details extends Fragment {
    private static final String TAG="DATA";
    private ViewPager pager,pager_details;
    private TabLayout tab, tab_details;
    private TabViewPagerAdapter tabViewPagerAdapter;
    private TabSliderAdapter tabSliderAdapter;
    private Slider_Nursery_Model.NurseryModel nurseryModel;
    private TimerTask timerTask;
    private Timer timer;
    private TextView tv_distance,tv_name;
    private SimpleRatingBar rateBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Details getInstance(Slider_Nursery_Model.NurseryModel nurseryModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,nurseryModel);
        Fragment_Details fragment_details = new Fragment_Details();
        fragment_details.setArguments(bundle);
        return fragment_details;
    }
    private void initView(View view) {
        pager = view.findViewById(R.id.pager);
        pager_details = view.findViewById(R.id.pager_details);
        tab = view.findViewById(R.id.tab);
        tab_details = view.findViewById(R.id.tab_details);
        tv_name = view.findViewById(R.id.tv_name);
        tv_distance = view.findViewById(R.id.tv_distance);
        rateBar = view.findViewById(R.id.rateBar);

        tab.setupWithViewPager(pager);
        tab_details.setupWithViewPager(pager_details);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            nurseryModel = (Slider_Nursery_Model.NurseryModel) bundle.getSerializable(TAG);
            UpdateUI(nurseryModel);
        }

    }

    private void UpdateUI(final Slider_Nursery_Model.NurseryModel nurseryModel) {

        tv_name.setText(nurseryModel.getUser_full_name());
        tv_distance.setText(String.valueOf(Math.round(Double.parseDouble(nurseryModel.getDst())))+getString(R.string.km));
        SimpleRatingBar.AnimationBuilder  builder = rateBar.getAnimationBuilder();
        builder.setRepeatCount(0);
        builder.setInterpolator(new AccelerateInterpolator());
        builder.setRatingTarget((float)nurseryModel.getStars_num());
        builder.setRepeatMode(ValueAnimator.REVERSE);
        builder.setDuration(2000);
        builder.start();
        UpdateSliderUI(nurseryModel.getGallary());
        tabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager());
        tabViewPagerAdapter.AddFragment(Fragment_Service.getInstance(nurseryModel));
        tabViewPagerAdapter.AddFragment(Fragment_Reserve.getInstance(nurseryModel));
        tabViewPagerAdapter.AddFragment(Fragment_Location.getInstance(nurseryModel));
        tabViewPagerAdapter.AddTitle(getString(R.string.serv));
        tabViewPagerAdapter.AddTitle(getString(R.string.reservs));
        tabViewPagerAdapter.AddTitle(getString(R.string.loc));
        pager_details.setAdapter(tabViewPagerAdapter);

        pager_details.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2)
                {
                    Log.e("pager pos","ddddddddddddddddddddddd");
                    tabViewPagerAdapter.removePage(2,Fragment_Location.getInstance(nurseryModel),getString(R.string.loc));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void UpdateSliderUI(List<Slider_Nursery_Model.NurseryModel.GalleryModel> gallery) {
        tabSliderAdapter = new TabSliderAdapter(getChildFragmentManager());

        if (gallery.size()>0)
        {
            for (Slider_Nursery_Model.NurseryModel.GalleryModel galleryModel:gallery)
            {
                tabSliderAdapter.AddFragment(Fragment_Slider.getInstance(galleryModel.getPhoto_name()));
            }
            pager.setAdapter(tabSliderAdapter);

            if (gallery.size()>0)
            {
                if (gallery.size()>1)
                {
                    timerTask = new MyTimerTask();
                    timer = new Timer();
                    timer.scheduleAtFixedRate(timerTask,4000,4000);
                }
            }
        }else
            {
                tabSliderAdapter.AddFragment(Fragment_Slider.getInstance(nurseryModel.getUser_image()));
                pager.setAdapter(tabSliderAdapter);


            }


    }



    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
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
