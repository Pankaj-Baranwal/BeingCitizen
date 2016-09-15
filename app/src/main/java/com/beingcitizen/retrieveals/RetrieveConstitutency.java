package com.beingcitizen.retrieveals;

import android.content.Context;
import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.interfaces.retrieveCampaign;

import org.json.JSONObject;

/**
 * Created by pankaj on 15/6/16.
 */
public class RetrieveConstitutency extends AsyncTask<String, Void, JSONObject> {

    Context context;
    boolean mains = false;
    retrieveCampaign rtC;

    public RetrieveConstitutency(Context context, retrieveCampaign rtC){
        this.context = context;
        this.rtC = rtC;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/constituency?pincode="+params[0]));
        } catch (Exception ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        if (rtC!=null)
        rtC.constituency(s);
    }
}