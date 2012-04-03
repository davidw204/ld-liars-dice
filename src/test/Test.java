package test;

import java.util.*;

import game.Player;
import game.data.Cast;
import util.data.PlayerInfo;
import util.pdu.client.Bid;
import util.pdu.client.Ready;
import util.pdu.client.Response;
import util.pdu.client.Disconnect;
import util.pdu.server.*;
import util.pdu.server.Error;
import static util.Converter.*;

public class Test {
	
	
	public static void main(String[] args) {
		byte[] bytes;
		
		String ip = "192.168.0.1";
		int port = 8088;
		String name = "imbaSERVERx";
		int min = 3;
		int cur = 5;
		int max = 7;
		BroadcastUDP b = new BroadcastUDP(ip, port, name, min, cur, max);
		System.out.println(b.getClass() + " created");
		System.out.println(ip + " " + port + " " + name + " " + min + " " + cur + " " + max );
		bytes = b.getBytes();
		//System.out.println(getHex(bytes));
		BroadcastUDP c = new BroadcastUDP(bytes);
		System.out.println(b.getClass() + " recreated");
		System.out.println("Package valid: " + c.isValid());
		System.out.println(c.getIp() + " " + c.getPort() + " " + c.getName() 
				+ " " + c.getMin() + " " + c.getCur() + " " + c.getMax() );
		//System.out.println(getHex(c.getBytes()));
		
		System.out.println("\n");
		
		Challenge c1 = new Challenge();
		bytes = c1.getBytes();
		System.out.println(c1.getClass() + " created");
		Challenge c2 = new Challenge(bytes);
		System.out.println(c2.getClass() + " recreated");
		System.out.println("Package valid: " + c2.isValid());
		
		System.out.println("\n");
		
		String p1name = "johnny";
		int p1ID = 5;
		Player p1 = new Player(p1name, p1ID);
		Connect ct = new Connect(p1);
		System.out.println(ct.getClass() + " created");
		System.out.println(p1name + " " + p1ID);
		bytes = ct.getBytes();
		Connect ct2 = new Connect(bytes);
		System.out.println(ct2.getClass() + " recreated");
		System.out.println("Package valid: " + ct2.isValid());
		System.out.println(ct2.getName() + " " + ct2.getId());
		
		System.out.println("\n");
		
		String p2name = "johnny";
		String reason = "johnny left the game";

		int p2ID = 10;
		Player p2 = new Player(p2name, p2ID);
		util.pdu.server.Disconnect dc = new util.pdu.server.Disconnect(p2,reason);
		System.out.println(dc.getClass() + " created");
		System.out.println(p2name + " " + p2ID+ " "+ reason);
		bytes = dc.getBytes();
		util.pdu.server.Disconnect dc2 = new util.pdu.server.Disconnect(bytes);
		System.out.println(dc2.getClass() + " recreated");
		System.out.println("Package valid: " + dc2.isValid());
		System.out.println(dc2.getName() + " " + dc2.getId() + " " + dc2.getReason());
		
		System.out.println("\n");
		
		String reason1 = "Error";
		Error e = new Error(reason1);
		System.out.println(e.getClass() + " created");
		System.out.println(reason1);
		bytes = e.getBytes();
		
		Error e2 = new Error(bytes);
		System.out.println(e2.getClass() + " recreated");
		System.out.println("Package valid: " + e2.isValid());
		System.out.println(e2.getReason());
		System.out.println("\n");
		
		int id = 23;
		GameEnd end = new GameEnd(id);
		System.out.println(end.getClass() + " created");
		System.out.println(id);
		bytes = end.getBytes();
		
		GameEnd end2 = new GameEnd(bytes);
		System.out.println(end2.getClass() + " recreated");
		System.out.println("Package valid: " + end2.isValid());
		System.out.println(end2.getWinnerID());
		System.out.println("\n");
		
		int p3ID = 11;
		String p3name = "Michael";
		Player p3 = new Player(p3name, p3ID);
		PlayerID pl3 = new PlayerID(p3);
		System.out.println(pl3.getClass() + " created");
		System.out.println(p3ID);
		bytes = pl3.getBytes();
		
		PlayerID pl32 = new PlayerID(bytes);
		System.out.println(pl32.getClass() + " recreated");
		System.out.println("Package valid: " + pl32.isValid());
		System.out.println(pl32.getPlayerID());
		
		System.out.println("\n");
		
		int timer = 2;
		int bidAmount = 4;
		int bidPips = 3;
		int pl1 = 11;
		game.data.Bid b12 = new game.data.Bid(0, bidAmount, bidPips);
		RoundUpdate u = new RoundUpdate(timer,b12,pl1);
		System.out.println(u.getClass() + " created");
		System.out.println(timer + " " + b12.getBidAmount()+ " " + b12.getBidPips() + " " + pl1);
		bytes = u.getBytes();
		
		RoundUpdate u1 = new RoundUpdate(bytes);
		System.out.println(u1.getClass() + " recreated");
		System.out.println("Package valid: " + u1.isValid());
		System.out.println(u1.getTimer() + " " + u1.getBid().getBidAmount() + " " + u1.getBid().getBidPips() + " " + u1.getNextPlayer());
		
		System.out.println("\n");
		
		String playername  = "Hanswurst";
		Response res = new Response(playername);
		System.out.println(res.getClass() + " created");
		System.out.println(playername);
		bytes = res.getBytes();
		
		Response res1 = new Response(bytes);
		System.out.println(res1.getClass() + " recreated");
		System.out.println("Package valid: " + res1.isValid());
		System.out.println(res1.getPlayername());
		
		System.out.println("\n");
		
		Ready ready = new Ready();
		System.out.println(ready.getClass() + " created");
		bytes = ready.getBytes();
		
		Ready ready1 = new Ready(bytes);
		System.out.println(ready1.getClass() + " recreated");
		System.out.println("Package valid: " + ready1.isValid());
		
		System.out.println("\n");
		
		Disconnect disc = new Disconnect();
		System.out.println(disc.getClass() + " created");
		bytes = disc.getBytes();
		
		Disconnect disc1 = new Disconnect(bytes);
		System.out.println(disc1.getClass() + " recreated");
		System.out.println("Package valid: " + disc1.isValid());
		
		System.out.println("\n");
		
		int liarflag = 1;
		int bidA = 3;
		int bidP = 6;
		Bid bid = new Bid(liarflag, bidA, bidP);
		System.out.println(bid.getClass() + " created");
		System.out.println(liarflag + " " + bidA + " "+ bidP);
		bytes = bid.getBytes();
		
		Bid bid1 = new Bid(bytes);
		System.out.println(bid1.getClass() + " recreated");
		System.out.println("Package valid: " + bid1.isValid());
		System.out.println(bid1.getLiarFlag() + " " + bid1.getBidAmount() + " " + bid1.getBidPips()); 
		
		System.out.println("\n");
		
		int id1 = 1;
		String hans = "hans";
		int id2 = 2;
		String wurst = "wurst";
		int id3 = 3;
		String fisch = "fisch";
		Player pla1 = new Player(hans,id1);
		Player pla2 = new Player(wurst,id2);
		Player pla3 = new Player(fisch,id3);
		pla1.setCast(new Cast(5, 6, 1));
		pla2.setCast(new Cast(5, 6, 1));
		pla3.setCast(new Cast(5, 6, 1));
		pla1.setReady();
		pla2.setActive();
		pla3.setReady();
		
//		ArrayList<Player> playerList = new ArrayList<Player>();
//		playerList.add(pla1);
//	
//		playerList.add(pla2);
//		playerList.add(pla3);
//		RoundEnd round = new RoundEnd(id1,id2,playerList);
//		System.out.println(round.getClass() + " created");
//		System.out.println(id1 + " " + id2 );
//		bytes = round.getBytes();
//		
//		RoundEnd round1 = new RoundEnd(bytes);
//		System.out.println(round1.getClass() + " recreated");
//		System.out.println("Package valid: " + round1.isValid());
//		if(round1.isValid()){
//			for (PlayerInfo p : round1.getPlayers()) {
//				String s ="";
//				for(int i = 0;i<p.getCast().getCast().length; i++){
//					s += p.getCast().getCast()[i];
//				}
//				System.out.println(p.getId() + " Casts: " + s);
//			}
//			System.out.println(round1.getWinner() + " " + round1.getLoser()); 
//		}
//		System.out.println("\n");
		
		
		ArrayList<Player> playerList1 = new ArrayList<Player>();
		playerList1.add(pla1);
		playerList1.add(pla2);
		playerList1.add(pla1);
		playerList1.add(pla2);
		RoundEnd round2 = new RoundEnd(id1,id2,playerList1);
		System.out.println(round2.getClass() + " created");
		for (Player p : playerList1) {
			String s ="";
			for(int i = 0;i<p.getCast().getCast().length; i++){
				s += p.getCast().getCast()[i];
			}
			System.out.println(p.getID() + " Casts: " + s);
		}
		System.out.println(id1 + " " + id2); 			
		bytes = round2.getBytes();
		
		RoundEnd round3 = new RoundEnd(bytes);
		System.out.println(round3.getClass() + " recreated");
		System.out.println("Package valid: " + round3.isValid());
		if(round3.isValid()){
			for (PlayerInfo p : round3.getPlayers()) {
				String s ="";
				for(int i = 0;i<p.getCast().getCast().length; i++){
					s += p.getCast().getCast()[i];
				}
				System.out.println(p.getId() + " Casts: " + s);
			}
			System.out.println(round3.getWinner() + " " + round3.getLoser()); 		
		}
		
		System.out.println("\n");
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(pla1);
	
		playerList.add(pla2);
		playerList.add(pla3);
		int minPlayer = 3;
		int tts = 240;
		PreGameStart pgs = new PreGameStart(playerList, minPlayer, tts);
		System.out.println(pgs.getClass() + " created");
		System.out.println("players: " + playerList.size() + " " + minPlayer 
				+ " " + tts);
		bytes = pgs.getBytes();
		
		PreGameStart pgs2 = new PreGameStart(bytes);
		System.out.println(pgs2.getClass() + " recreated");
		System.out.println("Package valid: " + pgs2.isValid());
		System.out.println("players: " + pgs2.getPlayers().size() 
				+ " " + pgs2.getMin() + " " + pgs2.getTts());
		for (PlayerInfo p : pgs2.getPlayers()) {
			System.out.println(p.getId() + " " + p.getReady() + " " + p.getName());
		}
		
		System.out.println("\n");
		
		RoundStart rs = new RoundStart(new Cast(5, 6, 1), tts, playerList);
		System.out.println(rs.getClass() + " created");
		for (Player p : playerList) {
			System.out.println(p.getID() + " " + p.isActive() + " " + p.getName());
		}		bytes = rs.getBytes();
		
		RoundStart rs1 = new RoundStart(bytes);
		System.out.println(rs1.getClass() + " recreated");
		System.out.println("Package valid: " + rs1.isValid());
		System.out.println("players: " + rs1.getPlayers().size());
		for (PlayerInfo p : rs1.getPlayers()) {
			System.out.println(p.getId() + " " + p.getActive() + " " + p.getName());
		}
	}

}
