package com.beingcitizen.beingcitizen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.beingcitizen.R;
import com.beingcitizen.adapters.Draweradapter;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by saransh on 14-06-2015.
 */
public class LoginMain extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public Button login,signup;
    public TextView skip,text;
    private SignInButton gplus_signin;
    private LoginButton fb_signin;
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = LoginMain.class.getSimpleName();
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    android.widget.VideoView video_player_view;
    DisplayMetrics dm;
    SurfaceView sur_view;
    Thread th;
    // Google client to communicate with Google
    private GoogleApiClient googleApiClient;
    private int requestCode;
    private int responseCode;
    private Intent intent;
    CallbackManager callbackManager;
    private static final int PROFILE_PIC_SIZE = 120;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

            Log.d("PLAYGROUND", "Permission is not granted, requesting");


            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.GET_ACCOUNTS,
            },10);

        }







        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);

        skip=(TextView)findViewById(R.id.skip);
        text=(TextView)findViewById(R.id.text);


        gplus_signin = (SignInButton) findViewById(R.id.google_signin);
        gplus_signin.setOnClickListener(onLoginListener());

        fb_signin=(LoginButton)findViewById(R.id.fb_signin);
        fb_signin.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));


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
        skip.setTypeface(tf);
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
             Intent intent = new Intent(LoginMain.this,MainActivity.class);
                startActivity(intent);
                getUserInfo(login_result);
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    facebook_id=profile.getId();
                    f_name=profile.getFirstName();
                    m_name=profile.getMiddleName();
                    l_name=profile.getLastName();
                    full_name=profile.getName();

                    profile_image=profile.getProfilePictureUri(400, 400).toString();
                }
                Toast.makeText(LoginMain.this,full_name,Toast.LENGTH_SHORT).show();


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
    protected void getUserInfo(LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            email_id=json_object.getString("email");

                            show(email_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("name", full_name);
                        editor.putString("email", email_id);
                        editor.putString("profile_pic", profile_image);
                        editor.putString("id",facebook_id);
                        editor.commit();


                        Intent intent = new Intent(LoginMain.this,MainActivity.class);
                        intent.putExtra("jsondata",json_object.toString());
                        startActivity(intent);
                    }
                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

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
                Log.i(TAG, "login clicked!");
                signInGooglePlusAccount();
            }
        };
    }



    public void onClicksignUp(View v) {
        Intent i = new Intent(getBaseContext(), signUp.class);
        startActivity(i);
        finish();
    }

    public void onClickSkip(View v) {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void onClicklogIn(View v) {
        Log.e("tag", "ENtering??");
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
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();

/*
       // Get user's information
        getUserInformation();

        // Update the UI after signin
        layoutAction(true);
*/
/*       Plus.PeopleApi.loadVisible(googleApiClient, null).setResultCallback(this);



        if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
            Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            personNameView.setText(person.getDisplayName());
            if (person.hasImage()) {

                Person.Image image = person.getImage();


                new AsyncTask<String, Void, Bitmap>() {

                    @Override
                    protected Bitmap doInBackground(String... params) {

                        try {
                            URL url = new URL(params[0]);
                            InputStream in = url.openStream();
                            return BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                        // TODO log error
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        personImageView.setImageBitmap(bitmap);
                    }
                }.execute(image.getUrl());
            }*/
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
    public void show(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private void gPlusRevokeAccess() {
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
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
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void setPersonalInfo(Person currentPerson){

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String email = Plus.AccountApi.getAccountName(googleApiClient);
        String dob =currentPerson.getBirthday();
        String tag_line=currentPerson.getTagline();
        String about_me=currentPerson.getAboutMe();
        setProfilePic(personPhotoUrl);
        //progress_dialog.dismiss();
        Log.i("name",personName);
        show(personName+"  "+email);
        Log.i("email",email);
        Log.i("dob",dob);
        Log.i("tag_line",tag_line);
        Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();
    }
    private void setProfilePic(String profile_pic){
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView    user_picture = (ImageView)findViewById(R.id.profile_picture);
  //      new LoadProfilePic(user_picture).execute(profile_pic);
    }
/*
    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmap_img;

        public LoadProfilePic(ImageView bitmap_img) {
            this.bitmap_img = bitmap_img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap result_img) {

            bitmap_img.setImageBitmap(result_img);
        }
    }
*/



}

