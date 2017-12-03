package com.savelyevlad.screensharing.sharing;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.savelyevlad.screensharing.R;

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

        if (bundle != null) {
            String msg = bundle.getString(KEY_MSG_1);
        }
        return view;
    }
}
