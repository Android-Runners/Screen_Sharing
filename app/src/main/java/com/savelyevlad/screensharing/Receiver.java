package com.savelyevlad.screensharing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.savelyevlad.screensharing.watch.WatchFragment;

public class Receiver implements Runnable {

    private byte[] concat(byte[] a, byte[] b) {
        byte[] t = new byte[a.length + b.length];
        System.arraycopy(a, 0, t, 0, a.length);
        System.arraycopy(b, 0, t, a.length, b.length);
        return t;
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
            synchronized (this) {
//                if (!ActivityWatch.isMustBeAlive()) {
//                    return;
//                }
            }
            try {
                byte[] buf = (byte[])PublicStaticObjects.getObjectInputStream().readObject();
                Bitmap receiveBitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
                PublicStaticObjects.setCount(PublicStaticObjects.getCount()+1);
                changeImage(receiveBitmap);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void changeImage(final Bitmap bitmap) {
//        activityWatch.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imageView.setImageBitmap(bitmap);
            }
//        });

    }
//}