package main.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import com.client.BCReceiver;
import com.client.BCServer;

public class ClientShell {
	
	private static BCReceiver bc;
	private static List<BCServer> servers;
	private static GameThread game = null;
	

	public static void main(String[] args) {

		int port = 30749;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-port")) {
				port = Integer.parseInt(args[i + 1]);
			}
		}

		bc = new BCReceiver(port);
		bc.start();
		String input;
		servers = bc.getServers();
		System.out.println("Willkommen zu Liar's"
						+ " Dice!\nBefehle werden unterstuetzt: "
						+ "\n - list server *Listet alle moeglichen Server auf*"
						+ "\n - connect \"server\" *Stellt die Verbindung zu einem Server her*"
						+ "\n - disconnect *Beendet die Verbindung zu einem Server*"
						+ "\n - ready *Spieler ist nun spielbereit.*"
						+ "\n - bid \"num\" \"num\" *Gibt ein Gebot ab*"
						+ "\n - liar *Wuerfel aufdecken* "
						+ "\n - commands *Listet alle moeglichen Befehle auf*");

		boolean quit = false;
		Scanner sc = new Scanner(System.in);

		while (!quit) {
			System.out.print(" \nLiar's dice> ");

			input = sc.nextLine();
			System.out.println("#" + input + "#");
			quit = evaluate(input);
		}

	}
	
	private static boolean evaluate(String input) {
		boolean quit = false;
		String[] arr = input.split("\\s+");
		int l = arr.length;
		if (l == 1) {
			String s = arr[0];
			if (s.equals("ready")) {
				ready();
			} else if (s.equals("disconnect")) {
				disconnect();
			} else if (s.equals("liar")) {
				liar();
			} else if (s.equals("commands")) {
				getCommands();
			} else {
				System.out.println("ERROR! No valid command.");
			}
		} else if (l == 2) {
			String s = arr[0];
			String d = arr[1];
			if (s.equals("connect")) {
				String ip = s;
				int port = 0;
				try {
				port = Integer.parseInt(d);
				} catch (NumberFormatException e) {
					System.out.println("ERROR! No valid number");
				}
				Socket socket = null;
				try {
					socket = new Socket(ip, port);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (socket != null) {
					game = new GameThread(socket);
					game.start();
				}
			} else if (s.equals("send")) {
				send(d);
			} else if (s.equals("list") && d.equals("server")) {
				listServers();
			} else {
				System.out.println("ERROR! No valid command.");
			}
			
		} else if (l == 3) {
			String s = arr[0];
			String d = arr[1];
			String f = arr[2];
			if (s.equals("connect")) {
				int port = 0;
				try {
					port = Integer.parseInt(f);
				} catch (NumberFormatException e) {
				}
				Socket socket = null;
				try {
					socket = new Socket(d, port);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (socket != null) {
					game = new GameThread(socket);
					game.start();
				}
				
			} else if (s.equals("bid")) {
				int bidAmount = 0, bidPips = 0;
				bidAmount = Integer.parseInt(d);
				bidPips = Integer.parseInt(f);
				bid(bidAmount, bidPips);
			} else {
				System.out.println("ERROR! No valid command.");
			}
			
		} else {
			System.out.println("ERROR! No valid command.");
		}
		
		return quit;
		
	}
	
	private static void ready() {
		if (game != null) {
			game.ready();
		} else {
			System.out.println("ERROR! There is no game existing.");
		}
	}
	
	private static void disconnect() {
		if (game != null) {
			game.disconnect();
		} else {
			System.out.println("ERROR! There is no game existing.");
		}
	}
	
	private static void bid(int bidAmount, int bidPips) {
		if (game != null) {
			game.bid(bidAmount, bidPips);
		} else {
			System.out.println("ERROR! There is no game existing.");
		}
	}
	
	private static void liar() {
		if (game != null) {
			game.liar();
		} else {
			System.out.println("ERROR! There is no game existing.");
		}
	}
	
	private static void send(String name) {
		if (game != null) {
			game.response(name);
		} else {
			System.out.println("ERROR! There is no game existing.");
		}
	}
	
	private static void listServers() {
		servers = bc.getServers();
		int i = 0;
		for (; i < servers.size() ; i++) {
			System.out.println("#"+ i + " " + servers.get(i).toString());
		}
	}
	
	public static void gameOver() {
		game = null;
	}
	
	private static void getCommands() {
		System.out
				.println("Folgende Befehle werden unterstützt: "
						+ "\n - list server *Listet alle möglichen Server auf*"
						+ "\n - connect \"server\" *Stellt die Verbindung zu einem Server her*"
						+ "\n - disconnect *Beendet die Verbindung zu einem Server*"
						+ "\n - ready *Spieler ist nun spielbereit.*"
						+ "\n - bid \"num\" \"num\" *Gibt ein Gebot ab*"
						+ "\n - liar *Würfel aufdecken* "
						+ "\n - commands *Listet alle möglichen Befehle auf*");
	}

}
