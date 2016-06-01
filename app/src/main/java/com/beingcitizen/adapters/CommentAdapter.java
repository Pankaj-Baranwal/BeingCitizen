package com.beingcitizen.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beingcitizen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 23-06-2015.
 */
public class CommentAdapter extends BaseAdapter{
    private Context mContext;
    private JSONObject categorynam;
    private Drawable icons;
    public CommentAdapter(Context c, JSONObject categoryname) {
        this.mContext = c;

        this.categorynam = categoryname;
        // this.icons = icons;

    }
    @Override
    public int getCount() {
        try {
            return Integer.parseInt(categorynam.getJSONArray("debDetails").getJSONObject(0).getString("total"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.allcomment_listcell, parent, false);
        TextView user_name = (TextView) rowView.findViewById(R.id.user_name);
        TextView comment_text = (TextView) rowView.findViewById(R.id.comment_text);
        try {
            user_name.setText(categorynam.getJSONArray("for").getJSONObject(0).getString("name"));
            comment_text.setText(categorynam.getJSONArray("for").getJSONObject(0).getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
