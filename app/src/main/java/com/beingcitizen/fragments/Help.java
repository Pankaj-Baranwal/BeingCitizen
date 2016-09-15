package com.beingcitizen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beingcitizen.R;

/**
 * Created by pankaj on 12/8/16.
 *
 * Loads up Help section.
 */
public class Help extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.help, container, false);
        return rootView;
    }
}
