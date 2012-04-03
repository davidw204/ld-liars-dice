package game;

import java.util.List;

import game.data.Bid;
import game.data.Cast;

public interface IPlayer {
	
		void notifiyChallenge();
		void notifyPlayerID(int playerID);
		void notifyConnect(Player p);
		void notifyDisconnect(Player p, String reason);
		void notifyError(String error);
		void notifyPreGameStart(int minPlayer ,int tts);
		void notifyRoundStart(Cast c, int timer);
		void notifyRoundUpdate(int timer, Bid b, int nextPlayer);
		void notifyRoundEnd(int winner, int loser, List<Player> player);
		void notifyGameEnd(int winner);
}
