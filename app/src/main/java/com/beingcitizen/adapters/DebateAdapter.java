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
import com.beingcitizen.beingcitizen.DebateExpanded;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by saransh on 27-06-2015.
 *
 * This class is the adapter to fill the entries in the debates section.
 */
public class DebateAdapter extends BaseAdapter{
    private Context mContext;
    private JSONArray categorynam;
    //private Drawable icons;
    CardView cardView;
    String debate_id="0", uid="16", nature = "yes";
    //private DisplayImageOptions options;
    boolean nulling = false;


    public DebateAdapter(Context c, JSONArray categoryname) {
        if (c!=null) {
            nulling = false;
            this.mContext = c;
            this.categorynam = categoryname;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            uid = sp.getString("id", "16");
        }else{
            nulling = true;
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
        View rowView = inflater.inflate(R.layout.alldebates_listcell, parent, false);
       // final View view =inflater.inflate(R.layout.allcomment_listcell,parent,false);
        cardView=(CardView) rowView.findViewById(R.id.CardView_alldebate);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        final ImageView image_debate = (ImageView)rowView.findViewById(R.id.image_debate);
        final ProgressView imageLoading = (ProgressView) rowView.findViewById(R.id.progress_imageLoading);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    debate_id = categorynam.getJSONObject(position).getString("debate_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(mContext, DebateExpanded.class);
                i.putExtra("debate_id", debate_id);
                mContext.startActivity(i);
            }
        });
        TextView title_debate = (TextView)rowView.findViewById(R.id.title_debate);
        TextView content_debate = (TextView)rowView.findViewById(R.id.content_debate);
        TextView num_for = (TextView)rowView.findViewById(R.id.num_for);
        TextView num_against = (TextView)rowView.findViewById(R.id.num_against);
        TextView yes_txt = (TextView)rowView.findViewById(R.id.yes_txt);
        TextView no_txt = (TextView)rowView.findViewById(R.id.no_txt);
        if (!nulling) {
            try {
                String url_img = ("http://beingcitizen.com/uploads/debates/" + categorynam.getJSONObject(position).getString("dimage") + categorynam.getJSONObject(position).getString("dext"));
                final LinearLayout ll_debate_adapter = (LinearLayout) rowView.findViewById(R.id.ll_adapter_debate);
                Picasso.with(mContext).load(url_img).resize(256, 256).into(image_debate, new Callback() {
                    @Override
                    public void onSuccess() {
                        image_debate.setVisibility(View.VISIBLE);
                        imageLoading.setVisibility(View.GONE);
                        ll_debate_adapter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });


                title_debate.setText(categorynam.getJSONObject(position).getString("name"));
                String debate_text = categorynam.getJSONObject(position).getString("debate_text");
                if (debate_text.length()>200){
                    debate_text = debate_text.substring(0, 197) + "...";
                }
                content_debate.setText(debate_text);
                String for_val, against_val;
                if (categorynam.getJSONObject(position).getString("fore")!=null)
                    for_val =  categorynam.getJSONObject(position).getString("fore");
                else
                    for_val = "0";
                if (categorynam.getJSONObject(position).getString("against")!=null)
                    against_val =  categorynam.getJSONObject(position).getString("against");
                else
                    against_val = "0";
                num_for.setText(for_val + " in favour");
                num_against.setText(against_val + " against");
                Float dv1 = Float.parseFloat(for_val);
                Float dv2 = Float.parseFloat(against_val);
                Float per1 = dv1/(dv1+dv2);
                Float per2 = dv2/(dv1+dv2);
                yes_txt.setText(Math.round(per1*100)+" % in favour");
                no_txt.setText(Math.round(per2*100)+" % against");
                debate_id = categorynam.getJSONObject(position).getString("debate_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rowView;
    }
}