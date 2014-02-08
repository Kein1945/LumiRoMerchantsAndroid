package com.lumiro.merchantmonitor;

import org.json.JSONArray;

import java.util.*;

/**
 * Created by kein on 04/02/14.
 */
public class Merc {
    private List<Item> items;
    private String name;

    public Merc() {
        this.items = new ArrayList<Item>();
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return getName() + "(" + String.valueOf(getItems().size()) + ")";
    }
}
