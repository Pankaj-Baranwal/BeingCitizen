package com.beingcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.beingcitizen.CommentActivity;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saransh on 23-06-2015.
 */
public class CampaignAdapter extends BaseAdapter{
    private Context mContext;
    private JSONArray categorynam;
    private Drawable icons;
    String campaign_id="";

    int count=0;
    CardView cardView;

    public CampaignAdapter(Context c, JSONArray categoryname) {
        this.mContext = c;

        this.categorynam = categoryname;

        // this.icons = icons;

    }
    @Override
    public int getCount() {
        return categorynam.length();
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

        TextView title = (TextView) rowView.findViewById(R.id.title);
        ImageView comment = (ImageView) rowView.findViewById(R.id.comment);
        TextView mla_name=(TextView)rowView.findViewById(R.id.mla_name);
        TextView city_name=(TextView)rowView.findViewById(R.id.city_name);
        TextView info_campaign=(TextView)rowView.findViewById(R.id.info_campaign);
        TextView category=(TextView)rowView.findViewById(R.id.category);
        CircleImageView civ = (CircleImageView) rowView.findViewById(R.id.circleImageView);
        final ImageView follow = (ImageView) rowView.findViewById(R.id.follow);
        try {
            campaign_id = categorynam.getJSONObject(position).getString("campaign_id");
            title.setText(categorynam.getJSONObject(position).getString("cname"));
            city_name.setText(categorynam.getJSONObject(position).getString("cconstituency"));
            String campaign_text = categorynam.getJSONObject(position).getString("campaign_text");
            if (campaign_text.length()>200)
                campaign_text=campaign_text.substring(0, 200);
            info_campaign.setText(campaign_text);
            category.setText(categorynam.getJSONObject(position).getString("category"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, CampaignExpanded.class);
                i.putExtra("campaign_id", (Integer.parseInt(campaign_id)-1)+"");
                mContext.startActivity(i);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, CommentActivity.class);
                i.putExtra("campaign_id", (Integer.parseInt(campaign_id)-1)+"");
                mContext.startActivity(i);
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //http://tnine.io/bc/main/followcampaign?uid=29&campaign_id=40
                follow.setImageResource(R.drawable.like_on);
            }
        });

        return rowView;
    }
}
