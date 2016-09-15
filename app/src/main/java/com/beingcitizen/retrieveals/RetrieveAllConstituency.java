package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CreateCampaign;

import org.json.JSONObject;

/**
 * Created by pankaj on 4/7/16.
 */
public class RetrieveAllConstituency extends AsyncTask<String, Void, JSONObject> {

    CreateCampaign context;
    boolean mains = false;

    public RetrieveAllConstituency(CreateCampaign context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/getConsts"));
        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.allConsts(s);
    }
}