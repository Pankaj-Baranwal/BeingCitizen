package com.beingcitizen.beingcitizen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.adapters.Draweradapter;
import com.beingcitizen.R;
import com.beingcitizen.fragments.Campaign;
import com.beingcitizen.fragments.DailyDigest;
import com.beingcitizen.fragments.Debate;
import com.beingcitizen.fragments.TermsCondition;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"Campaign", "Debated", "Daily Digest", "Terms and Conditions", "Help", "Logout"};
    int ICONS[] = {R.drawable.campaign, R.drawable.debates, R.drawable.digest, R.drawable.terms, R.drawable.help, R.drawable.logout};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String user,email;
    JSONObject response, profile_pic_data, profile_pic_url;
    int PROFILE= R.drawable.ic_profile2;
    Fragment campaign = new Campaign();
    Fragment debate = new Debate();
    Fragment dailydigest = new DailyDigest();
    Fragment terms = new TermsCondition();
    private Toolbar toolbar;     // Declaring the Toolbar Object
    ImageView profile_pic;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer; // Declaring DrawerLayout


    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

    profile_pic=(ImageView)findViewById(R.id.profile_picture);
        setUserProfile(jsondata);  // call setUserProfile Method.


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.beingcitizen.beingcitizen",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, campaign);
            ft.commit();
        }
    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Campaign");


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String NAME=sharedpreferences.getString("name", null);
        String EMAIL=sharedpreferences.getString("email", null);
        //String face_id=sharedpreferences.getString("id",null);
//        PROFILE=Integer.parseInt(face_id);
       // PROFILE=getBitmapFromURL("http://graph.facebook.com/" +facebook_id+ "/picture?type=square");
        mAdapter = new Draweradapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of Draweradapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                int childv = recyclerView.getChildPosition(child);
                //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               // LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.item_row, null);
                //TextView textView = (TextView) rowView.findViewById(R.id.rowText);


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    switch (childv) {
                        case 0:
                            float i=motionEvent.getX();
                            if(i<300){
                                Intent l = new Intent(getBaseContext(), UserProfileActivity.class);
                                startActivity(l);
                            }
                            else {
                                Intent l = new Intent(getBaseContext(),MlaProfileActivity.class);
                                startActivity(l);
                            }
                        case 1:
                            Draweradapter.mSelectedPosition=1;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            getSupportActionBar().setTitle("Campaign");
                            ft.replace(R.id.content_frame, campaign);
                           // textView.setTypeface(null, Typeface.BOLD);
                            //textView.setTextColor(Color.parseColor(""));
                            break;
                        case 2:
                            Draweradapter.mSelectedPosition=2;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            getSupportActionBar().setTitle("Debate");
                            ft.replace(R.id.content_frame, debate);
                            break;
                        case 3:
                            Draweradapter.mSelectedPosition=3;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            getSupportActionBar().setTitle("Daily Digest");
                            ft.replace(R.id.content_frame, dailydigest);
                            break;
                        case 4:
                            Draweradapter.mSelectedPosition=4;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            getSupportActionBar().setTitle("Terms & Conditions");
                            ft.replace(R.id.content_frame, terms);
                            break;
                        case 5:
                            Draweradapter.mSelectedPosition=5;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            //TODO: WTF???
                            Toast.makeText(MainActivity.this, "asfsafd: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                            break;
                        case 6:
                            Draweradapter.mSelectedPosition=6;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            logout();
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "default: "+ recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    ft.commit();
                    // Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view

        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void logout() {


        final Dialog dialogLogout = new Dialog(MainActivity.this);
        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.setContentView(R.layout.dialog_for_logout_click);
        TextView msg = (TextView) dialogLogout.findViewById(R.id.txt_for_logout);
        msg.setText("Are you sure you want to logout?");
        Button no = (Button) dialogLogout.findViewById(R.id.btn_no);
        Button yes = (Button) dialogLogout.findViewById(R.id.btn_yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogout.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(MainActivity.this, LoginMain.class);
                MainActivity.this.startActivity(i);
            }
        });

        dialogLogout.show();
    }


    // set this to set according to category  ---Campaign fragment
    @Override
    public void onMenuItemClick(View view, int i) {
        Toast.makeText(MainActivity.this, "Clicked on position: " + i, Toast.LENGTH_SHORT).show();
        Log.e("campaign", "" + i);
    }







    public  void  setUserProfile(String jsondata){
        try {
            response = new JSONObject(jsondata);
             user=response.get("name").toString();
             email=response.get("email").toString();
            //user_name.setText(response.get("name").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));

            Toast.makeText(getApplicationContext(),user+"  "+email,Toast.LENGTH_LONG).show();
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(profile_pic);
        } catch (Exception e){
            e.printStackTrace();
        }
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
}