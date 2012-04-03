package com.server;

import game.GameContext;
import game.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

	private String bcIP;
	private String name;
	private String ip;
	private int port;
	private int min;
	private int max;
	
	public ServerThread(String ip, int port, String name, int min, int max, String bcIP) {
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.min = min;
		this.max = max;
		this.bcIP = bcIP;
	}

	@Override
	public void run() {
		int cnt = 0;
		try {
			System.out.println("Starting server...");
			System.out.println("ip\t\t: " + ip + "\nport\t\t: " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Server running...");
			
			BroadcastThread bt = new BroadcastThread(ip, port, name, min, max, bcIP, (port + 1));
			GameContext game = new GameContext(min, max, bt);
			bt.start();
			
			while (true) {
				Socket s = ss.accept();
				System.out.println("Client connected to server...");
				Player player = new Player(game);
				ClientThread ct = new ClientThread(++cnt, s, game, player);
				player.setClientThread(ct);
				ct.start();
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
	}

}
