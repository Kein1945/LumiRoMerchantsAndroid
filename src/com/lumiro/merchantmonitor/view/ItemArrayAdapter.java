package com.lumiro.merchantmonitor.view;

import android.content.Context;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lumiro.merchantmonitor.Item;
import com.lumiro.merchantmonitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kein on 07/02/14.
 */
public class ItemArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<Item> values;

    public ItemArrayAdapter(Context context, ArrayList values) {
        super(context, R.layout.item_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        Item item = (Item)values.get(position);

        if(null == convertView){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_row, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) rowView.findViewById(R.id.label);
            holder.count = (TextView) rowView.findViewById(R.id.count);
            holder.profit = (TextView) rowView.findViewById(R.id.profit);
            holder.image = (ImageView) rowView.findViewById(R.id.icon);

            int image_resource_id = this.getContext().getResources().getIdentifier("item_" + String.valueOf(item.getId()), "drawable", "com.lumiro.merchantmonitor");
            if(0 == image_resource_id){
                image_resource_id = R.drawable.item_500;
            }
            holder.image.setImageResource(image_resource_id);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.label.setText(item.getName());
        if(item.getNow_count() > 0){
            holder.count.setText(String.valueOf(item.getNow_count()));
        } else {
            holder.count.setText("");
        }

        if( (item.getCount() - item.getNow_count()) != 0){
            Integer profit = (item.getCount() - item.getNow_count()) * item.getPrice();
            holder.profit.setText("+" + String.valueOf(profit));
        } else {
            holder.profit.setText("");
        }

        return rowView;
    }

    static class ViewHolder {
        protected TextView label;
        protected TextView count;
        protected TextView profit;
        protected ImageView image;
    }
}
