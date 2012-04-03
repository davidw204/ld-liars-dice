package game.data.timer;

import game.GameContext;

public abstract class Timer extends Thread {
	
	private int timer;
	private int current;
	protected GameContext game;
	private boolean valid;
	private boolean running;
	
	public Timer(GameContext game,int sec) {
		this.game = game;
		this.timer = sec;
		this.current = 0;
		this.valid = true;
		this.running = false;
	}

	@Override
	public void run() {
		running = true;
		try {
			for (current = timer; current > 0; current--) {
				sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (valid) triggerEvent();
	}
	
	public void triggerEvent() {	
	}
	
	public void notValid() {
		valid = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public synchronized int getCurrent() {
		return current;
	}
}
