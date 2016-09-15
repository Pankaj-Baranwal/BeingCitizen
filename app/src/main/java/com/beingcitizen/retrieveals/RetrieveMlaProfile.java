package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.MlaProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 27/6/16.
 */
public class RetrieveMlaProfile extends AsyncTask<String, Void, String> {
    MlaProfileActivity context;

    public RetrieveMlaProfile(MlaProfileActivity context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://beingcitizen.com/bc/index.php/main/mlaprofile?id="+params[0]+"&user_id="+params[1]);
        } catch (IOException ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject obj = new JSONObject(s);
            context.functions(obj);
        } catch (JSONException e) {
            Toast.makeText(context, "Error retrieving data", Toast.LENGTH_SHORT).show();
        }
    }
}

