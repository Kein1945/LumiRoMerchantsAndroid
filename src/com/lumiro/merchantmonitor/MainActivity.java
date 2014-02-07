package com.lumiro.merchantmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.lumiro.merchantmonitor.Market.Parser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    public static final String EXTRA_MERCH = "com.lumiro.merchantmonitor.MERC_NAME";

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
        mercListView.setClickable(true);
        sync_merc_list_with_db();
        final MainActivity self = this;

        mercListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                final Merc merc = (Merc) mercListView.getItemAtPosition(position);
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
        mercListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Merc merc = (Merc)mercListView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), merc.getName(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(MainActivity.this, MerchantActivity.class);
                myIntent.putExtra(EXTRA_MERCH, merc.getName()); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    public void addMerc(View view){
//        sync_merc_list_with_db();
        Intent i = new Intent(this, Market_Service.class);
        i.setAction(Market_Service.ACTION_SYNC_MERC);

        String mercName = ((EditText)findViewById(R.id.new_merc)).getText().toString();
        i.putExtra("merc_name", mercName);

        startService(i);
    }

    public void startService(View view){
        startService(new Intent(this, Market_Service.class));
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
