package game;

import java.util.List;

import game.data.Bid;
import game.data.Cast;

public final class BidCalculator {

	private BidCalculator() {
	}
	
	public static boolean checkBid(Bid old, Bid current) {
		boolean valid = false;
		
		if (current.getLiarFlag() == 1) {
			/* you can't call liar after round start */
			if (!old.equals(new Bid(0,0,0))) {
				valid = true;
			}
		} else {
			valid = validate(old, current);
		}
		return valid;
	}
	
	public static int[] sumUpCasts(List<Player> players) {
		int[] result = new Cast().getCast();
		for (Player p : players) {
			int[] c = p.getCast().getCast();
			for (int i = 0; i < result.length; i++) {
				result[i] += c[i];
			}
		}
		return result;
	}
	
	private static boolean validate(Bid old, Bid current) {
		boolean valid = false;
		int oldAmount, newAmount, oldPips, newPips;
		oldAmount = old.getBidAmount();
		oldPips = old.getBidPips();
		newAmount = current.getBidAmount();
		newPips = current.getBidPips();
		
		if (oldAmount == newAmount) {
			if (oldPips < newPips) valid = true;
		} else if (oldPips == newPips) {
			if (oldAmount < newAmount) valid = true;
		} else if (oldAmount < newAmount) {
			if (oldPips <= newPips) valid = true;
		} else if (oldPips < newPips) {
			if (oldAmount <= newPips) valid = true;
		}
		return valid;
	}
}