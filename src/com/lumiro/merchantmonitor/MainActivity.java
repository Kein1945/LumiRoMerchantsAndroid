package com.lumiro.merchantmonitor;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.lumiro.merchantmonitor.db.DBAdaptor;

import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "net.lumiro.market.main_activity";

    public static final String EXTRA_MERCH = "com.lumiro.merchantmonitor.MERC_NAME";

    DBAdaptor db;

    ListView mercListView;

    //Your activity will respond to this action String
    public static final String UPDATE_MERC_LIST = "com.com.lumiro.merchantmonitor.UPDATE_MERC_LIST";
    public static final String SHOW_TOAST = "com.com.lumiro.merchantmonitor.SHOW_TOAST";

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Recive intent: " + intent.getAction());
            if(intent.getAction().equals(UPDATE_MERC_LIST)) {
                sync_merc_list_with_db();
            }
            if(intent.getAction().equals(SHOW_TOAST)){
                Log.d(TAG, intent.getStringExtra("toast"));
                createInfoNotification(intent.getStringExtra("toast"));
                Toast.makeText(context, intent.getStringExtra("toast"), Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new DBAdaptor(this);

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

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_MERC_LIST);
        intentFilter.addAction(SHOW_TOAST);
        bManager.registerReceiver(bReceiver, intentFilter);

        Intent i = new Intent(this, Market_Service.class);
        i.setAction(Market_Service.ACTION_SYNC_START);
        startService(i);
    }

    public void addMerc(View view){
//        sync_merc_list_with_db();
        Intent i = new Intent(this, Market_Service.class);
        i.setAction(Market_Service.ACTION_SYNC_MERC);

        String mercName = ((EditText)findViewById(R.id.new_merc)).getText().toString();
        i.putExtra("merc_name", mercName);

        startService(i);
    }

    public void updateMerchants(View view){
        Intent i = new Intent(this, Market_Service.class);
        i.setAction(Market_Service.ACTION_SYNC_MERCS);
        startService(i);
    }


    public void syncMercList(View view){
        sync_merc_list_with_db();
    }

    public void sync_merc_list_with_db(){
        Toast.makeText(this, "Sync list", Toast.LENGTH_SHORT).show();
        List<Merc> mercs = db.getMercs();
        ArrayAdapter<Merc> adapter = (ArrayAdapter<Merc>) mercListView.getAdapter();

        adapter.clear();
        for(Merc merc: mercs){
            adapter.add(merc);
        }
    }

    public void createInfoNotification(String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("LumiRo")
                        .setContentText(message);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
