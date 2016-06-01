package com.beingcitizen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beingcitizen.R;
import com.beingcitizen.adapters.BlogsAdapter;
import com.beingcitizen.adapters.CampaignAdapter;

/**
 * Created by saransh on 22-06-2015.
 */
public class Blogs extends Fragment {
    private ListView userList;

    String TITLES[] = {"Public Law & Order","Police","Public Health & Sanitation","Local Government","Communications – Roads & Bridges"
            ,"Water Supplies","Industries","Markets & Fairs","Trade & Commerce (within State)","State Taxes (Electricity, Land, Roads, Toll)"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.allblogs, container, false);

        userList = (ListView) rootView.findViewById(R.id.blogs_listview);
        userList.setAdapter(new BlogsAdapter(getActivity(), TITLES));
        return rootView;
    }
    @Override
    public void onResume() {

        userList.setAdapter(new BlogsAdapter(getActivity(), TITLES));
        super.onResume();
    }
}
