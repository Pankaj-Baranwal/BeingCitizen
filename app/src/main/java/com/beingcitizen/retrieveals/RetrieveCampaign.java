package com.beingcitizen.retrieveals;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.signUp;
import com.beingcitizen.fragments.AllCampaign;
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
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/campaign?uid="+params[0]));

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
        if (ref!=null && s!=null) {
            ref.retrieve(s);
        }
        else
            Log.e("NULL", "not geting right json");
    }
}


