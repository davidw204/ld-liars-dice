package util.pdu.client;

import static util.CommandIDs.MSG_DISCONNECT;

import java.util.Arrays;

import util.pdu.PDU;

public class Disconnect extends PDU {
	
	public Disconnect() {
		//Liar's Dice Packet ID
		addBytes(MSG_DISCONNECT);
		//Total Message Length
		addLength();
		valid();
	}
	
	public Disconnect(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l >= 7 && bytes[4] == MSG_DISCONNECT[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				valid();
			}
		}
	}
}