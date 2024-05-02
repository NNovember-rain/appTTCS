package vn.mrlongg71.ps09103_assignment.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import vn.mrlongg71.ps09103_assignment.view.statistical.day.StaDayFragment;
import vn.mrlongg71.ps09103_assignment.view.statistical.month.StaMonthFragment;
import vn.mrlongg71.ps09103_assignment.view.statistical.other.StaOtherFragment;
import vn.mrlongg71.ps09103_assignment.view.statistical.yesterday.StaYesterdayFragment;

public class TablayoutStatisticsAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;

    public TablayoutStatisticsAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StaYesterdayFragment staYesterdayFragment = new StaYesterdayFragment();
                return staYesterdayFragment;

            case 1:
                StaDayFragment staDayFragment = new StaDayFragment();
                return staDayFragment;
            case 2:
                StaMonthFragment staMonthFragment = new StaMonthFragment();
                return staMonthFragment;
            case 3:
                StaOtherFragment staOtherFragment = new StaOtherFragment();
                return staOtherFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
