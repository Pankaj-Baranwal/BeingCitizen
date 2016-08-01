package com.beingcitizen.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.adapters.DebateAdapter;
import com.beingcitizen.beingcitizen.MainActivity;
import com.beingcitizen.retrieveals.RetrieveAllDebates;

import org.json.JSONObject;

/**
 * Created by saransh on 19-06-2015.
 */
public class Debate extends Fragment {
    View rootView = null;
    private ListView userList;
    String uid = "16";
    SharedPreferences sharedpreferences;
    //String TITLES[] = {"Public Law & Order","Police","Public Health & Sanitation","Local Government","Communications – Roads & Bridges"
      //      ,"Water Supplies","Industries","Markets & Fairs","Trade & Commerce (within State)","State Taxes (Electricity, Land, Roads, Toll)"};
    MainActivity parent;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sharedpreferences.contains("id"))
            uid = sharedpreferences.getString("id", "16");
        else
            Toast.makeText(getContext(), "No UID stored", Toast.LENGTH_SHORT).show();
        if (rootView!=null) {
            userList = (ListView) rootView.findViewById(R.id.debate_listview);
            setHasOptionsMenu(true);
            parent.rl.setVisibility(View.VISIBLE);
            RetrieveAllDebates radb = new RetrieveAllDebates(Debate.this);
            radb.execute(uid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.alldebates, container, false);
        parent = (MainActivity)getActivity();
        return rootView;
    }

    public void functions(JSONObject s) {
        try {
            userList.setAdapter(new DebateAdapter(getContext(), s.getJSONArray("debates")));
            parent.rl.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG_ERROR", "ERROR   "+e.getMessage());
        }
    }
}
