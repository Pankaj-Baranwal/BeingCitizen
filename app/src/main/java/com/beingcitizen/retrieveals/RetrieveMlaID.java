package com.beingcitizen.retrieveals;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.interfaces.mla_id;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 28/6/16.
 */
public class RetrieveMlaID extends AsyncTask<String, Void, String> {
    mla_id ref;
    Context mContext;

    public RetrieveMlaID(mla_id ref, Context context){
        this.ref = ref;
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://beingcitizen.com/bc/index.php/main/getmla?const="+params[0].replace(" ", "%20"));
        } catch (IOException ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject obj = new JSONObject(s);
            if (ref != null) {
                if (obj.getJSONArray("mla").length() != 0)
                    ref.mla_ids(obj.getJSONArray("mla").getJSONObject(0).getString("user_id"));
                else{
                    ref.mla_ids("null");
                }
            }
        } catch (JSONException e) {
            Toast.makeText(mContext, "Error retrieving data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

