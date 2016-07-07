package com.beingcitizen.retrieveals;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.adapters.CampaignAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by pankaj on 22/6/16.
 */
public class RetrieveImages {
    /*Activity context;

    public RetrieveImages(Activity context){
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap myBitmap = null;
        Http http = new Http();
        try {
            myBitmap = Glide.with(context.this)
                    .load("http://beingcitizen.com/uploads/campaigns/" + params[0])
                    .asBitmap()
                    .centerCrop()
                    .into(250, 250)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap s) {
        super.onPostExecute(s);
        try {
            context.functions(s);
        } catch (JSONException e) {
            Toast.makeText(context, "Error retrieving data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

*/
}