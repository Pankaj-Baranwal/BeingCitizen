package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.interfaces.mla_id;
import com.beingcitizen.interfaces.retrieveCampaign;
import com.beingcitizen.interfaces.signUp_interface;
import com.beingcitizen.retrieveals.RetrieveConstitutency;
import com.beingcitizen.retrieveals.RetrieveMlaID;
import com.beingcitizen.retrieveals.RetrieveSignUp;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zon 14-06-2015.
 *
 * Contains implementation of User sign-up page and dialog boxes for pin code and constituency retrieval.
 */
public class signUp extends Activity implements retrieveCampaign, signUp_interface, mla_id {

    private EditText name;
    private EditText password;
    private EditText email;
    private String gender_text = "", constit="";

    ArrayAdapter<String> adapter;
    ProgressView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        loading = (ProgressView) findViewById(R.id.progress_imageLoading);
        name = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText2);
        Spinner gender = (Spinner) findViewById(R.id.gender_spinner);
        Button signup = (Button) findViewById(R.id.button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSign(v);
            }
        });
        //ArrayAdapter adapter1=ArrayAdapter.createFromResource(this,R.array.constituency_array,R.layout.spinner_item);
        // TODO: Implement constituency using location service
        final ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, R.layout.spinner_item);

        gender.setAdapter(adapter1);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                    gender_text= (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Font path
        String fontPath = "fonts/GOTHIC_0.TTF";
        String fontPath1 = "fonts/GOTHICB_0.TTF";

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);

        // Applying font
        name.setTypeface(tf);
        password.setTypeface(tf);
        email.setTypeface(tf);
        signup.setTypeface(tf1);
    }

    public void onClickSign(View v) {
        if (name.getText().toString().length() > 0 && email.getText().toString().length() > 0 && password.getText().toString().length() > 0 && gender_text.length() > 0) {
            final Dialog dialogLogout = new Dialog(this);
            dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLogout.setContentView(R.layout.dialog_pincode);
            final EditText pin_txt = (EditText) dialogLogout.findViewById(R.id.pin_txt);
            Button update = (Button) dialogLogout.findViewById(R.id.update);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pin_txt.getText().toString().length() != 6) {
                        Toast.makeText(signUp.this, "Wrong Pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        loading.setVisibility(View.VISIBLE);
                        RetrieveConstitutency rcc = new RetrieveConstitutency(signUp.this, signUp.this);
                        dialogLogout.dismiss();
                        rcc.execute(pin_txt.getText().toString());
                    }
                }
            });
            dialogLogout.show();
        } else
            Toast.makeText(this, "Details incomplete", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), LoginMain.class);
        startActivity(i);
        finish();
    }

    @Override
    public void constituency(JSONObject s) {
        JSONArray namearray;
        try {
            namearray = s.getJSONObject("const").names();
            ArrayList<String> obj = new ArrayList<>();
            obj.add("Enter constituency");
            for (int i = 0; i < namearray.length(); i++)
                obj.add(namearray.getString(i));
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, obj);
            loading.setVisibility(View.GONE);
            final Dialog dialogLogout = new Dialog(this);
            dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLogout.setContentView(R.layout.dialog_pincode);
            final EditText pin_txt = (EditText)dialogLogout.findViewById(R.id.pin_txt);
            pin_txt.setVisibility(View.GONE);
            Button update = (Button) dialogLogout.findViewById(R.id.update);
            final Spinner consti = (Spinner) dialogLogout.findViewById(R.id.consti_spinner);
            consti.setAdapter(adapter);
            consti.setVisibility(View.VISIBLE);
            final String[] content = new String[]{"err"};
            consti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position==0)
                        content[0] = "err";
                    else
                        content[0] = (String)parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content[0].contentEquals("err")){
                        Toast.makeText(signUp.this, "Choose constituency", Toast.LENGTH_SHORT).show();
                    }else {
                        loading.setVisibility(View.VISIBLE);
                        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(signUp.this);
                        SharedPreferences.Editor edit = sharedpreferences.edit();
                        edit.putString("constituency", content[0]);
                        edit.apply();
                        RetrieveMlaID rmlaid = new RetrieveMlaID(signUp.this, signUp.this);
                        constit = content[0];
                        rmlaid.execute(content[0]);
                        dialogLogout.cancel();
                    }
                }
            });
            dialogLogout.show();
        } catch (JSONException e) {
            loading.setVisibility(View.GONE);
            Toast.makeText(signUp.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void result(JSONObject obj) {
        loading.setVisibility(View.GONE);
        if (obj.has("user_id")) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            try {
                String uid = obj.getString("user_id");
                edit.putString("id", uid);
                edit.putString("name", obj.getString("name"));
                edit.putString("email", obj.getString("email"));
                edit.putString("sex", obj.getString("gender"));
                edit.apply();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                Toast.makeText(signUp.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }else if(obj.has("status")){
            try {
                if (obj.getString("status").contentEquals("Already used email")) {
                    Toast.makeText(signUp.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(signUp.this, "Error signing in!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(signUp.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void mla_ids(String mlaID) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(signUp.this);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        if (!mlaID.contentEquals("null")) {
            edit.putString("mla_id", mlaID);
            edit.apply();
        }else{
            edit.putString("mla_id", "No_mla_id");
            edit.apply();
        }
        RetrieveSignUp rsup = new RetrieveSignUp(signUp.this, signUp.this);
        rsup.execute(name.getText().toString(), email.getText().toString(), password.getText().toString(), gender_text, constit);
    }


//    public void showSettingsAlert() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                signUp.this);
//        alertDialog.setTitle("SETTINGS");
//        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
//        alertDialog.setPositiveButton("Settings",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        signUp.this.startActivity(intent);
//                    }
//                });
//        alertDialog.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        alertDialog.show();
//    }
}
