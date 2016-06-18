package de.riedelgames.onedpongapp.networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;



public class NetworkAppClient implements Runnable,InputProcessor {

	
	/**
	 * Size of the package size identifier in bytes.
	 */
	private static final int PACKAGE_SIZE_IDENTIFIER_BYTE_LENGTH = 1;
	
	/**
	 * Handshake Package.
	 */
	private static final int HANDSHAKE = 100;
	
	/**
	 * Handshake Response Package.
	 */
	private static final int HANDSHAKE_RESPONSE = 101;
	
	/**
	 * Keys Pressed Package
	 */
	private static final int KEYS_PRESSED = 201;
	
	/**
	 * Key Pressed Response Package.
	 */
	private static final int KEYS_PRESSED_RESPONSE = 202;
	private static final int KEYS_PRESSED_HANDELED = 1;
	
	/**
	 * Key Down.
	 */
	private static final int KEY_DOWN = -1;
	
	/**
	 * Key Up.
	 */
	private static final int KEY_UP = 1;
	
	
	private String hostName;
	private int port;
	private Socket socket;
	private boolean connected;
	
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	
	
	
	public NetworkAppClient(String hostName, int port){
		this.port = port;
		this.hostName = hostName;
		this.connected = false;
		
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	
		
	protected void finalize(){
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error during socket.close() in finalize");
			e.printStackTrace();
		}
	}

	
	private boolean sendKeyDownPackage(int keycode) {
		int packageLength = 2;
		int[] arrayToSend = new int[]{KEYS_PRESSED, packageLength, keycode, KEY_DOWN};
		try {
			writeIntArray(KEYS_PRESSED, arrayToSend);
			int[] responseArray = readIntArray(KEYS_PRESSED_RESPONSE);
			if (responseArray[0] == KEYS_PRESSED_HANDELED) {
				return true;
			} else {
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private boolean sendKeyUpPackage(int keycode) {
		
		int packageLength = 2;
		int[] arrayToSend = new int[]{KEYS_PRESSED, packageLength, keycode, KEY_UP};
		try {
			writeIntArray(KEYS_PRESSED, arrayToSend);
			int[] responseArray = readIntArray(KEYS_PRESSED_RESPONSE);
			if (responseArray[0] == KEYS_PRESSED_HANDELED) {
				return true;
			} else {
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private void writeIntArray(int key, int[] array) throws IOException {
		dataOutputStream.writeInt(key);
		dataOutputStream.writeInt(array.length);
		for (int i = 0; i < array.length; i++) {
			dataOutputStream.writeInt(array[i]);
		}
	}
	
	private int[] readIntArray(int key) throws IOException {
		if (dataInputStream.readInt() == key) {
			int packageLength = dataInputStream.readInt();
			int[] returnArray = new int[packageLength];
			for (int i = 0; i < packageLength; i++) {
				returnArray[i] = dataInputStream.readInt();
			}
			return returnArray;
		} else {
			throw new IOException("Unexpected Key...");
		}
	}


	@Override
	public boolean keyDown(int keycode) {
		return sendKeyDownPackage(keycode);
	}




	@Override
	public boolean keyUp(int keycode) {
		return sendKeyUpPackage(keycode);
	}




	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}




	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
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




	@Override
	public void run() {
		try {
			Gdx.app.log("Newtork", "Looking for server: " + hostName + ":" + port);
			socket = new Socket(hostName, port);
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			Gdx.app.log("Newtork", "Connected to server: " + hostName + ":" + port);
			connected = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
