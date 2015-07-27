package com.cdhxqh.polling_mobile.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cdhxqh.polling_mobile.AppManager;
import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.utils.AccountUtils;
import com.cdhxqh.polling_mobile.utils.FileUtils;
import com.cdhxqh.polling_mobile.utils.MessageUtils;
import com.cdhxqh.polling_mobile.utils.PhoneUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.ArrayList;

/**
 * 设置
 * Created by yw on 2015/5/13.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String TAG = "SettingsFragment";
    SharedPreferences mPreferences;
    Preference mCache;
    Preference mFeedback;
    Preference mUpdate;
    Preference mAbout;
    CheckBoxPreference mHttps;
    CheckBoxPreference mLoadimage;
    Button mLogout;
    Application mApp = Application.getInstance();

    private ProgressDialog mProgressDialog;

    private MemberModel mLoginProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLoginProfile = AccountUtils.readLoginMember(getActivity());
        ViewGroup root = (ViewGroup) getView();
        ListView localListView = (ListView) root.findViewById(android.R.id.list);
        localListView.setBackgroundColor(0);
        localListView.setCacheColorHint(0);
        root.removeView(localListView);

        ViewGroup localViewGroup = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_settings, null);
        ((ViewGroup) localViewGroup.findViewById(R.id.setting_content))
                .addView(localListView, -1, -1);
        localViewGroup.setVisibility(View.VISIBLE);
        root.addView(localViewGroup);

        //退出登录
        mLogout = (Button) localViewGroup.findViewById(R.id.setting_logout);
        if (AccountUtils.isLogined(getActivity())) {
            mLogout.setVisibility(View.VISIBLE);
        } else {
            mLogout.setVisibility(View.GONE);
        }
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginOutExit();

            }
        });

        // https
        mHttps = (CheckBoxPreference) findPreference("pref_https");
        mHttps.setChecked(mApp.isHttps());
        mHttps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                mApp.setConfigHttps(mHttps.isChecked());
                return true;
            }
        });

        // 加载图片loadimage
        mLoadimage = (CheckBoxPreference) findPreference("pref_noimage_nowifi");
        mLoadimage.setChecked(!mApp.isLoadImageInMobileNetwork());
        mLoadimage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                mApp.setConfigLoadImageInMobileNetwork(!mLoadimage.isChecked());
                return true;
            }
        });

        // // 版本更新
        mUpdate = (Preference) findPreference("pref_check_update");
        mUpdate.setSummary("版本: " + PhoneUtils.getPackageInfo(getActivity()).versionName);
        mUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                checkUpdate();
                return true;
            }
        });

        // 清除缓存
        mCache = (Preference) findPreference("pref_cache");
        mCache.setSummary(FileUtils.getFileSize(FileUtils.getCacheSize(getActivity())));
        mCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FileUtils.clearAppCache(getActivity());
                mCache.setSummary("0KB");
                return true;
            }
        });

        // 意见反馈
        mFeedback = (Preference) findPreference("pref_feedback");
        mFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    MessageUtils.showMiddleToast(getActivity(), "软件市场里暂时没有找到V2EX");
                }
                return true;
            }

        });

        // 关于我们
        mAbout = (Preference) findPreference("pref_about");
        mAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });
    }


    /**
     * 退出登陆*
     */
    private void LoginOutExit() {
        mProgressDialog = ProgressDialog.show(getActivity(), null,
                mApp.getString(R.string.loginout_text), true, true);
        Manager.getLoginout(getActivity(), mLoginProfile.access_token, true, new HttpRequestHandler<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                mProgressDialog.dismiss();
                Manager.logout(getActivity());
                AccountUtils.removeAll(getActivity());
                AppManager.AppExit(getActivity());

            }

            @Override
            public void onSuccess(ArrayList<String> data, int totalPages, int currentPage) {
                mProgressDialog.dismiss();
                Manager.logout(getActivity());
                AccountUtils.removeAll(getActivity());
                AppManager.AppExit(getActivity());
            }

            @Override
            public void onFailure(String error) {
                mProgressDialog.dismiss();
                Manager.logout(getActivity());
                AccountUtils.removeAll(getActivity());
                AppManager.AppExit(getActivity());
            }
        });
    }

    private void checkUpdate() {
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        MessageUtils.showMiddleToast(getActivity(), "已经是最新版本");
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        MessageUtils.showMiddleToast(getActivity(), "没有wifi连接， 只在wifi下更新");
                        break;
                    case UpdateStatus.Timeout: // time out
                        MessageUtils.showMiddleToast(getActivity(), "超时");
                        break;
                }
            }
        });
        UmengUpdateAgent.update(getActivity());
    }

}
