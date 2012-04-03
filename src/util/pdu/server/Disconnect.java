package util.pdu.server;

import static util.CommandIDs.NOTIFY_DISCONNECT;
import game.Player;

import java.util.Arrays;

import util.pdu.PDU;

public class Disconnect extends PDU {
	private int id;
	private String reason;
	private String name;
	
	public Disconnect(Player p, String reason){
		// Liar's Dice Packet ID
		addBytes(NOTIFY_DISCONNECT);
		// Total Message Length
		addByte(p.getID());
		addString(p.getName());
		addString(reason);

		addLength();
		valid();
	}
	
	public Disconnect(byte[] bytes){
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor(){
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == NOTIFY_DISCONNECT[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			
			if (l == len + 7) {
				byte[] pid = Arrays.copyOfRange(bytes, 7, 8);
				byte[] strL = Arrays.copyOfRange(bytes, 8, 9);
				int off = toInt(strL);
				byte[] str = Arrays.copyOfRange(bytes, 9, 9 + off);
				String name = fromUTF8(str);
				off = 9+off;
				strL = Arrays.copyOfRange(bytes, off, ++off);
				int off1 = toInt(strL);
				str = Arrays.copyOfRange(bytes, off, off+off1);
				String reason = fromUTF8(str);

				this.id = toInt(pid);
				this.name = name;
				this.reason = reason;
				
				valid();
			}
		}
	}
	
	public int getId() {
		return id;
	}

	public String getReason() {
		return reason;
	}

	public String getName() {
		return name;
	}
}
