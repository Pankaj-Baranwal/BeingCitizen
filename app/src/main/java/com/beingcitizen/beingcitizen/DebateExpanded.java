package com.beingcitizen.beingcitizen;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveSingleDebate;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pankaj Baranwal on 20-06-2016.
 *
 * Details of particular debate are handled here. This activity is loaded when a particular debate is
 * chosen from debate-adapter.
 */


public class DebateExpanded extends AppCompatActivity{

    private Toolbar toolbar;
    CardView cardView;
    String uid = "16", debate_id = "10";
    String nature="yes";
    String url_img = "", title="", content="";
    ProgressView image_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
//        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
        setContentView(R.layout.debate_expanded);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Debate");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        debate_id = getIntent().getExtras().getString("debate_id");
        image_loading = (ProgressView) findViewById(R.id.progress_imageLoading);
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
        if (item.getItemId()==R.id.share){
            final Dialog dialogLogout = new Dialog(this);
            dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLogout.setContentView(R.layout.dialog_share);
            Button fb = (Button) dialogLogout.findViewById(R.id.fb);
            Button others = (Button) dialogLogout.findViewById(R.id.others);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareOnFacebook(url_img, content, title);
                    dialogLogout.dismiss();
                }
            });
            others.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, "http://beingcitizen.com/Main/viewDebate/"+debate_id);
                    intent.putExtra(android.content.Intent.EXTRA_STREAM, url_img);
                    startActivity(Intent.createChooser(intent, "Share via:"));
                }
            });
            dialogLogout.show();
        }
        else
            finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void functions(JSONObject s) {
        final ImageView img_debate = (ImageView)findViewById(R.id.image_debate);
        TextView debate_title = (TextView) findViewById(R.id.debate_title);
        TextView debate_content = (TextView) findViewById(R.id.debate_content);
        TextView viewComments = (TextView) findViewById(R.id.viewComments);
        //userName.setText(s.getJSONArray("debDetails"));
        TextView yes_txt = (TextView)findViewById(R.id.yes_txt);
        TextView no_txt = (TextView)findViewById(R.id.no_txt);
        try {
            title = s.getJSONArray("debDetails").getJSONObject(0).getString("name");
            content =s.getJSONArray("debDetails").getJSONObject(0).getString("debate_text");
            debate_title.setText(s.getJSONArray("debDetails").getJSONObject(0).getString("name"));
            debate_content.setText(s.getJSONArray("debDetails").getJSONObject(0).getString("debate_text"));
            Float dv1 = Float.parseFloat(s.getJSONArray("debDetails").getJSONObject(0).getString("fore"));
            Float dv2 = Float.parseFloat(s.getJSONArray("debDetails").getJSONObject(0).getString("against"));
            Float per1 = dv1/(dv1+dv2);
            Float per2 = dv2/(dv1+dv2);
            yes_txt.setText(Math.round(per1*100)+" % in favour");
            no_txt.setText(Math.round(per2*100)+" % against");
            //            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.weight = per1;
//            yes_perc.setLayoutParams(params);
//            params.weight = per2;
//            no_perc.setLayoutParams(params);
            url_img = "http://beingcitizen.com/uploads/debates/" + s.getJSONArray("debDetails").getJSONObject(0).getString("dimage") + s.getJSONArray("debDetails").getJSONObject(0).getString("dext");
            Picasso.with(this).load(url_img).resize(256, 256).into(img_debate, new Callback() {
                @Override
                public void onSuccess() {
                    img_debate.setVisibility(View.VISIBLE);
                    image_loading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void shareOnFacebook(String pictureFile, String text, String title){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentUrl(Uri.parse("http://beingcitizen.com/Main/viewDebate/"+debate_id))
                .setImageUrl(Uri.parse(pictureFile))
                .setContentDescription(text)
                .build();
        ShareDialog.show(DebateExpanded.this, content);
    }
}