package com.lumiro.merchantmonitor;

import android.content.Context;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView countTextView = (TextView) rowView.findViewById(R.id.count);
        TextView profitTextView = (TextView) rowView.findViewById(R.id.profit);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        Item item = (Item)values.get(position);
        textView.setText( item.getName() );
        countTextView.setText(String.valueOf(item.getNow_count()));

        if( (item.getCount() - item.getNow_count()) != 0){
            Integer profit = (item.getCount() - item.getNow_count()) * item.getPrice();
            profitTextView.setText("+"+String.valueOf( profit ));
            Log.d("com.lumiro.merc", String.valueOf( item.getNow_count()) + " " + String.valueOf( item.getCount()));
        }


        int image_resource_id = this.getContext().getResources().getIdentifier("item_" + String.valueOf(item.getId()), "drawable", "com.lumiro.merchantmonitor");
        if(0 == image_resource_id){
            image_resource_id = R.drawable.item_500;
        }
        imageView.setImageResource(image_resource_id);

        return rowView;
    }
}
