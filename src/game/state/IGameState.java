package game.state;

import java.util.List;

import game.IGame;
import game.Player;

public interface IGameState extends IGame {
	
	boolean addPlayer(Player p);
	
}
