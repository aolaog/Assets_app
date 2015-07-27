package com.cdhxqh.polling_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.ui.DeviceListActivity;
import com.cdhxqh.polling_mobile.ui.TaskScannActivity;

import java.util.ArrayList;


/**
 * 设备列表
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private static final String TAG = "DeviceListAdapter";
    Context mContext;
    ArrayList<Ins_task_device> mDeviceModel = new ArrayList<Ins_task_device>();
    PollDataSource mDataSource = Application.getDataSource();

    public DeviceListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Ins_task_device deviceModel = mDeviceModel.get(i);

        viewHolder.nameText.setText(deviceModel.assetNo);
        viewHolder.numText.setText(deviceModel.rfid);
        viewHolder.locationText.setText(deviceModel.position);


        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelable("ins_task_device",deviceModel);
                intent.putExtras(bundle);
                intent.setClass(mContext, TaskScannActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceModel.size();
    }

    public void update(ArrayList<Ins_task_device> data, boolean merge) {
        Log.i(TAG, "mItems=" + mDeviceModel.size());
        if (merge && mDeviceModel.size() > 0) {
            for (int i = 0; i < mDeviceModel.size(); i++) {
                Ins_task_device obj = mDeviceModel.get(i);
                boolean exist = false;
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).rfid == obj.rfid) {
                        exist = true;
                        break;
                    }
                }
                if (exist) continue;
                data.add(obj);
            }
        }
        mDeviceModel = data;

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 布局文件*
         */
        RelativeLayout relativeLayout;
        /**
         * 资产名称*
         */
        public TextView nameText;
        /**
         * 资产编码
         */
        public TextView numText;
        /**
         * 位置
         */
        public TextView locationText;


        public ViewHolder(View view) {
            super(view);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.content_id);
            nameText = (TextView) view.findViewById(R.id.item_d_name);
            numText = (TextView) view.findViewById(R.id.item_d_num);
            locationText = (TextView) view.findViewById(R.id.item_d_location);
        }
    }
}