package hs_mannheim.mmobile.Model;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    private static final String TAG = "[UDPClient]";
    private final String mIp;
    private final int mPort;

    public UDPClient(String ip, int port) {
        mIp = ip;
        mPort = port;
    }

    public void send(final String message) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;

                try {
                    InetAddress mAddress = InetAddress.getByName(mIp);

                    int msg_length = message.length();
                    byte[] msg_bytes = message.getBytes();

                    socket = new DatagramSocket();
                    DatagramPacket p = new DatagramPacket(msg_bytes, msg_length, mAddress, mPort);

                    Log.d(TAG, "sending packet");
                    socket.send(p);

                } catch (IOException ex) {
                    Log.d(TAG, "Sending failed: " + ex.getMessage());
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        };

        new Thread(r).start();
    }

}