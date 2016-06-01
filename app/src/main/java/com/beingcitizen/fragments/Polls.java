package com.beingcitizen.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beingcitizen.R;
import com.beingcitizen.adapters.CategoryAdapter;
import com.beingcitizen.adapters.PollsAdapter;

/**
 * Created by saransh on 21-06-2015.
 */
public class Polls  extends Fragment {
    private ListView userList;

    Drawable drawable;
    String TITLES[] = {"Public Law & Order","Police","Public Health & Sanitation","Local Government","Communications – Roads & Bridges"
            ,"Water Supplies","Industries","Markets & Fairs","Trade & Commerce (within State)","State Taxes (Electricity, Land, Roads, Toll)"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.allpolls, container, false);
        userList = (ListView) rootView.findViewById(R.id.polls_listview);
        userList.setAdapter(new PollsAdapter(getActivity(), TITLES));
        return rootView;
    }

    @Override
    public void onResume() {

        userList.setAdapter(new PollsAdapter(getActivity(), TITLES));
        super.onResume();
    }
}
