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

/**
 * Created by saransh on 21-06-2015.
 *
 * Fragment to actually fill up the menu items in campaigns section.
 */
public class CampaignCategory  extends Fragment {
    private ListView userList;

    Drawable drawable;
    String TITLES[] = {"Law and Order", "Public health and Sanitation","Communication"
    ,"Water Supplies","Lands, Agriculture", "Trade,Commerce,Employment","Environment and Horticulture", "Tourism, Art and Culture", "Power", "Corruption/Vigillance"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.campaign_category, container, false);
        userList = (ListView) rootView.findViewById(R.id.campaign_category_list);
        userList.setAdapter(new CategoryAdapter(getActivity().getApplicationContext(), TITLES));
        return rootView;
    }

    @Override
    public void onResume() {
        userList.setAdapter(new CategoryAdapter(getActivity(), TITLES));
        super.onResume();
    }
}
