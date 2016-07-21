package com.beingcitizen.fragments;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.SendPollResult;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
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
    PieChart pieChart;

    LinearLayout yes, no, other;
    boolean pollable = true;
    TextView poll_title;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String uid =  sharedpreferences.getString("id", "16");
        getDetails();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollable) {
                    SendPollResult spr = new SendPollResult(Polls.this);
                    spr.execute(uid, pid, "yes");
                    Toast.makeText(getActivity(), "  Polled!  ", Toast.LENGTH_SHORT).show();
                }else{
                    if (poll_title.getText().toString().contentEquals("No Polls Found!")) {
                        Toast.makeText(getActivity(), "  Cannot poll!  ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "  Already polled!  ", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollable) {
                    SendPollResult spr = new SendPollResult(Polls.this);
                    spr.execute(uid, pid, "no");
                    Toast.makeText(getActivity(), "  Polled!  ", Toast.LENGTH_SHORT).show();
                }else {
                    if (poll_title.getText().toString().contentEquals("No Polls Found!")) {
                        Toast.makeText(getActivity(), "  Cannot poll!  ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "  Already polled!  ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollable) {
                    SendPollResult spr = new SendPollResult(Polls.this);
                    spr.execute(uid, pid, "other");
                    Toast.makeText(getActivity(), "  Polled!  ", Toast.LENGTH_SHORT).show();
                }else {
                    if (poll_title.getText().toString().contentEquals("No Polls Found!")) {
                        Toast.makeText(getActivity(), "  Cannot poll!  ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "  Already polled!  ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.allpolls_listcell, container, false);

        cardView=(CardView) rootView.findViewById(R.id.CardView_allpolls);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);

        pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        yes = (LinearLayout) rootView.findViewById(R.id.btn_yes);
        other = (LinearLayout) rootView.findViewById(R.id.btn_other);
        no = (LinearLayout) rootView.findViewById(R.id.btn_no);

        return rootView;
    }

    void getDetails() {
        if (called) {
            poll_title = (TextView) rootView.findViewById(R.id.poll_title);
            TextView poll_content = (TextView) rootView.findViewById(R.id.poll_content);
            JSONObject t;
            try {
                if (s.getJSONArray("poll").length() > 0) {
                    t = s.getJSONArray("poll").getJSONObject(0);
                    Log.e("POLL", s.getJSONObject("pie").length() + "");
                    if (s.getJSONObject("pie").length() > 1) {
                        pollable = false;
                        float dv1 = 0, dv2 = 0, dv3 = 0;
                        try {
                            dv1 = Float.parseFloat(s.getJSONObject("pie").getString("dv1")) / 100;
                            dv2 = Float.parseFloat(s.getJSONObject("pie").getString("dv2")) / 100;
                            dv3 = Float.parseFloat(s.getJSONObject("pie").getString("dv3")) / 100;
                            pieChart.setDrawValueInPie(true);
                            pieChart.addPieSlice(new PieModel("YES", Math.round(dv1 * 100), Color.parseColor("#B111FF1D")));
                            pieChart.addPieSlice(new PieModel("NO", Math.round(dv2 * 100), Color.parseColor("#b1ff553a")));
                            pieChart.addPieSlice(new PieModel("Other", Math.round(dv3 * 100), Color.parseColor("#b1111Dff")));
                            pieChart.setDrawValueInPie(true);
                            pieChart.startAnimation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        TextView yes_txt = (TextView) rootView.findViewById(R.id.yes_text);
                        TextView no_txt = (TextView) rootView.findViewById(R.id.no_text);
                        TextView other_txt = (TextView) rootView.findViewById(R.id.other_text);
                        yes_txt.setText(Math.round(dv1 * 100) + "%");
                        no_txt.setText(Math.round(dv2 * 100) + "%");
                        other_txt.setText(Math.round(dv3 * 100) + "%");
                        no_txt.setVisibility(View.VISIBLE);
                        yes_txt.setVisibility(View.VISIBLE);
                        other_txt.setVisibility(View.VISIBLE);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            0, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    params.weight = dv1;
//                    yes.setLayoutParams(params);
//                    params.weight = dv2;
//                    no.setLayoutParams(params);
//                    params.weight = dv3;
//                    other.setLayoutParams(params);

                    }
                    poll_title.setText(t.getString("poll_title"));
                    poll_content.setText(t.getString("poll_description"));
                    pid = t.getString("poll_id");
                }
                else{
                    pollable = false;
                    poll_title.setText("No Polls Found!");
                }
                }catch(JSONException e){
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
        pollable = false;
    }
}