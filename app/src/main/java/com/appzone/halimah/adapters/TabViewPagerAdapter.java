package com.appzone.halimah.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titlesList;
    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        titlesList = new ArrayList<>();
    }


    public void AddFragment(Fragment fragment)
    {
        this.fragmentList.add(fragment);
    }

    public void AddTitle(String title)
    {
        this.titlesList.add(title);
    }

    public void removePage(int pos,Fragment fragment,String title)
    {
        fragmentList.remove(pos);
        titlesList.remove(pos);
        notifyDataSetChanged();
        AddFragment(fragment);
        AddTitle(title);
        notifyDataSetChanged();

    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titlesList.get(position);
    }
}
