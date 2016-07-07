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
 * Created by pankaj on 15/6/16.
 */
public class SendDebateComment extends AsyncTask<String, Void, JSONObject> {
    CommentActivity context;

    public SendDebateComment(CommentActivity context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/argue?uid="+params[0]+"&debate_id="+params[1]+"&nature="+params[2]+"&comment="+params[3]));

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