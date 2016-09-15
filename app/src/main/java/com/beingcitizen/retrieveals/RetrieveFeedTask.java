package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.interfaces.login;

import org.json.JSONObject;

/**
 * Created by pankaj on 29/5/16.
 */
public class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {
    login logg = null;

    public RetrieveFeedTask(login context){
        logg = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/login/process?email="+params[0].replace(" ", "%20")+"&password="+params[1].replace(" ", "%20")));
        } catch (Exception ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        if (s!=null && logg != null)
            logg.login_feed(s);
    }
}
