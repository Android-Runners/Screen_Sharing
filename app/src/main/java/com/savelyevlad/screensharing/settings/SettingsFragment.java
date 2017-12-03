package com.savelyevlad.screensharing.settings;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.savelyevlad.screensharing.MainActivity;
import com.savelyevlad.screensharing.PublicStaticObjects;
import com.savelyevlad.screensharing.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public final class SettingsFragment extends Fragment {

    final static String KEY_MSG_3 = "FRAGMENT3_MSG";
    private EditText editTextIp;
    private EditText editTextPORT;

    private SeekBar seekBar;
    private TextView editTextBar;

    public static MainActivity getMain() {
        return main;
    }

    public static void setMain(MainActivity main) {
        SettingsFragment.main = main;
    }

    private static MainActivity main;
    private Button buttonSave;

    private String folderPath = Environment.getExternalStorageDirectory().getPath() + "/ScreenSharing/";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_settings, null);

        Bundle bundle = getArguments();

        if(bundle != null) {
            String msg = bundle.getString(KEY_MSG_3);
            if(msg != null) {
                Log.e("lol", "in settings");
                editTextIp = main.findViewById(R.id.editText_ip);
                editTextPORT = main.findViewById(R.id.editText_port);

                editTextBar = main.findViewById(R.id.textQuality);
                seekBar = main.findViewById(R.id.seekBar);
                seekBar.setMax(90);
                seekBar.incrementProgressBy(1);
                seekBar.setProgress(PublicStaticObjects.getQuality());

                editTextBar.setText(String.valueOf(seekBar.getProgress()));

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if (seekBar.getProgress() == 0) {
                            seekBar.setProgress(1);
                        }
                        editTextBar.setText(String.valueOf(seekBar.getProgress()));
                        PublicStaticObjects.setQuality(seekBar.getProgress());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress() == 0) {
                            seekBar.setProgress(1);
                        }
                        editTextBar.setText(String.valueOf(seekBar.getProgress()));
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress() == 0) {
                            seekBar.setProgress(1);
                        }
                        editTextBar.setText(String.valueOf(seekBar.getProgress()));
                    }
                });

                buttonSave = main.findViewById(R.id.button_save);

                Log.e("", (editTextPORT == null) + "");

                String s = String.valueOf(PublicStaticObjects.getIp());
                s = s.substring(1);

                editTextPORT.setText(String.valueOf(PublicStaticObjects.getPORT()));
                editTextIp.setText(s);

                buttonSave.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String IP = String.valueOf(editTextIp.getText());
                        String PORT = String.valueOf(editTextPORT.getText());

                        try {
                            InetAddress inetAddress = InetAddress.getByName(IP);
                        } catch (Exception e) {
                            Context context = main.getApplicationContext();
                            CharSequence text = "It's not an IP!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            return;
                        }

                        File file = new File(folderPath + "Config.txt");

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write((IP + " " + PORT).getBytes());
                            PublicStaticObjects.setIp(InetAddress.getByName(IP));
                            PublicStaticObjects.setPORT(Integer.valueOf(PORT));
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        return view;
    }
}
