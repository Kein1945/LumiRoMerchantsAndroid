package com.lumiro.merchantmonitor;

import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kein on 04/02/14.
 */
public class Item {
    private Integer id;
    private String name;
    private Integer refain;
    private Integer price;
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
            this.setPrice( item.getInt("price"));
            this.setCount(item.getInt("count"));
            this.setNow_count(item.getInt("now_count"));
            this.setAttr( item.getString("attr"));
            this.setOwner(item.getString("owner"));
        } catch (JSONException e){

//            e.printStackTrace();
        }
    }

    public String encodeJson(){
        try {
            JSONObject item = new JSONObject();
            item.put("id", this.id );
            item.put("name", this.name );
            item.put("refain", this.refain );
            item.put("price", this.price );
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

    public String toString(){
        return encodeJson();
    }

    public static List<Item> decodeJSONItems(String json){
        List<Item> items = new ArrayList<Item>();
        try {
            JSONArray json_items = new JSONArray(json);
            for(Integer i=0; i< json_items.length(); i++){
                Item item = new Item();
                JSONObject jitem = (JSONObject)json_items.get(i);
                item.setId( jitem.getInt("id") );
                item.setName(jitem.getString("name"));
                item.setRefain(jitem.getInt("refain"));
                item.setPrice(jitem.getInt("price"));
                item.setCount(jitem.getInt("count"));
                item.setNow_count(jitem.getInt("now_count"));
                item.setAttr(jitem.getString("attr"));
                item.setOwner(jitem.getString("owner"));
                items.add(item);
            }
        } catch (JSONException e) {
        }
        return items;
    }

    public static String encodeJSONItems(List<Item> items){
        JSONArray jitems = new JSONArray();
        for(Item item: items){
            JSONObject jitem = new JSONObject();
            try {
                jitem.put("id", item.getId());
                jitem.put("name", item.getName());
                jitem.put("refain", item.getRefain());
                jitem.put("price", item.getPrice());
                jitem.put("count", item.getCount());
                jitem.put("now_count", item.getNow_count());
                jitem.put("attr", item.getAttr());
                jitem.put("owner", item.getOwner());
                jitems.put(jitem);
            } catch (JSONException e) {}
        }
        return jitems.toString();
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getProfit() {
        return (getCount() - getNow_count()) * getPrice();
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
