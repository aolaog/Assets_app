package com.cdhxqh.polling_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.ui.TaskScannActivity;

import java.util.ArrayList;


/**
 * 图片列表
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private static final String TAG = "GalleryAdapter";
    Context mContext;
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    public GalleryAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Bitmap bitmap = bitmaps.get(i);
        Log.i(TAG,"bitmap="+bitmap);
        viewHolder.imageView.setImageBitmap(bitmap);


        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public void update(ArrayList<Bitmap> data, boolean merge) {
        Log.i(TAG, "mItems=" + bitmaps.size());
        if (merge && bitmaps.size() > 0) {
            for (int i = 0; i < bitmaps.size(); i++) {
                Bitmap obj = bitmaps.get(i);
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
        bitmaps = data;

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 布局文件*
         */
        RelativeLayout relativeLayout;
        /**
         * 图片*
         */
        ImageView imageView;


        public ViewHolder(View view) {
            super(view);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.content_id);
            imageView = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        }
    }
}