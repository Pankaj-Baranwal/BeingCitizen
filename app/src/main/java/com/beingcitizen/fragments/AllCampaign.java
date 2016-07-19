package com.beingcitizen.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.adapters.CampaignAdapter;
import com.beingcitizen.beingcitizen.CreateCampaign;
import com.beingcitizen.interfaces.adapterUpdate;
import com.beingcitizen.interfaces.retrieveCamp;
import com.beingcitizen.interfaces.retrieveCampaign;
import com.beingcitizen.retrieveals.RetrieveCampaign;
import com.beingcitizen.retrieveals.SendHashTag;
import com.facebook.FacebookSdk;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by saransh on 22-06-2015.
 */
public class AllCampaign extends Fragment implements retrieveCampaign, retrieveCamp, adapterUpdate {
    private ListView userList;
    View rootView;
    String uid = "16", pinCode= "110007";

    String consti="New Delhi";
    CampaignAdapter cA;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        facebookSDKInitialize();
        userList = (ListView) rootView.findViewById(R.id.allcampaign_listview);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        uid = sp.getString("id", "16");
        RetrieveCampaign rtC = new RetrieveCampaign(AllCampaign.this);
        rtC.execute(uid);
        final FloatingActionMenu fab_menu = (FloatingActionMenu) rootView.findViewById(R.id.fab_main);
        FloatingActionButton fab_create_campaign = (FloatingActionButton) rootView.findViewById(R.id.fab_create_campaign);
        fab_create_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateCampaign.class);
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
                Spinner tags = (Spinner) dialogLogout.findViewById(R.id.tags_spinner);
                final String[] feed_tag = {""};
                tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position!=0){
                            feed_tag[0] = parent.getItemAtPosition(position).toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final EditText feed = (EditText)dialogLogout.findViewById(R.id.feed);
                Button update = (Button) dialogLogout.findViewById(R.id.hash_update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag_txt = feed.getText().toString();
                        String feed_txt = feed_tag[0];
                        if (tag_txt.length()==0 || feed_txt.length()==0){
                            Toast.makeText(getContext(), "INVALID ENTRIES", Toast.LENGTH_SHORT).show();
                        }else {
                            SendHashTag sht = new SendHashTag(AllCampaign.this);
                            sht.execute(uid, tag_txt, feed_txt);
                            dialogLogout.cancel();
                            fab_menu.close(true);
                        }
                    }
                });
                dialogLogout.show();
            }
        });

        fab_menu.setClosedOnTouchOutside(true);
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
//        CallbackManager callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.allcampaigns, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        RetrieveCampaign rtC = new RetrieveCampaign(AllCampaign.this);
        rtC.execute(uid);
        super.onResume();
    }

    @Override
    public void constituency(JSONObject s) {
        JSONArray namearray;
        try {
            namearray = s.getJSONObject("const").names();
            consti = namearray.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void functions_tag(JSONObject s) {
        Toast.makeText(getContext(), "Feed sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void retrieve(JSONObject param) {
        if (param!=null) {
            try {
                if (param.getJSONArray("campaigns") != null && param.getJSONArray("campaigns").length() != 0) {
                    JSONArray jA;
                    JSONArray jASorted = new JSONArray();
                    JSONArray jAExtra = new JSONArray();
                    jA = param.getJSONArray("campaigns");
                    for (int i = 0; i < jA.length(); i++) {
                        if (jA.getJSONObject(i).getString("cconstituency").contentEquals(consti)) {
                            jASorted.put(jA.getJSONObject(i));
                        } else
                            jAExtra.put(jA.getJSONObject(i));
                    }
                    jA = new JSONArray();
                    for (int i = 0; i < jASorted.length(); i++) {
                        jA.put(jASorted.getJSONObject(i));
                    }
                    for (int i = 0; i < jAExtra.length(); i++) {
                        jA.put(jAExtra.getJSONObject(i));
                    }
                    JSONArray jB = new JSONArray();
                    for (int i =0; i<(jA.length()>10?10:jA.length()); i++){
                        jB.put(jA.getJSONObject(i));
                    }
                    //
                    cA = new CampaignAdapter(getContext(), jB, this);
                    userList.setAdapter(cA);
                    cA.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(getActivity(), "Error in connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapt(JSONArray jA) {
        if (jA!=null) {
            if (cA.categorynam!=null) {
                cA.categorynam = jA;
                cA.notifyDataSetChanged();
            }
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            pinCode = locationAddress;
        }
    }
}