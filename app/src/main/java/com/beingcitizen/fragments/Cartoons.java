package com.beingcitizen.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beingcitizen.R;
import com.beingcitizen.adapters.CartoonsAdapter;
import com.beingcitizen.adapters.CategoryAdapter;

/**
 * Created by saransh on 21-06-2015.
 */
public class Cartoons  extends Fragment {
    private ListView userList;

    Drawable drawable;
    String TITLES[] = {"Police","Public Law & Order"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.allcartoons, container, false);
        userList = (ListView) rootView.findViewById(R.id.cartoons_listview);
        userList.setAdapter(new CartoonsAdapter(getActivity(), TITLES));
        return rootView;
    }

    @Override
    public void onResume() {

        userList.setAdapter(new CartoonsAdapter(getActivity(), TITLES));
        super.onResume();
    }
}
