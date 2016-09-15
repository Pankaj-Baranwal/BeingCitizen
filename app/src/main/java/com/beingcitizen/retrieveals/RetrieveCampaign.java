package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.interfaces.retrieveCamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 30/5/16.
 */
public class RetrieveCampaign extends AsyncTask<String, Void, JSONObject> {

    retrieveCamp ref;

    public RetrieveCampaign(retrieveCamp context){
        ref = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/campaign?id="+params[0]));

        } catch (IOException | JSONException ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        if (ref!=null && s!=null) {
            ref.retrieve(s);
        }
    }
}


