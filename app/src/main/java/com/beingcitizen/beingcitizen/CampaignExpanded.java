package com.beingcitizen.beingcitizen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveSingleCampaign;
import com.beingcitizen.retrieveals.SendFollowCampaign;
import com.beingcitizen.retrieveals.SendUnfollowCampaign;
import com.beingcitizen.retrieveals.SendUnvolunteerCampaign;
import com.beingcitizen.retrieveals.SendVolunteerCampaign;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class CampaignExpanded extends AppCompatActivity{

    CardView cardView;
    String campaign_id="30", uid ="16";
    TextView viewComments;
    ImageView like, volunteer;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    String uid_creator="16", foll, volu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_expanded);

        /*Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Campaign");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        campaign_id = getIntent().getExtras().getString("campaign_id");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();
        uid = sp.getString("id", "16");
        LinearLayout userll = (LinearLayout)findViewById(R.id.user_ll);
        userll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CampaignExpanded.this, UserProfileActivity.class);
                intent.putExtra("uid", uid_creator);
                startActivity(intent);
            }
        });
        cardView=(CardView)findViewById(R.id.CardView_campaignExpanded);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        viewComments = (TextView) findViewById(R.id.viewComments);
        RetrieveSingleCampaign rsc = new RetrieveSingleCampaign(CampaignExpanded.this);
        rsc.execute("16", campaign_id);

        final TextView viewComments = (TextView) findViewById(R.id.viewComments);
        viewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    Intent i = new Intent(CampaignExpanded.this, CommentActivity.class);
                    i.putExtra("campaign_id", campaign_id);
                    startActivity(i);
            }
        });
        like = (ImageView)findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foll.contentEquals("false")){
                        SendFollowCampaign sfc = new SendFollowCampaign(CampaignExpanded.this);
                        sfc.execute(uid, campaign_id);
                    }else{
                        SendUnfollowCampaign sfc = new SendUnfollowCampaign(CampaignExpanded.this);
                        sfc.execute(uid, campaign_id);
                    }
            }
        });
        volunteer = (ImageView)findViewById(R.id.volunteer);
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volu.contentEquals("false")){
                        SendVolunteerCampaign sfc = new SendVolunteerCampaign(CampaignExpanded.this);
                        sfc.execute(uid, campaign_id);
                    }else{
                        SendUnvolunteerCampaign sfc = new SendUnvolunteerCampaign(CampaignExpanded.this);
                        sfc.execute(uid, campaign_id);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

    public void functions(JSONObject s) {
        try {
            if (s!=null) {
                if (s.getJSONArray("campDetails").length() > 0) {
                    ImageView img_campaign = (ImageView) findViewById(R.id.image_campaign);
                    TextView title = (TextView) findViewById(R.id.title);
                    TextView username = (TextView) findViewById(R.id.username);
                    TextView time_posted = (TextView) findViewById(R.id.time_posted);
                    TextView campaign_txt = (TextView) findViewById(R.id.campaign_txt);
                    TextView num_followers = (TextView) findViewById(R.id.num_followers);
                    TextView num_volunteers = (TextView) findViewById(R.id.num_volunteers);
                    uid_creator = s.getJSONArray("campDetails").getJSONObject(0).getString("started_by");
                    //campaign_id = s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_id");
                    CircleImageView user_pic = (CircleImageView) findViewById(R.id.user_pic);
                    CircleImageView level_img = (CircleImageView) findViewById(R.id.game_img);
                    title.setText(s.getJSONArray("campDetails").getJSONObject(0).getString("cname"));
                    username.setText(s.getJSONArray("campDetails").getJSONObject(0).getString("name"));
                    time_posted.setText("Posted at " + s.getJSONArray("campDetails").getJSONObject(0).getString("created_at"));
                    campaign_txt.setText(s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_text").substring(0, s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_text").length() > 550 ? 550 : s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_text").length()));
                    num_followers.setText(s.getString("fols") + " followers");
                    num_volunteers.setText(s.getString("vols") + " volunteers");
                    int score = Integer.parseInt(s.getString("score"));
                    TextView user_level = (TextView) findViewById(R.id.level_content);
                    if (score < 50) {
                        user_level.setText("ONE");
                        level_img.setImageResource(R.drawable.level1);
                    } else if (score < 200) {
                        user_level.setText("TWO");
                        level_img.setImageResource(R.drawable.level2);
                    } else if (score < 500) {
                        user_level.setText("THREE");
                        level_img.setImageResource(R.drawable.level3);
                    } else if (score >= 500) {
                        user_level.setText("FOUR");
                        level_img.setImageResource(R.drawable.level4);
                    }
                    foll = s.getString("follow");
                    Log.e("follow", foll);
                    volu = s.getString("volunteer");
                    if (foll.contentEquals("true")) {
                        like.setBackgroundResource(R.drawable.like_on);
                    } else {
                        like.setBackgroundResource(R.drawable.like);
                    }
                    String url_img = "http://beingcitizen.com/uploads/campaigns/" + s.getJSONArray("campDetails").getJSONObject(0).getString("image") + s.getJSONArray("campDetails").getJSONObject(0).getString("ext");
                    String user_img_loc = "http://beingcitizen.com/uploads/display/" + s.getJSONArray("campDetails").getJSONObject(0).getString("uimage") + s.getJSONArray("campDetails").getJSONObject(0).getString("uext");
                    Picasso.with(this).load(url_img).resize(256, 256).into(img_campaign);
                    Picasso.with(this).load(user_img_loc).resize(256, 256).into(user_pic);
                    if (s.getJSONArray("comment").length() != 0)
                        viewComments.setText("View all " + s.getJSONArray("comment").length() + " comments");
                    else
                        viewComments.setText("No comment");


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void follow_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    edit.putString("followed"+campaign_id, "1");
                    edit.apply();
                    Toast.makeText(this, "Followed", Toast.LENGTH_SHORT).show();
                    RetrieveSingleCampaign rsc = new RetrieveSingleCampaign(CampaignExpanded.this);
                    rsc.execute(uid, campaign_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Unable to process", Toast.LENGTH_SHORT).show();
        }
    }

    public void volunteer_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    edit.putString("volunteered"+campaign_id, "1");
                    edit.apply();
                    Toast.makeText(this, "Volunteered", Toast.LENGTH_SHORT).show();
                    RetrieveSingleCampaign rsc = new RetrieveSingleCampaign(CampaignExpanded.this);
                    rsc.execute(uid, campaign_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(this, "Unable to process", Toast.LENGTH_SHORT).show();
    }

    public void unfollow_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    edit.putString("followed"+campaign_id, "0");
                    edit.apply();
                    Toast.makeText(this, "UnFollowed", Toast.LENGTH_SHORT).show();
                    RetrieveSingleCampaign rsc = new RetrieveSingleCampaign(CampaignExpanded.this);
                    rsc.execute(uid, campaign_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(this, "Unable to process", Toast.LENGTH_SHORT).show();
    }

    public void unvolunteer_function(JSONObject param) {
        if (param.has("status")){
            try {
                if (param.getString("status").contentEquals("true")) {
                    edit.putString("volunteered"+campaign_id, "0");
                    edit.apply();
                    Toast.makeText(this, "UnVolunteered", Toast.LENGTH_SHORT).show();
                    RetrieveSingleCampaign rsc = new RetrieveSingleCampaign(CampaignExpanded.this);
                    rsc.execute(uid, campaign_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            Toast.makeText(this, "Unable to process", Toast.LENGTH_SHORT).show();
    }
}