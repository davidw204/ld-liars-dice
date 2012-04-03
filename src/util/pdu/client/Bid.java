package util.pdu.client;

import static util.CommandIDs.MSG_BID;

import java.util.Arrays;

import util.pdu.PDU;

public class Bid extends PDU {
	private int liarFlag;
	private int bidAmount;
	private int bidPips;

	public Bid(int liarFlag, int bidAmount, int bidPips) {
		// Liar's Dice Packet ID
		addBytes(MSG_BID);
		// Total Message Length
		addByte(liarFlag);
		addByte(bidAmount);
		addByte(bidPips);

		addLength();
		valid();
	}

	public Bid(byte[] bytes) {
		reset();
		addBytes(bytes);
		reverseConstructor();
	}

	@Override
	protected void reverseConstructor() {
		byte[] bytes = getBytes();
		int l = bytes.length;
		if (l > 7 && bytes[4] == MSG_BID[0]) {
			byte[] length = Arrays.copyOfRange(bytes, 5, 7);
			int len = toInt(length);
			if (l == len + 7) {
				byte[] li = Arrays.copyOfRange(bytes, 7, 8);
				byte[] bidA = Arrays.copyOfRange(bytes, 8, 9);
				byte[] bidP = Arrays.copyOfRange(bytes, 9, 10);
				this.liarFlag = toInt(li);
				this.bidAmount = toInt(bidA);
				this.bidPips = toInt(bidP);

				valid();
			}
		}
	}

	public int getLiarFlag() {
		return liarFlag;
	}

	public int getBidAmount() {
		return bidAmount;
	}

	public int getBidPips() {
		return bidPips;
	}
}