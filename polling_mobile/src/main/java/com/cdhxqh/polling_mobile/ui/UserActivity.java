package com.cdhxqh.polling_mobile.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.model.MemberModel;
import com.cdhxqh.polling_mobile.ui.fragment.UserFragment;


/**
 * Created by yw on 2015/5/2.
 */
public class UserActivity extends BaseActivity {
    private static final String TAG = "UserActivity";
    MemberModel mMember;
    String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra("model")) {
                mMember = intent.getParcelableExtra("model");
                mUsername = mMember.display_name;
                setTitle(mUsername);
            } else {
                mUsername = intent.getStringExtra("displayname");
                setTitle(mUsername);
            }
        } else {
            mUsername = savedInstanceState.getString("displayname");
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model",(Parcelable) mLoginProfile);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("loginid", mUsername);
        super.onSaveInstanceState(outState);
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

}