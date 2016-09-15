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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saransh on 23-06-2015.
 *
 * This class is the adapter to fill the entries in the comments section within a particular debate.
 */
public class CommentAdapter extends BaseAdapter{
    private Context mContext;
    public JSONArray categorynam;
    String uid = "16";
    int total = 0;
    public CommentAdapter(Context c, JSONArray categoryname) {
        this.mContext = c;

        this.categorynam = categoryname;
        // this.icons = icons;
        total = categorynam.length();
    }

    public void updating(JSONArray s){
        categorynam = s;
        total = categorynam.length();
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
        RelativeLayout rL_user_against = (RelativeLayout)rowView.findViewById(R.id.rL_user_against);
        final TextView user_name = (TextView) rowView.findViewById(R.id.user_name_for);
        CircleImageView user_image = (CircleImageView)rowView.findViewById(R.id.user_image_for);
        TextView comment_text = (TextView) rowView.findViewById(R.id.comment_text_for);
        final TextView user_name_against = (TextView) rowView.findViewById(R.id.user_name_against);
        CircleImageView user_image_against = (CircleImageView)rowView.findViewById(R.id.user_image_against);
        TextView comment_text_against = (TextView) rowView.findViewById(R.id.comment_text_against);
//        LinearLayout ll_for = (LinearLayout) rowView.findViewById(R.id.lL_for);
//        LinearLayout ll_against = (LinearLayout) rowView.findViewById(R.id.lL_against);
        String which;
        try {
            if (total > 0) {
                if (categorynam.getJSONObject(position).getString("which").contentEquals("for")) {
                    which = "for";
                    rL_user.setBackgroundColor(0xB111FF1D);
                    rL_user_against.setVisibility(View.GONE);
                    rL_user.setVisibility(View.VISIBLE);
                } else {
                    which = "against";
                    rL_user_against.setBackgroundColor(0xb1ff553a);
                    rL_user_against.setVisibility(View.VISIBLE);
                    rL_user.setVisibility(View.GONE);
                }
                if (which.contentEquals("for")) {
                    user_name.setText(categorynam.getJSONObject(position).getString("name"));
                    comment_text.setText(categorynam.getJSONObject(position).getString("content"));
                    uid = categorynam.getJSONObject(position).getString("user_id");
                    if (!categorynam.getJSONObject(position).getString("uimage").contains("null")) {
                        String user_img_loc = "http://beingcitizen.com/uploads/display/" + categorynam.getJSONObject(position).getString("uimage") + categorynam.getJSONObject(position).getString("uext");
                        Picasso.with(mContext).load(user_img_loc).resize(100, 100).error(R.drawable.ic_profile).into(user_image);
                    }
                }else{
                    user_name_against.setText(categorynam.getJSONObject(position).getString("name"));
                    comment_text_against.setText(categorynam.getJSONObject(position).getString("content"));
                    uid = categorynam.getJSONObject(position).getString("user_id");
                    if (!categorynam.getJSONObject(position).getString("uimage").contains("null")) {
                        String user_img_loc = "http://beingcitizen.com/uploads/display/" + categorynam.getJSONObject(position).getString("uimage") + categorynam.getJSONObject(position).getString("uext");
                        Picasso.with(mContext).load(user_img_loc).resize(100, 100).error(R.drawable.ic_profile).into(user_image_against);
                    }
                }
            }
        }catch (JSONException e) {

        }
        rL_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uid = categorynam.getJSONObject(position).getString("user_id");
                }catch (JSONException e) {

                }
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("uid", uid);
                mContext.startActivity(intent);
            }
        });
        rL_user_against.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uid = categorynam.getJSONObject(position).getString("user_id");
                }catch (JSONException e) {

                }
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("uid", uid);
                mContext.startActivity(intent);
            }
        });
        return rowView;
    }
}