package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Toast;

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
    /**暂无信息的布局**/
    LinearLayout linearLayout;


    /**
     * DeviceListAdapter*
     */
    DeviceListAdapter deviceListAdapter;
    String task_id;

    PollDataSource mDataSource = Application.getDataSource();

    private ProgressDialog mProgressDialog;

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

        linearLayout=(LinearLayout)view.findViewById(R.id.not_linear_layout_id);

        return view;
    }

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
            Log.i(TAG,"task_id="+task_id);


        }

        mProgressDialog = ProgressDialog.show(getActivity(), null,
                getActivity().getString(R.string.loading_hint_text), true, true);


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
        deviceListAdapter = new DeviceListAdapter(getActivity(), task_id);
        mRecyclerView.setAdapter(deviceListAdapter);
        getByTaskId(task_id);
    }


    /**
     * 根据task_id查询巡检设备*
     */
    private void getByTaskId(String task_id) {
        ArrayList<Ins_task_device> list = mDataSource.getAllDevice(task_id);
        mProgressDialog.dismiss();
        if (list == null) {
            linearLayout.setVisibility(View.VISIBLE);
            return;
        }
        deviceListAdapter.update(list, true);
    }

}
