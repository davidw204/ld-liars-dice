package util.pdu.client;

import static util.CommandIDs.MSG_READY;

import java.util.Arrays;

import util.pdu.PDU;

public class Ready extends PDU {
	
	public Ready() {
		//Liar's Dice Packet ID
		addBytes(MSG_READY);
		//Total Message Length
		addLength();
		valid();
	}
	
	public Ready(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l >= 7 && bytes[4] == MSG_READY[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			System.out.println("ready length: " + len);
			if (l == len + 7) {
				valid();
			}
		}
	}
}