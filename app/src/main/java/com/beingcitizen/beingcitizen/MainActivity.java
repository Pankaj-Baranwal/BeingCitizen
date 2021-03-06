package com.beingcitizen.beingcitizen;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.adapters.Draweradapter;
import com.beingcitizen.fragments.Campaign;
import com.beingcitizen.fragments.DailyDigest;
import com.beingcitizen.fragments.Debate;
import com.beingcitizen.fragments.Help;
import com.beingcitizen.fragments.TermsCondition;
import com.beingcitizen.interfaces.adapterUpdate;
import com.beingcitizen.interfaces.getUserProfile;
import com.beingcitizen.interfaces.retrieveCamp;
import com.beingcitizen.retrieveals.RetrieveCampaign;
import com.beingcitizen.retrieveals.RetrieveUserProfile;
import com.squareup.picasso.Picasso;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Pankaj Baranwal on 14-07-2016
 *
 * Handles calls to all fragments like campaign, debate, about us, logout etc.
 * Also responsible for navigation drawer functionality.
 */

public class MainActivity extends AppCompatActivity implements OnMenuItemClickListener, retrieveCamp, getUserProfile {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"Campaign", "Debate", "Daily Digest", "Privacy Policy", "About", "Logout"};
    String menuTITLES[] = {"Law and Order", "Public health and Sanitation", "Communication", "Water-Irrigation,Drainage,Embankments", "Lands, Agriculture", "Trade,Commerce,Employment", "Environment and Horticulture", "Tourism, Art and Culture", "Power", "Corruption/Vigillance"};
    int ICONS[] = {R.drawable.campaign, R.drawable.debates, R.drawable.digest, R.drawable.terms, R.drawable.help, R.drawable.logout};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME_i, EMAIL_i, MLA_nam1e="No_mla_id", consti;
    String selected="";

    int PROFILE= R.drawable.ic_profile2;
    Fragment campaign = new Campaign();
    Fragment debate = new Debate();
    Fragment dailydigest = new DailyDigest();
    Fragment terms = new TermsCondition();
    Fragment about = new Help();
    private Toolbar toolbar;     // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer; // Declaring DrawerLayout


    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    boolean exiting = false;
    public RelativeLayout rl;
//    FragmentManager fragmentManager = getSupportFragmentManager();
//    FragmentTransaction ft = fragmentManager.beginTransaction();
//
//    RecyclerView recyclerVi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl = (RelativeLayout)findViewById(R.id.progress_imageLoading);
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
        editor = sharedpreferences.edit();
        NAME_i=sharedpreferences.getString("name", null);
        EMAIL_i=sharedpreferences.getString("email", null);
        final String MLA_name = sharedpreferences.getString("mla_id", "No_mla_id");
        consti = sharedpreferences.getString("constituency", "");
        mAdapter = new Draweradapter(TITLES, ICONS, NAME_i, EMAIL_i, MLA_name, consti, PROFILE);
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        RetrieveUserProfile userProfile = new RetrieveUserProfile(this);
        userProfile.execute(sharedpreferences.getString("id", "17"), sharedpreferences.getString("id", "17"));
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                exiting = false;
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                int childv = recyclerView.getChildPosition(child);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    switch (childv) {
                        case 0:
                            //TODO: Profiles of user and MLA
                            float i=motionEvent.getX();
                            if(i<300){
                                Intent l = new Intent(getBaseContext(), UserProfileActivity.class);
                                startActivity(l);
                            }
                            else {
                                if (!MLA_name.contentEquals("No_mla_id")) {
                                    Intent l = new Intent(getBaseContext(), MlaProfileActivity.class);
                                    startActivity(l);
                                }
                            }
                        case 1:
                            Draweradapter.mSelectedPosition=1;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            getSupportActionBar().setTitle("Campaign");
                            ft.replace(R.id.content_frame, campaign);
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
                            getSupportActionBar().setTitle("Privacy Policy");
                            ft.replace(R.id.content_frame, terms);
                            break;
                        case 5:
                            Draweradapter.mSelectedPosition=5;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            getSupportActionBar().setTitle("About");
                            ft.replace(R.id.content_frame, about);
                            break;
                        case 6:
                            Draweradapter.mSelectedPosition=6;
                            recyclerView.getAdapter().notifyDataSetChanged();
                            logout();
                            break;
                        default:

                            break;
                    }
                    ft.commit();

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
        android.widget.Button no = (android.widget.Button) dialogLogout.findViewById(R.id.btn_no);
        android.widget.Button yes = (android.widget.Button) dialogLogout.findViewById(R.id.btn_yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogout.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                dialogLogout.dismiss();
//                LoginManager.getInstance().logOut();
                Intent i = new Intent(MainActivity.this, LoginMain.class);
                MainActivity.this.startActivity(i);
                finish();
            }
        });

        dialogLogout.show();
    }


    // set this to set according to category  ---Campaign fragment
    @Override
    public void onMenuItemClick(View view, int i) {
        if (i!=0) {
            selected = menuTITLES[i-1];
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            RetrieveCampaign rtC = new RetrieveCampaign(MainActivity.this);
            String uid = sp.getString("id", "16");
            rtC.execute(uid);
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

    @Override
    public void retrieve(JSONObject param) {
        try {
            if (param.getJSONArray("campaigns")!=null && param.getJSONArray("campaigns").length()!=0) {
                JSONArray jA;
                JSONArray jASorted = new JSONArray();
                jA = param.getJSONArray("campaigns");
                for (int i=0; i<jA.length(); i++) {
                    if (jA.getJSONObject(i).getString("category").contains(selected))
                        jASorted.put(jA.getJSONObject(i));
                }
                if (jASorted.length()==0){
                    Toast.makeText(MainActivity.this, "No campaigns found!", Toast.LENGTH_SHORT).show();
                }
                for (int i=0; i<jA.length(); i++) {
                    if (!jA.getJSONObject(i).getString("category").contains(selected))
                        jASorted.put(jA.getJSONObject(i));
                }

                    adapterUpdate upd = Campaign.alc;
                    upd.updateAdapt(jASorted);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Draweradapter.mSelectedPosition=1;
    }

    @Override
    public void getImage(String img) {
        String image_loc = "http://beingcitizen.com/uploads/display/"+img;
        if (Draweradapter.profile!=null)
        Picasso.with(this).load(image_loc).resize(256, 256).error(R.drawable.ic_profile2).into(Draweradapter.profile);
    }

    @Override
    public void onBackPressed() {
        if (!exiting){
            exiting = true;
            Toast.makeText(MainActivity.this, "Press again to exit!", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }
}