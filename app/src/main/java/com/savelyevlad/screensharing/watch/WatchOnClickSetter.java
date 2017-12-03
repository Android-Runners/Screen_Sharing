package com.savelyevlad.screensharing.watch;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.savelyevlad.screensharing.PublicStaticObjects;
import com.savelyevlad.screensharing.Receiver;

import java.io.IOException;

public class WatchOnClickSetter {

    public static boolean isMustBeAlive() {
        return mustBeAlive;
    }

    public static void setMustBeAlive(boolean mustBeAlive) {
        WatchOnClickSetter.mustBeAlive = mustBeAlive;
    }

    private FloatingActionButton startFab;
    private FloatingActionButton stopFab;
    private ImageView imageView;

    private EditText editText;

    private Thread thread;

    private boolean canJoin = false;

    private static boolean mustBeAlive = false;

    public void start() {

        PublicStaticObjects.initSocket();

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
//                    Thread thread1 = new Thread(new Receiver(activityWatch, imageView));
//                    thread1.setPriority(Thread.MAX_PRIORITY);
//                    thread1.start();
                }
            }
        });
    }
}
