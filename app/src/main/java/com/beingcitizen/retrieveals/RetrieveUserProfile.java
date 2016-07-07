package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.UserProfileActivity;
import com.beingcitizen.beingcitizen.signUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 22/6/16.
 */
public class RetrieveUserProfile extends AsyncTask<String, Void, String> {
    UserProfileActivity context;

    public RetrieveUserProfile(UserProfileActivity context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://beingcitizen.com/bc/index.php/main/userprofile?id="+params[0]);
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
            context.functions(obj);
        } catch (JSONException e) {
            Toast.makeText(context, "Error retrieving data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

