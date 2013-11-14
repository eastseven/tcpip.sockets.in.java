package cn.eastseven.tcpip.sockets.in.java;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class TcpServer {

	private static final Logger log = Logger.getLogger(TcpServer.class);
	
	public static final int SERVER_PORT = 9527;
	private static ServerSocket ser = null;
	private TcpServer() {}
	
	public void init() {
		try {
			if(ser == null) {
				ser = new ServerSocket(SERVER_PORT);
				log.info("server instantiation");
			}
		} catch (IOException e) {
			log.error(e);
		}
		//this.bindInetAddress();
	}
	
	public void start() {
		this.init();
	}
	
	public void bindInetAddress() {
		try {
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			while(nis.hasMoreElements()) {
				NetworkInterface ni = nis.nextElement();//网卡
				Enumeration<InetAddress> addressList = ni.getInetAddresses();
				while(addressList.hasMoreElements()) {
					InetAddress inetAddress = addressList.nextElement();
					if(inetAddress instanceof Inet4Address) {
						log.info("["+ser.getInetAddress()+"]"+inetAddress.getClass()+": "+inetAddress.getHostName()+", "+inetAddress.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			log.error(e);
		}
	}
	
	public static ServerSocket getServer() {
		return ser;
	}
	
	public static void main(String[] args) {

		TcpServer ser = new TcpServer();
		ser.start();
	}
}
