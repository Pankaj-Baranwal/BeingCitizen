package com.beingcitizen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingcitizen.R;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 21-06-2015.
 *
 * Fragment for cartoons section. Loads cartoon of the day.
 */
public class Cartoons  extends Fragment {
    static JSONObject s;
    static boolean called = false;
    ProgressView image_loading;
    ImageView cartoon_image;

    //Drawable drawable;
    //String TITLES[] = {"Police","Public Law & Order"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.allcartoons_listcell, container, false);

        CardView cardView=(CardView) rootView.findViewById(R.id.CardView_allcartoons);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);

        if (called){
            TextView cartoon_title = (TextView)rootView.findViewById(R.id.cartoon_title);
            TextView cartoon_content= (TextView)rootView.findViewById(R.id.cartoon_text);
            image_loading = (ProgressView) rootView.findViewById(R.id.progress_imageLoading);
            image_loading.setVisibility(View.VISIBLE);
            cartoon_image = (ImageView)rootView.findViewById(R.id.cartoon_image);
            JSONObject t = null;
            try {
                int position = s.getJSONArray("cartoon").length()-1;
                t = s.getJSONArray("cartoon").getJSONObject(position);
                Picasso.with(getContext()).load("http://beingcitizen.com/uploads/cartoons/"+t.getString("carimage")+t.getString("carext")).resize(256, 256).into(cartoon_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        cartoon_image.setVisibility(View.VISIBLE);
                        image_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

                cartoon_title.setText(t.getString("Cartoon of the day"));
                cartoon_content.setText(t.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rootView;
    }

    //TODO: Implement onResume

    public static void function(JSONObject b)
    {
        called = true;
        s = b;
    }
}
