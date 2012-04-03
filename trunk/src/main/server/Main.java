package main.server;

import game.Player;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;

import org.ksmsa.utils.io.ByteBuilder;

import util.pdu.server.BroadcastUDP;
import util.pdu.server.Challenge;
import util.pdu.server.Connect;
import util.pdu.server.GameEnd;
import util.pdu.server.PlayerID;

import com.server.BroadcastThread;
import com.server.ClientThread;
import com.server.ServerThread;

import static util.Converter.*;

public final class Main {

	private Main() {
	}

	private static String getSimpleBC(String ip) {
		String bcIP = "";
		String[] s = ip.split("\\.");
		if (s.length == 4) {
			bcIP = s[0] + "." + s[1] + "." + s[2] + ".255";
		}
		return bcIP;
	}

	private static String getIP() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		InetAddress[] addrs = null;
		try {
			addrs = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String myIp = "UNKNOWN";
		for (InetAddress addr : addrs) {
			// System.out.println("addr.getHostAddress() = "
			// + addr.getHostAddress());
			// System.out.println("addr.getHostName() = " + addr.getHostName());
			// System.out.println("addr.isAnyLocalAddress() = "
			// + addr.isAnyLocalAddress());
			// System.out.println("addr.isLinkLocalAddress() = "
			// + addr.isLinkLocalAddress());
			// System.out.println("addr.isLoopbackAddress() = "
			// + addr.isLoopbackAddress());
			// System.out.println("addr.isMulticastAddress() = "
			// + addr.isMulticastAddress());
			// System.out.println("addr.isSiteLocalAddress() = "
			// + addr.isSiteLocalAddress());
			// System.out.println("");
			if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
				myIp = addr.getHostAddress();
			}
		}
		return myIp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String ip = getIP();
		String bc = "";
		bc = getSimpleBC(ip);
		//int port = 31747;
		int port = 30750;
		String name = "IMBAserver";
		int min = 2;
		int max = 7;
		// System.out.println("IP\t: " + ip);
		// System.out.println("BC IP\t: " + bc);
		// String ip, int port, String name, int min, int max, String bcIP
		
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-ip")) {
				ip = args[i + 1];
			} else if (args[i].equals("-p")) {
				port = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("-name")) {
				name = args[i + 1];
			} else if (args[i].equals("-min")) {
				min = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("-max")) {
				max = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("-bcip")) {
				bc = args[i + 1];
			}
		}

		ServerThread game1 = new ServerThread(ip, port, name, min, max, bc);
		game1.start();
	}
}
