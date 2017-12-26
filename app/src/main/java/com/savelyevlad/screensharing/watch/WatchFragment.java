package com.savelyevlad.screensharing.watch;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.savelyevlad.screensharing.FullscreenActivity;
import com.savelyevlad.screensharing.MainActivity;
import com.savelyevlad.screensharing.PublicStaticObjects;
import com.savelyevlad.screensharing.R;
import com.savelyevlad.screensharing.Receiver;

import java.io.IOException;

public final class WatchFragment extends Fragment {

    final static String KEY_MSG_2 = "FRAGMENT2_MSG";
    private FloatingActionButton startFab;
    private FloatingActionButton pauseFab;
    private FloatingActionButton fullScreenFab;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private ImageView imageView;

    private EditText editTextID;
    private WatchFragment activityWatch;

    private boolean canJoin = false;
    private static boolean mustBeAlive = false;

    public static boolean isMustBeAlive() {
        return mustBeAlive;
    }

    public static void setMustBeAlive(boolean mustBeAlive) {
        WatchFragment.mustBeAlive = mustBeAlive;
    }

    public MainActivity getMain() {
        return main;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }

    private MainActivity main;

    private Thread thread;
    private Thread thread1;

    public static boolean isIsReceiving() {
        return isReceiving;
    }

    public static void setIsReceiving(boolean isReceiving) {
        WatchFragment.isReceiving = isReceiving;
    }

    private static boolean isReceiving = false;

    private int id;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_watch, null);

        Bundle bundle = getArguments();

        if(bundle != null) {
            String msg = bundle.getString(KEY_MSG_2);
            if(msg != null) {
                startFab = view.findViewById(R.id.startFab);
                imageView = view.findViewById(R.id.imageView);
                pauseFab = view.findViewById(R.id.pauseFab);
                editTextID = view.findViewById(R.id.editText_ID);
                fullScreenFab = view.findViewById(R.id.fullScreenFab);

                PublicStaticObjects.initSocket();

                pauseFab.setOnClickListener(view1 -> {
                    isReceiving = false;
                });

                fullScreenFab.setOnClickListener(view1 -> {
                    MainActivity.getMain().changeAcivity();
                    FullscreenActivity.setImageFullScreen(imageView);
                });

                startFab.setOnClickListener((View view12) -> {
                    isReceiving = true;
                    // Can I join someone?
                    mustBeAlive = false;
                    Log.e("here", "here");
                    thread = new Thread(() -> {
                        try {
                            Log.e("here", "here");
                            PublicStaticObjects.getObjectOutputStream().writeObject(-3);
                            try {
                                id = Integer.valueOf(String.valueOf(editTextID.getText()));
                            } catch (NumberFormatException e) {
                                MainActivity.getMain().show("You entered wrong number!");
                                return;
                            }
                            PublicStaticObjects.getObjectOutputStream().writeObject(Integer.valueOf(String.valueOf(editTextID.getText())));
                            Log.e("here", "here");
                            try {
                                Object object = PublicStaticObjects.getObjectInputStream().readObject();
                                Log.e("here", "here");
                                if (!object.equals(-3)) {
                                    canJoin = true;
                                } else {
                                    canJoin = false;
                                    Log.e("here", "here");
                                    // TODO:
                                    MainActivity.getMain().show("Cannot connect");
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (canJoin) {
                        if(thread1 != null) {
                            try {
                                thread1.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        thread1 = new Thread(new Receiver(activityWatch, imageView));
                        thread1.setPriority(Thread.MAX_PRIORITY);
                        mustBeAlive = true;
                        thread1.start();
                    }
                });

            }
        }

        return view;
    }
}
