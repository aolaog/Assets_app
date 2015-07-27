package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.adapter.AssetDetailAdapter;
import com.cdhxqh.polling_mobile.adapter.PollListAdapter;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.model.Asset_device;
import com.cdhxqh.polling_mobile.ui.widget.ItemDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产详情的fragment*
 */
public class AssetDetailsFragment extends BaseFragment {

    private static final String TAG = "AssetDetailsFragment";
    RecyclerView recyclerView;

    AssetDetailAdapter assetDetailAdapter;

    List<Asset_device> asset_devices;

    public AssetDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_asset_details, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recyclerView);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        assetDetailAdapter = new AssetDetailAdapter(getActivity());
        recyclerView.setAdapter(assetDetailAdapter);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new ItemDivider(getActivity(),
                ItemDivider.VERTICAL_LIST));
        if (getArguments().containsKey("attributes")) {
            String attributes = getArguments().getString("attributes");
            //解析attributes
            asset_devices = parsingAsset_Device(attributes);
            Log.i(TAG, "asset_devices=" + asset_devices.size());
            if(asset_devices!=null) {
                assetDetailAdapter.update((ArrayList<Asset_device>) asset_devices, true);
            }
        }


    }

    private List<Asset_device> parsingAsset_Device(String attributes) {
        List<Asset_device> list = new ArrayList<Asset_device>();
        try {
            JSONArray jsonArray = new JSONArray(attributes);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Asset_device asset_device = new Asset_device();
                asset_device.setLabel(json.getString("label"));
                String value=json.getString("value");
                if(value==null||value.equals("")){
                    value="暂无信息";
                }
                asset_device.setValue(value);
                Log.i(TAG, "lable=" + json.getString("label") + "value=" + json.getString("value"));
                list.add(asset_device);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
