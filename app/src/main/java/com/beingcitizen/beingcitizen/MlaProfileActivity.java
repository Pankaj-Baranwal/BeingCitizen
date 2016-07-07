package com.beingcitizen.beingcitizen;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveMlaProfile;
import com.beingcitizen.retrieveals.RetrieveUserProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saransh on 12-07-2015.
 */
public class MlaProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageView img_mla;
    TextView mla_name, num_camps, mla_consti, mla_gender;
    String mla_id = "m001", name, gender, consti;
    int num_camp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mlaprofile_layout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mla_id = sp.getString("mla_id", "m001");
        img_mla = (ImageView)findViewById(R.id.profile_picture);
        mla_consti = (TextView)findViewById(R.id.mla_consti);
        mla_name = (TextView)findViewById(R.id.mla_name);
        num_camps = (TextView)findViewById(R.id.num_camps);
        mla_gender = (TextView)findViewById(R.id.mla_gender);
        RetrieveMlaProfile rup = new RetrieveMlaProfile(this);
        rup.execute(mla_id);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

    public void functions(JSONObject obj) {
        try {
            JSONArray info = obj.getJSONArray("info");
            name = info.getJSONObject(0).getString("name");
            num_camp = obj.getJSONArray("new").length()+obj.getJSONArray("active").length()+obj.getJSONArray("closed").length();
            consti = info.getJSONObject(0).getString("constituency");
            gender = info.getJSONObject(0).getString("gender");
            String image_loc = "http://beingcitizen.com/uploads/mla/display/"+info.getJSONObject(0).getString("mlaimage")+info.getJSONObject(0).getString("mlaext");
            Picasso.with(this).load(image_loc).resize(150, 150).into(img_mla);
            JSONArray jA = new JSONArray();
            for (int i =0; i<obj.getJSONArray("new").length(); i++){
                jA.put(obj.getJSONArray("new").getJSONObject(i));
            }
            for (int i =0; i<obj.getJSONArray("active").length(); i++){
                jA.put(obj.getJSONArray("active").getJSONObject(i));
            }
            for (int i =0; i<obj.getJSONArray("closed").length(); i++){
                jA.put(obj.getJSONArray("closed").getJSONObject(i));
            }
            createUIForConsti(jA);
            createUIForActive(obj.getJSONArray("active"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createUIForActive(JSONArray campStarted) {
        LinearLayout ll_active_camps = (LinearLayout)findViewById(R.id.active_camps);
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
            if (ll_active_camps != null) {
                ll_active_camps.addView(rL);
            }
        }
    }

    private void createUIForConsti(JSONArray campStarted) {
        LinearLayout ll_consti_camps = (LinearLayout)findViewById(R.id.lL_consti_camp);
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
            if (ll_consti_camps != null) {
                ll_consti_camps.addView(rL);
            }


        }
    }
}
