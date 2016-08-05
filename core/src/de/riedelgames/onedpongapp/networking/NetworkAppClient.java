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
	
	public boolean sendNamePackage(String name) {
		char[] nameArray = name.toCharArray();
		
		int packageLength = nameArray.length;
		int[] arrayToSend = new int[packageLength + 2];
		arrayToSend[0] = NetworkCodes.NAME;
		arrayToSend[1] = packageLength;
		for (int i = 0; i < nameArray.length; i++) {
			arrayToSend[i + 2] = nameArray[i];
		}
		try {
			writeIntArray(NetworkCodes.NAME, arrayToSend);
			int[] responseArray = readIntArray(NetworkCodes.NAME_RESPONSE);
			if (responseArray[0] == NetworkCodes.NAME_ACCEPTED) {
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

	
	private boolean sendKeyDownPackage(int keycode) {
		int packageLength = 2;
		int[] arrayToSend = new int[]{NetworkCodes.KEYS_PRESSED, packageLength, keycode, NetworkCodes.KEY_DOWN};
		try {
			writeIntArray(NetworkCodes.KEYS_PRESSED, arrayToSend);
			int[] responseArray = readIntArray(NetworkCodes.KEYS_PRESSED_RESPONSE);
			if (responseArray[0] == NetworkCodes.KEYS_PRESSED_HANDELED) {
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
		int[] arrayToSend = new int[]{NetworkCodes.KEYS_PRESSED, packageLength, keycode, NetworkCodes.KEY_UP};
		try {
			writeIntArray(NetworkCodes.KEYS_PRESSED, arrayToSend);
			int[] responseArray = readIntArray(NetworkCodes.KEYS_PRESSED_RESPONSE);
			if (responseArray[0] == NetworkCodes.KEYS_PRESSED_HANDELED) {
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
		
		return sendKeyDownPackage(NetworkCodes.MAIN_KEY);
	}




	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return sendKeyUpPackage(NetworkCodes.MAIN_KEY);
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
