package com.lumiro.merchantmonitor;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;
import com.lumiro.merchantmonitor.Market.Parser;
import com.lumiro.merchantmonitor.db.DBAdaptor;
import com.lumiro.merchantmonitor.db.ItemAdapter;
import com.lumiro.merchantmonitor.helper.MerchantUpdater;

import android.text.format.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kein on 06/02/14.
 */
public class Market_Service extends Service implements Runnable {
    private static final String TAG = "net.lumiro.market.market_service";

    public List<String> merchants = new ArrayList<String>();

    private boolean looped = false;

    public static final String ACTION_SYNC_MERC = "com.lumiro.merchantmonitor.SYNC_MERC";
    public static final String ACTION_SYNC_MERCS = "com.lumiro.merchantmonitor.SYNC_MERCS";
    public static final String ACTION_SYNC_START = "com.lumiro.merchantmonitor.SYNC_START";

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Start service");
    }

    public int onStartCommand (Intent intent, int flags, int startId){
        Log.e(TAG, intent.getAction());
        if (ACTION_SYNC_MERC.equals(intent.getAction())) {
            Log.e(TAG, "Start merc update");
            merchants.add( intent.getStringExtra("merc_name") );

            Thread t = new Thread(this);
            t.start();
        }
        if (ACTION_SYNC_MERCS.equals(intent.getAction())) {
            Log.e(TAG, "Start update");
            merchants = new ArrayList<String>();
            Thread t = new Thread(this);
            t.start();
        }
        if (ACTION_SYNC_START.equals(intent.getAction())) {
            Log.e(TAG, "Start loop");
            merchants = new ArrayList<String>();
            looped = true;
            Thread t = new Thread(this);
            t.start();
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        if(looped){
            Log.e(TAG, "Add event loop");
            Intent i = new Intent(this, Market_Service.class);
            i.setAction(Market_Service.ACTION_SYNC_START);

            PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Time time = new Time();
            time.set(System.currentTimeMillis() + UPDATE_INTERVAL);

            long nextUpdate = time.toMillis(false);
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdate, pendingIntent);
        }
        if(0 == merchants.size()){
            DBAdaptor db = new DBAdaptor(this);
            List<Merc> mersc = db.getMercs();
            for(Merc merc : mersc){
                merchants.add( merc.getName() );
            }
        }
        Log.d(TAG, "Service start sync");
        for(String merchant_name : merchants){
            Log.d(TAG, "Requested SYNC_MERC action ["+merchant_name+"]");
            DBAdaptor db = new DBAdaptor(this);
            List<Item> items = Parser.getItems(merchant_name);
            if(null == items){
                continue;
            }
            Merc merc = db.getMercByName(merchant_name);

            MerchantUpdater updater = new MerchantUpdater(merc);
            updater.updateItems(items);

            if(updater.isNew()){
                Intent RTReturn = new Intent(MainActivity.SHOW_TOAST);
                RTReturn.putExtra("toast", "Merchant "+merc.getName()+" is new ");
                LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
            }
            if(updater.isOffline()){
                Intent RTReturn = new Intent(MainActivity.SHOW_TOAST);
                RTReturn.putExtra("toast", "Merchant " + merc.getName() + " is offline");
                LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
            }
            if(updater.getProfit() > 0){
                Intent RTReturn = new Intent(MainActivity.SHOW_TOAST);
                RTReturn.putExtra("toast", "Merchant " + merc.getName() + " have income " + String.valueOf(updater.getProfit()));
                LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
            }

            db.updateMerc(merc);
            db.getMercByName(merchant_name);
        }
        Intent RTReturn = new Intent(MainActivity.UPDATE_MERC_LIST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(RTReturn);
    }
    private static final long UPDATE_INTERVAL = 20 * DateUtils.MINUTE_IN_MILLIS;
}
