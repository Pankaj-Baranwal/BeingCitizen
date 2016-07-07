package com.beingcitizen.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by saransh on 27-06-2015.
 */
public class BlogsAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private JSONArray categorynam;
    private Drawable icons;
    String blog_id = "10", url_img="";
    CardView cardView;

    //private DisplayImageOptions options;

    public BlogsAdapter(Context c, JSONArray categoryname) {
        this.mContext = c;
        this.categorynam = categoryname;

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
        View rowView = inflater.inflate(R.layout.allblogs_listcell, parent, false);
        // final View view =inflater.inflate(R.layout.allcomment_listcell,parent,false);
        cardView=(CardView) rowView.findViewById(R.id.CardView_allblogs);
        cardView.setRadius(5.0f);
        cardView.setCardElevation(10.0f);

        ImageView blog_img = (ImageView) rowView.findViewById(R.id.blog_image);
        TextView blog_text = (TextView) rowView.findViewById(R.id.blog_heading);
        TextView user_name = (TextView) rowView.findViewById(R.id.user_name);
        TextView posted_at = (TextView) rowView.findViewById(R.id.posted_at);

        try {
            url_img = "http://beingcitizen.com/uploads/blogs/" + categorynam.getJSONObject(position).getString("bimage") + categorynam.getJSONObject(position).getString("bext");
            Picasso.with(mContext).load(url_img).resize(256, 256).into(blog_img);
            blog_id = categorynam.getJSONObject(position).getString("blog_id");
            blog_text.setText(categorynam.getJSONObject(position).getString("title"));
            user_name.setText(categorynam.getJSONObject(position).getString("author"));
            posted_at.setText(categorynam.getJSONObject(position).getString("created_at"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, BlogExpanded.class);
                try {
                    BlogExpanded.function(categorynam.getJSONObject(position), url_img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mContext.startActivity(i);
            }
        });

        return rowView;
    }

    @Override
    public void onClick(View view) {

    }
}
