package com.cdhxqh.polling_mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.model.Asset_device;

import java.util.ArrayList;


/**
 * Created by yw on 2015/4/28.
 * 资产详情的Adapter
 */
public class AssetDetailAdapter extends RecyclerView.Adapter<AssetDetailAdapter.ViewHolder> {
    private static final String TAG = "AssetDetailAdapter";
    Context mContext;
    ArrayList<Asset_device> asset_devices = new ArrayList<Asset_device>();


    public AssetDetailAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_asset_device_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Asset_device asset_device = asset_devices.get(i);
        String lable = asset_device.label;
        String value = asset_device.value;


        Log.i(TAG, "lable=" + lable + ",value=" + value);

        viewHolder.label.setText(lable + ":");

        viewHolder.value.setText(value);


    }

    @Override
    public int getItemCount() {
        return asset_devices.size();
    }

    public void update(ArrayList<Asset_device> data, boolean merge) {
        Log.i(TAG, "size=" + asset_devices.size());
        if (merge && asset_devices.size() > 0) {
            for (int i = 0; i < asset_devices.size(); i++) {
                Asset_device obj = asset_devices.get(i);
                boolean exist = false;
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).label == obj.label) {
                        exist = true;
                        break;
                    }
                }
                if (exist) continue;
                data.add(obj);
            }
        }
        asset_devices = data;

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * label
         */
        TextView label;
        /**
         * value
         */
        public TextView value;

        public ViewHolder(View view) {
            super(view);

            label = (TextView) view.findViewById(R.id.item_label_id);
            value = (TextView) view.findViewById(R.id.item_value_id);
        }
    }


}