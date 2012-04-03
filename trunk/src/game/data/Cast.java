package game.data;

import java.util.Random;

public class Cast {
	
	private final int numOfDiceSides = 6;
	private int[] cast;
	
	private int numDices;
	private int lostDices;

	public Cast(int dices, int numDices, int lostDices) {
		this.numDices = numDices;
		this.lostDices = lostDices;
		this.cast = new int[numOfDiceSides];
		Random r = new Random();
		
		for (int i = 0; i < dices; i++) {
			this.cast[r.nextInt(numOfDiceSides)]++;
		}	
	}
	
	public Cast() {
		this.numDices = 0;
		this.lostDices = 0;
		this.cast = new int[numOfDiceSides];
	}
	
	public Cast(int[] cast, int numDices, int lostDices) {
		this.numDices = numDices;
		this.lostDices = lostDices;
		this.cast = cast;
	}
	
	public Cast(int[] cast, int lostDices) {
		this.numDices = 0;
		this.lostDices = lostDices;
		this.cast = cast;
	}

	public int getNumDices() {
		return numDices;
	}
	

	public int getLostDices() {
		return lostDices;
	}

	public int[] getCast() {
		return this.cast;
	}
}
