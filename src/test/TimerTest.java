package test;

public class TimerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int timer = 15;
		TentativeTimer t = new TentativeTimer(timer);
		t.start();

	}

}
