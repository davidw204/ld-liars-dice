package util.pdu.server;

//TEST

import static util.CommandIDs.*;
import static util.Converter.*;

import java.util.Arrays;

import util.pdu.PDU;

public class Challenge extends PDU {
	
	private byte[] challenge = hexStringToByteArray("4C494152");
	private int version = 1;
	
	public Challenge() {
		//Liar's Dice Packet ID
		addBytes(MSG_CHALLENGE);
		//Total Message Length
		addBytes(challenge);
		addByte(version);
		
		addLength();
		valid();
	}
	
	public Challenge(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == MSG_CHALLENGE[0]) {
//			byte[] packetID = Arrays.copyOfRange(bytes, 0, 4);
//			byte[] cid = Arrays.copyOfRange(bytes, 4, 5);
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] ch = Arrays.copyOfRange(bytes, 7, 11);
				byte[] version = Arrays.copyOfRange(bytes, 11, 12);
				this.version = toInt(version);
				
				if (match(ch, challenge)) {
					valid();
				}
			}

		}
	}

	public int getVersion() {
		return version;
	}
}
