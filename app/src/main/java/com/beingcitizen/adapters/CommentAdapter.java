package com.beingcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.UserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 23-06-2015.
 */
public class CommentAdapter extends BaseAdapter{
    private Context mContext;
    public JSONObject categorynam;
    private int total = 0, fore = 0;
    String uid = "16";

    public CommentAdapter(Context c, JSONObject categoryname) {
        this.mContext = c;

        this.categorynam = categoryname;
        try {
            fore = categorynam.getJSONArray("for").length();
            total = categorynam.getJSONArray("against").length()+fore;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // this.icons = icons;

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
        View rowView = inflater.inflate(R.layout.allcomment_listcell, parent, false);
        RelativeLayout rL_user = (RelativeLayout)rowView.findViewById(R.id.rL_user);
        final TextView user_name = (TextView) rowView.findViewById(R.id.user_name);
        TextView comment_text = (TextView) rowView.findViewById(R.id.comment_text);
        if (total>0) {
            try {
                if (position<fore) {
                    rL_user.setBackgroundColor(0xB111FF1D);
                    user_name.setText(categorynam.getJSONArray("for").getJSONObject(position).getString("name"));
                    comment_text.setText(categorynam.getJSONArray("for").getJSONObject(position).getString("content"));
                    uid = categorynam.getJSONArray("for").getJSONObject(position).getString("user_id");
                }else{
                    if (total-fore>position-fore){
                        rL_user.setBackgroundColor(0xb1ff553a);
                        user_name.setText(categorynam.getJSONArray("against").getJSONObject(position-fore).getString("name"));
                        comment_text.setText(categorynam.getJSONArray("against").getJSONObject(position-fore).getString("content"));
                        uid = categorynam.getJSONArray("against").getJSONObject(position).getString("user_id");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rL_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uid = categorynam.getJSONArray("for").getJSONObject(position).getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("uid", uid);
                mContext.startActivity(intent);
            }
        });
        return rowView;
    }
}