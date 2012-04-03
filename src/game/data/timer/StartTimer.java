package game.data.timer;

import game.GameContext;

public class StartTimer extends Timer {

	public StartTimer(GameContext game, int sec) {
		super(game, sec);
	}
	
	@Override
	public void triggerEvent() {
		super.game.startGameByTimer();
	}
}
