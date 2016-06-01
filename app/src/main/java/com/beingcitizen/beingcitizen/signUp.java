package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveSignUp;
import com.beingcitizen.utils.AppLocationService;
import com.beingcitizen.utils.LocationAddress;

/**
 * Created by saransh on 14-06-2015.
 */
public class signUp extends Activity {

    public EditText name,password,email,pin;
    public Button signup;
    public Spinner constituency, gender;
    public String cont="", gender_text="";

    /**************************************/
    AppLocationService appLocationService;
    /***************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText3);
        email=(EditText)findViewById(R.id.editText2);
        pin=(EditText)findViewById(R.id.editText4);
        gender=(Spinner) findViewById(R.id.gender_spinner);
        signup=(Button) findViewById(R.id.button);
        constituency=(Spinner) findViewById(R.id.contituency_spinner);

        //ArrayAdapter adapter1=ArrayAdapter.createFromResource(this,R.array.constituency_array,R.layout.spinner_item);

        final ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.constituency_array, R.layout.spinner_item);

        constituency.setAdapter(adapter);
        constituency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                    cont = (String) parent.getItemAtPosition(position);
                else
                    Toast.makeText(signUp.this, "Incorrect Entry", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, R.layout.spinner_item);

        gender.setAdapter(adapter1);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                gender_text= (String) parent.getItemAtPosition(position);
                else
                    Toast.makeText(signUp.this, "Incorrect Entry", Toast.LENGTH_SHORT).show();
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
        pin.setTypeface(tf);
        signup.setTypeface(tf1);

        //Spinner constituency = (Spinner) findViewById(R.id.contituency_spinner);
        // Create an adapter from the string array resource and use
        // android's inbuilt layout file simple_spinner_item
        // that represents the default spinner in the UI
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.constituency_array, android.R.layout.simple_spinner_item);
        // Set the layout to use for each dropdown item
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //constituency.setAdapter(adapter);
        /*************************************/
        appLocationService = new AppLocationService(signUp.this);

        Location location = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);



        //you can hard-code the lat & long if you have issues with getting it
        //remove the below if-condition and use the following couple of lines
        //double latitude = 37.422005;
        //double longitude = -122.084095

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        } else {
            //showSettingsAlert();
        }

        /***************************************/
    }

    public void onClickSign(View v) {
        if(name.getText().toString().length()>0 && email.getText().toString().length()>0 && password.getText().toString().length()>0 && gender_text.length()>0 && cont.length()>0) {
            RetrieveSignUp retrieveSignUp = new RetrieveSignUp(this);
            retrieveSignUp.execute(name.getText().toString(), email.getText().toString(), password.getText().toString(), gender_text, cont);
        }else{
            Toast.makeText(this, "Details incomplete", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), LoginMain.class);
        startActivity(i);
        finish();
    }

    public void functions(String status) {
        if (status.contentEquals("success")){
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            pin.setText(locationAddress);
        }
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                signUp.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        signUp.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
