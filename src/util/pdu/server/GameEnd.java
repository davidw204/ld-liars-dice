package util.pdu.server;

import static util.CommandIDs.GAME_END;
import static util.CommandIDs.MSG_ERROR;

import java.util.Arrays;

import util.pdu.PDU;

public class GameEnd extends PDU {
	
	private int winnerID;
	
	public int getWinnerID() {
		return winnerID;
	}

	public GameEnd(int winnerID) {
		//Liar's Dice Packet ID
		addBytes(GAME_END);
		//Total Message Length
		addByte(winnerID);
		
		addLength();
	}
	
	public GameEnd(byte[] bytes){
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	protected void reverseConstructor(){
		byte[] bytes = getBytes();
		int l = bytes.length;

		if (l > 7 && bytes[4] == GAME_END[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] winnerID = Arrays.copyOfRange(bytes, 7, 8);
				this.winnerID = toInt(winnerID);
				
				valid();
			}
		}
	}
	
	
}
