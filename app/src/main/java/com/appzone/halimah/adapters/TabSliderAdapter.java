package com.appzone.halimah.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabSliderAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    public TabSliderAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }


    public void AddFragment(Fragment fragment)
    {
        this.fragmentList.add(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
