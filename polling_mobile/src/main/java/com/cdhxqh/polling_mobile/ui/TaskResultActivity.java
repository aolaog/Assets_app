package com.cdhxqh.polling_mobile.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.ui.BaseActivity;
import com.cdhxqh.polling_mobile.ui.fragment.ResultFragment;
import com.cdhxqh.polling_mobile.ui.fragment.TaskScannFragment;

/**巡检结果界面**/
public class TaskResultActivity extends BaseActivity {

    private static final String TAG="TaskResultActivity";

    ResultFragment resultFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_result);


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
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
