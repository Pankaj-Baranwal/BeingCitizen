package com.beingcitizen.retrieveals;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CreateCampaign;
import com.beingcitizen.interfaces.retrieveCampaign;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        context.allConsts(s);
    }
}