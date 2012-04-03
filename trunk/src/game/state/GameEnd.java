package game.state;
import java.util.List;

import game.BidCalculator;
import game.GameContext;
import game.Player;
import game.data.Bid;
import game.data.timer.StartTimer;
public class GameEnd implements IGameState {
	
	private GameContext game;
	private int winner;
	private int looser;
	private Player win;
	
	
	public GameEnd(GameContext game){
		this.game = game;
		int[] allDices = BidCalculator.sumUpCasts(game.getPlayers());
		Bid current = game.getCurrentBid();
		int pip = current.getBidPips();
		pip--; // Client values pips with [1-6], we need [0-5]
		int amount = current.getBidAmount();
		int pipAmountCounted = allDices[pip];
		if (pip != 5) pipAmountCounted += allDices[5]; // add amount of jokers
		
		Player currentP = game.getCurrentPlayer();
		Player previousP = game.getPreviousPlayer();
		
		if (amount == pipAmountCounted) {
			bidEquals();
		} else if (amount < pipAmountCounted) {
			int diff = pipAmountCounted - amount;
			currentP.looseDices(diff);
			looser = currentP.getID();
			winner = previousP.getID();
			win = previousP;
		} else if (amount > pipAmountCounted) {
			int diff = amount - pipAmountCounted;
			previousP.looseDices(diff);
			winner = currentP.getID();
			looser = previousP.getID();
			win = currentP;
		}
		
		game.setCurrentPlayer(win);
		
		// game end + check players...
		for (Player p: game.getPlayers()) {
			p.notifyRoundEnd(winner, looser, game.getPlayers());
		}
		
		if (game.getActivePlayers().size() > 1) {
			// next round...
			game.setCurrentState(new Initialize(game));
		} else {
			for (Player p: game.getPlayers()) {
				p.notifyGameEnd(winner);
			}
			for (Player p: game.getPlayers()) {
				p.notifyPreGameStart(game.getMinPlayer(), game.getTTS());
			}
			// make all players active, un-ready...// waiting resets players...
			game.setCurrentState(new Waiting(game));
		}
		
	}
	
	private void bidEquals(){
		for (Player p: game.getPlayers()) {
			if (!p.equals(game.getPreviousPlayer())) p.looseDices(1);
		}
		winner = game.getPreviousPlayer().getID();
		looser = winner;
	}
	
	@Override
	public boolean addPlayer(Player p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized void notifiyResponse(Player p, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void notifyDisconnect(Player p, String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void notifyReady(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void notifyBid(Player p, Bid b) {
		// TODO Auto-generated method stub
		
	}

}
