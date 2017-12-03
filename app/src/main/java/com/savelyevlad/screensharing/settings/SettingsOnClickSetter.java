package com.savelyevlad.screensharing.settings;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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

/**
 * Created by savelyevlad on 03.12.2017.
 */

public class SettingsOnClickSetter {

    public SettingsOnClickSetter(EditText editTextIp, EditText editTextPORT, SeekBar seekBar, TextView editTextBar, Button buttonSave, MainActivity mainActivity) {
        this.editTextIp = editTextIp;
        this.editTextPORT = editTextPORT;
        this.seekBar = seekBar;
        this.editTextBar = editTextBar;
        this.buttonSave = buttonSave;
        this.mainActivity = mainActivity;
    }

    private EditText editTextIp;
    private EditText editTextPORT;

    private SeekBar seekBar;
    private TextView editTextBar;

    private Button buttonSave;

    private MainActivity mainActivity;

    private String folderPath = Environment.getExternalStorageDirectory().getPath() + "/ScreenSharing/";

    public void start() {

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
                    Context context = mainActivity.getApplicationContext();
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
