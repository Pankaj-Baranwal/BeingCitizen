package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.interfaces.mla_id;
import com.beingcitizen.retrieveals.RetrieveFeedTask;
import com.beingcitizen.retrieveals.RetrieveMlaID;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saransh on 14-06-2015.
 */
public class LoginActivity extends Activity implements com.beingcitizen.interfaces.login, mla_id {

    public TextView welocomeback, citizen, forgotPassword;
    public Button login;
    public EditText email, password;
    String uid = "16";
    SharedPreferences sharedpreferences;
    ProgressView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedpreferences.contains("email")) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            finish();
        } else {
            setContentView(R.layout.login1);
            loading = (ProgressView)findViewById(R.id.progress_imageLoading);
            welocomeback = (TextView) findViewById(R.id.textView);
            citizen = (TextView) findViewById(R.id.textView1);
            login = (Button) findViewById(R.id.button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClicklogIn(v);
                }
            });
            email = (EditText) findViewById(R.id.editText);
            password = (EditText) findViewById(R.id.editText2);
            forgotPassword = (TextView) findViewById(R.id.textView5);
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Forgot passowrd
                    Toast.makeText(LoginActivity.this, "Facility not available yet", Toast.LENGTH_SHORT).show();
                }
            });

            // Font path
            String fontPath = "fonts/GOTHIC_0.TTF";
            String fontPath1 = "fonts/GOTHICB_0.TTF";

            // Loading Font Face
            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
            Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);

            // Applying font
            welocomeback.setTypeface(tf);
            citizen.setTypeface(tf);
            login.setTypeface(tf1);
            email.setTypeface(tf);
            password.setTypeface(tf);
            forgotPassword.setTypeface(tf);
        }
    }

    public void onClicklogIn(View v) {
        String email_edit = email.getText().toString();
        String pass_edit = password.getText().toString();
        if (email_edit.length() > 0 && pass_edit.length() > 0) {
            loading.setVisibility(View.VISIBLE);
            RetrieveFeedTask rft = new RetrieveFeedTask(this);
            rft.execute(email_edit, pass_edit);
        } else {
            Toast.makeText(this, "Entries incomplete", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), LoginMain.class);
        //i.putExtra("uid", uid);
        startActivity(i);
        finish();
    }

    @Override
    public void login_feed(JSONObject s) {
        if (s.has("id")) {
            SharedPreferences.Editor edit = sharedpreferences.edit();
            try {
                uid = s.getString("id");
                edit.putString("id", s.getString("id"));
                edit.putString("name", s.getString("name"));
                edit.putString("email", s.getString("email"));
                edit.putString("sex", s.getString("sex"));
                edit.putString("const", s.getString("const"));
                edit.apply();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", "Error in storing to shared prefs");
            }
            RetrieveMlaID rmlaid = new RetrieveMlaID(LoginActivity.this, LoginActivity.this);
            rmlaid.execute(sharedpreferences.getString("const", "Burari"));
        } else {
            loading.setVisibility(View.GONE);
            Toast.makeText(this, "Incorrect details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void mla_ids(String mlaID) {
        SharedPreferences.Editor edit = sharedpreferences.edit();
        if (!mlaID.contentEquals("null")) {
            edit.putString("mla_id", mlaID);
            edit.apply();
        }else{
            edit.putString("mla_id", "No_mla_id");
            edit.apply();
        }
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        loading.setVisibility(View.GONE);
        startActivity(i);
        finish();
    }
}
