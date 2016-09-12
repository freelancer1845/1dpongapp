package de.riedelgames.onedpongapp.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;

import de.riedelgames.core.networking.api.constants.NetworkingConstants;

public class ServerFinder implements Runnable {

    /** Thread the finder runs on. */
    private Thread serverFinderThread;

    /** Is vissible. */
    private boolean visible;

    private int portNumber;

    private MulticastSocket socket;
    private InetAddress group;

    private HashMap<InetAddress, Integer> serverList;

    public ServerFinder(int portNumber) {
        this.portNumber = portNumber;
        serverList = new HashMap<InetAddress, Integer>();
    }

    public void start() {
        visible = true;
        if (serverFinderThread == null) {
            serverFinderThread = new Thread(this);
        }
        serverFinderThread.start();
    }

    public void stop() {
        visible = false;
        if (serverFinderThread != null) {
            serverFinderThread.interrupt();
            while (!serverFinderThread.isInterrupted())
                ;
            socket.close();
            ;
            serverFinderThread = null;
        }
    }

    @Override
    public void run() {

        try {
            socket = new MulticastSocket(portNumber);
            group = InetAddress.getByName(NetworkingConstants.GROUPNAME);
            socket.joinGroup(group);
            socket.setSoTimeout(5000);
        } catch (IOException e) {
            Gdx.app.log("Network: ", "Failed to initialize UDP listener");
            e.printStackTrace();
        }
        DatagramPacket packet;
        while (visible) {
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                serverList.put(packet.getAddress(), packet.getPort());
            } catch (SocketException e) {
                // do nothing
            } catch (IOException e) {
                Gdx.app.log("Network: ", "IO Error receving datagram packet");
                e.printStackTrace();
            }
        }
    }

    public HashMap<InetAddress, Integer> getServerList() {
        return serverList;
    }

}
