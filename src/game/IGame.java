package game;

import game.data.Bid;

public interface IGame {

	void notifiyResponse(Player p, String name);
	void notifyDisconnect(Player p, String reason);
	void notifyReady(Player p);
	void notifyBid(Player p, Bid b);
	
}
