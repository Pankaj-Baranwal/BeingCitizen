package com.beingcitizen.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.BlogExpanded;
import com.juliomarcos.ImageViewPopUpHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pankaj on 14/6/16.
 */
public class CampaignExpandedAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    ArrayList<String> url;
    public CampaignExpandedAdapter(Context c, ArrayList<String> url) {
        this.mContext = c;
        this.url = url;
        // this.icons = icons;

    }
    @Override
    public int getCount() {
        return url.size();
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
        View rowView = inflater.inflate(R.layout.campaignexpanded_layout, parent, false);
        // final View view =inflater.inflate(R.layout.allcomment_listcell,parent,false);
        ImageView img = (ImageView)rowView.findViewById(R.id.image_expanded);
        ImageViewPopUpHelper.enablePopUpOnClick((Activity) mContext, img);

        Picasso.with(mContext).load(url.get(position)).into(img);
        return rowView;
    }

    @Override
    public void onClick(View view) {

    }
}
