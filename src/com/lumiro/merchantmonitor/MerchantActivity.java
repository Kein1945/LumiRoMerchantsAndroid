package com.lumiro.merchantmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by kein on 07/02/14.
 */
public class MerchantActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String mercName = intent.getStringExtra(MainActivity.EXTRA_MERCH);
        MercDB db = new MercDB(this);
        Merc merc = db.getMercByName(mercName);

        ItemArrayAdapter adapter = new ItemArrayAdapter (this, (ArrayList)merc.getItems());
        ListView mercListView = new ListView(getApplicationContext());
        mercListView.setAdapter(adapter);


        setTitle(mercName);
        setContentView(mercListView);
    }
}