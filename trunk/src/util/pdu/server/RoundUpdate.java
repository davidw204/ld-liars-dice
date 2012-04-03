package util.pdu.server;

import static util.CommandIDs.ROUND_UPDATE;

import game.data.Bid;

import java.util.Arrays;

import util.pdu.PDU;

public class RoundUpdate extends PDU {
	private int timer;
	private Bid bid;
	private int nextPlayer;

	public RoundUpdate(int timer, Bid b, int nextPlayer) {
		// Liar's Dice Packet ID
		addBytes(ROUND_UPDATE);
		// Total Message Length
		addByte(timer);
		addByte(b.getBidAmount());
		addByte(b.getBidPips());
		addByte(nextPlayer);

		addLength();
		valid();
	}

	public RoundUpdate(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == ROUND_UPDATE[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] ti = Arrays.copyOfRange(bytes, 7, 8);
				int timer = toInt(ti);
				byte[] biA = Arrays.copyOfRange(bytes, 8, 9);
				int bidAmount = toInt(biA);
				byte[] biP = Arrays.copyOfRange(bytes, 9, 10);
				int bidPips = toInt(biP);
				byte[] next = Arrays.copyOfRange(bytes, 10, 11);
				int nextPlayer = toInt(next);
				this.timer = timer;
				this.bid = new Bid(0, bidAmount, bidPips);
				this.nextPlayer = nextPlayer;
				
				valid();
			}
		}
	}

	public int getTimer() {
		return timer;
	}

	public Bid getBid() {
		return bid;
	}

	public int getNextPlayer() {
		return nextPlayer;
	}

}
