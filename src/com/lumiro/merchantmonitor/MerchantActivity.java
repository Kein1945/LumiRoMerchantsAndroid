package com.lumiro.merchantmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1);
        ListView mercListView = new ListView(getApplicationContext());
        mercListView.setAdapter(adapter);
        for(Item item : merc.getItems()){
            adapter.add(item);
        }


        setTitle(mercName);
        setContentView(mercListView);
    }
}