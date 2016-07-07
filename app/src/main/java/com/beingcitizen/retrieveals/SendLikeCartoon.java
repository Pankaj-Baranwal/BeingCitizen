package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.fragments.Cartoons;
import com.beingcitizen.fragments.Polls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 22/6/16.
 */
public class SendLikeCartoon extends AsyncTask<String, Void, JSONObject> {
    Cartoons context;
    boolean allC = true;

    public SendLikeCartoon(Cartoons context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/cartoonVote?uid="+params[0]+"&cid="+params[1]));
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
        if (allC)
            context.cartoon_function(s);
    }
}


