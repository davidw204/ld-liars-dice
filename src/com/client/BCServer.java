package com.client;


public class BCServer {
	
	private String ip;
	private int port;
	private String name;
	private int minPlayer;
	private int curPlayer;
	private int maxPlayer;
	
	public BCServer(String ip, int port, String name, int minPlayer, int curPlayer, int maxPlayer) {
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.minPlayer = minPlayer;
		this.curPlayer = curPlayer;
		this.maxPlayer = maxPlayer;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean equals = false;
		if (o instanceof BCServer) {
			if (this.ip.equals(((BCServer) o).getIp())
					&& this.port == ((BCServer) o).getPort()) {
				equals = true;
			}
		}
		return equals;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	@Override
	public String toString() {
		String server = "";
		server += "ip: " + ip + " ";
		server += "port: " + port + " ";
		server += "name: " + name + " ";
		server += "min: " + minPlayer + " ";
		server += "cur: " + curPlayer + " ";
		server += "max: " + maxPlayer + " ";
		
		return server;
		
	}

	public String getName() {
		return name;
	}

	public int getMinPayer() {
		return minPlayer;
	}

	public int getCurPlayer() {
		return curPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}
}
