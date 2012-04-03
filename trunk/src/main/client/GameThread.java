package main.client;

import static util.CommandIDs.GAME_END;
import static util.CommandIDs.MSG_CHALLENGE;
import static util.CommandIDs.MSG_ERROR;
import static util.CommandIDs.MSG_PLAYERID;
import static util.CommandIDs.NOTIFY_CONNECT;
import static util.CommandIDs.NOTIFY_DISCONNECT;
import static util.CommandIDs.PID;
import static util.CommandIDs.PRE_GAME;
import static util.CommandIDs.ROUND_END;
import static util.CommandIDs.ROUND_START;
import static util.CommandIDs.ROUND_UPDATE;
import static util.Converter.toInt;
import game.data.Bid;
import game.data.Cast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.ksmsa.utils.io.ByteBuilder;

import util.data.PlayerInfo;
import util.pdu.client.Ready;
import util.pdu.client.Response;
import util.pdu.server.Challenge;
import util.pdu.server.Connect;
import util.pdu.server.GameEnd;
import util.pdu.server.PlayerID;
import util.pdu.server.PreGameStart;
import util.pdu.server.RoundEnd;
import util.pdu.server.RoundStart;
import util.pdu.server.RoundUpdate;

public class GameThread extends Thread {
	
	private ByteBuilder bytes;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private boolean stop = false;
	
	public GameThread(Socket socket) {
		this.socket = socket;
		try {
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ready() {
		Ready r = new Ready();
		try {
			out.write(r.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void response(String name) {
		Response r = new Response(name);
		try {
			out.write(r.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		util.pdu.client.Disconnect d = new util.pdu.client.Disconnect();
		try {
			out.write(d.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stop = true;
		ClientShell.gameOver();
	}
	
	public void bid(int bidAmount, int bidPips) {
		util.pdu.client.Bid b = new util.pdu.client.Bid(0, bidAmount, bidPips);
		try {
			out.write(b.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void liar() {
		util.pdu.client.Bid b = new util.pdu.client.Bid(1, 0, 0);
		try {
			out.write(b.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		bytes = new ByteBuilder();
		int c;
		try {
			int i = 0;
			while (!stop) {
				if (((c = in.read()) != -1)) {
				if (i == 3) {
					bytes.append((byte) c);
					if (match(bytes.getBytes(), PID)) {
						byte[] b = bytes.getBytes();
						processLength(in);
						
					}
					i = 0;
				} else {
					bytes.append((byte) c);
					i++;
				}
			} else {
				stop = true;
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if(cid == MSG_CHALLENGE[0]){
			Challenge c = new Challenge(pdu);
			if (c.isValid()) {
				System.out.println("Got Challenge!\nEnter: 'send <username>'");
			}
			
		}else if(cid == MSG_PLAYERID[0]){
			PlayerID p = new PlayerID(pdu);
			if (p.isValid()) {
				System.out.println("Got playerID: " + p.getPlayerID());
				main.client.Shell.setMyPlayerID(p.getPlayerID());
			}
		}else if(cid == NOTIFY_CONNECT[0]){
			Connect c = new Connect(pdu);
			if (c.isValid()) {
				System.out.println("Notify connect: " + c.getName() + ", id: " + c.getId());
			}
		}else if(cid == NOTIFY_DISCONNECT[0]){
			util.pdu.server.Disconnect d = new util.pdu.server.Disconnect(pdu);
			if (d.isValid()) {
				System.out.println("Player: "  + d.getName() +" disconnected for reason: " + d.getReason());
				stop = true;
				ClientShell.gameOver();
			}
		}else if(cid == MSG_ERROR[0]){
			util.pdu.server.Error e = new util.pdu.server.Error(pdu);
			if (e.isValid()) {
				System.out.println("Bid error: " + e.getReason());
			}
		}else if(cid == PRE_GAME[0]){
			PreGameStart p = new PreGameStart(pdu);
			if (p.isValid()) {
				System.out.println("PreGameStart: min: " + p.getMin() + " " + p.getTts());
				for (PlayerInfo player : p.getPlayers()) {
					System.out.println("id: " + player.getId() + " rdy: " 
				+ player.getReady() + " name: " + player.getName());
				}
			}
		}else if(cid == ROUND_START[0]){
			RoundStart r = new RoundStart(pdu);
			if (r.isValid()) {
				System.out.println("Round started:");
				Cast c = r.getCast();
				int[] cast = c.getCast();
				System.out.println("cast. lost dices: " + c.getLostDices() + " numDices: " + c.getNumDices());
				System.out.println("1 2 3 4 5 *");
				System.out.println(cast[0] + " " + cast[1] + " "+ cast[2] + " "+ cast[3] + " "+ cast[4] + " "+ cast[5]);
				int i = 0;
				for (PlayerInfo pi : r.getPlayers()) {
					System.out.println(pi.getName() + "(" + pi.getId() + ") " + ((pi.getActive() == 1) ? "active" : "inactive"));
					if (i == 0 && pi.getId() == main.client.Shell.getMyPlayerID()) System.out.println("It's your turn.");
					i++;
				}
			}
		}else if(cid == ROUND_UPDATE[0]){
			RoundUpdate r = new RoundUpdate(pdu);
			if (r.isValid()) {
				System.out.println("round update:");
				System.out.println("bid amount: " + r.getBid().getBidAmount() + " bid pips: " 
				+ r.getBid().getBidPips() + " nextPlayer with ID: " + r.getNextPlayer());
				if (r.getNextPlayer() == main.client.Shell.getMyPlayerID()) System.out.println("It's your turn.");
			}
		}else if(cid == ROUND_END[0]){
			RoundEnd r = new RoundEnd(pdu);
			if (r.isValid()) {
				System.out.println("winner: " + r.getWinner() + " loser: " + r.getLoser());
				for (PlayerInfo p : r.getPlayers()) {
					Cast c = p.getCast();
					int[] cast = c.getCast();
					System.out.println("cast of player: " + p.getId() + " lost dices: " + c.getLostDices());
					System.out.println("1 2 3 4 5 *");
					System.out.println(cast[0] + " " + cast[1] + " "+ cast[2] + " "+ cast[3] + " "+ cast[4] + " "+ cast[5]);
				}
			}
		}else if(cid == GAME_END[0]){
			GameEnd g = new GameEnd(pdu);
			if (g.isValid()) {
				System.out.println("Game ended. Winner is: " + g.getWinnerID());
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
		return matches;
	}
	
	public void end() {
		stop = true;
	}
}