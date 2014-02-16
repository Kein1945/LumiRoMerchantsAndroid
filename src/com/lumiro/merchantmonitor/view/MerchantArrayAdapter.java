package com.lumiro.merchantmonitor.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.merchant_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView countTextView = (TextView) rowView.findViewById(R.id.count);
        TextView profitTextView = (TextView) rowView.findViewById(R.id.profit);

        Merc merc = (Merc)values.get(position);
        textView.setText( merc.getName() );
        Integer items_count = merc.getItemsCount(); String item_count_label = "";
        if(0 < items_count){
            item_count_label = String.valueOf( items_count );
        } else {
            item_count_label = "";
        }
        countTextView.setText( item_count_label );

        Integer profit = merc.getProfit();
        if( profit > 0)
            profitTextView.setText("+"+String.valueOf( profit ));


        return rowView;
    }
}
