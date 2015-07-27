package com.cdhxqh.polling_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.R;


/**
 * Created by yugy on 14-3-15.
 */
public class DrawerAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mTitles;
    private final int mIcons[] = new int[]{
            R.drawable.ic_polling,
            R.drawable.ic_seach,

            R.drawable.ic_setting

    };

    public DrawerAdapter(Context context) {
        mContext = context;
        mTitles = context.getResources().getStringArray(R.array.v2ex_nav_drawers);
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

    public int getIconId(int position) {
        return mIcons[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView item = (TextView) convertView;
        if (item == null) {
            item = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_drawer_item, null);
        }
        item.setText(getItem(position));
        item.setCompoundDrawablesWithIntrinsicBounds(getIconId(position), 0, 0, 0);
        return item;
    }
}
