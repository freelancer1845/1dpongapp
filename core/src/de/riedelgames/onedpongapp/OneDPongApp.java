package de.riedelgames.onedpongapp;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.onedpongapp.networking.NetworkAppClient;
import de.riedelgames.onedpongapp.networking.ServerFinder;

public class OneDPongApp extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    private NetworkAppClient networkAppClient;
    private ServerFinder serverFinder;
    private long lastTime = 0;
    private boolean keydown = false;

    private boolean serverFound = false;

    private boolean connected = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        // networkAppClient = new NetworkAppClient("192.168.178.29", 4000);
        serverFinder = new ServerFinder(4000);
        new Thread(serverFinder).start();
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
                HashMap<String, Integer> serverList = serverFinder.getServerList();
                Gdx.app.log("Network: ", "Current server list");
                for (String key : serverList.keySet()) {
                    Gdx.app.log(key, String.valueOf(serverList.get(key)));
                }
                lastTime = System.nanoTime();
            }
            if (!serverFinder.getServerList().isEmpty()) {
                serverFound = true;
                String serverIP = serverFinder.getServerList().keySet().iterator().next();
                int port = serverFinder.getServerList().get(serverIP);
                networkAppClient = new NetworkAppClient(serverIP, port);
                new Thread(networkAppClient).start();
                while (!networkAppClient.isConnected()) {
                    Gdx.app.log("Network", "not connected");
                }
                connected = true;
                networkAppClient.sendNamePackage("Freelancer");
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
            if (screenX > Gdx.graphics.getDisplayMode().width / 2) {
                long time = System.currentTimeMillis();
                networkAppClient.keyDown(Input.Keys.DOWN);
                Gdx.app.log("TimeNeeded: ", Long.toString(System.currentTimeMillis() - time));
            } else {
                networkAppClient.keyDown(Input.Keys.UP);
            }

        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (connected) {
            if (screenX > Gdx.graphics.getDisplayMode().width / 2) {
                networkAppClient.keyUp(Input.Keys.DOWN);
            } else {
                networkAppClient.keyUp(Input.Keys.UP);
            }
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
