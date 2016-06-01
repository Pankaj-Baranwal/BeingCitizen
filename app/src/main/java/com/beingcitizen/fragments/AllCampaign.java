package com.beingcitizen.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.adapters.CampaignAdapter;
import com.beingcitizen.beingcitizen.CreateCampaign;
import com.beingcitizen.retrieveals.RetrieveCampaign;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 22-06-2015.
 */
public class AllCampaign extends Fragment {
    private ListView userList;
    View rootView;
    //TODO: Get list from kaustobh bhaiya
//    String TITLES[] = {"Public Law & Order","Police","Public Health & Sanitation","Local Government","Communications – Roads & Bridges"
  //          ,"Water Supplies","Industries","Markets & Fairs","Trade & Commerce (within State)","State Taxes (Electricity, Land, Roads, Toll)", "", "", "", "", "", ""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.allcampaigns, container, false);
        userList = (ListView) rootView.findViewById(R.id.allcampaign_listview);
        RetrieveCampaign rtC = new RetrieveCampaign(AllCampaign.this);
        rtC.execute("");
        final FloatingActionMenu fab_menu = (FloatingActionMenu) rootView.findViewById(R.id.fab_main);
        FloatingActionButton fab_create_campaign = (FloatingActionButton) rootView.findViewById(R.id.fab_create_campaign);
        fab_create_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), CreateCampaign.class);
                startActivity(i);
                fab_menu.close(true);
            }
        });

        FloatingActionButton fab_add_hash = (FloatingActionButton) rootView.findViewById(R.id.fab_hash);
        fab_add_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogLogout = new Dialog(getActivity());
                dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLogout.setContentView(R.layout.addhash);

                Button update = (Button) dialogLogout.findViewById(R.id.hash_update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AllCampaign.this.getActivity(), "Update Clicked", Toast.LENGTH_SHORT).show();
                        fab_menu.close(true);
                    }
                });
                dialogLogout.show();
            }
        });

        fab_menu.setClosedOnTouchOutside(true);

        return rootView;
    }

    public void functions(JSONObject param){
        //TITLES = new String[param.length()];
        try {
            if (param.getJSONArray("campaigns").length()!=0) {
                userList.setAdapter(new CampaignAdapter(getActivity(), param.getJSONArray("campaigns")));
                Log.e("TAG_ADAPTER", param.getJSONArray("campaigns").length()+"");
            }
        }catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onResume() {
        RetrieveCampaign rtC = new RetrieveCampaign(AllCampaign.this);
        rtC.execute("");
        super.onResume();
    }
}
