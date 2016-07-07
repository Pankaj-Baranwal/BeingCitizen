package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.LoginActivity;
import com.beingcitizen.fragments.DailyDigest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG_ERROR", "ERROR");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.functions(s);
    }
}

