package util.pdu.server;

import static util.CommandIDs.NOTIFY_CONNECT;
import static util.CommandIDs.UDP_BROADCAST;

import game.Player;

import java.util.Arrays;

import util.pdu.PDU;

public class Connect extends PDU {
	
	private int id;
	private String name;


	public Connect(Player p) {
		// Liar's Dice Packet ID
		addBytes(NOTIFY_CONNECT);
		// Total Message Length
		addByte(p.getID());
		addString(p.getName());

		addLength();
		valid();
	}

	public Connect(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == NOTIFY_CONNECT[0]) {
//			byte[] packetID = Arrays.copyOfRange(bytes, 0, 4);
//			byte[] cid = Arrays.copyOfRange(bytes, 4, 5);
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] pid = Arrays.copyOfRange(bytes, 7, 8);
				byte[] strL = Arrays.copyOfRange(bytes, 8, 9);
				int off = toInt(strL);
				byte[] str = Arrays.copyOfRange(bytes, 9, 9 + off);
				String name = fromUTF8(str);
				this.id = toInt(pid);
				this.name = name;
				
				valid();
			}
		}
			
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
