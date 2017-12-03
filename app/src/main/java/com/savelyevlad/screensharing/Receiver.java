package com.savelyevlad.screensharing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.savelyevlad.screensharing.watch.WatchFragment;

public class Receiver implements Runnable {

    private ImageView imageView;

    private WatchFragment activityWatch;

    public Receiver(WatchFragment activityWatch, ImageView imageView) {
        this.imageView = imageView;
        this.activityWatch = activityWatch;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = (byte[])PublicStaticObjects.getObjectInputStream().readObject();
                Bitmap receiveBitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
                Log.e("in Receiver", "" + (receiveBitmap == null));
                changeImage(receiveBitmap);
            } catch (Throwable e) {
                Log.e("!!!!!!",e.getMessage());
            }
        }
    }

    private void changeImage(final Bitmap bitmap) {
        MainActivity.getMain().runOnUiThread(() -> imageView.setImageBitmap(bitmap));
    }
}