package com.lumiro.merchantmonitor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.lumiro.merchantmonitor.Market.Parser;
import com.lumiro.merchantmonitor.db.DBAdaptor;
import com.lumiro.merchantmonitor.db.ItemAdapter;

import java.util.List;

/**
 * Created by kein on 06/02/14.
 */
public class Market_Service extends Service implements Runnable {
    private static final String TAG = "net.lumiro.market.market_service";

    public String mercName;

    public static final String ACTION_SYNC_MERC = "com.lumiro.merchantmonitor.SYNC_MERC";

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand (Intent intent, int flags, int startId){
        if (ACTION_SYNC_MERC.equals(intent.getAction())) {
            mercName = intent.getStringExtra("merc_name");

            Thread t = new Thread(this);
            t.start();
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        Log.d(TAG, "Requested SYNC_MERC action ["+mercName+"]");
        DBAdaptor db = new DBAdaptor(this);
        List<Item> items = Parser.getItems(mercName);
        Merc merc = db.getMercByName(mercName);
        for(Item item : items){
            merc.addItem(item);
        }
        db.updateMerc(merc);
        merc = db.getMercByName(mercName);
        Log.d(TAG, ItemAdapter.encodeJSONItems(merc.getItems()));
    }
}
