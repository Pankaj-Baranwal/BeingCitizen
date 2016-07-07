package com.beingcitizen.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 21-06-2015.
 */
public class Cartoons  extends Fragment {
    private ListView userList;
    static JSONObject s;
    static boolean called = false;

    //Drawable drawable;
    //String TITLES[] = {"Police","Public Law & Order"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.allcartoons_listcell, container, false);

        CardView cardView=(CardView) rootView.findViewById(R.id.CardView_allcartoons);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        if (called){
            TextView cartoon_title = (TextView)rootView.findViewById(R.id.cartoon_title);
            TextView cartoon_content= (TextView)rootView.findViewById(R.id.cartoon_text);
            ImageView cartoon_image = (ImageView)rootView.findViewById(R.id.cartoon_image);
            JSONObject t = null;
            try {
/*                Glide.with(this)
                        .load("http://beingcitizen.com/uploads/cartoons/"+t.getString("carimage")+t.getString("carext"))
                        .into(cartoon_image);
                        */
                int position = s.getJSONArray("cartoon").length()-1;
                t = s.getJSONArray("cartoon").getJSONObject(position);
                Picasso.with(getContext()).load("http://beingcitizen.com/uploads/cartoons/"+t.getString("carimage")+t.getString("carext")).resize(256, 256).into(cartoon_image);

                cartoon_title.setText(t.getString("Cartoon of the day"));
                cartoon_content.setText(t.getString("description"));
                //imgLoad.setImage("http://beingcitizen.com/bc/index.php/uploads/display/"+t.getString("carimage")+t.getString("carext"), cartoon_image);
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

    public void cartoon_function(JSONObject s) {
        Toast.makeText(getActivity(), " Liked! ", Toast.LENGTH_SHORT).show();
    }
}
