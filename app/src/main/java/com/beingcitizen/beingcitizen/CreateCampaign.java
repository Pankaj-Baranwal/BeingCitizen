package com.beingcitizen.beingcitizen;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Map;

/**
 * Created by saransh on 12-07-2015.
 */
public class CreateCampaign extends AppCompatActivity {
    Spinner consti_spinner, category_spinner, tag_spinner;
    EditText title_create, text_create;
    String category="err", tag="err", consti="err", text="", tags = "", name = "", iname = "";
    Bitmap bitmap = null;
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

        final FloatingActionMenu fab_menu = (FloatingActionMenu) findViewById(R.id.fab_main);
        fab_menu.setClosedOnTouchOutside(true);
        //ImageView buttonLoadImage = (ImageView) findViewById(R.id.buttonLoadPicture);
        fab_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
                fab_menu.close(true);
            }
        });
        fab_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                fab_menu.close(true);
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
}
