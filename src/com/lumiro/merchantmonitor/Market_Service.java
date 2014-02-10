package com.lumiro.merchantmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.lumiro.merchantmonitor.Market.Parser;
import com.lumiro.merchantmonitor.db.DBAdaptor;
import com.lumiro.merchantmonitor.db.ItemAdapter;
import com.lumiro.merchantmonitor.helper.MerchantUpdater;

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

        MerchantUpdater updater = new MerchantUpdater(merc);
        if(items.size()>0){
            Item item = items.get(0);
            item.setCount(item.getCount()-1);
        }
        updater.updateItems(items);

        if(updater.isNew()){
            Log.d(TAG, "Merchant is new");
        }
        if(updater.isOffline()){
            Log.d(TAG, "Merchant is offline");
        }

        db.updateMerc(merc);
        merc = db.getMercByName(mercName);
        Log.d(TAG, ItemAdapter.encodeJSONItems(merc.getItems()));
    }
}
