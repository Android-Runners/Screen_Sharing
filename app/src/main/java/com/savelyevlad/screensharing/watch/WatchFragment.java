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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.savelyevlad.screensharing.MainActivity;
import com.savelyevlad.screensharing.PublicStaticObjects;
import com.savelyevlad.screensharing.R;
import com.savelyevlad.screensharing.Receiver;

import java.io.IOException;

public final class WatchFragment extends Fragment {

    final static String KEY_MSG_2 = "FRAGMENT2_MSG";
    private FloatingActionButton startFab;
    private FloatingActionButton pauseFab;
    private FloatingActionButton reloadFab;
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

    private Spinner spinner;

    private int id;
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
                reloadFab = view.findViewById(R.id.refreshFab);
                editTextID = view.findViewById(R.id.editText_ID);
                spinner = view.findViewById(R.id.spinner);

                String[] data = {"Savelyev", "Daniil", "Alex", "Serg"};

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setPrompt("Translators");

                spinner.setSelection(0);

                PublicStaticObjects.initSocket();

                pauseFab.setOnClickListener(view1 -> new Thread(() -> mustBeAlive = false).start());

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity().getBaseContext(), "Position = " + i, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                reloadFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public synchronized void run() {
                                try {
                                    Log.e("in reload", "Before reading");
                                    PublicStaticObjects.getObjectOutputStream().writeObject(-5);
                                    Log.e("in reload", "After reading");
                                  /*  byte[] trans = (byte[])PublicStaticObjects.getObjectInputStream().readObject();
                                    Log.e("in reload", Arrays.toString(trans));*/
                                }catch(Exception e){}
                            }
                        });
                        t.start();
                        try {
                            t.join();
                            PublicStaticObjects.setMinusFive(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                startFab.setOnClickListener(view12 -> {
                    // Can I join someone?
                    mustBeAlive = true;
                    thread = new Thread(() -> {
                        try {
                            PublicStaticObjects.getObjectOutputStream().writeObject(-3);
                            id = Integer.valueOf(String.valueOf(editTextID.getText()));
                            PublicStaticObjects.getObjectOutputStream().writeObject(Integer.valueOf(String.valueOf(editTextID.getText())));
                            try {
                                Object object = PublicStaticObjects.getObjectInputStream().readObject();
                                if (!object.equals("-3")) {
                                    canJoin = true;
                                } else {
                                    canJoin = false;
//                                TODO: toast
//                                Toast.makeText();
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
                        Thread thread1 = new Thread(new Receiver(activityWatch, imageView));
                        thread1.setPriority(Thread.MAX_PRIORITY);
                        thread1.start();
                    }
                });

            }
        }

        return view;
    }
}
