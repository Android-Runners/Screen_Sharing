package com.savelyevlad.screensharing.sharing;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.savelyevlad.screensharing.PublicStaticObjects;
import com.savelyevlad.screensharing.R;

import java.io.IOException;

public final class SharingFragment extends Fragment {

    final static String KEY_MSG_1 = "FRAGMENT1_MSG";

    private Button startButton;
    private Button stopButton;
    private TextView textID;

    private boolean running = false;

    private MediaProjectionManager projectionManager;

    private int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_sharing, null);

        Bundle bundle = getArguments();

        if(bundle != null) {
            String msg = bundle.getString(KEY_MSG_1);
            if(msg != null) {
                PublicStaticObjects.initSocket();

                textID = findViewById(R.id.sharingID);
                startButton = findViewById(R.id.startButton);
                stopButton = findViewById(R.id.stopButton);

                projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

                if(PublicStaticObjects.isClicked()) {
                    textID.setText(String.valueOf(PublicStaticObjects.getID()));
                }

                startButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        PublicStaticObjects.setStopped(false);
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
                            startActivityForResult(projectionManager.createScreenCaptureIntent(), 228);
                        }
                        PublicStaticObjects.setWasFirstClick(true);
                    }
                });

                stopButton.setOnClickListener((v) -> {
                        PublicStaticObjects.setStopped(true);
                    }
                );
            }
        }

        return view;
    }
}
