package game.state;
import java.util.List;

import game.GameContext;
import game.Player;
import game.data.Bid;
import game.data.Cast;

public class Initialize implements IGameState {
	private GameContext game;
	
	public Initialize(GameContext game) {
		System.out.println("Initializing game...");
		this.game = game;
		//roundstart, wuerfel, zustand wechseln -> spiel aktiv
		for (Player p : game.getPlayers()) {
			if (p.isActive()) {
				int dices = p.getDices(), numDices = p.getNumDices(), lostDices = p
						.getLostDices();
				Cast c = new Cast(dices, numDices, lostDices);
				p.setCast(c);
			} else {
				p.setCast(new Cast());
			}
		}
		for (Player p : game.getPlayers()) {
			p.notifyRoundStart(p.getCast(), util.Constants.TIME_FOR_ACTION);
		}
		game.setCurrentState(new WaitingForMove(game));
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
