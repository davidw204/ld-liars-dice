package util.pdu.server;

import static util.CommandIDs.MSG_PLAYERID;

import game.Player;

import java.util.Arrays;

import util.pdu.PDU;

public class PlayerID extends PDU {
	
	private int playerID;
	
	public int getPlayerID() {
		return playerID;
	}

	public PlayerID(Player p) {
		//Liar's Dice Packet ID
		addBytes(MSG_PLAYERID);
		//Total Message Length
		addByte(p.getID());
		
		addLength();
		valid();
	}
	
	public PlayerID(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}
	
	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == MSG_PLAYERID[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] id = Arrays.copyOfRange(bytes, 7, 8);
				int playerID = toInt(id);
				this.playerID = playerID;
				valid();
			}
		}
	}
}
