package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.server.BroadcastThread;
import game.data.Bid;
import game.data.timer.StartTimer;
import game.state.*;

public class GameContext implements IGame {
	
	private IGameState currentState;
	
	private int currentPlayerIndex = 0;
	
	private Bid currentBid = new Bid(0, 0, 0);

	private BroadcastThread broadCaster;
	private List<Player> players;
	private List<Player> lobby;

	private int minPlayer;
	private int maxPlayer;
	
	private int playerID = 1;
	
	private StartTimer st;
	private int tts = util.Constants.GAME_START_TIMER;

	public GameContext(int min, int max, BroadcastThread bc) {
		setCurrentState(new Waiting(this));
		
		this.lobby = new LinkedList<Player>();
		this.broadCaster = bc;
		this.players = new LinkedList<Player>();
		this.minPlayer = min;
		this.maxPlayer = max;
		this.st = new StartTimer(this, getTTS());
	}

	public void setCurrentState(IGameState state) {
		currentState = state;
	}
	
	public Bid getCurrentBid() {
		return currentBid;
	}
	
	public void setCurrentBid(Bid b) {
		currentBid = b;
	}
	
	
	public void setCurrentPlayer(Player p) {
		currentPlayerIndex = players.indexOf(p);
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	public Player getPreviousPlayer() {
		int i = currentPlayerIndex - 1;
		i %= players.size();
		if (i < 0) i+= players.size();
		Player p = players.get(i);
		if (!p.isActive()) p = getPreviousPlayer();
		return p;
	}
	
	public void nextPlayer() {
		currentPlayerIndex++;
		currentPlayerIndex %= players.size();
		if (!players.get(currentPlayerIndex).isActive()) nextPlayer();
	}

	public synchronized boolean addPlayer(Player p) {
		return currentState.addPlayer(p);
	}
	
	public synchronized int getPlayerID(){
		return playerID++;
	}
	
	public List<Player> getActivePlayers() {
		List<Player> act = new LinkedList<Player>();
		for (Player p : players) {
			if (p.isActive()) act.add(p);
		}
		return act;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public int getPlayerCount() {
		return players.size();
	}
	
	public int getReadyCount() {
		int ready = 0;
		for (Player p : players) if (p.isReady() == 1) ready++;
		return ready;
	}
	
	public int getTTS() {
		return tts;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}
	
	public StartTimer getStartTimer() {
		return this.st;
	}
	
	public void setStartTimer(StartTimer st) {
		this.st = st;
	}
	
	public void startGameByTimer() {
		setCurrentState(new Initialize(this));
		System.out.println("Started game by timer...");
	}
	
	public void startGame() {
		st.notValid();
		setCurrentState(new Initialize(this));
		System.out.println("Started game...");		
	}
	
	public synchronized void updateBC(int players) {
		broadCaster.update(lobby.size());
	}

	@Override
	public synchronized void notifiyResponse(Player p, String name) {
		p.setName(name);
		currentState.notifiyResponse(p, name);
	}

	@Override
	public synchronized void notifyDisconnect(Player p, String reason) {
		/* remove player from list, set to inactive etc... */
		currentState.notifyDisconnect(p, reason);
	}

	@Override
	public synchronized void notifyReady(Player p) {
		//start game?!
		p.setReady();
		currentState.notifyReady(p);
	}

	@Override
	public synchronized void notifyBid(Player p, Bid b) {
		// TODO Auto-generated method stub
		currentState.notifyBid(p, b);
	}
}