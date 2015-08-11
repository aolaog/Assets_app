package com.cdhxqh.polling_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.ui.Asset_Details_Activity;
import com.cdhxqh.polling_mobile.ui.TaskResultActivity;
import com.cdhxqh.polling_mobile.ui.TaskScannActivity;

import java.util.ArrayList;


/**
 * 设备扫描列表
 */
public class TaskScannAdapter extends RecyclerView.Adapter<TaskScannAdapter.ViewHolder> {
    private static final String TAG = "TaskScannAdapter";
    Context mContext;
    ArrayList<Asset_three_class> asset_three_classes = new ArrayList<Asset_three_class>();
    /**巡检ID**/
    String task_id;
    /**巡检设备ID**/
    String insDeviceID;

    public TaskScannAdapter(Context context,String task_id,String insDeviceID) {
        mContext = context;
        this.task_id=task_id;
        this.insDeviceID=insDeviceID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rfid_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final String assetNo = asset_three_classes.get(i).assetNo;
        final String attributes=asset_three_classes.get(i).attributes;
        viewHolder.numText.setText(assetNo);

        /**资产详情**/
        viewHolder.assetDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("attributes", attributes);
                intent.putExtras(bundle);
                intent.setClass(mContext, Asset_Details_Activity.class);
                mContext.startActivity(intent);
            }
        });

        /**巡检回单**/
        viewHolder.assetReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("task_id", task_id);
                bundle.putString("rfid", assetNo);
                bundle.putString("insDeviceID", insDeviceID);
                intent.putExtras(bundle);
                intent.setClass(mContext, TaskResultActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return asset_three_classes.size();
    }

    public void update(ArrayList<Asset_three_class> data, boolean merge) {
        Log.i(TAG, "mItems=" + asset_three_classes.size());
        if (merge && asset_three_classes.size() > 0) {
            for (int i = 0; i < asset_three_classes.size(); i++) {
                Asset_three_class obj = asset_three_classes.get(i);
                boolean exist = false;
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j) == obj) {
                        exist = true;
                        break;
                    }
                }
                if (exist) continue;
                data.add(obj);
            }
        }
        asset_three_classes = data;
        Log.i(TAG, "mItems1=" + asset_three_classes.size());
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 布局文件*
         */
        RelativeLayout relativeLayout;
        /**
         * 资产编码
         */
        public TextView numText;

        /**资产详情**/
        public Button assetDetailsBtn;

        /**巡检回单**/
        public Button assetReplyBtn;

        public ViewHolder(View view) {
            super(view);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.content_id);
            numText = (TextView) view.findViewById(R.id.rfid_num_id);

            assetDetailsBtn = (Button) view.findViewById(R.id.asset_details_id);
            assetReplyBtn = (Button) view.findViewById(R.id.asset_reply_id);
        }
    }
}