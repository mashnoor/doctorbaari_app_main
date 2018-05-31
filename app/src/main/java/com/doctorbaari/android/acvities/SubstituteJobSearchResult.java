package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.JobAdapter;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.ListViewEmptyMessageSetter;
import com.doctorbaari.android.utils.SideNToolbarController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubstituteJobSearchResult extends AppCompatActivity {


    @BindView(R.id.lvSubstituteJobSearchResult)
    ListView lvSubstituteJobSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitute_job_search_result);
        ButterKnife.bind(this);

        Intent i = getIntent();
        if(i.getStringExtra("type").equals("per"))
        {
            SideNToolbarController.attach(this, "Permanent Job Search Result");
        }
        else
        {
            SideNToolbarController.attach(this, "Temporary Job Search Result");
        }
        String response = i.getStringExtra("response");
        Job[] posts = Geson.g().fromJson(response, Job[].class);
        JobAdapter adapter = new JobAdapter(SubstituteJobSearchResult.this, posts);

        ListViewEmptyMessageSetter.set(this, lvSubstituteJobSearchResult, "As soon as any job is available, the authority will contact with you");
        lvSubstituteJobSearchResult.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}
