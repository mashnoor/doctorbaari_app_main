package com.doctorbaari.android.acvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.AdvertiseAdapter;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.SideNToolbarController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMedicineUpdatesActivity extends AppCompatActivity {

    @BindView(R.id.lvAdvertises)
    ListView lvAdvertises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine_updates);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "New Medicine Updates");
        showNewMedicineUpdates();
    }

    private void showNewMedicineUpdates() {
        AdvertiseAdapter advertiseAdapter = new AdvertiseAdapter(this, DBHelper.getAdvertises(this));
        lvAdvertises.setAdapter(advertiseAdapter);
    }
}
