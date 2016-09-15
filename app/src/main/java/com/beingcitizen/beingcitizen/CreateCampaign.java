package com.beingcitizen.beingcitizen;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beingcitizen.R;
import com.beingcitizen.retrieveals.RetrieveAllConstituency;
import com.beingcitizen.retrieveals.RetrieveTags;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saransh on 12-07-2015.
 *
 * Handles new campaign creation.
 */
public class CreateCampaign extends AppCompatActivity {
    Spinner consti_spinner, category_spinner, tag_spinner;
    EditText title_create, text_create;
    String category="err", tag="err", consti="err", text="", name = "", iname = "temp", followable_val = "1", volunteerable_val = "1";
    Bitmap bitmap = null;
    CheckBox can_follow, can_volunteer;
    boolean calledUpload = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcampaign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Campaign");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);

        title_create = (EditText)findViewById(R.id.title_create);
        text_create = (EditText) findViewById(R.id.text_create);

        consti_spinner = (Spinner)findViewById(R.id.constituency_spinner);
        RetrieveAllConstituency rac = new RetrieveAllConstituency(this);
        rac.execute();
        tag_spinner = (Spinner)findViewById(R.id.tag_spinner);
        category_spinner = (Spinner)findViewById(R.id.category_spinner);
        can_volunteer = (CheckBox) findViewById(R.id.can_volunteer);
        can_follow = (CheckBox) findViewById(R.id.can_follow);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    category = "err";
                }else{
                    category = (String)parent.getItemAtPosition(position);
                    RetrieveTags rtags = new RetrieveTags(CreateCampaign.this);
                    rtags.execute(category.replace(" ", "%20"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tag_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    tag = "err";
                }else
                    tag = ((String)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        consti_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    consti = "err";
                }else{
                    consti = (String)parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        can_follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    followable_val = isChecked?"1":"0";
            }
        });
        can_volunteer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                volunteerable_val = isChecked?"1":"0";
            }
        });


        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDummyContactWrapper();
            }
        });


        Button update = (Button) findViewById(R.id.campaign_create);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = title_create.getText().toString();
                text = text_create.getText().toString();
                if (text.length()<3){
                    Toast.makeText(CreateCampaign.this, "Add more content!", Toast.LENGTH_SHORT).show();
                }else if (name.length()<3){
                    Toast.makeText(CreateCampaign.this, "Title too small!", Toast.LENGTH_SHORT).show();
                }else if (consti.contentEquals("err")){
                    Toast.makeText(CreateCampaign.this, "Enter correct Constituency", Toast.LENGTH_SHORT).show();
                }else if (category.contentEquals("errr")){
                    Toast.makeText(CreateCampaign.this, "Enter correct category", Toast.LENGTH_SHORT).show();
                }else if (tag.contentEquals("errr")){
                    Toast.makeText(CreateCampaign.this, "Enter correct Tag", Toast.LENGTH_SHORT).show();
                }else if (bitmap==null){
                    Toast.makeText(CreateCampaign.this, "Upload an image", Toast.LENGTH_SHORT).show();
                }else if (followable_val.contentEquals("0") && volunteerable_val.contentEquals("0")){
                    Toast.makeText(CreateCampaign.this, "Fill up all options!", Toast.LENGTH_SHORT).show();
                }else {
                    if (!calledUpload)
                        makeCampaign();
                }
            }
        });
    }

    void makeCampaign(){
        final Dialog dialog = new Dialog(CreateCampaign.this);
        dialog.setContentView(R.layout.dialog_create_campaign);
        dialog.setTitle("Updating Indexes");
        dialog.show();
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://beingcitizen.com/bc/index.php/main/createcampaign",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        calledUpload = false;
                        dialog.dismiss();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        calledUpload = false;
                        Toast.makeText(CreateCampaign.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CreateCampaign.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sp.getString("id", "17"));
                params.put("name", name);
                params.put("iname", iname);
                params.put("image", getStringImage(bitmap));
                params.put("text", text);
                params.put("const", consti);
                params.put("category", category);
                params.put("tags", tag);
                params.put("ext", ".JPG");
                params.put("volunteerable", volunteerable_val);
                params.put("followable", followable_val);
                return params;
            }
        };
        calledUpload = true;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);
    }

    public String getStringImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = (float) 0.80;
        float scaleHeight = (float) 0.80;
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

    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri =  getCaptureImageOutputUri();

        List<Intent> allIntents = new  ArrayList<>();
        PackageManager packageManager =  getPackageManager();

// collect all camera intents
        Intent captureIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new  Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    @Override
    public void onActivityResult(int  requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final ImageView imageView = (ImageView) findViewById(R.id.imgView);
            Uri imageUri = getPickImageResultUri(data);
            iname = getRealPathFromURI(imageUri);
            final File file = new File(iname);
            Picasso.with(CreateCampaign.this).load(file).resize(512, 512).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Picasso.with(CreateCampaign.this).load(file).resize(120, 120).into(imageView);
                    savebitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Toast.makeText(CreateCampaign.this, "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    
                }
            });
        }
    }

    private void savebitmap(Bitmap bmp) {
        FileOutputStream fOut = null;
        if (bmp!=null)
            bitmap = bmp;
        else
            Toast.makeText(CreateCampaign.this, "Please try again!", Toast.LENGTH_SHORT).show();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
            iname = sdf.format(new Date())+"pickImageResult.jpg";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), iname);
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException ignored) {
        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Uri getPickImageResultUri(Intent  data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }

    private Uri getCaptureImageOutputUri() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File f = new File(file, "pickImageResult.jpg");
        Uri outputFileUri = Uri.fromFile(f);
        return outputFileUri;
    }

    public void allConsts(JSONObject s) {
        if (s!=null){
            ArrayList<String> cons = new ArrayList<>();
            cons.add("Select you constituency");
            try {
                for (int i=0; i<s.getJSONArray("consts").length(); i++)
                    cons.add(s.getJSONArray("consts").getJSONObject(i).getString("Constituency"));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cons);
                consti_spinner.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getTags(JSONObject s) {
        try {
            JSONArray jA = s.getJSONArray("tags");
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("--Select Tag--");
            for (int i=0; i<jA.length(); i++){
                arrayList.add(jA.getString(i));
            }
            if (arrayList.size()!=0){
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
                tag_spinner.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                finish();
        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap!=null){
            if (!bitmap.isRecycled())
                bitmap.recycle();
        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CreateCampaign.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    startActivityForResult(getPickImageChooserIntent(), 200);
                } else {
                    // Permission Denied
                    Toast.makeText(CreateCampaign.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CreateCampaign.this, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        startActivityForResult(getPickImageChooserIntent(), 200);
    }
}
