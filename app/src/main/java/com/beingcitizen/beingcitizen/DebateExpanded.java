package com.beingcitizen.beingcitizen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveSingleDebate;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class DebateExpanded extends AppCompatActivity{

    private Toolbar toolbar;
    CardView cardView;
    String uid = "16", debate_id = "10";
    String nature="yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debate_expanded);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Debate");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        debate_id = getIntent().getExtras().getString("debate_id");
        cardView=(CardView)findViewById(R.id.CardView_campaignExpanded);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        nature = getIntent().getExtras().getString("nature");
        ImageView comment = (ImageView)findViewById(R.id.comment_icon);
        comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(DebateExpanded.this, CommentActivity.class);
                i.putExtra("calling", "debate");
                i.putExtra("debate_id", debate_id);
                i.putExtra("nature", nature);
                startActivity(i);
            }
        });

        TextView viewComments = (TextView) findViewById(R.id.viewComments);
        viewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(DebateExpanded.this, CommentActivity.class);
                i.putExtra("calling", "debate");
                i.putExtra("debate_id", debate_id);
                i.putExtra("nature", nature);
                startActivity(i);
            }
        });
        RetrieveSingleDebate rsd = new RetrieveSingleDebate(this);
        rsd.execute(uid, debate_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

    public void functions(JSONObject s) {
        ImageView img_debate = (ImageView)findViewById(R.id.image_debate);
        TextView debate_title = (TextView) findViewById(R.id.debate_title);
        TextView debate_content = (TextView) findViewById(R.id.debate_content);
        LinearLayout yes_perc = (LinearLayout) findViewById(R.id.yes_percent);
        LinearLayout no_perc = (LinearLayout) findViewById(R.id.no_percent);
        TextView yes_txt = (TextView)findViewById(R.id.yes_txt);
        TextView no_txt = (TextView)findViewById(R.id.no_txt);
        TextView viewComments = (TextView) findViewById(R.id.viewComments);
        //userName.setText(s.getJSONArray("debDetails"));
        try {
            debate_title.setText(s.getJSONArray("debDetails").getJSONObject(0).getString("name"));
            debate_content.setText(s.getJSONArray("debDetails").getJSONObject(0).getString("debate_text"));
            viewComments.setText("View all "+s.getJSONArray("debDetails").getJSONObject(0).getString("total")+" comments");
            Float dv1 = Float.parseFloat(s.getJSONArray("debDetails").getJSONObject(0).getString("fore"));
            Float dv2 = Float.parseFloat(s.getJSONArray("debDetails").getJSONObject(0).getString("against"));
            Float per1 = dv1/(dv1+dv2);
            Float per2 = dv2/(dv1+dv2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = per1;
            yes_perc.setLayoutParams(params);
            params.weight = per2;
            no_perc.setLayoutParams(params);
            yes_txt.setText(Math.round(per1*100)+" %");
            no_txt.setText(Math.round(per2*100)+" %");
            String url_img = "http://beingcitizen.com/uploads/debates/" + s.getJSONArray("debDetails").getJSONObject(0).getString("dimage") + s.getJSONArray("debDetails").getJSONObject(0).getString("dext");
            Picasso.with(this).load(url_img).resize(256, 256).into(img_debate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}