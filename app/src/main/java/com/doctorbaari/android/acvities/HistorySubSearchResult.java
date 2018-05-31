package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.loopj.android.http.AsyncHttpClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistorySubSearchResult extends AppCompatActivity {

    AsyncHttpClient client;

    ProgressDialog dialog;

    @BindView(R.id.lvSubstitute)
    ListView lvSubstitute;

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onBackPressed() {

        if(SideNToolbarController.isDrawerOpen())
            SideNToolbarController.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_sub_search_result);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Search Results");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);

        dialog.setMessage("Connecting with server...");
        Intent i = getIntent();
        String fromDate = i.getStringExtra("fromdate");
        String todate = i.getStringExtra("todate");
        String placename = i.getStringExtra("place");
        String placelat = i.getStringExtra("placelat");
        String placelon = i.getStringExtra("placelon");
        String degree = i.getStringExtra("degree");
        //loadSearchResult(fromDate, todate, placename, placelat, placelon, degree);

    }


}
