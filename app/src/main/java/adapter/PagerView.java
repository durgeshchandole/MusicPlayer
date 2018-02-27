package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


/**
 * Created by Rebelute User on 4/26/2016.
 */

public class PagerView extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<Fragment> tabList;

    public PagerView(FragmentManager fm, ArrayList<Fragment> tabList, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabList = tabList;
    }

    @Override
    public Fragment getItem(int position) {
        return tabList.get(position);



    }

    @Override
    public int getCount() {
        return 1;
    }
}