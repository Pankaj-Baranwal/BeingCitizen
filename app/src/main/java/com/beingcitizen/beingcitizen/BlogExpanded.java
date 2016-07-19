package com.beingcitizen.beingcitizen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.SendLikeBlog;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class BlogExpanded extends AppCompatActivity{

    private Toolbar toolbar;
    CardView cardView;
    //Fragment dailydigest;
    static JSONObject b;
    static String url="", blog_id = "2", uid = "16";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_expanded);

        /*Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar*/
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blog");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardView=(CardView)findViewById(R.id.CardView_blogExpanded);
        cardView.setRadius(10.0f);
        cardView.setCardElevation(10.0f);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit ();
        uid = sp.getString("id", "16");

        final ImageView blog_img = (ImageView) findViewById(R.id.blog_img);
        final ProgressView imageLoading = (ProgressView) findViewById(R.id.progress_imageLoading);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView posted_at = (TextView) findViewById(R.id.posted_at);
        TextView title_blog = (TextView) findViewById(R.id.title_blog);
        TextView text_blog = (TextView) findViewById(R.id.text_blog);
        Picasso.with(this).load(url).resize(300, 300).into(blog_img, new Callback() {
            @Override
            public void onSuccess() {
                blog_img.setVisibility(View.VISIBLE);
                imageLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        try {
            text_blog.setText(b.getString("body"));
            posted_at.setText(b.getString("created_at"));
            title_blog.setText(b.getString("title"));
            user_name.setText(b.getString("author"));
            blog_id = b.getString("blog_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ImageButton like = (ImageButton)findViewById(R.id.like_blog);
        final ImageButton dislike = (ImageButton)findViewById(R.id.dislike_blog);
        if (sp.contains("blog_like_"+blog_id)){
            if (sp.getString("blog_like_"+blog_id, "downvote").contentEquals("upvote"))
                like.setBackgroundResource(R.drawable.like_on);
            else
                dislike.setBackgroundResource(R.drawable.like_on);
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.contains("blog_like_"+blog_id)){
                    Toast.makeText(BlogExpanded.this, "Already Voted!", Toast.LENGTH_SHORT).show();
                }else {
                    editor.putString("blog_like_"+blog_id, "upvote");
                    editor.apply();
                    like.setBackgroundResource(R.drawable.like_on);
                    SendLikeBlog slb = new SendLikeBlog(BlogExpanded.this);
                    slb.execute("upvote", blog_id, uid);
                }
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.contains("blog_like_"+blog_id)){
                    Toast.makeText(BlogExpanded.this, "Already Voted!", Toast.LENGTH_SHORT).show();
                }else {
                    editor.putString("blog_like_"+blog_id, "downvote");
                    editor.apply();
                    dislike.setBackgroundResource(R.drawable.like_on);
                    SendLikeBlog slb = new SendLikeBlog(BlogExpanded.this);
                    slb.execute("downvote", blog_id, uid);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

    public static void function(JSONObject s, String url_img){
        b= s;
        url = url_img;
    }

    public void like_func(JSONObject s) {
        Toast.makeText(BlogExpanded.this, "Successful!", Toast.LENGTH_SHORT).show();
    }
}