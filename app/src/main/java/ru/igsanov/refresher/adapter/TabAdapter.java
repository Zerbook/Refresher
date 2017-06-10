package ru.igsanov.refresher.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import ru.igsanov.refresher.fragment.CurrentTaskFragment;
import ru.igsanov.refresher.fragment.DoneTaskFragment;

/**
 * Created by Zver on 10.06.2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;

    public TabAdapter(FragmentManager fm,int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return  new CurrentTaskFragment();
            case 1:
                return new DoneTaskFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
