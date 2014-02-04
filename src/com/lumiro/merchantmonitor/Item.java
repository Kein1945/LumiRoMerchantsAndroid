package com.lumiro.merchantmonitor;

import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kein on 04/02/14.
 */
public class Item {
    private Integer id;
    private String name;
    private Integer refain;
    private String price;
    private Integer real_price;
    private Integer profit;
    private Integer count;
    private Integer now_count;
    private String attr;
    private String owner;

    public void decodeJSON(String jsonItems){
        try{
            JSONObject item = new JSONObject(jsonItems);
//            Toast.makeText(this, merc.getString("name"), Toast.LENGTH_SHORT).show();
            this.setId( item.getInt("id") );

            this.setName( item.getString("name"));
            this.setRefain(item.getInt("refain"));
            this.setPrice( item.getString("price"));
            this.setReal_price(item.getInt("real_price"));
            this.setProfit(item.getInt("profit"));
            this.setCount(item.getInt("count"));
            this.setNow_count(item.getInt("now_count"));
            this.setAttr( item.getString("attr"));
            this.setOwner(item.getString("owner"));
        } catch (JSONException e){

//            e.printStackTrace();
        }
    }

    public JSONObject getJSON(){
        JSONObject item = new JSONObject();
        try {
            item.put("id", this.id );
            item.put("name", this.name );
            item.put("refain", this.refain );
            item.put("price", this.price );
            item.put("real_price", this.real_price );
            item.put("profit", this.profit );
            item.put("count", this.count );
            item.put("now_count", this.now_count );
            item.put("attr", this.attr );
            item.put("owner", this.owner );
        } catch (JSONException e){

        }
        return item;
    }

    public String encodeJson(){
        try {
            JSONObject item = new JSONObject();
            item.put("id", this.id );
            item.put("name", this.name );
            item.put("refain", this.refain );
            item.put("price", this.price );
            item.put("real_price", this.real_price );
            item.put("profit", this.profit );
            item.put("count", this.count );
            item.put("now_count", this.now_count );
            item.put("attr", this.attr );
            item.put("owner", this.owner );
            return item.toString();
        } catch (JSONException e) {
//            e.printStackTrace();
            return "{}";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRefain() {
        return refain;
    }

    public void setRefain(Integer refain) {
        this.refain = refain;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getReal_price() {
        return real_price;
    }

    public void setReal_price(Integer real_price) {
        this.real_price = real_price;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNow_count() {
        return now_count;
    }

    public void setNow_count(Integer now_count) {
        this.now_count = now_count;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
