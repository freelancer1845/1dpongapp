package de.riedelgames.onedpongapp.networking;

import java.io.Serializable;

public class NetworkCodes implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7683230348339042906L;

	/**
	 * Handshake Package.
	 */
	public static final int HANDSHAKE = 100;
	
	/**
	 * Handshake Response Package.
	 */
	public static final int HANDSHAKE_RESPONSE = 101;
	
	/**
	 * Name Package
	 */
	public static final int NAME = 300;
	
	/**
	 * Name Response
	 */
	public static final int NAME_RESPONSE = 301;
	public static final int NAME_ACCEPTED = 1;
	
	/**
	 * Keys Pressed Package
	 */
	public static final int KEYS_PRESSED = 201;
	
	/**
	 * Key Pressed Response Package.
	 */
	public static final int KEYS_PRESSED_RESPONSE = 202;
	public static final int KEYS_PRESSED_HANDELED = 1;
	
	/**
	 * Main Key
	 */
	public static final int MAIN_KEY = 1010;
	
	/**
	 * Key Down.
	 */
	public static final int KEY_DOWN = -1;
	
	/**
	 * Key Up.
	 */
	public static final int KEY_UP = 1;
	
	private NetworkCodes() {};
}
