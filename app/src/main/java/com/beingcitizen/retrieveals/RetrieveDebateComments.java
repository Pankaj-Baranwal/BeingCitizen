package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CommentActivity;

import org.json.JSONObject;

/**
 * Created by pankaj on 31/5/16.
 */
public class RetrieveDebateComments extends AsyncTask<String, Void, JSONObject> {
    CommentActivity context;

    public RetrieveDebateComments(CommentActivity context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/viewdebate?uid="+params[0]+"&debate_id="+params[1]));
        } catch (Exception ignored) {
        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        context.functions_debate(s);

    }
}