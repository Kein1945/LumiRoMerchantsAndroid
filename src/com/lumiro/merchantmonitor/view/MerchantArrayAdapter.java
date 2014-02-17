package com.lumiro.merchantmonitor.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lumiro.merchantmonitor.Merc;
import com.lumiro.merchantmonitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kein on 15/02/14.
 */
public class MerchantArrayAdapter extends ArrayAdapter<Merc> {
    private final Context context;
    private final List<Merc> values;

    public MerchantArrayAdapter(Context context, ArrayList values) {
        super(context, R.layout.merchant_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.merchant_row, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) rowView.findViewById(R.id.label);
            holder.count = (TextView) rowView.findViewById(R.id.count);
            holder.profit = (TextView) rowView.findViewById(R.id.profit);
            holder.image_state = (ImageView) rowView.findViewById(R.id.image_state);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Merc merc = (Merc)values.get(position);
        holder.label.setText( merc.getName() );
        Integer items_count = merc.getItemsCount(); String item_count_label = "";
        if(0 < items_count){
            item_count_label = String.valueOf( items_count );
            holder.image_state.setBackgroundColor(getContext().getResources().getColor(R.color.online));
            holder.count.setText( item_count_label );
            Integer profit = merc.getProfit();
            if( profit > 0){
                holder.profit.setText("+"+String.valueOf( profit ));
                holder.image_state.setBackgroundColor(getContext().getResources().getColor(R.color.profit));
            } else {
                holder.profit.setText("");
            }
        } else {
            holder.count.setText("");
            holder.image_state.setBackgroundColor(getContext().getResources().getColor(R.color.offline));
        }


        return rowView;
    }

    static class ViewHolder {
        protected TextView label;
        protected TextView count;
        protected TextView profit;
        protected ImageView image_state;
    }
}
