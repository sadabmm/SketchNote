package com.example.smm.SketchNote.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by SMM on 28-Dec-17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> tabTitleList = new ArrayList<>();

    //Puts all the fragments in this array. Used inside instantiateItem and destroyItem override methods.
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Override
    public String getPageTitle(int position) {
        return tabTitleList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void addToFragment(Fragment fragment, String name){
        fragmentArrayList.add(fragment);
        tabTitleList.add(name);
    }
}