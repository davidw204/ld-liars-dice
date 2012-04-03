package com.server;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import util.pdu.server.BroadcastUDP;

public class BroadcastThread extends Thread {
	
	private boolean success = false;
	private DatagramPacket packet = null;
	private InetSocketAddress addr = null;
	private byte[] data;
	private int bcPort;
	private String bcIP;
	private int curPlayers;
	private String ip; 
	private int serverPort; 
	private String name;
	private int min;
	private int max;

	public BroadcastThread(String ip, int serverPort, String name, int min, int max, String bcIP, int bcPort) { 
		this.ip = ip;
		this.serverPort = serverPort;
		this.name = name;
		this.min = min;
		this.curPlayers = 0;
		this.max = max;
		this.bcPort = bcPort;
		this.bcIP = bcIP;
		this.data = new BroadcastUDP(ip, serverPort, name, min, curPlayers, max).getBytes();
	}

	@Override
	public void run() {
		System.out.println("Broadcast thread started...");
		System.out.println("Broadcast IP\t: " + bcIP + "\nBroadcast port\t: " + bcPort);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			addr = new InetSocketAddress(bcIP, bcPort);
			packet = new DatagramPacket(data, data.length, addr);
			success = true;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (success) {
			try {
				socket.send(packet);
				//System.out.println("sending broadcast...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sleep(3500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void update(int curPlayers) {
		this.curPlayers = curPlayers;
		this.data = new BroadcastUDP(ip, serverPort, name, min, curPlayers, max).getBytes();
		try {
			packet = new DatagramPacket(data, data.length, addr);
			success = true;
		} catch (SocketException e) {
			success = false;
			e.printStackTrace();
		}
	}
}
