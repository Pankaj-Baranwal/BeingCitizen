package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.fragments.AllCampaign;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 14/6/16.
 */
public class SendVolunteerCampaign extends AsyncTask<String, Void, JSONObject> {
    CampaignExpanded ccontext;

    public SendVolunteerCampaign(CampaignExpanded ccontext){
        this.ccontext = ccontext;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/volunteercampaign?uid="+params[0]+"&campaign_id="+params[1]));
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
        ccontext.volunteer_function(s);

    }
}