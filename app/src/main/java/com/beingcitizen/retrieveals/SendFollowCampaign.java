package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;

import org.json.JSONObject;

/**
 * Created by pankaj on 13/6/16.
 */
public class SendFollowCampaign extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/followcampaign?uid="+params[0]+"&campaign_id="+params[1]));
        } catch (Exception e) {

        }
        return null;
    }
}


