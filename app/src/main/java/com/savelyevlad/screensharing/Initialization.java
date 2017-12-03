package com.savelyevlad.screensharing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Initialization implements Runnable {

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private Socket socket;

    @Override
    public void run() {
        try {
            socket = new Socket(PublicStaticObjects.getIp(), PublicStaticObjects.getPORT());
            PublicStaticObjects.setSocket(socket);
            PublicStaticObjects.setObjectInputStream(new ObjectInputStream(socket.getInputStream()));
            PublicStaticObjects.setObjectOutputStream(new ObjectOutputStream(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
