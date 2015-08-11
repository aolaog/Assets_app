package com.cdhxqh.polling_mobile.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.JsonUtils;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.database.PollDataSource;
import com.cdhxqh.polling_mobile.model.Asset_class;
import com.cdhxqh.polling_mobile.model.Asset_three_class;
import com.cdhxqh.polling_mobile.model.Asset_two_class;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.utils.AccountUtils;
import com.cdhxqh.polling_mobile.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载基础数据*
 */

public class DownloadBaseDataActivity extends BaseActivity {


    private static final String TAG = "DownloadBaseDataActivity";

    /**
     * 资产一级*
     */
    Button assetOneButton;
    /**
     * 资产二级*
     */
    Button assetTwoButton;
    /**
     * 资产三级*
     */
    Button assetThreeButton;


    /**
     * 全部下载*
     */
    Button allDownBtn;


    private MemberModel mLoginProfile;


    private ProgressDialog mProgressDialog;


    PollDataSource mDataSource = Application.getDataSource();


    /**
     * 是否全部下载*
     */
    boolean isAllDown = false;


    /**
     * 获取资产一级的数据*
     */
    ArrayList<Asset_class> asset_classes = new ArrayList<Asset_class>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_base_data);

        mLoginProfile = AccountUtils.readLoginMember(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById();

        initView();
    }

    /**
     * 创建进度条*
     */
    private void createProgressDialog(String message) {
        mProgressDialog = ProgressDialog.show(DownloadBaseDataActivity.this, null,
                message, true, true);
    }


    /**
     * 初始化Id*
     */
    private void findViewById() {
        assetOneButton = (Button) findViewById(R.id.asset_one_button_id);
        assetTwoButton = (Button) findViewById(R.id.asset_two_button_id);
        assetThreeButton = (Button) findViewById(R.id.asset_three_button_id);
        allDownBtn = (Button) findViewById(R.id.btn_all_down_id);
    }

    /**
     * 设置事件控件*
     */
    private void initView() {

        assetOneButton.setOnClickListener(assetOneButtonOnClickListener);
        assetTwoButton.setOnClickListener(assetTwoButtonOnClickListener);
        assetThreeButton.setOnClickListener(assetThreeButtonOnClickListener);
        allDownBtn.setOnClickListener(allDownBtnOnClickListener);

    }


    /**
     * 获取一级资产数据*
     */
    private View.OnClickListener assetOneButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createProgressDialog("获取资产一级");

            getAssetsOne();


        }


    };


    /**
     * 获取二级资产数据*
     */
    private View.OnClickListener assetTwoButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createProgressDialog("获取资产二级");
            if (asset_classes == null || asset_classes.size() == 0) {
                MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "请下载二级资产数据");
            } else {
                getAssetsTwo(asset_classes);
            }
        }


    };

    /**
     * 获取三级资产数据*
     */
    private View.OnClickListener assetThreeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createProgressDialog("获取资产三级");

            ArrayList<Asset_two_class> tList = findByTwoAsset();
            if (tList == null || tList.size() == 0) {
                MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "请下载三级资产数据");
            } else {
                getAssetThere(tList);
            }

        }


    };


    /**
     * 全部下载*
     */
    private View.OnClickListener allDownBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isAllDown = true;
            createProgressDialog("正在下载数据");
            //一级
            getAssetsOne();
            //二级
            getAssetsTwo(asset_classes);
            //三级
            ArrayList<Asset_two_class> tList = findByTwoAsset();
            getAssetThere(tList);
        }
    };




    /**
     * 获取资产一级数据*
     */

    private void getAssetsOne() {
        Manager.getAsset_class(this, mLoginProfile.access_token, true, profileHandler);

    }

    /**
     * 获取资产二级数据
     *
     * @param asset_class*
     */
    private void getAssetsTwo(ArrayList<Asset_class> asset_class) {

        Manager.getAsset_Child_class(this, asset_class, mLoginProfile.access_token, true, profileHandler1);
    }


    private HttpRequestHandler<ArrayList<Asset_class>> profileHandler = new HttpRequestHandler<ArrayList<Asset_class>>() {
        @Override
        public void onSuccess(ArrayList<Asset_class> data) {
            asset_classes = data;
            if (!isAllDown) {
                MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息成功");
                mProgressDialog.dismiss();
            }

            assetOneButton.setText("资产一级(完成)");
            assetOneButton.setBackgroundColor(getResources().getColor(R.color.alert));
            assetOneButton.setEnabled(false);
        }

        @Override
        public void onSuccess(ArrayList<Asset_class> data, int totalPages, int currentPage) {
            MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息成功");
            mProgressDialog.dismiss();
        }

        @Override
        public void onFailure(String error) {
            MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息失败");
            mProgressDialog.dismiss();

        }
    };


    private HttpRequestHandler<Boolean> profileHandler1 = new HttpRequestHandler<Boolean>() {
        @Override
        public void onSuccess(Boolean b) {
            Log.i(TAG, "b=" + b);

            if (b) {
                if (!isAllDown) {
                    MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息成功");
                    mProgressDialog.dismiss();
                }
                assetTwoButton.setText("资产二级(完成)");
                assetTwoButton.setBackgroundColor(getResources().getColor(R.color.alert));
                assetTwoButton.setEnabled(false);
            } else {
                assetTwoButton.setText("资产二级(失败)");
                assetTwoButton.setBackgroundColor(getResources().getColor(R.color.confirm));
            }

        }

        @Override
        public void onSuccess(Boolean b, int totalPages, int currentPage) {
            MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息成功");
            mProgressDialog.dismiss();
        }

        @Override
        public void onFailure(String error) {
            MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息失败");
            mProgressDialog.dismiss();

        }
    };


    private HttpRequestHandler<Boolean> profileHandler2 = new HttpRequestHandler<Boolean>() {
        @Override
        public void onSuccess(Boolean b) {
            Log.i(TAG, "b=" + b);

            if (b) {

                MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息成功");
                mProgressDialog.dismiss();

                assetThreeButton.setText("资产三级(完成)");
                assetThreeButton.setBackgroundColor(getResources().getColor(R.color.alert));
                assetTwoButton.setEnabled(false);
                if (isAllDown) {
                    allDownBtn.setText("下载(完成)");
                    allDownBtn.setBackgroundColor(getResources().getColor(R.color.alert));
                }
                startIntent();

                /**记录数据下载状态**/
                AccountUtils.setIsDown(DownloadBaseDataActivity.this, true);


            } else {
                assetThreeButton.setText("资产三级(失败)");
                assetThreeButton.setBackgroundColor(getResources().getColor(R.color.confirm));
                if (isAllDown) {
                    allDownBtn.setText("下载(失败)");
                    allDownBtn.setBackgroundColor(getResources().getColor(R.color.confirm));
                }
                startIntent();
            }

        }

        @Override
        public void onSuccess(Boolean b, int totalPages, int currentPage) {
            MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息成功");
            mProgressDialog.dismiss();
        }

        @Override
        public void onFailure(String error) {
            MessageUtils.showMiddleToast(DownloadBaseDataActivity.this, "获取信息失败");
            mProgressDialog.dismiss();

        }
    };


    /**
     * 查询二级分类的数据*
     */
    private ArrayList<Asset_two_class> findByTwoAsset() {

        ArrayList<Asset_two_class> three_list = mDataSource.getAllTwoDevice();

        return three_list;

    }


    /**
     * 获取三级资产设备*
     */
    private void getAssetThere(ArrayList<Asset_two_class> asset_two_classes) {

        Manager.getAsset_Three_class(this, asset_two_classes, mLoginProfile.access_token, true, profileHandler2);

    }


    /**
     * 启动*
     */
    private void startIntent() {
        Intent inetnt = new Intent();
        inetnt.setClass(this, MainActivity.class);
        startActivity(inetnt);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                startIntent();
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_download_base_data, menu);

        return true;
    }

}
