package com.beingcitizen.beingcitizen;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.beingcitizen.R;
import com.beingcitizen.adapters.CommentAdapter;
import com.beingcitizen.retrieveals.RetrieveDebateComments;
import com.beingcitizen.retrieveals.RetrieveSingleDebate;

import org.json.JSONObject;

/**
 * Created by saransh on 14-06-2015.
 */
public class CommentActivity extends Activity {


    private ListView commentList;
    /*String TITLES[] = {"Public Law & Order","Police","Public Health & Sanitation","Local Government","Communications – Roads & Bridges"
            ,"Water Supplies","Industries","Markets & Fairs","Trade & Commerce (within State)","State Taxes (Electricity, Land, Roads, Toll)"};
            */
    String debate_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allcomments);
        debate_id = getIntent().getExtras().getString("debate_id");
        commentList = (ListView)findViewById(R.id.allcomment_listview);
        RetrieveDebateComments rsd = new RetrieveDebateComments(CommentActivity.this);
        rsd.execute(debate_id);
    }

    @Override
    public void onResume() {
        RetrieveDebateComments rsd = new RetrieveDebateComments(CommentActivity.this);
        rsd.execute(debate_id);
        super.onResume();
    }

    public void functions(JSONObject s) {
        commentList.setAdapter(new CommentAdapter(this, s));
    }
}
