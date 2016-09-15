package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CreateCampaign;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 5/7/16.
 */
public class RetrieveTags extends AsyncTask<String, Void, JSONObject> {

    CreateCampaign context;

    public RetrieveTags(CreateCampaign context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/getTags?category="+params[0].replace(" ", "%20")));
        } catch (IOException | JSONException ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.getTags(s);
    }
}