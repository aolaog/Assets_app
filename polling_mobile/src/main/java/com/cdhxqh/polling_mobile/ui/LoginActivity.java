package com.cdhxqh.polling_mobile.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cdhxqh.polling_mobile.AppManager;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.api.HttpRequestHandler;
import com.cdhxqh.polling_mobile.api.Manager;
import com.cdhxqh.polling_mobile.model.Asset_class;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.utils.AccountUtils;
import com.cdhxqh.polling_mobile.utils.MessageUtils;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;


/**
 * Created by yugy on 14-2-26.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private ProgressDialog mProgressDialog;
    private MemberModel mLoginProfile;
    private CheckBox checkBox;

    private boolean isRemember; //是否记住密码

    /**设置IP**/
    TextView ipText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UmengUpdateAgent.setDefault();
        UmengUpdateAgent.update(this);
        mLoginProfile = AccountUtils.readLoginMember(this);
        mUsername = (EditText) findViewById(R.id.login_username_edit);
        mPassword = (EditText) findViewById(R.id.login_password_edit);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        mLogin = (Button) findViewById(R.id.login_login_btn);

        ipText=(TextView)findViewById(R.id.login_set_ip_text);
        ipText.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(cheBoxOnCheckedChangListener);
        boolean isChecked = AccountUtils.getIsChecked(LoginActivity.this);
        if (isChecked) {
            checkBox.setChecked(isChecked);
            mUsername.setText(AccountUtils.getUserName(LoginActivity.this));
            mPassword.setText(AccountUtils.getUserPassword(LoginActivity.this));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_btn:
                if (mUsername.getText().length() == 0) {
                    mUsername.setError(getString(R.string.login_error_empty_user));
                    mUsername.requestFocus();
                } else if (mPassword.getText().length() == 0) {
                    mPassword.setError(getString(R.string.login_error_empty_passwd));
                    mPassword.requestFocus();
                } else {
                    login();
                }
                break;
            case R.id.login_set_ip_text:
                setIpAddress();
                break;

        }
    }


    private CompoundButton.OnCheckedChangeListener cheBoxOnCheckedChangListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isRemember = isChecked;
        }
    };

    private void login() {
        mProgressDialog = ProgressDialog.show(LoginActivity.this, null,
                getString(R.string.login_loging), true, true);

        Manager.loginWithUsername(this,
                mUsername.getText().toString(),
                mPassword.getText().toString(),
                new HttpRequestHandler<Integer>() {
                    @Override
                    public void onSuccess(Integer data) {

                        MessageUtils.showMiddleToast(LoginActivity.this, "登陆成功");


                        if (checkBox.isChecked()) {
                            //保存用户名和密码
                            saveUserNameAndPassWord();
                        }

                        mProgressDialog.dismiss();
                        boolean b = AccountUtils.getIsDown(LoginActivity.this);
                        Log.i(TAG, "b=" + b);
                        startIntent(b);

                    }

                    @Override
                    public void onSuccess(Integer data, int totalPages, int currentPage) {
                    }

                    @Override
                    public void onFailure(String error) {
                        MessageUtils.showErrorMessage(LoginActivity.this, error);
                        mProgressDialog.dismiss();
                    }
                });
    }

    /**
     * 保存用户名和密码*
     */
    private void saveUserNameAndPassWord() {
        if (isRemember) {
            AccountUtils.setChecked(LoginActivity.this, isRemember);
            //记住密码
            AccountUtils.setUserNameAndPassWord(LoginActivity.this, mUsername.getText().toString(), mPassword.getText().toString());
        }
    }


    /**
     * 启动*
     */
    private void startIntent(boolean b) {
        Intent inetnt = new Intent();
        if (b) {
            inetnt.setClass(this, MainActivity.class);
        } else {
            inetnt.setClass(this, DownloadBaseDataActivity.class);
        }
        startActivity(inetnt);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.AppExit(LoginActivity.this);
        }
    }


    /**设置服务段的IP地址**/
   private void setIpAddress(){
       final EditText editText = new EditText(LoginActivity.this);
       String sIp=AccountUtils.getIp(LoginActivity.this);
       if(TextUtils.isEmpty(sIp)) {
           sIp = Manager.HTTP_API_URL;
       }
           editText.setText(sIp);

       editText.setTextSize(20);
       final InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

       new AlertDialog.Builder(LoginActivity.this)
               .setTitle("设置服务器地址")
               .setIcon(android.R.drawable.ic_dialog_info)
               .setView(editText)
               .setPositiveButton(
                       "确定",
                       new android.content.DialogInterface.OnClickListener() {

                           @Override
                           public void onClick(DialogInterface dialog,
                                               int which) {
                               String values = editText.getText()
                                       .toString();
                               AccountUtils.setIP(LoginActivity.this,values);
                               Log.i(TAG, editText.getText().toString());
                               manager.toggleSoftInput(0,
                                       InputMethodManager.HIDE_NOT_ALWAYS);
                           }
                       })
               .setNegativeButton(
                       "取消",
                       new android.content.DialogInterface.OnClickListener() {

                           @Override
                           public void onClick(DialogInterface dialog,
                                               int which) {
                               manager.toggleSoftInput(0,
                                       InputMethodManager.HIDE_NOT_ALWAYS);

                           }
                       }).show();
   }


}


