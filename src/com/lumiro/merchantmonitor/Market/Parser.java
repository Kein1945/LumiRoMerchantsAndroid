package com.lumiro.merchantmonitor.Market;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by kein on 07/02/14.
 */
public class Parser {
    private static final String TAG = "net.lumiro.market.parser";

    public static final String SELL_URI = "http://market.lumiro.net/whosell.php?field=price&order=asc&s=";

    public static String getSellItems(String search) {
        String url = SELL_URI;
        try{
            url += URLEncoder.encode(search, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Search string encodeURI fail. "+search);
            return "";
        }
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            Log.d(TAG, "Requesting url:" + url);
            response = client.execute(request);
        } catch (IOException e) {
            Log.e(TAG, "Get html failed. IOException");
            return "";
        }

        String html = "";
        HttpEntity in = response.getEntity();
        try {
            html = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Log.e(TAG, "Read html failed. IOException");
            return "";
        }
        Log.d(TAG, html);
        return html;
    }
}
