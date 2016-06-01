package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.LoginActivity;
import com.beingcitizen.beingcitizen.signUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 29/5/16.
 */
public class RetrieveSignUp extends AsyncTask<String, Void, String> {
    signUp context;

    public RetrieveSignUp(signUp context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://tnine.io/bc/register/reg?name="+params[0]+"&email="+params[1]+"&password="+params[2]+"&gender="+params[3]+"&const="+params[4]);
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

