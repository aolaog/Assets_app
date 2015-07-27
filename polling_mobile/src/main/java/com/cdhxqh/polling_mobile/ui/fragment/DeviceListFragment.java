package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.DeviceListAdapter;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.ui.widget.ItemDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备列表的Fragment
 */
public class DeviceListFragment extends BaseFragment {

    private static final String TAG = "DeviceListFragment";
    /**
     * mRecyclerView*
     */
    private RecyclerView mRecyclerView;
    /****/
    RecyclerView.LayoutManager mLayoutManager;
    /****/
    SwipeRefreshLayout mSwipeLayout;

    /**
     * DeviceListAdapter*
     */
    DeviceListAdapter deviceListAdapter;
    String task_id;

    PollDataSource mDataSource = Application.getDataSource();

    public DeviceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("task_id")) {
            task_id = getArguments().getString("task_id");

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);
        setAdapter();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new ItemDivider(getActivity(),
                ItemDivider.VERTICAL_LIST));
    }


    /**
     * setAdapter*
     */
    private void setAdapter() {
        deviceListAdapter = new DeviceListAdapter(getActivity());
        mRecyclerView.setAdapter(deviceListAdapter);
//        addTestData();
        getByTaskId(task_id);
    }


    /**
     * 根据task_id查询巡检设备*
     */
    private void getByTaskId(String task_id) {
        ArrayList<Ins_task_device> list = mDataSource.getAllDevice(task_id);
        deviceListAdapter.update( list, true);
    }

}
