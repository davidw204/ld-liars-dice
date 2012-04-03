package util.pdu.server;

import static util.CommandIDs.ROUND_END;
import game.Player;
import game.data.Cast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import util.data.PlayerInfo;
import util.pdu.PDU;

public class RoundEnd extends PDU {

	private int winner;
	private int loser;
	private int[] playerIDs;
	private int[] lostDices;
	private List<PlayerInfo> player;

	// Game has to make sure that players.size == casts.size
	// hence just players who participated in round should be in list
	public RoundEnd(int winner, int loser, List<Player> players) {
		// Liar's Dice Packet ID
		addBytes(ROUND_END);
		// Total Message Length
		addByte(winner);
		addByte(loser);
		addByte(players.size());

		for (Player p : players) {
			addByte(p.getID());
			Cast c = ((p.getCast() == null) ? (new Cast()) : p.getCast());
			addByte(c.getLostDices());
			for (int i : c.getCast())
				addByte(i);
		}

		addLength();
		valid();
	}

	public RoundEnd(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		player = new LinkedList<PlayerInfo>();
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == ROUND_END[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] wid = Arrays.copyOfRange(bytes, 7, 8);
				int winnerID = toInt(wid);
				byte[] lid = Arrays.copyOfRange(bytes, 8, 9);
				int loserID = toInt(lid);
				byte[] number = Arrays.copyOfRange(bytes, 9, 10);
				int numberOfPlayers = toInt(number);
				playerIDs = new int[numberOfPlayers];
				lostDices = new int[numberOfPlayers];
				int h = 0;
				for (int i = 0; i < numberOfPlayers; i++) {
					int x = 10 + h + i;
					int z = 11 + h + i;
					int y = 12 + h + i;
					h = h + 7;
					byte[] id = Arrays.copyOfRange(bytes, x, z);
					playerIDs[i] = toInt(id);
					byte[] lost = Arrays.copyOfRange(bytes, z, y);
					lostDices[i] = toInt(lost);
					int[] pips = new int[6];
					for (int j = 0; j < 6; j++) {
						byte[] pi = Arrays.copyOfRange(bytes, y + j, y + 1 + j);
						pips[j] = toInt(pi);
					}
					PlayerInfo p = new PlayerInfo();
					Cast c = new Cast(pips,lostDices[i]);
					p.setCast(c);
					p.setId(playerIDs[i]);
					player.add(p);
				}

				this.loser = loserID;
				this.winner = winnerID;
				
				valid();
			}
		}
	}

	public int getWinner() {
		return winner;
	}

	public int getLoser() {
		return loser;
	}

	public int[] getPlayerIDs() {
		return playerIDs;
	}

	public List<PlayerInfo> getPlayers() {
		return player;
	}

}
