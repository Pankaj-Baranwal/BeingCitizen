package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.UserProfileActivity;

import org.json.JSONObject;

/**
 * Created by pankaj on 20/7/16.
 */
public class SendUserUnfollow extends AsyncTask<String, Void, JSONObject> {
    UserProfileActivity context;


    public SendUserUnfollow(UserProfileActivity context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/unfollowUser?followed_by="+params[0]+"&uid="+params[1]));
        } catch (Exception e) {

        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
    }
}