package com.lumiro.merchantmonitor;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.lumiro.merchantmonitor.Market.Parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        MercDB db = new MercDB(this);
        List<Item> items = Parser.getItems(mercName);
        Merc merc = db.getMercByName(mercName);
        for(Item item : items){
            merc.addItem(item);
        }
        db.updateMerc(merc);
        merc = db.getMercByName(mercName);
        Log.d(TAG, Item.encodeJSONItems(merc.getItems()));
    }
}
