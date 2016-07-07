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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.fragments.AllCampaign;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saransh on 23-06-2015.
 */
public class CampaignAdapter extends BaseAdapter{
    private Context mContext;
    public JSONArray categorynam;
    String campaign_id="30", uid = "16";
    AllCampaign alc;
    boolean nulling = false;
    CardView cardView;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.allcampaigns_listcell, parent, false);
        cardView=(CardView) rowView.findViewById(R.id.CardView_allcampaign);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        LinearLayout verification = (LinearLayout)rowView.findViewById(R.id.verification_lL);
        TextView title = (TextView) rowView.findViewById(R.id.title);
//        ImageButton comment = (ImageButton) rowView.findViewById(R.id.comment);
        final ImageView camp_img = (ImageView) rowView.findViewById(R.id.image_campaign);
        TextView city_name=(TextView)rowView.findViewById(R.id.city_name);
        TextView info_campaign=(TextView)rowView.findViewById(R.id.info_campaign);
        TextView category=(TextView)rowView.findViewById(R.id.category);
        CircleImageView civ = (CircleImageView) rowView.findViewById(R.id.circleImageView);
        if (!nulling)
        try {
            String url_img = "http://beingcitizen.com/uploads/campaigns/" + categorynam.getJSONObject(position).getString("image") + categorynam.getJSONObject(position).getString("ext");
            Picasso.with(mContext).load(url_img).resize(256, 256).into(camp_img);
            String status = categorynam.getJSONObject(position).getString("status");
            if (status.contentEquals("1")){
                verification.setBackgroundColor(0xB111FF1D);
            }else if (status.contentEquals("2")){
                verification.setBackgroundColor(0xB10000FF);
            }
            else{
                verification.setBackgroundColor(0xB1FF0000);
            }
            campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
            title.setText(categorynam.getJSONObject(position).getString("cname"));
            city_name.setText(categorynam.getJSONObject(position).getString("cconstituency"));
            String campaign_text = categorynam.getJSONObject(position).getString("campaign_text");
            if (campaign_text.length()>200)
                campaign_text=campaign_text.substring(0, 200);
            info_campaign.setText(campaign_text);
            category.setText(categorynam.getJSONObject(position).getString("category"));
        } catch (JSONException e) {
            //Log.e("ERROR", "YEES");
            e.printStackTrace();
        }

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
//        comment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent i = new Intent(mContext, CommentActivity.class);
//                i.putExtra("campaign_id", campaign_id);
//                mContext.startActivity(i);
//            }
//        });

        return rowView;
    }
}