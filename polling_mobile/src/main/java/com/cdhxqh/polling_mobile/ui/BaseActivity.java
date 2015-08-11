package com.cdhxqh.polling_mobile.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.cdhxqh.polling_mobile.AppManager;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.ui.fragment.BaseFragment;
import com.cdhxqh.polling_mobile.utils.AccountUtils;


/**
 * Created by yugy on 14-1-29.
 */
public class BaseActivity extends ActionBarActivity
        implements AccountUtils.OnAccountListener, BaseFragment.BackHandledInterface {
    private static final String TAG="BaseActivity";
    private ProgressDialog mProgressDialog;
    protected boolean mIsLogin;
    protected MemberModel mLoginProfile;

    private String sverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sverIp=AccountUtils.getIp(BaseActivity.this);

        mIsLogin = AccountUtils.isLogined(this);
        if (mIsLogin)
            mLoginProfile = AccountUtils.readLoginMember(this);
        AccountUtils.registerAccountListener(this);


        //添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        AccountUtils.unregisterAccountListener(this);
        super.onDestroy();
        //结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.toString());
//        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.toString());
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public void onLogout() {
        mIsLogin = false;
    }

    @Override
    public void onLogin(MemberModel member) {
        mIsLogin = true;
        mLoginProfile = member;
    }

    private BaseFragment mBackHandedFragment;

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    private void initProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
    }

    public void showProgressBar(boolean show, String message) {
        initProgressBar();
        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    public void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    /**获取设置的Ip信息**/
    public String ipInfo(){
        sverIp=AccountUtils.getIp(BaseActivity.this);
        return sverIp;
    }


}
