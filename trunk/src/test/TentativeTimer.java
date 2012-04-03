package test;

public class TentativeTimer extends Thread {
	
	private int timer;
	
	public TentativeTimer(int sec) {
		this.timer = sec;
	}
	
	@Override
	public void run() {
		
		for (int i = 0; i < timer; i++) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(i + "s");
		}
		
	}

}
