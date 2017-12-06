package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.JobAdapter;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Geson;
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
        SideNToolbarController.attach(this, "Substitute Job Search Result");
        Intent i = getIntent();
        String response = i.getStringExtra("response");
        Job[] posts = Geson.g().fromJson(response, Job[].class);
        JobAdapter adapter = new JobAdapter(SubstituteJobSearchResult.this, posts);
        lvSubstituteJobSearchResult.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
