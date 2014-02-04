package com.lumiro.merchantmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    MercDB db;

    ListView mercListView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new MercDB(this);

        ArrayAdapter<Merc> adapter = new ArrayAdapter<Merc>(this,
                android.R.layout.simple_list_item_1);
        mercListView = (ListView)findViewById(R.id.listView);
        mercListView .setAdapter(adapter);
        sync_merc_list_with_db();
        final MainActivity self = this;

        mercListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                final Merc merc = (Merc)mercListView.getItemAtPosition(position);
                new AlertDialog.Builder(self)
                .setTitle(merc.getName())
                .setMessage(getString(R.string.confrim_delete_merc, merc.getName()))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.removeMerc(merc);
                        sync_merc_list_with_db();
                        Toast.makeText(self, merc.getName(), Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("No", null)
                .show();

                return true;
            }
        });
    }

    public void addMerc(View view){
        Item item = new Item();
        item.decodeJSON("{\"id\": \"567\",\"name\": \"Strawberry\",\"refain\": \"0\",\"price\": \"2,000\",\"real_price\": \"2000\",\"profit\": \"50000\",\"count\": \"100\",\"now_count\": \"75\",\"attr\": \"\",\"owner\": \"ДжаРастафарай\"}");

        String mercName = ((EditText)findViewById(R.id.new_merc)).getText().toString();
        Merc m = new Merc();
        m.addItem(item);
        m.setName(mercName);

        db.addMerchant(m);
        sync_merc_list_with_db();

        Toast.makeText(this, item.encodeJson(),Toast.LENGTH_SHORT).show();
    }

    public void syncMercList(View view){
        sync_merc_list_with_db();
    }

    public void sync_merc_list_with_db(){
        List<Merc> mercs = db.getMercs();
        ArrayAdapter<Merc> adapter = (ArrayAdapter<Merc>) mercListView.getAdapter();

        adapter.clear();
        for(Merc merc: mercs){
            adapter.add(merc);
        }
    }
}
