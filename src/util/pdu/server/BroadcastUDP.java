package util.pdu.server;

import static util.CommandIDs.UDP_BROADCAST;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import util.pdu.PDU;

public class BroadcastUDP extends PDU {

	private int version = 1;
	
	private String ip = "";
	private String name = "";
	private int port = 0;
	private int min = 0;
	private int cur = 0;
	private int max = 0;

	public BroadcastUDP(String ip, int port, String name, int min, int cur, int max) {
		// Liar's Dice Packet ID
		addBytes(UDP_BROADCAST);
		// Total Message Length
		addBytes(getIpAddress(ip));
		add2Bytes(port);
		addByte(version);
		addString(name);
		addByte(min);
		addByte(cur);
		addByte(max);

		addLength();
		valid();
	}

	public BroadcastUDP(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == UDP_BROADCAST[0]) {
//			byte[] packetID = Arrays.copyOfRange(bytes, 0, 4);
//			byte[] cid = Arrays.copyOfRange(bytes, 4, 5);
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] ip = Arrays.copyOfRange(bytes, 7, 11);
				byte[] port = Arrays.copyOfRange(bytes, 11, 13);
				byte[] version = Arrays.copyOfRange(bytes, 13, 14);
				byte[] stringL = Arrays.copyOfRange(bytes, 14, 15);
				int off = toInt(stringL);
				byte[] str = Arrays.copyOfRange(bytes, 15, 15 + off);

				int idx = 15 + off;
				byte[] minPlayer = Arrays.copyOfRange(bytes, idx, ++idx);
				byte[] curPlayer = Arrays.copyOfRange(bytes, idx, ++idx);
				byte[] maxPlayer = Arrays.copyOfRange(bytes, idx, ++idx);
				String name = fromUTF8(str);
				this.name = name;
				this.ip = getIpAddress(ip);
				this.port = toInt(port);
				this.min = toInt(minPlayer);
				this.cur = toInt(curPlayer);
				this.max = toInt(maxPlayer);
				this.version = toInt(version);
				
				valid();
			}

		}
	}

	public int getVersion() {
		return version;
	}

	public String getIp() {
		return ip;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public int getMin() {
		return min;
	}

	public int getCur() {
		return cur;
	}

	public int getMax() {
		return max;
	}
}
