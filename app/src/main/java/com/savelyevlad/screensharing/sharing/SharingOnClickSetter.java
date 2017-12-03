package com.savelyevlad.screensharing.sharing;

import android.content.Context;
import android.media.projection.MediaProjectionManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.savelyevlad.screensharing.MainActivity;
import com.savelyevlad.screensharing.PublicStaticObjects;

import java.io.IOException;

public class SharingOnClickSetter {

    private MainActivity mainActivity;

    private Button startButton;
    private Button stopButton;
    private TextView textID;

    private boolean running = false;

    public MediaProjectionManager getProjectionManager() {
        return projectionManager;
    }

    private MediaProjectionManager projectionManager;

    private int id;

    public SharingOnClickSetter(Button startButton, Button stopButton, TextView textID, MainActivity mainActivity) {
        this.startButton = startButton;
        this.stopButton = stopButton;
        this.textID = textID;

        this.mainActivity = mainActivity;
    }

    public void start() {
        PublicStaticObjects.initSocket();

        projectionManager = (MediaProjectionManager) mainActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        if(PublicStaticObjects.isClicked()) {
            textID.setText(String.valueOf(PublicStaticObjects.getID()));
        }

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PublicStaticObjects.setStopped(false);
//                isStopped = false;
//                if(running) {
//                    return;
//                }
//                running = true;
                if(!PublicStaticObjects.isWasFirstClick()) {
                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                PublicStaticObjects.getObjectOutputStream().writeObject(-1);
                                Object object = PublicStaticObjects.getObjectInputStream().readObject();
                                id = (Integer) object;
                            } catch (IOException | ClassNotFoundException e) {
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
                    PublicStaticObjects.setIsClicked(true);
                    PublicStaticObjects.setID(id);
                    textID.setText(String.valueOf(id));
                    mainActivity.startActivityForResult(projectionManager.createScreenCaptureIntent(), 228);
                }
                PublicStaticObjects.setWasFirstClick(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PublicStaticObjects.setStopped(true);
//                isStopped = true;
            /*    new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        try {
                            PublicStaticObjects.getObjectOutputStream().writeObject(-2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });*/
            }
        });
    }
}
