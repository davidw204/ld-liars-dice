package game.state;
import java.util.List;

import game.BidCalculator;
import game.GameContext;
import game.Player;
import game.data.Bid;
import game.data.Cast;
import game.data.timer.MoveTimer;

public class WaitingForMove implements IGameState {
	private GameContext game;
	MoveTimer mt = null;
	
	public WaitingForMove(GameContext game) {
		this.game = game;
		mt = new MoveTimer(game, this, util.Constants.TIME_FOR_ACTION);
		mt.start();
		
		for (Player p : game.getPlayers()) {
			p.notifyRoundUpdate(mt.getCurrent(), game.getCurrentBid(), game.getCurrentPlayer().getID());
		}
		
	}

	@Override
	public boolean addPlayer(Player p) {
		return false;
	}

	@Override
	public synchronized void notifiyResponse(Player p, String name) {
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
		if (game.getCurrentPlayer().equals(p)) {
			if (BidCalculator.checkBid(game.getCurrentBid(), b)) {
				if (b.getLiarFlag() == 1) {
					mt.notValid();
					game.setCurrentState(new GameEnd(game));
				} else {
					mt.notValid();
					game.setCurrentBid(b);
					nextTurn();
				}
			} else {
				p.notifyError("Bid was not valid!");
			}
		} else {
			p.notifyError("It's not your turn!");
		}
		
	}
	
	public void nextTurn() {
		game.nextPlayer();
		mt = new MoveTimer(game, this, util.Constants.TIME_FOR_ACTION);
		mt.start();
		
		for (Player p : game.getPlayers()) {
			p.notifyRoundUpdate(mt.getCurrent(), game.getCurrentBid(), 
					game.getCurrentPlayer().getID());
		}
	}
}
