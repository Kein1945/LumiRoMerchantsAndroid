package com.lumiro.merchantmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.lumiro.merchantmonitor.Market.Parser;
import com.lumiro.merchantmonitor.db.DBAdaptor;
import com.lumiro.merchantmonitor.db.ItemAdapter;
import com.lumiro.merchantmonitor.helper.MerchantUpdater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kein on 06/02/14.
 */
public class Market_Service extends Service implements Runnable {
    private static final String TAG = "net.lumiro.market.market_service";

    public List<String> merchants = new ArrayList<String>();

    public static final String ACTION_SYNC_MERC = "com.lumiro.merchantmonitor.SYNC_MERC";
    public static final String ACTION_SYNC_MERCS = "com.lumiro.merchantmonitor.SYNC_MERCS";

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand (Intent intent, int flags, int startId){
        if (ACTION_SYNC_MERC.equals(intent.getAction())) {
            merchants.add( intent.getStringExtra("merc_name") );

            Thread t = new Thread(this);
            t.start();
        }
        if (ACTION_SYNC_MERCS.equals(intent.getAction())) {
            DBAdaptor db = new DBAdaptor(this);
            List<Merc> mersc = db.getMercs();
            for(Merc merc : mersc){
                merchants.add( merc.getName() );
            }
            Thread t = new Thread(this);
            t.start();
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        for(String merchant_name : merchants){
            Log.d(TAG, "Requested SYNC_MERC action ["+merchant_name+"]");
            DBAdaptor db = new DBAdaptor(this);
            List<Item> items = Parser.getItems(merchant_name);
            Merc merc = db.getMercByName(merchant_name);

            MerchantUpdater updater = new MerchantUpdater(merc);
            updater.updateItems(items);

            if(updater.isNew()){
                Log.d(TAG, "Merchant is new");
                Intent RTReturn = new Intent(MainActivity.SHOW_TOAST);
                RTReturn.putExtra("toast", "Merchant "+merc.getName()+" is new ");
                LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
            }
            if(updater.isOffline()){
                Intent RTReturn = new Intent(MainActivity.SHOW_TOAST);
                RTReturn.putExtra("toast", "Merchant "+merc.getName()+" is offline");
                LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
                Log.d(TAG, "Merchant is offline");
            }

            db.updateMerc(merc);
            db.getMercByName(merchant_name);
        }
        Intent RTReturn = new Intent(MainActivity.UPDATE_MERC_LIST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
    }
}
