package com.marcinmejner.instaclone.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marc on 18.03.2018.
 */

public class SectionStagePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final HashMap<Fragment, Integer> fragments = new HashMap<>();
    private final HashMap<String, Integer> fragmentNumbers = new HashMap<>();
    private final HashMap<Integer, String> fragmentNames = new HashMap<>();


    public SectionStagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String fragmentName) {
        fragmentList.add(fragment);
        fragments.put(fragment, fragmentList.size() - 1);
        fragmentNumbers.put(fragmentName, fragmentList.size() - 1);
        fragmentNames.put(fragmentList.size() - 1, fragmentName);
    }


    public Integer getFragmentNumber(String fragmentName) {

        if (fragmentNumbers.containsKey(fragmentName)) {
            return fragmentNumbers.get(fragmentName);
        } else {
            return null;
        }
    }

    public Integer getFragmentNumber(Fragment fragment) {

        if (fragmentNumbers.containsKey(fragment)) {
            return fragmentNumbers.get(fragment);
        } else {
            return null;
        }
    }

    public String getFragmentName(Integer fragmentNumber) {

        if (fragmentNames.containsKey(fragmentNumber)) {
            return fragmentNames.get(fragmentNumber);
        } else {
            return null;
        }
    }
}
