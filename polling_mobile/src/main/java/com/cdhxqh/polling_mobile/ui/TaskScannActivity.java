package com.cdhxqh.polling_mobile.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.cdhxqh.polling_mobile.Application;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.model.Ins_task_device;
import com.cdhxqh.polling_mobile.ui.fragment.DeviceListFragment;
import com.cdhxqh.polling_mobile.ui.fragment.TaskScannFragment;


/**巡检扫描**/

public class TaskScannActivity extends BaseActivity {

    Ins_task_device ins_task_device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_scann);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ins_task_device=getIntent().getParcelableExtra("ins_task_device");
        setTitle("巡检设备");
        if (Application.getRfid() == null)
        {
            Toast.makeText(this, R.string.MakeSurePDAexitRfid, Toast.LENGTH_LONG).show();
            finish();
        } else {

        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        TaskScannFragment fragment = new TaskScannFragment();
        Bundle bundle = getIntent().getExtras();
        bundle.putParcelable("ins_task_device",ins_task_device);
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
