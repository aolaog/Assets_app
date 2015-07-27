package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.cdhxqh.polling_mobile.adapter.PollListAdapter;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Ins_task_ticket;
import com.cdhxqh.polling_mobile.ui.widget.ItemDivider;

import java.util.ArrayList;

public class PollListFragment extends BaseFragment implements HttpRequestHandler<ArrayList<Ins_task_ticket>> {
    private static final String TAG = "PollListFragment";
    /**
     * mRecyclerView*
     */
    private RecyclerView mRecyclerView;
    /****/
    RecyclerView.LayoutManager mLayoutManager;

    /**
     * PollListAdapter*
     */
    PollListAdapter pollListAdapter;


    PollDataSource mDataSource = Application.getDataSource();

    public static PollListFragment newInstance(String param1, String param2) {
        PollListFragment fragment = new PollListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poll_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
        return view;
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);
        Manager.getInspect_ticket(getActivity(), mLoginProfile.access_token, true, this);
//        setAdapter();

        pollListAdapter = new PollListAdapter(getActivity());
        mRecyclerView.setAdapter(pollListAdapter);


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new ItemDivider(getActivity(),
                ItemDivider.VERTICAL_LIST));
    }


    /**
     * setAdapter*
     */
    private void setAdapter() {
        pollListAdapter = new PollListAdapter(getActivity());
        mRecyclerView.setAdapter(pollListAdapter);
        addTestData();
    }


    /**
     * 添加测试数据*
     */
    private void addTestData() {
        ArrayList<Ins_task_ticket> pollTaskModels = new ArrayList<Ins_task_ticket>();
        for (int i = 0; i < 10; i++) {
            Ins_task_ticket pollTaskModel = new Ins_task_ticket();
            pollTaskModel.setId(i + "");
            pollTaskModel.setTaskTempletName("2层3区UPS设备检查");
            pollTaskModel.setInspectDate("2015-7-7");
            pollTaskModel.setStatus(-2);
            pollTaskModels.add(pollTaskModel);
        }
        pollListAdapter.update(pollTaskModels, true);
    }

    @Override
    public void onSuccess(ArrayList<Ins_task_ticket> data) {

        Log.i(TAG, "data1=" + data);

        if (data.size() == 0) return;

        mDataSource.isInsertTask(data);
        pollListAdapter.update(getAllIns_task_tickets(), true);

    }

    @Override
    public void onSuccess(ArrayList<Ins_task_ticket> data, int totalPages, int currentPage) {
        Log.i(TAG, "data2=" + data);
    }

    @Override
    public void onFailure(String error) {

    }


    /**
     * 查询当天所有巡检任务单*
     */
    private ArrayList<Ins_task_ticket> getAllIns_task_tickets() {
        ArrayList<Ins_task_ticket> list = mDataSource.getAllDevice();
        return list == null ? null : list;
    }
}
