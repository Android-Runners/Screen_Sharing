package com.savelyevlad.screensharing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.savelyevlad.screensharing.help.HelpFragment;
import com.savelyevlad.screensharing.settings.SettingsFragment;
import com.savelyevlad.screensharing.sharing.SharingFragment;
import com.savelyevlad.screensharing.watch.WatchFragment;

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
        int index = item.getItemId();

        if(index == lastSelected) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        lastSelected = index;

        Log.e("lol", "here");

        if (index == R.id.nav_share) {
//            SharingFragment sharingFragment = (SharingFragment) fragmentManager.findFragmentByTag(TAG_1);

            if(sharingFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_1, "lol");
                sharingFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, sharingFragment,
                        TAG_1);
                fragmentTransaction.commit();

                {
                    PublicStaticObjects.initSocket();

                    textID = sharingFragment.getActivity().findViewById(R.id.sharingID);
                    startButton = sharingFragment.getActivity().findViewById(R.id.startButton);
                    stopButton = sharingFragment.getActivity().findViewById(R.id.stopButton);

                    projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

                    if(PublicStaticObjects.isClicked()) {
                        textID.setText(String.valueOf(PublicStaticObjects.getID()));
                    }

                    startButton.setOnClickListener(view1 -> {
                        PublicStaticObjects.setStopped(false);
                        if(!PublicStaticObjects.isWasFirstClick()) {
                            Thread thread = new Thread(() -> {
                                try {
                                    PublicStaticObjects.getObjectOutputStream().writeObject(-1);
                                    Object object = PublicStaticObjects.getObjectInputStream().readObject();
                                    id = (Integer) object;
                                } catch (IOException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            PublicStaticObjects.setIsClicked(true);
                            PublicStaticObjects.setID(id);
                            textID.setText(String.valueOf(id));
                            startActivityForResult(projectionManager.createScreenCaptureIntent(), 228);
                        }
                        PublicStaticObjects.setWasFirstClick(true);
                    });

                    stopButton.setOnClickListener((v) -> PublicStaticObjects.setStopped(true));
                }
            }
        } else if (index == R.id.nav_watch) {
            if(watchFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_2, "lol");
                watchFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, watchFragment, TAG_2);
                fragmentTransaction.commit();
            }
        } else if (index == R.id.nav_settings) {

            if(settingsFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_MSG_3, "lol");
                settingsFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, settingsFragment, TAG_3);
                fragmentTransaction.commit();
            }
        } else if (index == R.id.nav_help) {

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

    private Button startButton;
    private Button stopButton;
    private TextView textID;

    private boolean running = false;

    private MediaProjectionManager projectionManager;

    private int id;

    private MediaProjection mediaProjection;
    private int displayWidth;
    private int displayHeight;

    private ImageReader imageReader;

    private Handler handler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 228) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);

            if (mediaProjection != null) {

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int density = metrics.densityDpi;
                int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
                        | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                displayHeight=size.y;
                displayWidth=size.x;


                imageReader = ImageReader.newInstance(size.x, size.y, PixelFormat.RGBA_8888, 2);

                mediaProjection.createVirtualDisplay("screencap",
                        size.x, size.y, density,
                        flags, imageReader.getSurface(), null, handler);
                imageReader.setOnImageAvailableListener(new ImageAvailableListener(), handler);
            }

        }
    }

    public void createImage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

        File file1 = new File(Environment.getExternalStorageDirectory() +"/captures");
        file1.mkdir();

        File file = new File(Environment.getExternalStorageDirectory() +
                "/captures/"+ System.currentTimeMillis() + ".jpg");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
            //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int i = 1;
    boolean isAlready = false;
    Bitmap bitmap;
    Bitmap bit;
    int pixelStride, rowStride, rowPadding;
    @SuppressLint("NewApi")
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
//            if(true) {
            //       Log.e(",", System.currentTimeMillis() +  " " + currentTime);
            Image image = null;
            FileOutputStream fos = null;
            //  Bitmap bitmap = null;

            ByteArrayOutputStream stream = null;
            try {
                image = imageReader.acquireLatestImage();
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    if(!isAlready) {
                        pixelStride = planes[0].getPixelStride();
                        rowStride = planes[0].getRowStride();
                        rowPadding = rowStride - pixelStride * displayWidth;
                    }
                    bitmap = Bitmap.createBitmap(displayWidth + rowPadding / pixelStride,
                            displayHeight, Bitmap.Config.ARGB_4444);
                    bitmap.copyPixelsFromBuffer(buffer);
                    //             Log.e("IN AVAILLIST", i++ + "");
                    bit = bitmap.copy(Bitmap.Config.ARGB_4444, false);
                    //   createImage(bit);
                    if(!PublicStaticObjects.isStopped()) {
                        sendImage(bit);
                    }
                    isAlready = true;
//                        if(!isStopped) {
//
//                        }
                 /*   Thread thread = new Thread(new Runnable() {
                        @Override
                        public synchronized void run() {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bit.compress(Bitmap.CompressFormat.JPEG, PublicStaticObjects.getQuality(), bytes);
                            try {
                                PublicStaticObjects.getObjectOutputStream().writeObject(bytes.toByteArray());
                                PublicStaticObjects.getObjectOutputStream().flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    thread.join();*/


                    //   sendImage(bitmap);
                }

            } catch (Throwable e) {
                Log.e("Throwable", e.getMessage());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (image != null) {
                    image.close();
                }
            }
        }
//        }
    }

    private byte[] concat(byte[] a, byte[] b) {
        byte[] t = new byte[a.length + b.length];
        System.arraycopy(a, 0, t, 0, a.length);
        System.arraycopy(b, 0, t, a.length, b.length);
        return t;
    }

    private synchronized void sendBytes(final byte[] buf) {
        try {
            PublicStaticObjects.getObjectOutputStream().writeObject(buf);
            PublicStaticObjects.getObjectOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendImage(final Bitmap bitmap) {
        Thread thread = new Thread(new Runnable() {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            @Override
            public synchronized void run() {
                bitmap.compress(Bitmap.CompressFormat.JPEG, PublicStaticObjects.getQuality(), bytes);
                try {
                    PublicStaticObjects.getObjectOutputStream().writeObject(bytes.toByteArray());
                    PublicStaticObjects.getObjectOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
