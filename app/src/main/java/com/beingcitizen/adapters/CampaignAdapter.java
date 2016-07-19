package com.beingcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.beingcitizen.CommentActivity;
import com.beingcitizen.fragments.AllCampaign;
import com.beingcitizen.interfaces.CampaignRelated;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 23-06-2015.
 */
public class CampaignAdapter extends BaseAdapter implements CampaignRelated{
    private Context mContext;
    public JSONArray categorynam;
    String campaign_id="30", uid = "16";
    AllCampaign alc;
    boolean nulling = false;
    CardView cardView;
    String foll="null", volun="null";
    boolean volunteerable = true, likeable = true, vols = true, fols = true;

    public CampaignAdapter(Context context, JSONArray categoryname, AllCampaign alc) {
        if (context!=null) {
            nulling = false;
            mContext = context;
            this.categorynam = categoryname;
            this.alc = alc;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            uid = sp.getString("id", "16");
        }else {
            nulling = true;
            categorynam = new JSONArray();
        }
    }
    @Override
    public int getCount() {
        if (!nulling)
            return categorynam.length();
        return 0;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.allcampaigns_listcell, parent, false);
        String url_img="";
        cardView=(CardView) rowView.findViewById(R.id.CardView_allcampaign);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        final ProgressView image_loading = (ProgressView) rowView.findViewById(R.id.progress_imageLoading);
        final ImageButton like = (ImageButton) rowView.findViewById(R.id.like);
        final ImageButton volunteer = (ImageButton) rowView.findViewById(R.id.volunteer);
        LinearLayout verification = (LinearLayout)rowView.findViewById(R.id.verification_lL);
        TextView title = (TextView) rowView.findViewById(R.id.title);
//        ImageButton comment = (ImageButton) rowView.findViewById(R.id.comment);
        final ImageView camp_img = (ImageView) rowView.findViewById(R.id.image_campaign);
//        final ImageView like = (ImageView) rowView.findViewById(R.id.like);
        final ImageView comment = (ImageView) rowView.findViewById(R.id.comment);
        TextView city_name=(TextView)rowView.findViewById(R.id.city_name);
        TextView info_campaign=(TextView)rowView.findViewById(R.id.info_campaign);
        TextView category=(TextView)rowView.findViewById(R.id.category);
        ImageView category_img = (ImageView) rowView.findViewById(R.id.category_img);
//        CircleImageView civ = (CircleImageView) rowView.findViewById(R.id.circleImageView);
        if (!nulling)
        try {
            url_img = "http://beingcitizen.com/uploads/campaigns/" + categorynam.getJSONObject(position).getString("image") + categorynam.getJSONObject(position).getString("ext");
            Picasso.with(mContext).load(url_img).resize(256, 256).into(camp_img, new Callback() {
                @Override
                public void onSuccess() {
                    camp_img.setVisibility(View.VISIBLE);
                    image_loading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
            if (categorynam.getJSONObject(position).getString("c_id").contentEquals(campaign_id+"")){
                likeable = true;
                like.setBackgroundResource(R.drawable.like_on);
            }else {
                likeable = false;
                like.setBackgroundResource(R.drawable.like);
            }
            if (categorynam.getJSONObject(position).getString("camid").contentEquals(campaign_id+"")){
                volunteerable = true;
                volunteer.setBackgroundResource(R.drawable.volunteer_on);
            }else {
                volunteerable = true;
                volunteer.setBackgroundResource(R.drawable.volunteer);
            }
            String status = categorynam.getJSONObject(position).getString("status");
            if (status.contentEquals("1")){
                verification.setBackgroundColor(0xB1FF0000);
            }else if (status.contentEquals("2")){
                verification.setBackgroundColor(0xB10000FF);
            }
            else{
                verification.setBackgroundColor(0xB111FF1D);
            }
            fols = categorynam.getJSONObject(position).getString("followable").contentEquals("1")?true:false;
            vols = categorynam.getJSONObject(position).getString("volunteerable").contentEquals("1")?true:false;
            if (!fols){
                like.setVisibility(View.GONE);
            }
            if (!vols){
                volunteer.setVisibility(View.GONE);
            }
            foll = categorynam.getJSONObject(position).getString("c_id");
            volun =categorynam.getJSONObject(position).getString("camid");
            campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
            title.setText(categorynam.getJSONObject(position).getString("cname"));
            city_name.setText(categorynam.getJSONObject(position).getString("cconstituency"));
            String campaign_text = categorynam.getJSONObject(position).getString("campaign_text");
            if (campaign_text.length()>200)
                campaign_text=campaign_text.substring(0, 200);
            info_campaign.setText(campaign_text);
            String cat = categorynam.getJSONObject(position).getString("category");
            category.setText(cat.length()<22?cat:cat.substring(0, 22)+"...");
            switch (cat){
                case "Law and Order":
                    category_img.setImageResource(R.drawable.public_law_and_order_black);
                    break;
                case "Public Health and Sanitation":
                    category_img.setImageResource(R.drawable.public_health_and_sanitation_black);
                    break;
                case "Communication":
                    category_img.setImageResource(R.drawable.debates);
                    break;
                case "Water-Irrigation,Drainage,Embankments":
                    category_img.setImageResource(R.drawable.water_supplies_black);
                    break;
                case "Lands, Agriculture":
                    category_img.setImageResource(R.drawable.ecology);
                    break;
                case "Trade,Commerce,Employment":
                    category_img.setImageResource(R.drawable.market);
                    break;
                case "Environment and Holticulture":
                    category_img.setImageResource(R.drawable.ecology);
                    break;
                case "Tourism, Art and Culture":
                    category_img.setImageResource(R.drawable.market);
                    break;
                case "Power":
                    category_img.setImageResource(R.drawable.power);
                    break;
                case "Corruption/Vigillance":
                    category_img.setImageResource(R.drawable.police_black);
                    break;
            }
        } catch (JSONException e) {
            //Log.e("ERROR", "YEES");
            e.printStackTrace();
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeable){
                    like.setImageResource(R.drawable.like_on);
                    likeable = false;
                }else{
                    like.setImageResource(R.drawable.like);
                    likeable = true;
                }
            }
        });
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volunteerable){
                    volunteer.setImageResource(R.drawable.volunteer_on);
                    volunteerable = false;
                }else{
                    volunteer.setImageResource(R.drawable.volunteer);
                    volunteerable = true;
                }
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(mContext, CampaignExpanded.class);

                i.putExtra("campaign_id", campaign_id);
                i.putExtra("uid", uid);
                mContext.startActivity(i);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, CommentActivity.class);
                i.putExtra("campaign_id", campaign_id);
                mContext.startActivity(i);
            }
        });

        return rowView;
    }

    public void follow_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    Toast.makeText(mContext, "Followed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
        }
    }

    public void volunteer_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    Toast.makeText(mContext, "Volunteered", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
    }

    public void unfollow_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    Toast.makeText(mContext, "UnFollowed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
    }

    public void unvolunteer_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
    }
}