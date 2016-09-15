package com.beingcitizen.fragments;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.MainActivity;
import com.beingcitizen.retrieveals.RetrieveDailyDigest;
import com.beingcitizen.tab.SlidingTabLayout;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by Pankaj Baranwal on 24-07-2016.
 *
 * Parent Fragment for daily digest section. Loads and further sends to individual fragments
 * details of polls, blogs, and cartoon.
 */

public class DailyDigest extends Fragment implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private SlidingTabLayout mTabs;
    String uid = "16";
    static boolean called = false;
    View view;
    MainActivity parent;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        parent.rl.setVisibility(View.VISIBLE);
        RetrieveDailyDigest rdd = new RetrieveDailyDigest(this);
        uid = sp.getString("id", "16");
        rdd.execute(uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.viewpager_main1, container, false);
        //mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);

        /*mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.opaque_red);
            }
        });*/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        parent = (MainActivity)getActivity() ;
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);

        //set tab strip backgroung color (grey)
        mTabs.setBackgroundColor(0xFF222222);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    public void functions(JSONObject s) {
        called = true;
        Polls.function(s);
        Blogs.function(s);
        Cartoons.function(s);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabs.setViewPager(mViewPager);
        parent.rl.setVisibility(View.GONE);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                // Open FragmentTab1.java
                case 0:
                    return new Polls();

                // Open FragmentTab2.java
                case 1:
                    return new Blogs();
                // Open FragmentTab3.java
                case 2:
                    return new Cartoons();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}
