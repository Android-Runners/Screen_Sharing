package com.savelyevlad.screensharing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.savelyevlad.screensharing.watch.WatchFragment;

import java.util.Arrays;

public class Receiver implements Runnable {

    private byte[] remove(byte[] bytes, int index) {
        if (index >= 0 && index < bytes.length) {
            byte[] copy = new byte[bytes.length-1];
            System.arraycopy(bytes, 0, copy, 0, index);
            System.arraycopy(bytes, index+1, copy, index, bytes.length-index-1);
            return copy;
        }
        return bytes;
    }

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
                    byte[] buf = (byte[]) PublicStaticObjects.getObjectInputStream().readObject();
                    if(buf[0] == 63 && buf[1] == 25 && buf[2] == 10 && buf[3] == 31){
                        buf = remove(buf, 0);
                        buf = remove(buf, 0);
                        buf = remove(buf, 0);
                        buf = remove(buf, 0);
                        Log.e("in RECEIVE", Arrays.toString(buf));
                    }else {
                        Bitmap receiveBitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
                        Log.e("in Receiver", "" + (receiveBitmap == null));
                        changeImage(receiveBitmap);
                    }

            } catch (Throwable e) {
                Log.e("!!!!!!",e.getMessage());
            }
        }
    }

    private void changeImage(final Bitmap bitmap) {
        MainActivity.getMain().runOnUiThread(() -> imageView.setImageBitmap(bitmap));
    }
}