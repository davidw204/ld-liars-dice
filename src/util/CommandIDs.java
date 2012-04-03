package util;

import static util.Converter.hexStringToByteArray;

public final class CommandIDs {

	private CommandIDs() {
	}

	public static final byte[] PID = hexStringToByteArray("4C444732");

	/* server to client */
	public static final byte[] MSG_CHALLENGE = hexStringToByteArray("A0");
	public static final byte[] MSG_PLAYERID = hexStringToByteArray("A1");
	public static final byte[] NOTIFY_CONNECT = hexStringToByteArray("A2");
	public static final byte[] NOTIFY_DISCONNECT = hexStringToByteArray("A3");
	public static final byte[] MSG_ERROR = hexStringToByteArray("A4");
	public static final byte[] PRE_GAME = hexStringToByteArray("A5");
	public static final byte[] ROUND_START = hexStringToByteArray("A6");
	public static final byte[] ROUND_UPDATE = hexStringToByteArray("A7");
	public static final byte[] ROUND_END = hexStringToByteArray("A8");
	public static final byte[] GAME_END = hexStringToByteArray("A9");
	public static final byte[] UDP_BROADCAST = hexStringToByteArray("F0");

	/* client to server */
	public static final byte[] MSG_RESPONSE = hexStringToByteArray("B0");
	public static final byte[] MSG_DISCONNECT = hexStringToByteArray("B1");
	public static final byte[] MSG_READY = hexStringToByteArray("B2");
	public static final byte[] MSG_BID = hexStringToByteArray("B3");
}
