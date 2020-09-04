package com.glg.mygif.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: FragmentViewPagerAdapter
 * @author: gao
 * @date: 2020/8/4 19:12
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public FragmentViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        fragments = mFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getClass().getSimpleName();
    }
}
