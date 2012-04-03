package com.server;

import static util.CommandIDs.PID;
import static util.CommandIDs.MSG_BID;
import static util.CommandIDs.MSG_DISCONNECT;
import static util.CommandIDs.MSG_READY;
import static util.CommandIDs.MSG_RESPONSE;
import static util.Converter.toInt;
import game.GameContext;
import game.IPlayer;
import game.Player;
import game.data.Bid;
import game.data.Cast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

import org.ksmsa.utils.io.ByteBuilder;

import util.pdu.client.Ready;
import util.pdu.client.Response;
import util.pdu.server.Challenge;
import util.pdu.server.Connect;
import util.pdu.server.Disconnect;
import util.pdu.server.GameEnd;
import util.pdu.server.PlayerID;
import util.pdu.server.PreGameStart;
import util.pdu.server.RoundEnd;
import util.pdu.server.RoundStart;
import util.pdu.server.RoundUpdate;

public class ClientThread extends Thread implements IPlayer {

	private GameContext game;
	private Player player;
	private String playerName = "";
	
	private int name;
	private Socket socket;

	private InputStream in;
	private OutputStream out;
	private boolean stop = false;
	private ByteBuilder bytes;
	
	boolean disconnected = false;
	boolean open = false;

	public ClientThread(int name, Socket socket) {
		this.name = name;
		this.socket = socket;
	}

	public ClientThread(int name, Socket socket, GameContext game, Player p) {
		this.name = name;
		this.socket = socket;
		this.game = game;
		this.player = p;
		player.setClientThread(this);
	}

	@Override
	public void run() {
		String msg = "Connection to client#" + name + " established.";
		System.out.println(msg);
		bytes = new ByteBuilder();


			try {
				in = socket.getInputStream();
				out = socket.getOutputStream();
				open = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			
			game.addPlayer(player);
			
			int c = 0;
			int i = 0;
			
			if (!socket.isClosed() || !socket.isInputShutdown()) {
				try {
					c = in.read();
				} catch (SocketException e){
					open = false;
					disconnected = true;
					//e.printStackTrace();
				} catch (IOException e) {
					open = false;
					//e.printStackTrace();
				}
			} else {
				open = false;
				disconnected = true;
			}
			
			while (open && !stop && (c  != -1) ) {
				if (i == 3) {
					bytes.append((byte) c);
					if (match(bytes.getBytes(), PID)) {
						//byte[] b = bytes.getBytes();
						processLength(in);
						
					}
					i = 0;
				} else {
					bytes.append((byte) c);
					i++;
				}
				if (!socket.isClosed() || socket.isInputShutdown()) {
					try {
						c = in.read();
					} catch (SocketException e){
						open = false;
						disconnected = true;
						//e.printStackTrace();
					} catch (IOException e) {
						open = false;
						//e.printStackTrace();
					}
				} else {
					open = false;
					disconnected = true;
				}
			}

			String msgOut = "Connection to client#" + name + " closed.";
			System.out.println(msgOut);
			if (disconnected) {
				game.notifyDisconnect(player, "Player disconnected.");
			} else {
				game.notifyDisconnect(player, "Player disconnected.");
			}
			try {
				socket.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		

	}
	
	private void processLength(InputStream in) {
		int c;
		boolean read = false;
		for (int i = 0; i < 3; i++) {
			try {
				if ((c = in.read()) != -1) {
					bytes.append((byte) c);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i == 2) {
				read = true;
			}
		}
		if (read) {
			byte[] b = bytes.getBytes();
			byte[] length = Arrays.copyOfRange(b, 5, 7);
			int len = toInt(length);
			readToEnd(in, len);
		}
	}
	
	private void readToEnd(InputStream in, int length) {
		int c;
		boolean read = false;
		for (int i = 0; i < length; i++) {
			try {
				if ((c = in.read()) != -1) {
					bytes.append((byte) c);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i == length - 1) read = true;
		}
		if (length == 0) read = true;
		if (read) {
			byte[] b = bytes.getBytes();
			bytes = new ByteBuilder();
			determinePDU(b);
		}
		
	}
	
	private void determinePDU(byte[] pdu) {
		byte cid = pdu[4];
		if(cid == MSG_BID[0]){
			util.pdu.client.Bid b = new util.pdu.client.Bid(pdu);
			if (b.isValid()) {
				game.notifyBid(player, new game.data.Bid(b.getLiarFlag(),
						b.getBidAmount(), b.getBidPips()));
				System.out.println("Received bid from: " + playerName);
			}
			
		}else if(cid == MSG_READY[0]){
			System.out.println("ready came in...");
			Ready r = new Ready(pdu);
			if (r.isValid()) {
				System.out.println("valid package ready came in...");
				game.notifyReady(player);
			}
		}else if(cid == MSG_DISCONNECT[0]){
			Connect c = new Connect(pdu);
			disconnected = true;
			open = false;
			game.notifyDisconnect(player, "Player disconnected.");
		}else if(cid == MSG_RESPONSE[0]){
			Response r = new Response(pdu);
			if (r.isValid()) {
				String name = r.getPlayername();
				playerName = name;
				game.notifiyResponse(player, name);
			}
		}
	}
	
	
	/* checks whether to byte arrays match */
	private boolean match(byte[] a, byte[] b) {
		boolean matches = false;
		int l = a.length;
		if (l == b.length) {
			matches = true;
			for (int i = 0; i < l; i++) {
				if (a[i] != b[i]) {
					matches = false;
					break;
				}
			}
		}
		System.out.println("match: " + matches);
		return matches;
	}

	@Override
	public void notifiyChallenge() {
		System.out.println("notifyChallenge entered");
		Challenge c = new Challenge();
		try {
			out.write(c.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failed sending challenge...");
		}
		System.out.println("sent challenge...");
	}

	@Override
	public void notifyPlayerID(int playerID) {
		player.setID(playerID);
		PlayerID p = new PlayerID(player);
		try {
			out.write(p.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void notifyConnect(Player p) {
		Connect c = new Connect(p);
		try {
			out.write(c.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void notifyDisconnect(Player p, String reason) {
		open = false;
		Disconnect d = new Disconnect(p, reason);
		try {
			out.write(d.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyError(String error) {
		util.pdu.server.Error err = new util.pdu.server.Error(error);
		try {
			out.write(err.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyPreGameStart(int minPlayer, int tts) {
		PreGameStart p = new PreGameStart(game.getPlayers(), minPlayer, tts);
		try {
			out.write(p.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyRoundStart(Cast c, int timer) {
		RoundStart r = new RoundStart(c, timer, game.getPlayers());
		try {
			out.write(r.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void notifyRoundUpdate(int timer, Bid b, int nextPlayer) {
		RoundUpdate r = new RoundUpdate(timer, b, nextPlayer);
		try {
			out.write(r.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyRoundEnd(int winner, int loser, List<Player> player) {
		RoundEnd r = new RoundEnd(winner, loser, player);
		try {
			out.write(r.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyGameEnd(int winner) {
		GameEnd g = new GameEnd(winner);
		try {
			out.write(g.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}