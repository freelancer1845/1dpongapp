package de.riedelgames.onedpongapp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.core.networking.api.constants.Keys;
import de.riedelgames.core.networking.api.constants.NetworkingConstants;
import de.riedelgames.core.networking.api.server.ClientNetworkTunnel;
import de.riedelgames.core.networking.api.server.UDPClient;
import de.riedelgames.core.networking.impl.server.UDPClientImpl;
import de.riedelgames.core.networking.impl.server.UDPConnection;
import de.riedelgames.onedpongapp.networking.ServerFinder;

public class OneDPongApp extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    private ServerFinder serverFinder;

    private ClientNetworkTunnel networkTunnel;

    private long lastTime = 0;

    private boolean serverFound = false;

    private boolean connected = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        // networkAppClient = new NetworkAppClient("192.168.178.29", 4000);
        serverFinder = new ServerFinder(NetworkingConstants.DEFAULT_PORT);
        serverFinder.start();
        lastTime = System.nanoTime();
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

        if (!serverFound) {
            long currentTime = System.nanoTime();
            long nanoSec = 1000000000;
            if (currentTime - lastTime > 5 * nanoSec) {
                HashMap<InetAddress, Integer> serverList = serverFinder.getServerList();
                Gdx.app.log("Network: ", "Current server list");
                for (InetAddress key : serverList.keySet()) {
                    Gdx.app.log(key.getHostAddress(), String.valueOf(serverList.get(key)));
                }
                lastTime = System.nanoTime();
            }
            if (!serverFinder.getServerList().isEmpty()) {
                serverFinder.stop();
                serverFound = true;
                InetAddress serverAdress = serverFinder.getServerList().keySet().iterator().next();
                int port = serverFinder.getServerList().get(serverAdress);
                networkTunnel = new ClientNetworkTunnel(serverAdress, port);
                connected = true;
                System.out.println("Successfully Connected");
            }
        } else if (connected) {

        }

    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (connected) {
            // if (screenX > Gdx.graphics.getDisplayMode().width / 2) {
            // networkTunnel.fireInputKeyDown(Keys.KEY_DOWN);
            // } else {
            // networkTunnel.fireInputKeyDown(Keys.KEY_UP);
            // }
            networkTunnel.fireInputKeyDown(Keys.FIRE);

        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (connected) {
            // if (screenX > Gdx.graphics.getDisplayMode().width / 2) {
            // networkTunnel.fireInputKeyUp(Keys.KEY_DOWN);
            // } else {
            // networkTunnel.fireInputKeyUp(Keys.KEY_UP);
            // }
            networkTunnel.fireInputKeyUp(Keys.FIRE);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}
