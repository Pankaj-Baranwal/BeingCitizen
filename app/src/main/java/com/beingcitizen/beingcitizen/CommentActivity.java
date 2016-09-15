package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.beingcitizen.R;
import com.beingcitizen.adapters.CommentAdapter;
import com.beingcitizen.adapters.CommentCampaignAdapter;
import com.beingcitizen.interfaces.callUserProfile;
import com.beingcitizen.retrieveals.RetrieveCampaignComments;
import com.beingcitizen.retrieveals.RetrieveDebateComments;
import com.beingcitizen.retrieveals.SendCampaignComment;
import com.beingcitizen.retrieveals.SendDebateComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saransh on 14-06-2015.
 *
 * Handles populating comments for both debate and campaign.
 */
public class CommentActivity extends Activity implements callUserProfile {
    String uid = "16", calling= null;
    CommentAdapter ca;
    CommentCampaignAdapter cca;
    private ListView commentList;
    String debate_id, nature = "err";
    Spinner debate_spinner;
    boolean first_time = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allcomments);
        //uid = getIntent().getExtras().getString("uid");
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = sharedpreferences.getString("id", "16");
        final EditText comment = (EditText)findViewById(R.id.editText5);
        commentList = (ListView)findViewById(R.id.allcomment_listview);
        debate_spinner = (Spinner)findViewById(R.id.debate_spinner);
        debate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                    nature = position==1?"yes":"no";
                else
                    nature = "err";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (getIntent().getExtras()!=null) {
            if (getIntent().getExtras().containsKey("calling")) {
                calling = getIntent().getExtras().getString("calling");
            }
        }
        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calling!=null){
                    if (nature.contentEquals("err")){
                        Toast.makeText(CommentActivity.this, "Incomplete Entry", Toast.LENGTH_SHORT).show();
                    }else {
                        SendDebateComment sdc = new SendDebateComment(CommentActivity.this);
                        String comm = comment.getText().toString();
                        comm = comm.replace("\n", "\\n");
                        comm = comm.replace(" ", "%20");
                        sdc.execute(uid, debate_id, nature, comm);
                        comment.setText("");
                    }
                    }else {
                        SendCampaignComment sdc = new SendCampaignComment(CommentActivity.this);
                        String comm = comment.getText().toString();
                        comm = comm.replace("\n", "\\n");
                        comm = comm.replace(" ", "%20");
                        sdc.execute(uid, debate_id, comm);
                        comment.setText("");
                    }
                }
        });
        if (calling != null) {
            if (calling.contentEquals("debate")){
                debate_id = getIntent().getExtras().getString("debate_id");
                RetrieveDebateComments rsd = new RetrieveDebateComments(CommentActivity.this);
                rsd.execute(uid, debate_id);
            }
        }else{
            debate_spinner.setVisibility(View.GONE);
            callCmp();
        }
    }

    private void callCmp() {
        debate_id = getIntent().getExtras().getString("campaign_id");
        RetrieveCampaignComments rsd = new RetrieveCampaignComments(CommentActivity.this);
        rsd.execute(uid, debate_id);
    }

    public void functions_debate(JSONObject s) {
        JSONArray total = new JSONArray();
        try {
            int count =0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date[] array_dates = new Date[s.getJSONArray("for").length()+s.getJSONArray("against").length()];
            for (int i=0; i<s.getJSONArray("for").length(); i++) {
                array_dates[count++] =  sdf.parse(s.getJSONArray("for").getJSONObject(i).getString("created_at"));
                s.getJSONArray("for").getJSONObject(i).put("which", "for");
                total.put(s.getJSONArray("for").getJSONObject(i));
            }
            for (int i=0; i<s.getJSONArray("against").length(); i++) {
                array_dates[count++] =  sdf.parse(s.getJSONArray("against").getJSONObject(i).getString("created_at"));
                s.getJSONArray("against").getJSONObject(i).put("which", "against");
                total.put(s.getJSONArray("against").getJSONObject(i));
            }
            for (int j = 0; j<array_dates.length; j++) {
                for (int k = 0; k < array_dates.length; k++){
                    if (array_dates[j].getTime() < array_dates[k].getTime()) {
                        Date buffer = array_dates[j];
                        JSONObject buffer_json = total.getJSONObject(j);
                        array_dates[j] = array_dates[k];
                        total.put(j, total.get(k));
                        total.put(k, buffer_json);
                        array_dates[k] = buffer;
                    }
                }
            }
        } catch (JSONException | ParseException e) {
        }
        if (first_time) {
            ca = new CommentAdapter(this, total);
            commentList.setAdapter(ca);
            first_time = false;
        }else{
            ca.updating(total);
            ca.notifyDataSetChanged();
        }
    }

    public void functions_campaign(JSONObject s) {
        JSONArray comm = new JSONArray();
        try {
            if (s.getJSONArray("comment").length()>0){
                for (int i=s.getJSONArray("comment").length()-1; i>=0; i--){
                    comm.put(s.getJSONArray("comment").get(i));
                }
                for (int i = 0; i<comm.length(); i++){
                    s.getJSONArray("comment").put(i, comm.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (first_time) {
            cca = new CommentCampaignAdapter(this, s, CommentActivity.this);
            commentList.setAdapter(cca);
            first_time = false;
        }else{
            cca.updating(s);
            cca.notifyDataSetChanged();
        }
    }

    public void functions(JSONObject s) {
        if (s.has("status")){
            try {
                if (s.getString("status").contains("suc")){
                    Toast.makeText(this, "Comment updated", Toast.LENGTH_SHORT).show();
                    if (calling!=null) {
                        RetrieveDebateComments rsd = new RetrieveDebateComments(CommentActivity.this);
                        rsd.execute(uid, debate_id);
                    }
                    else {
                        RetrieveCampaignComments rcc = new RetrieveCampaignComments(this);
                        rcc.execute(uid, debate_id);
                    }
                }else
                    Toast.makeText(this, "Error uploading comment", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void callingUser(String uid) {
        Intent intent = new Intent(CommentActivity.this, UserProfileActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}