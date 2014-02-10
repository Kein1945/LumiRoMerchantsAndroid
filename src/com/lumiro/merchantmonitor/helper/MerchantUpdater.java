package com.lumiro.merchantmonitor.helper;

import android.util.Log;
import com.lumiro.merchantmonitor.Item;
import com.lumiro.merchantmonitor.Merc;
import com.lumiro.merchantmonitor.db.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kein on 09/02/14.
 */
public class MerchantUpdater {

    public final static String TAG = "net.lumiro.market.market_updater";

    private boolean is_new = false;

    private boolean is_offline = false;

    private Integer profit = 0;

    private List<Item> items = new ArrayList<Item>();

    private Merc merc;


    public MerchantUpdater(Merc merc) {
        this.merc = merc;
    }

    public void updateItems(List<Item> new_items){
        // Merchant offline
        if(0 == new_items.size()){
            is_offline = true;
            merc.setItems(new ArrayList<Item>());
            return;
        }

        List<Item> old_items = merc.getItems();
        // Merchant online
        if( 0 == old_items.size()){
            is_new = true;
            merc.setItems(new_items);
            return;
        }

        ArrayList<String> old_items_checked = new ArrayList<String>(), new_items_checked = new ArrayList<String>();
        String old_hash, new_hash;
        // Cycles for updating new count of items
        for(int i = 0; i<old_items.size(); i++){
            old_hash = item_hash(old_items.get(i));
            for(int j=0; j<new_items.size(); j++){
                new_hash = item_hash(new_items.get(j));
                if(!old_items_checked.contains(old_hash+String.valueOf(i))
                        && !new_items_checked.contains(new_hash+String.valueOf(j))
                        && old_hash.equals(new_hash)
                        ){
                    old_items_checked.add(old_hash+String.valueOf(i));
                    new_items_checked.add(new_hash+String.valueOf(j));
                    old_items.get(i).setNow_count(new_items.get(j).getCount());
                }
            }
        }

        // Now set now_count=0 for items that not found in new items list
        for(int i=0; i<old_items.size(); i++){
            old_hash = item_hash(old_items.get(i));
            if(!old_items_checked.contains(old_hash+String.valueOf(i))){
                old_items.get(i).setNow_count(0);
            }
        }

        // Check is new vend? Does we have new items?
        boolean old_item_found = false;
        for(int i=0; i<new_items.size(); i++){
            new_hash = item_hash(new_items.get(i));
            for(int j=0; j<old_items.size(); j++){
                old_item_found = false;
                old_hash = item_hash(old_items.get(j));
                if(new_hash.equals(old_hash)){
                    Log.e(TAG, "Old "+old_items.get(j).getName());
                    old_item_found = true;
                    break;
                }
            }
            if(!old_item_found){
                is_new = true;
                old_items = new_items;
                break;
            }
        }
        merc.setItems(old_items);
    }

    public boolean isOffline() {
        return is_offline;
    }

    public boolean isNew() {
        return is_new;
    }

    private static String item_hash(Item item){
        // item.id+item.refain+item.price+item.attr.join('|');
        return String.valueOf(item.getId()) + "|"
                + String.valueOf(item.getRefain()) + "|"
                + String.valueOf(item.getPrice()) + "|"
                + String.valueOf(item.getAttr()) + "|"
                ;
    }
}
