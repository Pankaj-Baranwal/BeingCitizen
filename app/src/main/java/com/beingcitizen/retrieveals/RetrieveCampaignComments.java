package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CommentActivity;

import org.json.JSONObject;

/**
 * Created by pankaj on 2/6/16.
 */
public class RetrieveCampaignComments extends AsyncTask<String, Void, JSONObject> {
    CommentActivity context;

    public RetrieveCampaignComments(CommentActivity context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            String output = http.read("http://beingcitizen.com/bc/index.php/main/viewcampaign?uid="+params[0]+"&campaign_id="+params[1]);
            bool = new JSONObject(output);
        } catch (Exception ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.functions_campaign(s);
    }
}