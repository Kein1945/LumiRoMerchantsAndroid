package com.lumiro.merchantmonitor.Market;

import android.util.Log;
import com.lumiro.merchantmonitor.Item;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kein on 07/02/14.
 */
public class Parser {
    private static final String TAG = "net.lumiro.market.parser";

    public static final String SELL_URI = "http://market.lumiro.net/whosell.php?field=price&order=asc&s=";

    private static final String ResultMatchRegExp = "tr\\sclass=\"line(?:[\\s\\S]*?)div\\sstyle=\"([\\s\\S]*?)\"(?:[\\s\\S]*?)<small>([\\d\\+]*)<\\/small>(?:[\\s\\S]*?)javascript:perf\\('([\\d]+)'\\);\">([^<]+?)<\\/a>([\\s\\S]*?)<\\/td>(?:[\\s\\S]*?)class=\"value\">([\\s\\S]*?)class=\"value\"\\salign=\"right\">([^<]+)<(?:[\\s\\S]*?)align=\"center\">([^<]+)(?:[\\s\\S]*?)class=\"trader(?:[\\s\\S]*?)<a[^>]+>([^<]+)";
    private static Pattern parseRegExp;

    public static Pattern getParser(){
        if(parseRegExp == null){
            parseRegExp = Pattern.compile(ResultMatchRegExp);
        }
        return parseRegExp;
    }

    public static String getSellItems(String search) {
        String url = SELL_URI;
        try{
            url += URLEncoder.encode(search, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Search string encodeURI fail. "+search);
            return null;
        }
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            Log.d(TAG, "Requesting url:" + url);
            response = client.execute(request);
        } catch (IOException e) {
            Log.e(TAG, "Get html failed. IOException");
            return null;
        }

        String html = "";
        HttpEntity in = response.getEntity();
        try {
            html = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Log.e(TAG, "Read html failed. IOException");
            return null;
        }
        return html;
    }

    public static List<Item> getItems(String search){
        String response = getSellItems(search);
        if(response == null){
            return null;
        }

        List<Item> items = new ArrayList<Item>();
        Matcher m = getParser().matcher(response);


        while(m.find()) {
            Item item = new Item();
            item.setId(Integer.parseInt(m.group(3)));
            Integer refain = 0;
            try {
                refain = Integer.parseInt(m.group(2));
            } catch(NumberFormatException e) {}
            item.setRefain( refain );
            item.setPrice(Integer.parseInt(m.group(7).replace(".","")));
            item.setCount(Integer.parseInt(m.group(8)));
            item.setNow_count(item.getCount());
            item.setName(m.group(4));
            item.setAttr("");
            item.setOwner(m.group(9));
            items.add(item);
        }
        Log.d(TAG, "Items count: " + String.valueOf(items.size()));
        return items;
    }
}
