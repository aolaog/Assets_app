package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.model.MemberModel;


public class UserFragment extends BaseFragment {
    private static final String TAG = "UserFragment";

    /**
     * 用户名*
     */
    private TextView loginid;
    /**
     * 密码*
     */
    private TextView password;
    /**
     * 人员名称*
     */
    private TextView personText;
    /**
     * 别名*
     */
    private TextView displayname;

    private MemberModel mMember;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        loginid = (TextView) view.findViewById(R.id.user_loginid_id);
        password = (TextView) view.findViewById(R.id.user_password_id);
        personText = (TextView) view.findViewById(R.id.user_personid);
        displayname = (TextView) view.findViewById(R.id.displayname_id);
        return view;
    }

    /**
     * 设置事件监听*
     */
    private void showData() {
        loginid.setText(mMember.login_id);
        password.setText(mMember.access_token);
        personText.setText(mMember.expires_in);
        displayname.setText(mMember.display_name);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getArguments().containsKey("model")) {
            mMember = getArguments().getParcelable("model");
            Log.i(TAG,"mMember="+mMember);
            showData();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


}
