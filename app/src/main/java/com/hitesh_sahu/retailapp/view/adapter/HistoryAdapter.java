package com.hitesh_sahu.retailapp.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hitesh_sahu.retailapp.R;
import com.hitesh_sahu.retailapp.model.entities.HistoryData;

import java.util.List;

/**
 * Created by Kuncoro on 29/02/2016.
 */
public class HistoryAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<HistoryData> historyItems;

    public HistoryAdapter(Activity activity, List<HistoryData> historyItems) {
        this.activity = activity;
        this.historyItems = historyItems;
    }

    @Override
    public int getCount() {
        return historyItems.size();
    }

    @Override
    public Object getItem(int location) {
        return historyItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.histori_list, null);


        TextView Nomorpesanan = (TextView) convertView.findViewById(R.id.item_name);
        TextView Status = (TextView) convertView.findViewById(R.id.iteam_amount);
        TextView Tgl = (TextView) convertView.findViewById(R.id.item_short_desc);
        HistoryData history = historyItems.get(position);


        Nomorpesanan.setText(history.getId());
        Tgl.setText(history.getTgl());
        Status.setText(history.getStatus());

        return convertView;
    }

}