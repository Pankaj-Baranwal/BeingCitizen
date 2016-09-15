package com.beingcitizen.retrieveals;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.beingcitizen.Http;
import com.beingcitizen.interfaces.signUp_interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 29/5/16.
 */
public class RetrieveSignUp extends AsyncTask<String, Void, String> {
    Context context;
    signUp_interface ref;

    public RetrieveSignUp(Context context, signUp_interface ref){
        this.context = context;
        this.ref = ref;
    }

    @Override
    protected String doInBackground(String... params) {
        String bool = "";
        Http http = new Http();
        try {
            bool = http.read("http://beingcitizen.com/bc/index.php/register/reg?name="+params[0]+"&email="+params[1]+"&password="+params[2]+"&gender="+params[3]+"&const="+params[4]);
        } catch (IOException ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONArray arr = new JSONArray(s);
            JSONObject obj = arr.getJSONObject(0);
            ref.result(obj);
        } catch (JSONException e) {
            Toast.makeText(context, "Error retrieving data", Toast.LENGTH_SHORT).show();
        }
    }
}

