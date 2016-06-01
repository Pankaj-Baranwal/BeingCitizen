package com.beingcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.PollExpanded;

/**
 * Created by saransh on 27-06-2015.
 */
public class CartoonsAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private String[] categorynam;
    private Drawable icons;
    CardView cardView;
    public CartoonsAdapter(Context c, String[] categoryname) {
        this.mContext = c;

        this.categorynam = categoryname;
        // this.icons = icons;

    }
    @Override
    public int getCount() {
        return categorynam.length;
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
        View rowView = inflater.inflate(R.layout.allcartoons_listcell, parent, false);
        cardView=(CardView) rowView.findViewById(R.id.CardView_allcartoons);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);

        return rowView;
    }

    @Override
    public void onClick(View view) {

    }
}
