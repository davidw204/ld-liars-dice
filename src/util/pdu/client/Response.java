package util.pdu.client;

import static util.CommandIDs.*;
import static util.Converter.*;

import java.util.Arrays;

import util.pdu.PDU;

public class Response extends PDU {
	
	private byte[] response = hexStringToByteArray("44494345");
	private int version = 1;
	private String playername;
	

	public Response(String playername) {
		//Liar's Dice Packet ID
		addBytes(MSG_RESPONSE);
		//Total Message Length
		addBytes(response);
		addByte(version);
		addString(playername);
		
		addLength();
		valid();
	}
	
	public Response(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == MSG_RESPONSE[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] ch = Arrays.copyOfRange(bytes, 7, 11);
				byte[] version = Arrays.copyOfRange(bytes, 11, 12);
				this.version = toInt(version);
				byte[] strL = Arrays.copyOfRange(bytes, 12, 13);
				int off = toInt(strL);
				byte[] str = Arrays.copyOfRange(bytes, 13, 13+off);
				this.playername = fromUTF8(str);
				
				if (match(ch, response)) {
					valid();
				}
			}

		}
	}

	public String getPlayername() {
		return playername;
	}
}
