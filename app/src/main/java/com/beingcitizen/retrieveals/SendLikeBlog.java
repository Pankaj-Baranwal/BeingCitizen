package com.beingcitizen.retrieveals;

import android.os.AsyncTask;
import android.util.Log;

import com.beingcitizen.Http;
import com.beingcitizen.beingcitizen.BlogExpanded;
import com.beingcitizen.beingcitizen.CampaignExpanded;
import com.beingcitizen.fragments.AllCampaign;
import com.beingcitizen.fragments.Blogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
            context.like_func(s);
    }
}