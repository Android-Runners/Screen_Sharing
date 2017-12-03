package com.savelyevlad.screensharing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.savelyevlad.screensharing.help.HelpFragment;
import com.savelyevlad.screensharing.settings.SettingsFragment;
import com.savelyevlad.screensharing.sharing.SharingFragment;
import com.savelyevlad.screensharing.watch.WatchFragment;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;

    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";
    final static String TAG_3 = "FRAGMENT_3";
    final static String TAG_4 = "FRAGMENT_4";

    final static String KEY_MSG_1 = "FRAGMENT1_MSG";
    final static String KEY_MSG_2 = "FRAGMENT2_MSG";
    final static String KEY_MSG_3 = "FRAGMENT3_MSG";
    final static String KEY_MSG_4 = "FRAGMENT4_MSG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getFragmentManager();

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.container, new SharingFragment(), TAG_1);
            fragmentTransaction.commit();
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        Log.e("title", "help fragment(this)");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getMain() {
        return main;
    }

    public static void setMain(MainActivity main) {
        MainActivity.main = main;
    }

    private static MainActivity main;
    private SharingFragment sharingFragment = new SharingFragment();
    private WatchFragment watchFragment = new WatchFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private HelpFragment helpFragment = new HelpFragment();

    private int lastSelected = 0;

    @Override
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @SuppressLint("CommitTransaction")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id == lastSelected) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        lastSelected = id;
        main = this;
        Log.e("lol", "here");

        if (id == R.id.nav_share) {
//            SharingFragment sharingFragment = (SharingFragment) fragmentManager.findFragmentByTag(TAG_1);

            helpFragment.setMainActivity(this);

            if(sharingFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_1, "lol");
                sharingFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, sharingFragment,
                        TAG_1);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_watch) {

            if(watchFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_2, "lol");
                watchFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, watchFragment, TAG_2);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_settings) {

            if(settingsFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_3, "lol");
                settingsFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, settingsFragment, TAG_3);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_help) {

            if (helpFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_4, "lol");
                helpFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, helpFragment, TAG_4);
                fragmentTransaction.commit();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

//        setTitle(item.getTitle());

        return true;
    }
}
