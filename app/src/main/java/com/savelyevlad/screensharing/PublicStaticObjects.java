package com.savelyevlad.screensharing;

import android.media.projection.MediaProjectionManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class PublicStaticObjects {

    public static ArrayList<Integer> getListTransId() {
        return listTransId;
    }

    public static void setListTransId(ArrayList<Integer> listTransId) {
        PublicStaticObjects.listTransId = listTransId;
    }

    private static ArrayList<Integer> listTransId = new ArrayList<Integer>();
    private static MediaProjectionManager projectionManager;

    public static void setProjectionManager(MediaProjectionManager projectionManager) {
        PublicStaticObjects.projectionManager = projectionManager;
    }

    public static boolean isMinusFive() {
        return isMinusFive;
    }

    public static void setMinusFive(boolean minusFive) {
        isMinusFive = minusFive;
    }

    private static boolean isMinusFive = false;

    public static boolean isClicked() {
        return isClicked;
    }


    public static void setIsClicked(boolean isClicked) {
        PublicStaticObjects.isClicked = isClicked;
    }

    private static boolean isClicked = false;

    public static MediaProjectionManager getProjectionManager() {
        return projectionManager;
    }

    private static InetAddress ip;

    private static int PORT;

    public static int getQuality() {
        return quality;
    }

    public static void setQuality(int quality) {
        PublicStaticObjects.quality = quality;
    }

    private static int quality = 1;

    private PublicStaticObjects() { }

    private static String folderPath = Environment.getExternalStorageDirectory().getPath() + "/ScreenSharing/";

    private static void init() {
        File file1 = new File(folderPath.substring(0, folderPath.length() - 1));
        if(!file1.exists()) {
            file1.mkdir();
        }
        File file = new File(folderPath + "Config.txt"); // IP and PORT
        if(!file.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write("192.168.43.1 50000".getBytes());
                fileOutputStream.close();
                ip = InetAddress.getByName("192.168.43.1");
                PORT = 50000;
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buf = new byte[fileInputStream.available()];
            fileInputStream.read(buf);
            String s = new String(buf);
            String[] kek = s.split(" ");
            ip = InetAddress.getByName(kek[0].substring(0, kek[0].length()));
            PORT = Integer.parseInt(kek[1]);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        init();
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        PublicStaticObjects.ID = ID;
    }

    private static int ID;

    public static ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public static void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        PublicStaticObjects.objectOutputStream = objectOutputStream;
    }

    private static ObjectOutputStream objectOutputStream;

    public static ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public static void setObjectInputStream(ObjectInputStream objectInputStream) {
        PublicStaticObjects.objectInputStream = objectInputStream;
    }

    private static int count = 0;

    private static ObjectInputStream objectInputStream;

    public static Socket getSocket() {
        return socket;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        PublicStaticObjects.count = count;
    }

    public static void setSocket(Socket socket) {
        PublicStaticObjects.socket = socket;
    }

    public static boolean isStopped() {
        return isStopped;
    }

    public static void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    private static boolean isStopped = false;

    public static boolean isWasFirstClick() {
        return wasFirstClick;
    }

    public static void setWasFirstClick(boolean wasFirstClick) {
        PublicStaticObjects.wasFirstClick = wasFirstClick;
    }

    private static boolean wasFirstClick = false;

    public static void initSocket() {
        if(PublicStaticObjects.getSocket() == null) {
            try {
                Thread thread = new Thread(new Initialization());
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Socket socket;

    public static InetAddress getIp() {
        return ip;
    }

    public static void setIp(InetAddress ip) {
        PublicStaticObjects.ip = ip;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        PublicStaticObjects.PORT = PORT;
    }
}
