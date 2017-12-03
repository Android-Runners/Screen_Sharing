package com.savelyevlad.screensharing.watch;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.savelyevlad.screensharing.MainActivity;
import com.savelyevlad.screensharing.PublicStaticObjects;
import com.savelyevlad.screensharing.R;
import com.savelyevlad.screensharing.Receiver;

import java.io.IOException;

public final class WatchFragment extends Fragment {

    final static String KEY_MSG_2 = "FRAGMENT2_MSG";
    private FloatingActionButton startFab;
    private FloatingActionButton pauseFab;
    private ImageView imageView;

    private EditText editText;
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

    private int id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_watch, null);

        Bundle bundle = getArguments();

        if(bundle != null) {
            String msg = bundle.getString(KEY_MSG_2);
            if(msg != null) {
                Log.e("lol", "in watch");
                editText = main.findViewById(R.id.editText_ID);
                startFab = main.findViewById(R.id.startFab);
                imageView = main.findViewById(R.id.imageView);
                pauseFab = main.findViewById(R.id.pauseFab);
              //  activityWatch = this;

                PublicStaticObjects.initSocket();

                pauseFab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public synchronized void run() {
                                try {
                                    mustBeAlive = false;
                                }catch(Exception e){}
                            }
                        }).start();
                    }
                });

                startFab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // Can I join someone?
                        mustBeAlive = true;
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    PublicStaticObjects.getObjectOutputStream().writeObject(-3);
                                    id = Integer.valueOf(String.valueOf(editText.getText()));
                                    PublicStaticObjects.getObjectOutputStream().writeObject(Integer.valueOf(String.valueOf(editText.getText())));
                                    try {
                                        Object object = PublicStaticObjects.getObjectInputStream().readObject();
                                        if (!object.equals("-3")) {
                                            canJoin = true;
                                        } else {
                                            canJoin = false;
//                        TODO: toast
//                        Toast.makeText();
                                        }
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
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
                        if (canJoin) {
                            Thread thread1 = new Thread(new Receiver(activityWatch, imageView));
                            thread1.setPriority(Thread.MAX_PRIORITY);
                            thread1.start();
                        }
                    }
                });

            }
        }

        return view;
    }
}
