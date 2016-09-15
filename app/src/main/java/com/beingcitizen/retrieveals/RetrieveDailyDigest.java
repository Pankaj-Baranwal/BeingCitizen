package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.fragments.DailyDigest;

import org.json.JSONObject;

/**
 * Created by pankaj on 2/6/16.
 */
public class RetrieveDailyDigest extends AsyncTask<String, Void, JSONObject> {
    DailyDigest context;

    public RetrieveDailyDigest(DailyDigest context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/poll?uid="+params[0]));

        } catch (Exception ignored) {

        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.functions(s);
    }
}

