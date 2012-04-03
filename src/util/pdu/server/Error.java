package util.pdu.server;
//TEST
import static util.CommandIDs.MSG_ERROR;
import java.util.Arrays;

import util.pdu.PDU;

public class Error extends PDU {
	private String reason;
	
	public String getReason() {
		return reason;
	}
	
	public Error(String reason) {
		//Liar's Dice Packet ID
		addBytes(MSG_ERROR);
		//Total Message Length
		addString(reason);
		
		addLength();
		valid();
	}
	
	public Error(byte[] bytes){
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor(){
		byte[] bytes = getBytes();
		int l = bytes.length;

		if (l > 7 && bytes[4] == MSG_ERROR[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] strL = Arrays.copyOfRange(bytes, 7, 8);
				int off = toInt(strL);
				byte[] str = Arrays.copyOfRange(bytes, 8, 8 + off);
				String reason = fromUTF8(str);
				this.reason = reason;

				valid();
			}
		}
	}
}
