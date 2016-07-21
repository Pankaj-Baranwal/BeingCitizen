package com.beingcitizen.beingcitizen;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.interfaces.mla_id;
import com.beingcitizen.interfaces.retrieveCampaign;
import com.beingcitizen.interfaces.signUp_interface;
import com.beingcitizen.retrieveals.RetrieveConstitutency;
import com.beingcitizen.retrieveals.RetrieveFeedTask;
import com.beingcitizen.retrieveals.RetrieveMlaID;
import com.beingcitizen.retrieveals.RetrieveSignUp;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by saransh on 14-06-2015.
 */
public class LoginMain extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, retrieveCampaign, signUp_interface, mla_id, com.beingcitizen.interfaces.login {
    SharedPreferences sharedpreferences;
    public Button login,signup;
    public TextView text;
    private SignInButton gplus_signin;
    private LoginButton fb_signin;
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = LoginMain.class.getSimpleName();
    private String facebook_id,f_name, m_name, l_name, profile_image, full_name, email_id="No_Email_ID";
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    // Google client to communicate with Google
    private GoogleApiClient googleApiClient;
    CallbackManager callbackManager;
    private static final int PROFILE_PIC_SIZE = 120;
    ArrayAdapter<String> adapter;
    String name, password, gender, uid = "16";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(LoginMain.this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hideSystemUI();
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        facebookSDKInitialize();
        setContentView(R.layout.login);





        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ) {
            Log.e("PLAYGROUND", "Permission is not granted, requesting");
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.GET_ACCOUNTS,
            },10);
        }







        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);

        text=(TextView)findViewById(R.id.text);


        gplus_signin = (SignInButton) findViewById(R.id.google_signin);
        gplus_signin.setOnClickListener(onLoginListener());

        fb_signin=(LoginButton)findViewById(R.id.fb_signin);
        fb_signin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        fb_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoginDetails(fb_signin);
            }
        });
        //initializing google api client when start
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().
                build()).addScope(Plus.SCOPE_PLUS_PROFILE).addApi(AppIndex.API).build();


        // Font path
        String fontPath = "fonts/GOTHICB_0.TTF";

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font
        login.setTypeface(tf);
        signup.setTypeface(tf);
        text.setTypeface(tf);
    }
    /*
Initialize the facebook sdk.
And then callback manager will handle the login responses.
*/
    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

     /*
  Register a callback function with LoginButton to respond to the login result.
 */

    protected void getLoginDetails(LoginButton login_button){

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                Profile profile = Profile.getCurrentProfile();
                //TODO: get more details from FB as well as Gmail.
                if (profile != null) {
                    facebook_id=profile.getId();
                    f_name=profile.getFirstName();
                    m_name=profile.getMiddleName();
                    l_name=profile.getLastName();
                    full_name=profile.getName();
                    Log.e("FB_PROFILE", facebook_id+" "+ full_name);
                    profile_image=profile.getProfilePictureUri(400, 400).toString();
                }else{
                    Log.e("PROFILE", "NULL");
                }
                getUserInfo(login_result);
//                Toast.makeText(LoginMain.this,full_name,Toast.LENGTH_SHORT).show();
                final Dialog dialogLogout = new Dialog(LoginMain.this);
                dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLogout.setContentView(R.layout.dialog_pincode);
                final EditText pin_txt = (EditText)dialogLogout.findViewById(R.id.pin_txt);
                Button update = (Button) dialogLogout.findViewById(R.id.update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pin_txt.getText().toString().length()!=6){
                            Toast.makeText(LoginMain.this, "Wrong Pincode", Toast.LENGTH_SHORT).show();
                        }else{
                            RetrieveConstitutency rcc = new RetrieveConstitutency(LoginMain.this, LoginMain.this);
                            dialogLogout.cancel();
                            rcc.execute(pin_txt.getText().toString());
                        }
                    }
                });
                dialogLogout.show();
            }

            @Override
            public void onCancel() {
                // code for cancellation
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
            }
        });
    }


    /*
To get the facebook user's own profile information via  creating a new request.
When the request is completed, a callback is called to handle the success condition.
*/
    protected void getUserInfo(LoginResult login_result) {

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            //JSONArray namearray = json_object.names();
                            if (json_object.has("email")) {
                                Log.e("email_fb", json_object.getString("email"));
                                email_id = json_object.getString("email");
                            } else {
                                email_id = json_object.getString("name");
                            }
                            gender = json_object.getString("gender");
                            name = json_object.getString("name");
                            password = "password";
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG_EMAIL", "ERROR");
                        }
                    }
                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,gender,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
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
            final String[] mlaID = new String[1];
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
                        Toast.makeText(LoginMain.this, "Choose constituency", Toast.LENGTH_SHORT).show();
                    }else {
                        SharedPreferences.Editor edit = sharedpreferences.edit();
                        edit.putString("constituency", content[0]);
                        edit.apply();
                        RetrieveMlaID rmlaid = new RetrieveMlaID(LoginMain.this, LoginMain.this);
                        rmlaid.execute(content[0]);
                        dialogLogout.dismiss();
                    }
                }
            });
            dialogLogout.show();
        } catch (JSONException e) {
            Log.e("namearray", "ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public void result(JSONObject obj) {
        if (obj.has("user_id")) {
            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            try {
                uid = obj.getString("user_id");
                edit.putString("id", uid);
                edit.putString("name", obj.getString("name"));
                edit.putString("email", obj.getString("email"));
                edit.putString("sex", obj.getString("gender"));
                edit.apply();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                Log.e("TAG_function", "JSONERROR");
                e.printStackTrace();
            }
        }else if (obj.has("status")){
            try {
                if (obj.getString("status").contentEquals("Already used email")) {
                    RetrieveFeedTask rft = new RetrieveFeedTask(this);
                    rft.execute(email_id, "password");
                }else{
                    Toast.makeText(LoginMain.this, "Error Signing in", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void custimizeSignBtn(){

        gplus_signin = (SignInButton) findViewById(R.id.google_signin);
        gplus_signin.setSize(SignInButton.SIZE_STANDARD);
        gplus_signin.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

    }

    private View.OnClickListener onLoginListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGooglePlusAccount();
            }
        };
    }



    public void onClicksignUp(View v) {
        Intent i = new Intent(getBaseContext(), signUp.class);
        startActivity(i);
        finish();
    }

    public void onClicklogIn(View v) {
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    protected void onStart() {

        super.onStart();
        LoginManager.getInstance().logOut();
        googleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signin Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.beingcitizen/http/host/path")
        );
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signin Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.beingcitizen/http/host/path")
        );
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
        if (googleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
        }
        gPlusRevokeAccess();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
getProfileInfo();

        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//gPlusRevokeAccess();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        //Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInfo();
//        Intent i = new Intent(getBaseContext(), MainActivity.class);
//        startActivity(i);
        finish();
    }


    @Override
    public void onConnectionSuspended(int arg0) {
        googleApiClient.connect();

    }
    private void signInGooglePlusAccount() {
        if (!googleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                googleApiClient.connect();
            }
        }
    }
    /*   @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
       }
   */
//    public void show(String str)
//    {
//        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
//    }

    private void gPlusRevokeAccess() {
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                       googleApiClient.connect();
                            changeUI(false);
                        }

                    });
        }
    }
    private void buidNewGoogleApiClient(){

        googleApiClient =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }
    private void changeUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.google_signin).setVisibility(View.GONE);

        } else {

            findViewById(R.id.google_signin).setVisibility(View.VISIBLE);

        }
    }

    private void getProfileInfo() {

        try {


            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                Log.e("TAG", currentPerson.getDisplayName()+" "+ currentPerson.getGender()+" "+ currentPerson.getName()+" "+ currentPerson.getAboutMe()+ " "+ currentPerson.getBirthday()+" " + currentPerson.getAgeRange()+" "+ currentPerson.getId());
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            Log.getStackTraceString(e);
        }

    }


    private void setPersonalInfo(Person currentPerson){

        name = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        email_id = Plus.AccountApi.getAccountName(googleApiClient);
        gender = currentPerson.getGender()==2?"Female":"Male";
        password = "password";
        final Dialog dialogLogout = new Dialog(LoginMain.this);
        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.setContentView(R.layout.dialog_pincode);
        final EditText pin_txt = (EditText)dialogLogout.findViewById(R.id.pin_txt);
        Button update = (Button) dialogLogout.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin_txt.getText().toString().length()!=6){
                    Toast.makeText(LoginMain.this, "Wrong Pincode", Toast.LENGTH_SHORT).show();
                }else{
                    RetrieveConstitutency rcc = new RetrieveConstitutency(LoginMain.this, LoginMain.this);
                    dialogLogout.cancel();
                    rcc.execute(pin_txt.getText().toString());
                }
            }
        });
        dialogLogout.show();
//        setProfilePic(personPhotoUrl);
    }
    private void setProfilePic(String profile_pic){
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView user_picture = (ImageView)findViewById(R.id.profile_picture);
    }


    @Override
    public void mla_ids(String mlaID) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(LoginMain.this);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        if (!mlaID.contentEquals("null")) {
            edit.putString("mla_id", mlaID);
        }else{
            edit.putString("mla_id", "No_mla_id");
        }
        edit.apply();
        RetrieveSignUp rsup = new RetrieveSignUp(LoginMain.this, LoginMain.this);
        rsup.execute(name.replace(" ", "%20"), email_id, password.replace(" ", "%20"), gender, sharedpreferences.getString("constituency", "").replace(" ", "%20"));
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
        } else {
            Toast.makeText(this, "Incorrect details", Toast.LENGTH_SHORT).show();
        }
    }
}

