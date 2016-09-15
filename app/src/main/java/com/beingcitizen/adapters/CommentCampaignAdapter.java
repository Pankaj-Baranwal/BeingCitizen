package com.beingcitizen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.interfaces.callUserProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 3/6/16.
 *
 * This class is the adapter to fill the entries in the comments section for a particular campaign.
 */
public class CommentCampaignAdapter extends BaseAdapter {
    private Context mContext;
    public JSONObject categorynam;
    private int total = 0;
    String uid = "16";
    callUserProfile cup=null;
    boolean nulling = false;

    public CommentCampaignAdapter(Context c, JSONObject categoryname, callUserProfile cup) {
        this.mContext = c;
        if (categoryname!=null) {
            nulling = false;
            this.categorynam = categoryname;
            this.cup = cup;
            try {
                total = categorynam.getJSONArray("comment").length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            nulling = true;
        }
        // this.icons = icons;

    }

    public void updating(JSONObject s){
        categorynam = s;
        try {
            total = categorynam.getJSONArray("comment").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return total;
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
        View rowView = inflater.inflate(R.layout.comment_campaign, parent, false);
        RelativeLayout rL_user = (RelativeLayout)rowView.findViewById(R.id.rL_user);
        TextView user_name = (TextView) rowView.findViewById(R.id.user_name);
        TextView comment_text = (TextView) rowView.findViewById(R.id.comment_text);
        CircleImageView user_image = (CircleImageView)rowView.findViewById(R.id.user_image);
        if (total>0) {
            try {
                user_name.setText(categorynam.getJSONArray("comment").getJSONObject(position).getString("name"));
                comment_text.setText(categorynam.getJSONArray("comment").getJSONObject(position).getString("content"));
                uid = categorynam.getJSONArray("comment").getJSONObject(position).getString("user_id");
                if (!categorynam.getJSONArray("comment").getJSONObject(position).getString("uimage").contains("null")) {
                    String user_img_loc = "http://beingcitizen.com/uploads/display/" + categorynam.getJSONArray("comment").getJSONObject(position).getString("uimage") + categorynam.getJSONArray("comment").getJSONObject(position).getString("uext");
                    Picasso.with(mContext).load(user_img_loc).resize(100, 100).error(R.drawable.ic_profile).into(user_image);
                }
            } catch (JSONException ignored) {

            }
        }
        if (!nulling) {
            rL_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        uid = categorynam.getJSONArray("comment").getJSONObject(position).getString("user_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (cup != null)
                        cup.callingUser(uid);
                }
            });
        }
        return rowView;
    }
}