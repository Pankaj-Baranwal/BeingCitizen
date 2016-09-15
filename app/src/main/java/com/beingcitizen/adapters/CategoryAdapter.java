package com.beingcitizen.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingcitizen.R;

/**
 * Created by saransh on 21-06-2015.
 *
 * * This class is the adapter to fill the entries in the menu section on campaign page.
 */
public class CategoryAdapter extends BaseAdapter{
    private Context mContext;
    private String[] categorynam;
    private Drawable icons;
    public CategoryAdapter(Context c, String[] categoryname) {
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
    public View getView(int position, View child, ViewGroup parent) {

        LayoutInflater inflater;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.category_listcell, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.camcat_txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.camcat_img);
        textView.setText(categorynam[position]);
        imageView.setImageResource(R.drawable.scales_of_justice);
        return rowView;
    }

}
