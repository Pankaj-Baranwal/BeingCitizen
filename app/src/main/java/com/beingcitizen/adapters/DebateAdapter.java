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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.beingcitizen.CommentActivity;
import com.beingcitizen.beingcitizen.DebateExpanded;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

/**
 * Created by saransh on 27-06-2015.
 */
public class DebateAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private JSONArray categorynam;
    private Drawable icons;
    CardView cardView;
    String debate_id="0";
    public DebateAdapter(Context c, JSONArray categoryname) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.alldebates_listcell, parent, false);
       // final View view =inflater.inflate(R.layout.allcomment_listcell,parent,false);
        cardView=(CardView) rowView.findViewById(R.id.CardView_alldebate);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);

        LinearLayout yes = (LinearLayout) rowView.findViewById(R.id.btn_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DebateExpanded.class);
                mContext.startActivity(i);
            }
        });

        LinearLayout no = (LinearLayout) rowView.findViewById(R.id.btn_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DebateExpanded.class);
                mContext.startActivity(i);
            }
        });
        TextView title_debate = (TextView)rowView.findViewById(R.id.title_debate);
        TextView content_debate = (TextView)rowView.findViewById(R.id.content_debate);
        TextView num_for = (TextView)rowView.findViewById(R.id.num_for);
        TextView num_against = (TextView)rowView.findViewById(R.id.num_against);
        try {
            title_debate.setText(categorynam.getJSONObject(position).getString("name"));
            content_debate.setText(categorynam.getJSONObject(position).getString("debate_text"));
            num_for.setText(categorynam.getJSONObject(position).getString("fore")+" in favour");
            num_against.setText(categorynam.getJSONObject(position).getString("against")+ " against");
            debate_id = categorynam.getJSONObject(position).getString("debate_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView comment = (ImageView) rowView.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, CommentActivity.class);
                i.putExtra("debate_id", debate_id);
                mContext.startActivity(i);
            }
        });

        return rowView;
    }

    @Override
    public void onClick(View view) {

    }


}
