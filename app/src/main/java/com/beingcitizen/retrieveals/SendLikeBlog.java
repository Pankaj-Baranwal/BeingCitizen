package com.beingcitizen.retrieveals;

import android.os.AsyncTask;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.BlogExpanded;

import org.json.JSONObject;

/**
 * Created by pankaj on 22/6/16.
 */
public class SendLikeBlog extends AsyncTask<String, Void, JSONObject> {
    BlogExpanded context;

    public SendLikeBlog(BlogExpanded context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject bool = null;
        Http http = new Http();
        try {
            //vid = 'upvote' or 'downvote, cid = blog id
            //TODO: Error because UID.
            bool = new JSONObject(http.read("http://beingcitizen.com/bc/index.php/main/blogVote?vid="+params[0]+"&cid="+params[1]+"&uid="+params[2]));
        } catch (Exception e) {

        }
        return bool;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
            context.like_func(s);
    }
}