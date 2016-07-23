package com.beingcitizen.beingcitizen;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.github.clans.fab.FloatingActionMenu;
import com.rey.material.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saransh on 12-07-2015.
 */
public class CreateCampaign extends AppCompatActivity {
    Spinner consti_spinner, category_spinner, tag_spinner, followable, volunteerable;
    EditText title_create, text_create;
    String category="err", tag="err", consti="err", text="", tags = "", name = "", iname = "", followable_val = "err", volunteerable_val = "err";
    Bitmap bitmap = null;
    boolean camera = false;
    FloatingActionMenu fab_menu;
    // private Dialog dialogLogout;
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
        FloatingActionButton fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        title_create = (EditText)findViewById(R.id.title_create);
        text_create = (EditText) findViewById(R.id.text_create);

        consti_spinner = (Spinner)findViewById(R.id.constituency_spinner);
        RetrieveAllConstituency rac = new RetrieveAllConstituency(this);
        rac.execute();
        tag_spinner = (Spinner)findViewById(R.id.tag_spinner);
        category_spinner = (Spinner)findViewById(R.id.category_spinner);
        volunteerable = (Spinner)findViewById(R.id.volunteerable_spinner);
        followable = (Spinner)findViewById(R.id.followable_spinner);

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
                    tag = (String)parent.getItemAtPosition(position);
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

        followable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                    followable_val = "err";
                else
                    followable_val = (position==1?1:0)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        volunteerable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                    volunteerable_val = "err";
                else
                    volunteerable_val = (position==1?1:0)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_main);
        fab_menu.setClosedOnTouchOutside(true);
        //ImageView buttonLoadImage = (ImageView) findViewById(R.id.buttonLoadPicture);
        fab_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                camera = true;
                insertDummyContactWrapper();
            }
        });
        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = false;
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
                }else if (followable_val.contentEquals("err") || volunteerable_val.contentEquals("err")){
                    Toast.makeText(CreateCampaign.this, "Fill up all options!", Toast.LENGTH_SHORT).show();
                }else {
                    makeCampaign();
                    fab_menu.close(true);
                }
            }
        });
    }

    void makeCampaign(){
        final Dialog dialog = new Dialog(CreateCampaign.this);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setTitle("Updating Indexes");
        dialog.show();
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://beingcitizen.com/bc/index.php/main/createcampaign",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        finish();
                        Log.e("RESPONSE", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        finish();
                        Log.e("RESPONSE", "ERROR!");
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
                params.put("tags", tags);
                params.put("ext", ".JPG");
                params.put("volunteerable", volunteerable_val);
                params.put("followable", followable_val);
                bitmap.recycle();
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

        ImageView imageView = (ImageView) findViewById(R.id.imgView);
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

                    imageView.setImageBitmap(bitmap);

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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
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
                Log.e("PICTURE", picturePath);
                c.close();
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, bitmapOptions);
                bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, 256, 256);
                bitmapOptions.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                Log.e("path of image", picturePath + "");
                imageView.setImageBitmap(bitmap);
            }
        }
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
    protected void onPause() {
        super.onPause();
        if (bitmap!=null){
            if (!bitmap.isRecycled())
                bitmap.recycle();
        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void insertDummyContact() {
        if (camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 1);
            fab_menu.close(true);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
            fab_menu.close(true);
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

        insertDummyContact();
    }

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
                    insertDummyContact();
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
}
