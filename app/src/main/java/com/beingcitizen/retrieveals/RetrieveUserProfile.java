package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.UserProfileActivity;
import com.beingcitizen.interfaces.getUserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 22/6/16.
 */
public class RetrieveUserProfile extends AsyncTask<String, Void, String> {
    UserProfileActivity context;
    getUserProfile getUserProfile;
    boolean notUser = false;

    public RetrieveUserProfile(UserProfileActivity context){
        notUser = false;
        this.context = context;
    }

    public RetrieveUserProfile(getUserProfile getUserProfile){
        this.getUserProfile = getUserProfile;
        notUser = true;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://beingcitizen.com/bc/index.php/main/userprofile?current_user="+params[0]+"&id="+params[1]);
        } catch (IOException ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject obj = new JSONObject(s);
            if (!notUser) {
                context.functions(obj);
            }else{
                if (getUserProfile!=null){
                    getUserProfile.getImage(obj.getJSONArray("info").getJSONObject(0).getString("uimage")+obj.getJSONArray("info").getJSONObject(0).getString("uext"));
                }
            }
        } catch (JSONException e) {
            if (!notUser)
            Toast.makeText(context, "Error retrieving data", Toast.LENGTH_SHORT).show();
        }
    }
}

