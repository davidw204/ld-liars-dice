package game.data;

public class Bid {
	
	private int liarFlag;
	private int bidAmount;
	private int bidPips;
	
	public Bid(int liarFlag, int bidAmount , int bidPips) {
		this.liarFlag = liarFlag;
		this.bidAmount = bidAmount;
		this.bidPips = bidPips;
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
	
	@Override
	public boolean equals(Object o) {
		boolean equals = false;
		if (o instanceof game.data.Bid) {
			if ( ((game.data.Bid) o).getLiarFlag() == this.liarFlag
					&& ((game.data.Bid) o).getBidAmount() == this.bidAmount
					&& ((game.data.Bid) o).getBidPips() == this.bidPips) {
				equals = true;
			}
		}
		return equals;
	}

}
