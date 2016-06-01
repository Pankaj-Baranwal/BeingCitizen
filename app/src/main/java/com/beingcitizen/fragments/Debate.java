package com.beingcitizen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beingcitizen.R;
import com.beingcitizen.adapters.DebateAdapter;
import com.beingcitizen.retrieveals.RetrieveAllDebates;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 19-06-2015.
 */
public class Debate extends Fragment {
    private ListView userList;
    //String TITLES[] = {"Public Law & Order","Police","Public Health & Sanitation","Local Government","Communications – Roads & Bridges"
      //      ,"Water Supplies","Industries","Markets & Fairs","Trade & Commerce (within State)","State Taxes (Electricity, Land, Roads, Toll)"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.alldebates, container, false);
        userList = (ListView) rootView.findViewById(R.id.debate_listview);
        RetrieveAllDebates radb = new RetrieveAllDebates(Debate.this);
        radb.execute("16");
        return rootView;
    }
    @Override
    public void onResume() {

        RetrieveAllDebates radb = new RetrieveAllDebates(Debate.this);
        radb.execute("16");
        super.onResume();
    }

    public void functions(JSONObject s) {
        try {
            userList.setAdapter(new DebateAdapter(getActivity(), s.getJSONArray("debates")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
