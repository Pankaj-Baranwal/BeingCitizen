package com.beingcitizen.beingcitizen;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveUserProfile;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 12-07-2015.
 */
public class UserProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    TextView nameview,emailview;
    ImageView profile;
    SharedPreferences sharedpreferences;
    String uid = "16";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_layout1);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        uid = sharedpreferences.getString("id", "16");
        if (getIntent().getExtras()!=null)
            uid = getIntent().getExtras().getString("uid");
        Log.e("UId", uid);
        nameview=(TextView)findViewById(R.id.name);
        emailview=(TextView)findViewById(R.id.email);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        profile = (ImageView)findViewById(R.id.profile_picture);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RetrieveUserProfile rup = new RetrieveUserProfile(this);
        rup.execute(uid);
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
        //getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


//    public Bitmap getBitmapFromURL(String src) {
//        try {
//            java.net.URL url = new java.net.URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void functions(JSONObject obj) {
        try {
            JSONObject info = obj.getJSONArray("info").getJSONObject(0);
            String name = info.getString("name");
            String email = info.getString("email");
            JSONArray campsStarted = obj.getJSONArray("camp");
            JSONArray campsFollowed = obj.getJSONArray("fcamp");
            String constituency = info.getString("constituency");
            String image_loc = "http://beingcitizen.com/uploads/display/"+info.getString("uimage")+info.getString("uext");
            Picasso.with(this).load(image_loc).resize(150, 150).into(profile);
            createUIForCreated(campsStarted);
            createUIForFollowed(campsFollowed);
            nameview.setText(name);
            emailview.setText(email);
            TextView const_view = (TextView)findViewById(R.id.consti);
            TextView numCamps = (TextView)findViewById(R.id.num_campaigns);
            TextView numConnect = (TextView)findViewById(R.id.num_connections);
            if (const_view != null) {
                const_view.setText(constituency);
            }
            if (numConnect != null) {
                numConnect.setText(campsStarted.length()+"");
            }
            if (numCamps != null) {
                numCamps.setText(campsFollowed.length()+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createUIForFollowed(JSONArray campStarted) {
        LinearLayout created_lL = (LinearLayout) findViewById(R.id.lL_campaigns_followed);
        for (int i =0; i<campStarted.length(); i++){
            Resources r = this.getResources();
            int px;
            px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    120,
                    r.getDisplayMetrics()
            );
            RelativeLayout rL = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlp_img = new RelativeLayout.LayoutParams(px, px);
            RelativeLayout.LayoutParams rlp_ll = new RelativeLayout.LayoutParams(px, RelativeLayout.LayoutParams.WRAP_CONTENT);

            px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5,
                    r.getDisplayMetrics()
            );
            rlp_img.setMargins(px, 0, px, 0);
            ImageView img = new ImageView(this);
            img.setLayoutParams(rlp_img);
            //img.setImageResource(R.drawable.ic_bg_main);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                img.setId(View.generateViewId());
            }else{
                img.setId(R.id.img_id);
            }
            rlp_ll.addRule(RelativeLayout.ALIGN_BOTTOM, img.getId());
            LinearLayout lL = new LinearLayout(this);
            lL.setBackgroundColor(0xB111FF1D);
            lL.setGravity(Gravity.BOTTOM);
            rlp_ll.setMargins(px, 0, px, 0);
            lL.setLayoutParams(rlp_ll);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(llp);
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8*scale + 0.5f);
            tv.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            try {
                tv.setText(campStarted.getJSONObject(i).getString("cname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tv.setTextColor(Color.WHITE);
            lL.addView(tv);
            String url_img = "http://beingcitizen.com/uploads/campaigns/";
            try {
                url_img += campStarted.getJSONObject(i).getString("image")+campStarted.getJSONObject(i).getString("ext");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Picasso.with(this).load(url_img).resize(150, 150).into(img);
            rL.addView(img);
            rL.addView(lL);
            if (created_lL != null) {
                created_lL.addView(rL);
            }
        }
    }

    private void createUIForCreated(JSONArray campStarted){
        LinearLayout created_lL = (LinearLayout) findViewById(R.id.lL_campaigns_created);
        for (int i =0; i<campStarted.length(); i++){
            Resources r = this.getResources();
            int px;
            px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    120,
                    r.getDisplayMetrics()
            );
            RelativeLayout rL = new RelativeLayout(this);
            RelativeLayout.LayoutParams rlp_img = new RelativeLayout.LayoutParams(px, px);
            RelativeLayout.LayoutParams rlp_ll = new RelativeLayout.LayoutParams(px, RelativeLayout.LayoutParams.WRAP_CONTENT);

            px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5,
                    r.getDisplayMetrics()
            );
            rlp_img.setMargins(px, 0, px, 0);
            ImageView img = new ImageView(this);
            img.setLayoutParams(rlp_img);
            //img.setImageResource(R.drawable.ic_bg_main);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                img.setId(View.generateViewId());
            }else{
                img.setId(R.id.img_id);
            }
            rlp_ll.addRule(RelativeLayout.ALIGN_BOTTOM, img.getId());
            LinearLayout lL = new LinearLayout(this);
            lL.setBackgroundColor(0xB111FF1D);
            lL.setGravity(Gravity.BOTTOM);
            rlp_ll.setMargins(px, 0, px, 0);
            lL.setLayoutParams(rlp_ll);
            lL.setPadding(px, 0, px, 0);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(llp);
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8*scale + 0.5f);
            tv.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            try {
                tv.setText(campStarted.getJSONObject(i).getString("cname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tv.setTextColor(Color.WHITE);
            lL.addView(tv);
            String url_img = "http://beingcitizen.com/uploads/campaigns/";
            try {
                url_img += campStarted.getJSONObject(i).getString("image")+campStarted.getJSONObject(i).getString("ext");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Picasso.with(this).load(url_img).resize(150, 150).into(img);
            rL.addView(img);
            rL.addView(lL);
            if (created_lL != null) {
                created_lL.addView(rL);
            }
        }
    }
}
