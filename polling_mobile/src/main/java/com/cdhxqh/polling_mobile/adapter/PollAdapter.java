package com.cdhxqh.polling_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.R;


/**
 * 巡检列表
 */
public class PollAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private Context mContext;
    private int[] images={R.drawable.ic_poll_grid};
    private String[] mTitles={"巡检"};

    public PollAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public String getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_grid_ietm, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image_id);
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.gridView_text_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.textView.setText(mTitles[position]);

        return convertView;

    }


    static class ViewHolder {
        /**
         * 图片*
         */
        ImageView imageView;
        /**
         * 文字*
         */
        TextView textView;

    }
}
