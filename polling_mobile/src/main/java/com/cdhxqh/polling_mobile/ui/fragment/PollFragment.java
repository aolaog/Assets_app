package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.PollAdapter;
import com.cdhxqh.polling_mobile.ui.PollListActivity;

public class PollFragment extends BaseFragment {

    private GridView gridView;

    private PollAdapter pollAdapter;

    public PollFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView_id);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
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
        showData();
        setEvent();
    }




    /**
     * 显示数据*
     */
    private void showData() {
        pollAdapter = new PollAdapter(getActivity());
        gridView.setAdapter(pollAdapter);
    }

    /**设置事件监听**/
    private void setEvent() {
        gridView.setOnItemClickListener(gridViewOnItemClickListener);
    }

    private AdapterView.OnItemClickListener gridViewOnItemClickListener=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    startIntent();
                    break;
            }

        }
    };

    private void startIntent() {

        Intent intent=new Intent();
        intent.setClass(getActivity(), PollListActivity.class);
        startActivity(intent);
    }
}
