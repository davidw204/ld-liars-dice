package main.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import util.pdu.client.Ready;
import util.pdu.client.Response;

import com.client.BCReceiver;
import com.client.BCServer;
import com.client.OutputThread;

public final class Shell {

	/**
	 * @param args
	 */

	private static String pattern = "[1-9] [1-6]";
	private static BCReceiver bc;
	private static List<BCServer> servers;
	private static Socket socket;
	private static InputStream in;
	private static OutputStream out;
	private static OutputThread outT;
	private static int myPlayerID;

	public static void main(String[] args) {
		int port = 30749;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-port")) {
				port = Integer.parseInt(args[i + 1]);
			} 
		}

		bc = new BCReceiver(port);
		bc.start();
		String eingabe;
		servers = bc.getServers();
		System.out
				.println("Willkommen zu Liar's Dice!\nFolgende Befehle werden unterstuetzt: "
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

			eingabe = sc.nextLine();
			quit = evaluate(eingabe);
		}
		sc.close();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// private static boolean process(String input) {
	// boolean quit = false;
	// String[] s = input.split("\\s+");
	// if (input.equals("quit")) {
	// bc.end();
	// outT.end();
	// try {
	// bc.join();
	// outT.join();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// quit = true;
	// }
	// return quit;
	// }

	private static void processConnect(String s) {
		boolean connected = false;
		String[] arr = s.split("\\s+");
		if (arr.length == 1) {
			int server = Integer.parseInt(arr[0]);
			System.out.println("connect to: " + server);
			if (server >= 0 && server < servers.size()) {
				try {
					socket = new Socket(servers.get(server).getIp(), servers
							.get(server).getPort());
					in = socket.getInputStream();
					out = socket.getOutputStream();
					connected = true;
					System.out.println("ip: " + servers.get(server).getIp()
							+ " port: " + servers.get(server).getPort());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (arr.length == 2) {
			String ip = arr[0];
			int port = Integer.parseInt(arr[1]);
			System.out.println(ip + " " + port);
			try {
				socket = new Socket(ip, port);
				in = socket.getInputStream();
				out = socket.getOutputStream();
				connected = true;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Param Error!");
		}
		if (connected) {
			outT = new OutputThread(in, out);
			outT.start();
		}
	}

	private static boolean evaluate(String eingabe) {
		boolean quit = false;
		if (eingabe.equals("list server")) {
			servers = bc.getServers();
			int i = 0;
			for (; i < servers.size() ; i++) {
				System.out.println("#"+ i + " " + servers.get(i).toString());
			}
		} else if ((eingabe.length() >= 8)
				&& eingabe.substring(0, 7).equals("connect")) {
			String server;
			server = eingabe.substring(8);
			processConnect(server);
			// connecte mit server
		} else if (eingabe.equals("disconnect")) {
			util.pdu.client.Disconnect d = new util.pdu.client.Disconnect();
			try {
				out.write(d.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Spieler verlässt Spiel
		} else if (eingabe.equals("quit")) {
			bc.end();
			outT.end();
			try {
				bc.join();
				outT.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			quit = true;
		} else if (eingabe.equals("liar")) {
			util.pdu.client.Bid b = new util.pdu.client.Bid(1, 0, 0);
			try {
				out.write(b.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(eingabe.equals("commands")) {
			getCommands();
		}else if (eingabe.equals("ready")) {
			// Spieler ist bereit zum Spielen
			Ready r = new Ready();
			try {
				out.write(r.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(eingabe.equals("bid")){
			System.out.println("Es fehlen die Parameter fuer bid. Bitte Wette erneut platzieren.");
		}else if (eingabe.length() >= 4 && eingabe.substring(0, 3).equals("bid")) {
			eingabe = eingabe.substring(4);

			if (eingabe.matches(pattern)) {
				int anzWuerf;
				int augenzahl;
				anzWuerf = Integer.parseInt(String.valueOf(eingabe.charAt((0))));
				augenzahl = Integer.parseInt(String.valueOf(eingabe.charAt((2))));
				util.pdu.client.Bid b = new util.pdu.client.Bid(0, anzWuerf, augenzahl);
				try {
					out.write(b.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Spieler bietet: anzWürf, augenzahl
			} else if (eingabe.matches("[1-6]")) {
				System.out.println("Es fehlt der zweite Parameter. Bitte erneut Wette platzieren.");
			} else {
				System.out.println("Unzulaessige Parameter! Es duerfen nur Zahlen zwischen 1 und 6 verwendet werden.");
			}
		} else if (eingabe.length() >= 5 && eingabe.substring(0, 4).equals("send")) {
			eingabe = eingabe.substring(5);
			Response r = new Response(eingabe);
			try {
				out.write(r.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Der Befehl \"" + eingabe + "\" existiert nicht. " );
			getCommands();
		}
		
		return quit;
	}

	public static void getCommands() {
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

	public static int getMyPlayerID() {
		return myPlayerID;
	}

	public static void setMyPlayerID(int myPlayerID) {
		Shell.myPlayerID = myPlayerID;
	}
}