package com.beingcitizen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.beingcitizen.R;
import com.beingcitizen.adapters.BlogsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 22-06-2015.
 *
 * Parent Fragment for blog list page. Loads and populates list of blogs.
 */
public class Blogs extends Fragment {
    private ListView userList;
    static JSONObject s;
    static boolean called = false;
    RelativeLayout rl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.allblogs, container, false);
        rl = (RelativeLayout) rootView.findViewById(R.id.progress_imageLoading);
        rl.setVisibility(View.VISIBLE);
        userList = (ListView) rootView.findViewById(R.id.blogs_listview);
        if (called) {
            try {
                if (s.getJSONArray("blog").length()!=0) {
                    JSONArray jA = new JSONArray();
                    if (s.getJSONArray("blog").length()>8){
                        int i =0;
                        while(i<8) {
                            jA.put(s.getJSONArray("blog").getJSONObject(i));
                            i++;
                        }
                    }else{
                        jA = s.getJSONArray("blog");
                    }
                    userList.setAdapter(new BlogsAdapter(getActivity(), jA));
                    rl.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    public static void function(JSONObject b){
        called = true;
        s = b;
    }
}
