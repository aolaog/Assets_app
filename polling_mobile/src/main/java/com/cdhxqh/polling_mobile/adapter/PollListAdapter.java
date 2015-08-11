package com.cdhxqh.polling_mobile.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.model.Ins_task_ticket;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.ui.DeviceListActivity;
import com.cdhxqh.polling_mobile.utils.AccountUtils;
import com.cdhxqh.polling_mobile.utils.MessageUtils;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by yw on 2015/4/28.
 */
public class PollListAdapter extends RecyclerView.Adapter<PollListAdapter.ViewHolder> {
    private static final String TAG = "PollListAdapter";
    Context mContext;
    ArrayList<Ins_task_ticket> mPollListModel = new ArrayList<Ins_task_ticket>();
    PollDataSource mDataSource = Application.getDataSource();
    private MemberModel mLoginProfile;

    private ProgressDialog mProgressDialog;

    public PollListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poll_list, viewGroup, false);
        mLoginProfile = AccountUtils.readLoginMember(v.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Ins_task_ticket pollTaskModel = mPollListModel.get(i);

        viewHolder.desc.setText(pollTaskModel.taskTempletName);
        viewHolder.time.setText(pollTaskModel.inspectDate);
        if (pollTaskModel.status == 1) {
            viewHolder.status.setText("下载");
            viewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.window_background));
            viewHolder.status.setTextColor(mContext.getResources().getColor(R.color.photo_crop_dim_color));

        } else if (pollTaskModel.status == 2) {
            viewHolder.status.setText("未巡检");
            viewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            viewHolder.status.setTextColor(mContext.getResources().getColor(R.color.window_background));
        }
        if(pollTaskModel.status==2){
            viewHolder.status.setFocusable(false);
            viewHolder.status.setEnabled(false);
        }
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pollTaskModel.status != 2) {
                    MessageUtils.showMiddleToast(mContext, "请下载相关巡检设备数据");
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("task_id", pollTaskModel.id);
                    bundle.putString("taskTempletName", pollTaskModel.taskTempletName);
                    intent.setClass(mContext, DeviceListActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });




        viewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(pollTaskModel, pollTaskModel.id, mLoginProfile.access_token);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPollListModel.size();
    }

    public void update(ArrayList<Ins_task_ticket> data, boolean merge) {
        Log.i(TAG, "mItems=" + mPollListModel.size());
        if (merge && mPollListModel.size() > 0) {
            for (int i = 0; i < mPollListModel.size(); i++) {
                Ins_task_ticket obj = mPollListModel.get(i);
                boolean exist = false;
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).id == obj.id) {
                        exist = true;
                        break;
                    }
                }
                if (exist) continue;
                data.add(obj);
            }
        }
        mPollListModel = data;

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 布局文件*
         */
        RelativeLayout relativeLayout;
        /**
         * 描述*
         */
        public TextView desc;
        /**
         * 时间*
         */
        public TextView time;

        /**
         * cardView*
         */
        public CardView cardView;

        /**
         * 状态*
         */
        public TextView status;

        public ViewHolder(View view) {
            super(view);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.content_id);
            desc = (TextView) view.findViewById(R.id.item_polllist_desc);
            time = (TextView) view.findViewById(R.id.item_polllist_time);
            cardView = (CardView) view.findViewById(R.id.card_view_id);
            status = (TextView) view.findViewById(R.id.item_polllist_status);
        }
    }


    private void showProgressDialog(final Ins_task_ticket pollTaskModel, String ticketID, String access_token) {


            mProgressDialog = ProgressDialog.show(mContext, null,
                    "努力下载中...", true, true);

            Manager.getIns_task_device(mContext, ticketID, access_token, true, new HttpRequestHandler<ArrayList<Ins_task_device>>() {
                @Override
                public void onSuccess(ArrayList<Ins_task_device> data) {
                    Log.i(TAG,"data＝"+data);
                    pollTaskModel.setStatus(2);
                    mDataSource.updateTaskTicket(pollTaskModel);
                    notifyDataSetChanged();
                    mDataSource.isInsertDevice(data);
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(ArrayList<Ins_task_device> data, int totalPages, int currentPage) {
                    MessageUtils.showMiddleToast(mContext, "数据下载成功");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(String error) {
                    MessageUtils.showMiddleToast(mContext, error);
                    mProgressDialog.dismiss();
                }
            });
    }



}