package de.riedelgames.onedpongapp.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

public class ServerFinder implements Runnable {
    /**
     * Group Name for UDP
     */
    private static final String GROUPNAME = "229.127.12.17";

    private int portNumber;

    private MulticastSocket socket;
    private InetAddress group;

    private HashMap<String, Integer> serverList;

    public ServerFinder(int portNumber) {
        this.portNumber = portNumber;
        serverList = new HashMap<String, Integer>();
    }

    @Override
    public void run() {

        try {
            socket = new MulticastSocket(portNumber);
            group = InetAddress.getByName(GROUPNAME);
            socket.joinGroup(group);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            Gdx.app.log("Network: ", "Failed to initialize UDP listener");
            e.printStackTrace();
        }
        DatagramPacket packet;
        while (true) {
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                serverList.put(packet.getAddress().getHostAddress(), packet.getPort());
            } catch (IOException e) {
                Gdx.app.log("Network: ", "IO Error receving datagram packet");
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, Integer> getServerList() {
        return serverList;
    }

}
