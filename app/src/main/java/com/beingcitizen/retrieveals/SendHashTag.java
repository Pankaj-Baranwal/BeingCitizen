package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.fragments.AllCampaign;

import org.json.JSONObject;

/**
 * Created by pankaj on 27/6/16.
 */
public class SendHashTag extends AsyncTask<String, Void, JSONObject> {
     AllCampaign context;

    public SendHashTag(AllCampaign context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/createfeed?uid="+params[0]+"&feed="+params[1].replace(" ", "%20")+"&feed-states="+params[2].replace(" ", "%20")));

        } catch (Exception e) {

        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.functions_tag(s);
    }
}