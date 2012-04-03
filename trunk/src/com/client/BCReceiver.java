package com.client;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static util.Converter.*;
import static util.CommandIDs.*;

public class BCReceiver extends Thread {
	
	private int port = 31748;
	
	public BCReceiver(int port) {
		this.port = port;
	}
	
	private List<BCServer> servers;
	private boolean end = false;

	@Override
	public void run() {
		servers = new LinkedList<BCServer>();
		//System.out.println("Starting receiver thread...");
		try {
			//int port = 31748;
			// Create a socket to listen on the port.
			DatagramSocket dsocket = new DatagramSocket(port);

			// Create a buffer to read datagrams into. If a
			// packet is larger than this buffer, the
			// excess will simply be discarded!
			byte[] buffer = new byte[256];

			// Create a packet to receive data into the buffer
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			// Now loop forever, waiting to receive packets and printing them.
			while (!end) {
				// Wait to receive a datagram
				dsocket.receive(packet);
//				System.out.println("Received packet:");
				BCServer bc = processPacket(buffer);
				if (servers.contains(bc)) {
					servers.set(servers.indexOf(bc), bc);
				} else {
					servers.add(bc);
				}
//				System.out.println(bc.toString());
				// Reset the length of the packet before reusing it.
				packet.setLength(buffer.length);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void end() {
		end = true;
	}
	
	public List<BCServer> getServers() {
		return servers;
	}

	private BCServer processPacket(byte[] packet) {
//		System.out.println(getHex(packet));
		BCServer bc = null;
//		String s = "";
		int l = packet.length;
		if (l > 7 && packet[4] == UDP_BROADCAST[0]) {
//			byte[] packetID = Arrays.copyOfRange(packet, 0, 4);
//			byte[] cid = Arrays.copyOfRange(packet, 4, 5);
			byte[] length = Arrays.copyOfRange(packet, 5, 7);
			int len = toInt(length);
			byte[] ip = Arrays.copyOfRange(packet, 7, 11);
			byte[] port = Arrays.copyOfRange(packet, 11, 13);
			byte[] version = Arrays.copyOfRange(packet, 13, 14);
			byte[] stringL = Arrays.copyOfRange(packet, 14, 15);
			int sl = toInt(stringL);
			byte[] str = Arrays.copyOfRange(packet, 15, 15 + sl);
	
			int idx = 15 + sl;
			byte[] minPlayer = Arrays.copyOfRange(packet, idx, ++idx);
			byte[] curPlayer = Arrays.copyOfRange(packet, idx, ++idx);
			byte[] maxPlayer = Arrays.copyOfRange(packet, idx, ++idx);
			String name = "";
			try {
				name = new String(str, "UTF8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String ipAddr = getIpAddress(ip);
			int portNum = toInt(port);
			int min = toInt(minPlayer);
			int cur = toInt(curPlayer);
			int max = toInt(maxPlayer);
//			s += getHex(packetID) + "\n";
//			s += getHex(cid) + "\n";
//			s += getHex(length) + " int val : " + len + "\n";
//			s += getHex(ip) + " ip addr: " + getIpAddress(ip) + "\n";
//			s += getHex(port) + " port: " + portNum + "\n";
//			s += getHex(version) + "\n";
//			s += getHex(stringL) + " string length: " + sl + "\n";
//			s += getHex(str) + " string: " + name + "\n";
//			s += getHex(minPlayer) + " minPlayer: " + toInt(minPlayer) + "\n";
//			s += getHex(curPlayer) + " curPlayer: " + toInt(curPlayer) + "\n";
//			s += getHex(maxPlayer) + " maxPlayer: " + toInt(maxPlayer) + "\n";
			bc = new BCServer(ipAddr, portNum, name, min, cur, max);	
		}
		return bc;
	}

}
