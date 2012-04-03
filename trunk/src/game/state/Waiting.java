package game.state;
import java.util.List;
import game.GameContext;
import game.Player;
import game.data.Bid;
import game.data.timer.StartTimer;

/**
 * State of game after server started.
 *
 */
public class Waiting implements IGameState {
	private GameContext game;
	
	public Waiting(GameContext game){
		if (game.getPlayers() != null) {
			for (Player p : game.getPlayers()) {
				p.reset();
			}
		}
		this.game = game;
	}

	@Override
	public synchronized boolean addPlayer(Player p) {
		p.setID(game.getPlayerID());
		if (game.getPlayerCount() <= game.getMaxPlayer()) {
			game.getPlayers().add(p);
			p.notifiyChallenge();
			game.updateBC(game.getPlayerCount());
		}
		
		if (game.getPlayerCount() >= game.getMinPlayer()) {
			if (!game.getStartTimer().isRunning()) {
				game.getStartTimer().start();
				//System.out.println("Timer started!");
			}
		}
		return false;
	}

	@Override
	public synchronized void notifiyResponse(Player p, String name) {
		p.notifyPlayerID(p.getID());
		for (Player player : game.getPlayers()) {
			player.notifyConnect(p);
			player.notifyPreGameStart(game.getMinPlayer(), game.getStartTimer().getCurrent());
		}
	}

	@Override
	public synchronized void notifyDisconnect(Player p, String reason) {
		for (Player player : game.getPlayers()) {
			player.notifyDisconnect(p, reason);
		}
	}

	@Override
	public synchronized void notifyReady(Player p) {
		for (Player player : game.getPlayers()) {
			player.notifyPreGameStart(game.getMinPlayer(), game.getStartTimer().getCurrent());
		}
		if (game.getReadyCount() == game.getPlayerCount()) {
			game.getStartTimer().notValid();
			game.setCurrentState(new Initialize(game));
		}
	}

	@Override
	public synchronized void notifyBid(Player p, Bid b) {
		// TODO Auto-generated method stub
	}
}