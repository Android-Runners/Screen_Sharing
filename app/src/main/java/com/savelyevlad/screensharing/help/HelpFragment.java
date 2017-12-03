package com.savelyevlad.screensharing.help;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savelyevlad.screensharing.MainActivity;
import com.savelyevlad.screensharing.R;

public final class HelpFragment extends Fragment {

    final static String KEY_MSG_4 = "FRAGMENT4_MSG";

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_help, null);

        Bundle bundle = getArguments();

        if(bundle != null) {
            String msg = bundle.getString(KEY_MSG_4);
            if(msg != null) {
                Log.e("lol", "in help");
            }
        }

        return view;
    }
}
