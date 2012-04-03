package game.data.timer;

import game.GameContext;
import game.state.WaitingForMove;

public class MoveTimer extends Timer {
	
	private WaitingForMove wfm;
	
	public MoveTimer(GameContext game, WaitingForMove wfm,int sec) {
		super(game, sec);
		this.wfm = wfm;
	}
	
	@Override
	public void triggerEvent() {
		wfm.nextTurn();
	}

}
