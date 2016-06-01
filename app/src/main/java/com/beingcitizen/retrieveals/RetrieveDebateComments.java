package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.CommentActivity;
import com.beingcitizen.beingcitizen.DebateExpanded;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pankaj on 31/5/16.
 */
public class RetrieveDebateComments extends AsyncTask<String, Void, JSONObject> {
    CommentActivity context;
    String debate_id="0";

    public RetrieveDebateComments(CommentActivity context){
        this.context = context;
        this.debate_id = debate_id;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://tnine.io/bc/main/viewcampaign?uid=16&campaign_id="+params[1]));
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
        context.functions(s);

    }
}