package util.pdu.server;

import static util.CommandIDs.ROUND_START;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import game.Player;
import game.data.Cast;
import util.data.PlayerInfo;
import util.pdu.PDU;

public class RoundStart extends PDU {
	
	private List<PlayerInfo> players;
	private Cast cast;
	private int timer;

	public RoundStart(Cast c, int timer, List<Player> players) {
		// Liar's Dice Packet ID
		addBytes(ROUND_START);
		// Total Message Length
		addByte(c.getNumDices());
		addByte(c.getLostDices());

		for (int i : c.getCast())
			addByte(i);

		addByte(timer);
		addByte(players.size());

		for (Player p : players) {
			addByte(p.getID());
			addByte(p.isActive() ? 1 : 0);
			addString(p.getName());
		}

		addLength();
		valid();
	}
	public RoundStart(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		players = new LinkedList<PlayerInfo>();
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == ROUND_START[0]) {
			// byte[] packetID = Arrays.copyOfRange(bytes, 0, 4);
			// byte[] cid = Arrays.copyOfRange(bytes, 4, 5);
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] nDices = Arrays.copyOfRange(bytes, 7, 8);
				byte[] lDices = Arrays.copyOfRange(bytes, 8, 9);
				int off = 9;
				int[] cast = new int[6];
				for (int idx = 0; idx < 6; idx++) {
					byte[] p = Arrays.copyOfRange(bytes, off, ++off);
					cast[idx] = toInt(p);
				}
				byte[] timer = Arrays.copyOfRange(bytes, off, ++off);
				byte[] numPlayers = Arrays.copyOfRange(bytes, off, ++off);
				int numP = toInt(numPlayers);
				
				for (int i = 0; i < numP; i++) {
					byte[] id = Arrays.copyOfRange(bytes, off, ++off);
					int pid = toInt(id);
					byte[] actFlag = Arrays.copyOfRange(bytes, off, ++off);
					int act = toInt(actFlag);
					byte[] strLen = Arrays.copyOfRange(bytes, off, ++off);
					int sLen = toInt(strLen);
					byte[] str = Arrays.copyOfRange(bytes, off, (off += sLen));
					String name = fromUTF8(str);
					PlayerInfo p = new PlayerInfo();
					p.setActive(act);
					p.setName(name);
					p.setId(pid);
					players.add(p);
				}
				int numDices = toInt(nDices);
				int lostDices = toInt(lDices);
				Cast c = new Cast(cast, numDices, lostDices);
				int t = toInt(timer);
				
				this.cast = c;
				this.timer = t;
				
				valid();
			}
		}
	}

	public List<PlayerInfo> getPlayers() {
		return players;
	}

	public Cast getCast() {
		return cast;
	}

	public int getTimer() {
		return timer;
	}
}
