package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.adapters.Draweradapter;
import com.beingcitizen.R;
import com.beingcitizen.adapters.ImageAdapter;
import com.beingcitizen.fragments.Campaign;
import com.beingcitizen.fragments.Debate;
import com.beingcitizen.fragments.TermsCondition;
import com.beingcitizen.retrieveals.RetrieveSingleCampaign;
import com.beingcitizen.utils.CirclePageIndicator;
import com.beingcitizen.utils.PageIndicator;
import com.google.android.gms.maps.model.Circle;
import com.juliomarcos.ImageViewPopUpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class CampaignExpanded extends AppCompatActivity{

    private Toolbar toolbar;
    CardView cardView;
    String campaign_id="30";
    TextView viewComments;
    ImageView imageView1,imageView2,imageView3;
    /*******************************/
    ImageAdapter imageAdapter;
    ViewPager mPager;
    //PageIndicator mIndicator;
    //CirclePageIndicator indicator;
    /******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_expanded);

        /*Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar*/
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Campaign");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        campaign_id = getIntent().getExtras().getString("campaign_id");
        cardView=(CardView)findViewById(R.id.CardView_campaignExpanded);
        cardView.setRadius(16.0f);
        cardView.setCardElevation(16.0f);
        viewComments = (TextView) findViewById(R.id.viewComments);
        ImageView comment = (ImageView)findViewById(R.id.comment);
        RetrieveSingleCampaign rsc = new RetrieveSingleCampaign(CampaignExpanded.this);
        rsc.execute("16", campaign_id);
        comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                i.putExtra("campaign_id", campaign_id);
                startActivity(i);
            }
        });

        final TextView viewComments = (TextView) findViewById(R.id.viewComments);
        viewComments.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (viewComments.getText().toString().length()>12) {
                    Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                    i.putExtra("campaign_id", campaign_id);
                    startActivity(i);
                }
            }
        });



        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);


        ImageViewPopUpHelper.enablePopUpOnClick(this, imageView1);
        ImageViewPopUpHelper.enablePopUpOnClick(this, imageView2);
        ImageViewPopUpHelper.enablePopUpOnClick(this, imageView3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

    public void functions(JSONObject s) {
        try {
            if (s.getJSONArray("campDetails").length()>0){
                TextView title = (TextView)findViewById(R.id.title);
                TextView username = (TextView)findViewById(R.id.username);
                TextView time_posted = (TextView) findViewById(R.id.time_posted);
                TextView campaign_txt = (TextView) findViewById(R.id.campaign_txt);
                TextView num_followers = (TextView) findViewById(R.id.num_followers);
                TextView num_volunteers = (TextView) findViewById(R.id.num_volunteers);

                //campaign_id = s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_id");
                CircleImageView user_pic = (CircleImageView)findViewById(R.id.user_pic);
                title.setText(s.getJSONArray("campDetails").getJSONObject(0).getString("cname"));
                username.setText(s.getJSONArray("campDetails").getJSONObject(0).getString("name"));
                time_posted.setText("Posted at "+s.getJSONArray("campDetails").getJSONObject(0).getString("created_at"));
                campaign_txt.setText(s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_text").substring(0, s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_text").length()>550?550:s.getJSONArray("campDetails").getJSONObject(0).getString("campaign_text").length()));
                num_followers.setText(s.getString("fols")+" followers");
                num_volunteers.setText(s.getString("vols")+" volunteers");
                if (s.getJSONArray("comment").length()!=0)
                viewComments.setText("View all "+ s.getJSONArray("comment").length()+" comments");
                else
                    viewComments.setText("No comment");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}