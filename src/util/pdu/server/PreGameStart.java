package util.pdu.server;

import static util.CommandIDs.PRE_GAME;

import game.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import util.data.PlayerInfo;
import util.pdu.PDU;

public class PreGameStart extends PDU {

	private List<PlayerInfo> players;
	private int min;
	private int tts;


	public PreGameStart(List<Player> ps, int minPlayers, int tts) {
		// Liar's Dice Packet ID
		addBytes(PRE_GAME);
		// Total Message Length
		addByte(minPlayers);
		add2Bytes(tts);
		int players = ps.size();
		addByte(players);

		if (players > 0) {
			for (Player p : ps) {
				addByte(p.getID());
				addByte(p.isReady());
				addString(p.getName());
			}
		}

		addLength();
		valid();
	}
	public PreGameStart(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		players = new LinkedList<PlayerInfo>();
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == PRE_GAME[0]) {
			// byte[] packetID = Arrays.copyOfRange(bytes, 0, 4);
			// byte[] cid = Arrays.copyOfRange(bytes, 4, 5);
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] min = Arrays.copyOfRange(bytes, 7, 8);
				byte[] time2start = Arrays.copyOfRange(bytes, 8, 10);
				byte[] numPlayers = Arrays.copyOfRange(bytes, 10, 11);
				int numP = toInt(numPlayers);
				int off = 11;
				for (int i = 0; i < numP; i++) {
					byte[] id = Arrays.copyOfRange(bytes, off, ++off);
					int pid = toInt(id);
					byte[] rdyFlag = Arrays.copyOfRange(bytes, off, ++off);
					int rdy = toInt(rdyFlag);
					byte[] strLen = Arrays.copyOfRange(bytes, off, ++off);
					int sLen = toInt(strLen);
					byte[] str = Arrays.copyOfRange(bytes, off, (off += sLen));
					String name = fromUTF8(str);
					PlayerInfo p = new PlayerInfo();
					p.setReady(rdy);
					p.setName(name);
					p.setId(pid);
					players.add(p);
				}
				this.min = toInt(min);
				this.tts = toInt(time2start);

				valid();
			}
		}
	}

	public List<PlayerInfo> getPlayers() {
		return players;
	}

	public int getMin() {
		return min;
	}

	public int getTts() {
		return tts;
	}
}
