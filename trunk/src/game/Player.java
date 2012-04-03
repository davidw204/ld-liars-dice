package game;

import java.util.List;

import game.data.Bid;
import game.data.Cast;

import com.server.ClientThread;

public class Player implements IPlayer {

	private GameContext game;
	private ClientThread ct;

	private String name = "player";
	private int playerID;
	private int readyFlag = 0;
	private int activeFlag = 0;
	
	private Cast cast;
	
	/* bad style... */
	private int dices = 6;
	private int numDices = 6;
	private int lostDices = 0;
	
	private boolean disconnected = false; 
	
	public Player(String name, int playerID) {
		this.name = name;
		this.playerID = playerID;
		this.readyFlag = 0;
		this.activeFlag = 0;
	}

	public Player() {
		this.readyFlag = 0;
		this.activeFlag = 0;
	}

	public Player(GameContext g) {
		this.game = g;
		this.readyFlag = 0;
		this.activeFlag = 0;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean equals = false;
		if (o instanceof Player) {
			if (((Player) o).getID() == this.playerID) equals = true;
		}
		return equals;
	}

	public void setClientThread(ClientThread ct) {
		this.ct = ct;
	}
	
	public void looseDices(int numOfDicesToLoose) {
		lostDices += numOfDicesToLoose;
		dices -= numOfDicesToLoose;
		
		if (dices <= 0) {
			setInactive();
		}
	}
	
	public int getDices() {
		return dices;
	}

	public void setDices(int dices) {
		this.dices = dices;
	}

	public int getNumDices() {
		return numDices;
	}

	public void setNumDices(int numDices) {
		this.numDices = numDices;
	}

	public int getLostDices() {
		return lostDices;
	}

	public void setLostDices(int lostDices) {
		this.lostDices = lostDices;
	}

	
	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected() {
		this.disconnected = true;
	}

	public void setReady() {
		readyFlag = 1;
	}
	
	public void setUnready() {
		readyFlag = 0;
	}
	
	public void setActive() {
		activeFlag = 1;
	}
	
	public void setInactive() {
		activeFlag = 0;
	}
	
	public void reset() {
		activeFlag = 1;
		readyFlag = 0;
		dices = 6;
		numDices = 6;
		lostDices = 0;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized int getID() {
		return playerID;
	}

	public synchronized void setID(int id) {
		this.playerID = id;
	}

	public synchronized Cast getCast() {
		return cast;
	}
	
	// just for testing purposes
	public synchronized void setCast(Cast c) {
		this.cast = c;
	}
	
	public synchronized int isReady() {
		return readyFlag;
	}

	public synchronized boolean isActive() {
		return activeFlag == 1;
	}

	@Override
	public void notifiyChallenge() {
		ct.notifiyChallenge();
	}

	@Override
	public void notifyPlayerID(int playerID) {
		ct.notifyPlayerID(playerID);
	}

	@Override
	public void notifyConnect(Player p) {
		ct.notifyConnect(p);
	}

	@Override
	public void notifyDisconnect(Player p, String reason) {
		ct.notifyDisconnect(p, reason);
	}

	@Override
	public void notifyError(String error) {
		ct.notifyError(error);
	}

	@Override
	public void notifyPreGameStart(int minPlayer, int tts) {
		ct.notifyPreGameStart(minPlayer, tts);
	}

	@Override
	public void notifyRoundStart(Cast c, int timer) {
		this.cast = c;
		ct.notifyRoundStart(c, timer);
	}

	@Override
	public void notifyRoundUpdate(int timer, Bid b, int nextPlayer) {
		ct.notifyRoundUpdate(timer, b, nextPlayer);
	}

	@Override
	public void notifyRoundEnd(int winner, int loser, List<Player> player) {
		ct.notifyRoundEnd(winner, loser, player);
		
	}

	@Override
	public void notifyGameEnd(int winner) {
		ct.notifyGameEnd(winner);
	}
}
