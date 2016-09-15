package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.fragments.Polls;

import org.json.JSONObject;

/**
 * Created by pankaj on 22/6/16.
 */
public class SendPollResult extends AsyncTask<String, Void, JSONObject> {
    Polls context;
    CampaignExpanded ccontext;
    boolean allC = true;

    public SendPollResult(Polls context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/pollVote?uid="+params[0]+"&pid="+params[1]+"&vote="+params[2]));
        } catch (Exception ignored) {

        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        if (allC)
            context.poll_function(s);
    }
}


