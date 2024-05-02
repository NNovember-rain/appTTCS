package vn.mrlongg71.ps09103_assignment.view.statistical;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import vn.mrlongg71.ps09103_assignment.R;
import vn.mrlongg71.ps09103_assignment.adapter.TablayoutStatisticsAdapter;
import vn.mrlongg71.ps09103_assignment.library.ActionBarLib;


public class StatisticalFragment extends Fragment {

    private ViewPager viewPager;
     TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical, container, false);
        initView(view);
        setActionBar();
        initTabLayout(view);
        return view;
    }
    private void setActionBar() {
        ActionBarLib.setSupportActionBar(getActivity(),getString(R.string.title_activity_home));
    }
    private void initTabLayout(View view) {
        TablayoutStatisticsAdapter tablayoutStatisticsAdapter = new TablayoutStatisticsAdapter(getChildFragmentManager(),view.getContext(),4);
        viewPager.setAdapter(tablayoutStatisticsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(getString(R.string.yesterday));
        tabLayout.getTabAt(1).setText(getString(R.string.day));
        tabLayout.getTabAt(2).setText(getString(R.string.month));
//        tabLayout.getTabAt(3).setText(getString(R.string.other));

    }

    private void initView(View view) {
        tabLayout= view.findViewById(R.id.tabLayout_sta);
        viewPager = view.findViewById(R.id.viewPager_sta);
    }
}