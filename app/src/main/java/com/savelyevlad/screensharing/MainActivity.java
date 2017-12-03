package com.savelyevlad.screensharing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.savelyevlad.screensharing.sharing.SharingOnClickSetter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

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

    public static class SharingFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_sharing, null);

            Bundle bundle = getArguments();

            if(bundle != null) {
                String msg = bundle.getString(KEY_MSG_1);
                if(msg != null) {
                    // wtf
                }
            }

            return view;
        }
    }

    public static class WatchFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_watch, null);

            Bundle bundle = getArguments();

            if(bundle != null) {
                String msg = bundle.getString(KEY_MSG_2);
                if(msg != null) {
                    // wtf
                }
            }

            return view;
        }
    }

    public static class SettingsFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_settings, null);

            Bundle bundle = getArguments();

            if(bundle != null) {
                String msg = bundle.getString(KEY_MSG_3);
                if(msg != null) {
                    // wtf
                }
            }

            return view;
        }
    }

    public static class HelpFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_help, null);

            Bundle bundle = getArguments();

            if(bundle != null) {
                String msg = bundle.getString(KEY_MSG_4);
                if(msg != null) {
                    // wtf
                }
            }

            return view;
        }
    }

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

//        SharingOnClickSetter sharingOnClickSetter = new SharingOnClickSetter(startButton, stopButton, textID, this);
//        sharingOnClickSetter.start();
//
//        projectionManager = sharingOnClickSetter.getProjectionManager();
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

    private SharingFragment sharingFragment = new SharingFragment();
    private WatchFragment watchFragment = new WatchFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private HelpFragment helpFragment = new HelpFragment();

    private int lastSelected = 0;

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

        if (id == R.id.nav_share) {
//            SharingFragment sharingFragment = (SharingFragment) fragmentManager.findFragmentByTag(TAG_1);
//            SharingFragment sharingFragment = new SharingFragment();

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

        setTitle(item.getTitle());

        return true;
    }
}