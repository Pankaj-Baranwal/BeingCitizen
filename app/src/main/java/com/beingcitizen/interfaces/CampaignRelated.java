package com.beingcitizen.interfaces;

import org.json.JSONObject;

/**
 * Created by pankaj on 18/7/16.
 */
public interface CampaignRelated {
    void follow_function(JSONObject s);

    void volunteer_function(JSONObject s);

    void unvolunteer_function(JSONObject s);

    void unfollow_function(JSONObject s);
}
