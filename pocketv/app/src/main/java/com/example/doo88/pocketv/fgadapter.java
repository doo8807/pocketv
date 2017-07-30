package com.example.doo88.pocketv;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by doo88 on 2017-07-04.
 */

public class fgadapter extends FragmentPagerAdapter {
    private int PAGE_NUMBER =3;
    public fgadapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "VOD";
            case 1:
                return "LIVE";
            case 2:
                return "MY PAGE";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return record.newInstance();
            case 1:
                return live.newInstance();
            case 2:
                return info.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }
}
