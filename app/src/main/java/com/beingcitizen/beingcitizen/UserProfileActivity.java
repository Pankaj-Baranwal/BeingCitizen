package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saransh on 12-07-2015.
 */
public class UserProfileActivity extends ActionBarActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private Toolbar toolbar;
    TextView nameview,emailview;
    CircleImageView profile;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_layout1);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);


        String name=sharedpreferences.getString("name", null);
        String email=sharedpreferences.getString("email",null);
        String f_id=sharedpreferences.getString("id",null);


       //            Bitmap bm= getBitmapFromURL("http://graph.facebook.com/"+f_id+"/picture?type=small");


        nameview=(TextView)findViewById(R.id.name);
        nameview.setText(name);
        emailview=(TextView)findViewById(R.id.email);
        emailview.setText(email);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
       // profile = (CircleImageView)findViewById(R.id.circleView1);

         //   Toast.makeText(getApplicationContext(),f_id,Toast.LENGTH_SHORT).show();
        /*
        HTTPExample task = new HTTPExample ();
        task.execute(new String[] {"http://graph.facebook.com/"+f_id+"/picture?type=small" });
*/
        //profile.setImageBitmap(bm);
        //    Picasso.with(this).load("http://graph.facebook.com/"+f_id+"/picture?type=small")
         //           .into(profile);

        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture);
        profilePictureView.setCropped(true);
        profilePictureView.setProfileId(f_id);

    setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_profile_edit:
                Toast.makeText(UserProfileActivity.this, "Edit: ", Toast.LENGTH_SHORT).show();
                break;

            default:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    private class HTTPExample extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //urls is an array not a string, so iterate through urls.
            //Get Picture Here - BUT DONT UPDATE UI, Return a reference of the object
          return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //Update UI
            Log.i("Result", result);
        }
    }


*/
    }
