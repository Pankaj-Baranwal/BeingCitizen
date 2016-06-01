package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.signUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 30/5/16.
 */
public class SendCampaign extends AsyncTask<String, Void, String> {
    signUp context;

    public SendCampaign(signUp context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://tnine.io/bc/main/campaign?id="+params[0]+"&title="+params[1]+"&const="+params[2]+"&startedby="+params[3]+"&status="+params[4]+"&category="+params[5]+"&tags="+params[4]+"&status="+params[4]+"&status="+params[4]);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG_ERROR", "ERROR");
        }
        return bool;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject obj = new JSONObject(s);
            context.functions(obj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}



