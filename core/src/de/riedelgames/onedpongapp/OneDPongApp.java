package de.riedelgames.onedpongapp;

import java.net.SocketAddress;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.onedpongapp.networking.NetworkAppClient;
import de.riedelgames.onedpongapp.networking.ServerFinder;

public class OneDPongApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private NetworkAppClient networkAppClient;
	private ServerFinder serverFinder;
	private long lastTime = 0;
	private boolean keydown = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		//networkAppClient = new NetworkAppClient("192.168.178.29", 4000);
		serverFinder = new ServerFinder(4000);
		new Thread(serverFinder).start();
		lastTime = System.nanoTime();
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
		boolean serverFound = false;
		boolean connected = false;
		if (!serverFound) {
			long currentTime = System.nanoTime();
			long nanoSec = 1000000000;
			if (currentTime - lastTime > 5 * nanoSec) {
				HashMap<SocketAddress, String> serverList = serverFinder.getServerList();
				Gdx.app.log("Network: ", "Current server list");
				for (SocketAddress key : serverList.keySet()) {
					Gdx.app.log(key.toString(), serverList.get(key));
				}
				lastTime = System.nanoTime();
			}
			if (!serverFinder.getServerList().isEmpty()) {
				serverFound = true;
			}
		} else if (connected){
			SocketAddress server = serverFinder.getServerList().keySet().iterator().next();
		}
		
		
	}
}
