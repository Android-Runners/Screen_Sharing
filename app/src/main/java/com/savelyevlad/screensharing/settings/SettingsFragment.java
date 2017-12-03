package com.savelyevlad.screensharing.settings;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savelyevlad.screensharing.R;

public final class SettingsFragment extends Fragment {

    final static String KEY_MSG_3 = "FRAGMENT3_MSG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_settings, null);

        Bundle bundle = getArguments();

        if(bundle != null) {
            String msg = bundle.getString(KEY_MSG_3);
            if(msg != null) {
                Log.e("lol", "in settings");
            }
        }

        return view;
    }
}
