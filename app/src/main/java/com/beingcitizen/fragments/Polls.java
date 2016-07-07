package com.beingcitizen.fragments;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.SendPollResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 21-06-2015.
 */
public class Polls  extends Fragment {
    CardView cardView;
    static JSONObject s;
    static boolean called = false;
    String pid="1";
    View rootView;
    LinearLayout yes, no;
    boolean pollable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.allpolls_listcell, container, false);

        cardView=(CardView) rootView.findViewById(R.id.CardView_allpolls);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid =  sharedpreferences.getString("id", "16");

        yes = (LinearLayout) rootView.findViewById(R.id.btn_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollable) {
                    SendPollResult spr = new SendPollResult(Polls.this);
                    spr.execute(uid, pid, "yes");
                    Toast.makeText(getActivity(), "  Polled!  ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "  Already polled!  ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        no = (LinearLayout) rootView.findViewById(R.id.btn_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollable) {
                    SendPollResult spr = new SendPollResult(Polls.this);
                    spr.execute(uid, pid, "no");
                    Toast.makeText(getActivity(), "  Polled!  ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "  Already polled!  ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getDetails();

        return rootView;
    }

    void getDetails(){
        if (called){
            TextView poll_title = (TextView)rootView.findViewById(R.id.poll_title);
            TextView poll_content= (TextView)rootView.findViewById(R.id.poll_content);
            JSONObject t;
            try {
                t = s.getJSONArray("poll").getJSONObject(0);
                if (s.getJSONObject("pie").length()>1) {
                    pollable = false;
                    float dv1 = 0, dv2 = 0;
                    try {
                        dv1 = Float.parseFloat(s.getJSONObject("pie").getString("dv1")) / 100;
                        dv2 = Float.parseFloat(s.getJSONObject("pie").getString("dv2")) / 100;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = Math.round(dv1);
                    yes.setLayoutParams(params);
                    params.weight = Math.round(dv2);
                    no.setLayoutParams(params);
                    TextView yes_txt = (TextView) rootView.findViewById(R.id.yes_text);
                    TextView no_txt = (TextView) rootView.findViewById(R.id.no_text);
                    yes_txt.setText(Math.round(dv1 * 100) + "");
                    no_txt.setText(Math.round(dv2 * 100) + "");
                    Log.e("pers", yes_txt.getText().toString());
                    Log.e("pers", no_txt.getText().toString());
                    no_txt.setVisibility(View.VISIBLE);
                    yes_txt.setVisibility(View.VISIBLE);
                }
                poll_title.setText(t.getString("poll_title"));
                poll_content.setText(t.getString("poll_description"));
                pid = t.getString("poll_id");
            } catch (JSONException e) {
                Log.e("TAG_ERROR", "Polls error");
                e.printStackTrace();
            }
        }
    }

    public static void function(JSONObject b){
        s = b;
        called = true;
    }

    public void poll_function(JSONObject param) {
        getDetails();
    }
}