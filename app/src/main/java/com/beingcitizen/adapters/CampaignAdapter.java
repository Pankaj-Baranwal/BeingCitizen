package com.beingcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.beingcitizen.CommentActivity;
import com.beingcitizen.fragments.AllCampaign;
import com.beingcitizen.retrieveals.SendFollowCampaign;
import com.beingcitizen.retrieveals.SendUnfollowCampaign;
import com.beingcitizen.retrieveals.SendUnvolunteerCampaign;
import com.beingcitizen.retrieveals.SendVolunteerCampaign;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by saransh on 23-06-2015.
 */
public class CampaignAdapter extends BaseAdapter{
    private Context mContext;
    public JSONArray categorynam;
    String campaign_id="30", uid = "16";
    AllCampaign alc;
    boolean nulling = false;

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
        CardView cardView;
        final boolean[] volunteerable = {true};
        final boolean[] likeable = { true };
        boolean vols = true;
        boolean fols = true;
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
        final TextView category=(TextView)rowView.findViewById(R.id.category);
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
            if (categorynam.getJSONObject(position).getString("us_id").contentEquals(uid)){
                likeable[0] = false;
                like.setBackgroundResource(R.drawable.like_on);
            }else {
                likeable[0] = true;
                like.setBackgroundResource(R.drawable.like);
            }
            if (categorynam.getJSONObject(position).getString("usid").contentEquals(uid)){
                volunteerable[0] = false;
                volunteer.setBackgroundResource(R.drawable.volunteer_on);
            }else {
                volunteerable[0] = true;
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
            fols = categorynam.getJSONObject(position).getString("followable").contentEquals("1");
            vols = categorynam.getJSONObject(position).getString("volunteerable").contentEquals("1");
            if (!fols){
                like.setVisibility(View.GONE);
            }else{
                like.setVisibility(View.VISIBLE);
            }
            if (!vols){
                volunteer.setVisibility(View.GONE);
            }else{
                volunteer.setVisibility(View.VISIBLE);
            }
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
                    category_img.setImageResource(R.drawable.communication_black);
                    break;
                case "Water-Irrigation,Drainage,Embankments":
                    category_img.setImageResource(R.drawable.water_supplies_black);
                    break;
                case "Lands, Agriculture":
                    category_img.setImageResource(R.drawable.land_black);
                    break;
                case "Trade,Commerce,Employment":
                    category_img.setImageResource(R.drawable.market_black);
                    break;
                case "Environment and Holticulture":
                    category_img.setImageResource(R.drawable.ecology_black);
                    break;
                case "Tourism, Art and Culture":
                    category_img.setImageResource(R.drawable.tourism_black);
                    break;
                case "Power":
                    category_img.setImageResource(R.drawable.power_black);
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
                try {
                    campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
                    likeable[0] = !categorynam.getJSONObject(position).getString("us_id").contentEquals(uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("LIKE", likeable[0] +" "+ uid+" "+ campaign_id);
                if (likeable[0]){
                    like.setBackgroundResource(R.drawable.like_on);
                    try {
                        categorynam.getJSONObject(position).remove("us_id");
                        categorynam.getJSONObject(position).put("us_id", uid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SendFollowCampaign sfc = new SendFollowCampaign();
                    sfc.execute(uid, campaign_id);

                }else{
                    like.setBackgroundResource(R.drawable.like);
                    try {
                        categorynam.getJSONObject(position).remove("us_id");
                        categorynam.getJSONObject(position).put("us_id", "null");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SendUnfollowCampaign sendUnfollowCampaign = new SendUnfollowCampaign();
                    sendUnfollowCampaign.execute(uid, campaign_id);
                }
            }
        });
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
                    volunteerable[0] = !categorynam.getJSONObject(position).getString("usid").contentEquals(uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("VOLUNTEER", volunteerable[0] +" "+ uid+" "+ campaign_id);
                if (volunteerable[0]){
                    volunteer.setBackgroundResource(R.drawable.volunteer_on);
                    try {
                        categorynam.getJSONObject(position).remove("usid");
                        categorynam.getJSONObject(position).put("usid", uid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SendVolunteerCampaign svc = new SendVolunteerCampaign();
                    svc.execute(uid, campaign_id);
                }else{
                    volunteer.setBackgroundResource(R.drawable.volunteer);
                    try {
                        categorynam.getJSONObject(position).remove("usid");
                        categorynam.getJSONObject(position).put("usid", "null");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SendUnvolunteerCampaign svc = new SendUnvolunteerCampaign();
                    svc.execute(uid, campaign_id);
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
                try {
                    campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(mContext, CommentActivity.class);
                i.putExtra("campaign_id", campaign_id);
                mContext.startActivity(i);
            }
        });

        return rowView;
    }
}