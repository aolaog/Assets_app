package com.cdhxqh.polling_mobile.ui;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cdhxqh.polling_mobile.AppManager;
import com.cdhxqh.polling_mobile.R;
import com.cdhxqh.polling_mobile.ui.fragment.NavigationDrawerFragment;
import com.cdhxqh.polling_mobile.ui.fragment.PollFragment;
import com.cdhxqh.polling_mobile.ui.fragment.SettingsFragment;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = "MainActivity";


    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ViewGroup mDrawerLayout;

    private View mActionbarCustom;

    private TextView text;

    private PollFragment pollFragment;

    private SettingsFragment settingsFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String[] mFavoriteTabTitles;
    private String[] mFavoriteTabPaths;
    private String[] mMainTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (ViewGroup) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.left_drawer);
        mTitle = getTitle();


        mFavoriteTabPaths = getResources().getStringArray(R.array.v2ex_favorite_tab_paths);
        mMainTitles = getResources().getStringArray(R.array.v2ex_nav_drawers);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setCustomView(R.layout.actionbar_custom_text);
        mActionbarCustom = supportActionBar.getCustomView();
        text = (TextView) mActionbarCustom.findViewById(R.id.text_title_id);
        text.setText(mTitle);
        supportActionBar.setTitle("");
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.left_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();

        }

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(true);

//        if (mIsLogin) initAccount();
    }

    int mSelectPos = 0;

    @Override
    public void onNavigationDrawerItemSelected(final int position) {
        mSelectPos = position;
        Log.i(TAG, "position=" + position);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (position) {
            case 0: //巡检
                if (pollFragment == null) {
                    pollFragment = new PollFragment();
                    Bundle bundle = new Bundle();
                    pollFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.container, pollFragment).commit();
                break;
            case 1:
                break;

            case 2:
                if (settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    Bundle bundle = new Bundle();
                    settingsFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.container, settingsFragment).commit();
                break;

        }

    }


    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        mTitle = mMainTitles[mSelectPos];
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(mActionbarCustom);
        text.setText(mTitle);
        actionBar.setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }
        exit();

    }

    /**
     * 退出登陆*
     */
    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.AppExit(MainActivity.this);
        }
    }
}
