package com.beingcitizen.beingcitizen;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveUserProfile;
import com.beingcitizen.retrieveals.SendUserFollow;
import com.beingcitizen.retrieveals.SendUserUnfollow;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saransh on 12-07-2015.
 */
public class UserProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    TextView nameview,emailview;
    ImageView profile;
    SharedPreferences sharedpreferences;
    String uid = "16", uid_viewer = "16";
    TextView follow_button;
    boolean followed = false;
    Bitmap bitmap=null;
    String iname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_layout1);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        follow_button = (TextView) findViewById(R.id.follow_button);
        uid_viewer = sharedpreferences.getString("id", "16");
        if (getIntent().getExtras()!=null) {
            uid = getIntent().getExtras().getString("uid");
            if (uid.contentEquals(uid_viewer)){
                follow_button.setVisibility(View.GONE);
            }
        }else{
            uid = uid_viewer;
            follow_button.setVisibility(View.GONE);
        }
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!followed){
                    followed = true;
                    follow_button.setText("  FOLLOWED    ");
                    SendUserFollow userFollow = new SendUserFollow(UserProfileActivity.this);
                    userFollow.execute(uid_viewer, uid);
                }else{
                    followed = false;
                    follow_button.setText("+ FOLLOW      ");
                    SendUserUnfollow userUnfollow = new SendUserUnfollow(UserProfileActivity.this);
                    userUnfollow.execute(uid_viewer, uid);
                }
            }
        });
        nameview=(TextView)findViewById(R.id.name);
        emailview=(TextView)findViewById(R.id.email);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        profile = (ImageView)findViewById(R.id.profile_picture);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.contentEquals(uid_viewer)) {
                    final Dialog dialogLogout = new Dialog(UserProfileActivity.this);
                    dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogLogout.setContentView(R.layout.dialog_changepic);
                    FloatingActionButton fab_camera = (FloatingActionButton) dialogLogout.findViewById(R.id.fab_camera);
                    FloatingActionButton fab_gallery = (FloatingActionButton) dialogLogout.findViewById(R.id.fab_gallery);
                    fab_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        }
                    });
                    fab_gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        }
                    });
                    Button update = (Button) dialogLogout.findViewById(R.id.update);
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bitmap != null) {
                                sendUserImage();
                                dialogLogout.dismiss();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "Choose Image first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialogLogout.show();
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RetrieveUserProfile rup = new RetrieveUserProfile(this);
        rup.execute(uid_viewer, uid);
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
            TextView hash = (TextView) findViewById(R.id.feed);
            TextView hash_tag = (TextView) findViewById(R.id.hash_tag);
            if (obj.getJSONArray("feed").length()>0) {
                hash.setText(obj.getJSONArray("feed").getJSONObject(0).getString("content"));
                hash_tag.setText(obj.getJSONArray("feed").getJSONObject(0).getString("tag"));
            }
            //hash.setText(obj.getJSONArray("feed").getJSONObject(0).getString("content"));
            if (const_view != null) {
                const_view.setText(constituency);
            }
            if (numConnect != null) {
                numConnect.setText(campsStarted.length()+"");
            }
            if (numCamps != null) {
                numCamps.setText(campsFollowed.length()+"");
            }
            if (obj.getString("follow").contentEquals("true")) {
                followed = true;
                follow_button.setText("  FOLLOWED    ");
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
            int maxLength = 16;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            tv.setFilters(fArray);
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
            int maxLength = 16;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            tv.setFilters(fArray);
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

    void sendUserImage(){
        final Dialog dialog = new Dialog(UserProfileActivity.this);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setTitle("Updating Index");
        dialog.show();
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://beingcitizen.com/bc/index.php/main/editPhoto",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.e("RESPONSE", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this, "Error Uploading", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Log.e("RESPONSE", "ERROR!");
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sp.getString("id", "17"));
                params.put("iname", uid_viewer);
                params.put("image", getStringImage(bitmap));
                params.put("ext", ".jpg");
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    public String getStringImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = (float) 0.25;
        float scaleHeight = (float) 0.25;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bmp, 0, 0, width, height, matrix, false);
        bmp = resizedBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //dialogLogout.dismiss();
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, 256, 256);
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    profile.setImageBitmap(bitmap);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    iname = String.valueOf(System.currentTimeMillis());
                    File file = new File(path, iname + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                File f = new File(picturePath);
                iname = f.getName();
                Log.e("PICTURE", picturePath);
                c.close();
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, bitmapOptions);
                bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, 256, 256);
                bitmapOptions.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                Log.e("path of image", picturePath + "");
                profile.setImageBitmap(bitmap);
            }
        }
        Log.e("Picture Path", iname);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
